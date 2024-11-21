package org.exoplatform.addons.ixbus;

import org.apache.ecs.html.S;
import org.exoplatform.addons.ixbus.entity.DocumentEntity;
import org.exoplatform.addons.ixbus.notification.IxbusNotificationPlugin;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
public class IxbusNotificationsJob  implements Job {

  private static final Log LOG = ExoLogger.getLogger(IxbusNotificationsJob.class);

  private static final String IXBUS_NOTIFICATION_LAST_EXECUTION = "ixBusNotificationLastExecution";


  private String pattern = "yyyy-MM-dd'T'HH:mm:ss";
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
  ZoneId zoneId = ZoneId.of("Europe/Paris");


  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    IxbusConnectorService ixbusConnectorService = CommonsUtils.getService(IxbusConnectorService.class);
    SettingService settingService = CommonsUtils.getService(SettingService.class);

    SettingValue lastExecutionSetting = settingService.get(Context.GLOBAL, Scope.APPLICATION, IXBUS_NOTIFICATION_LAST_EXECUTION);
    Instant lastExecutionInstant;
    if (lastExecutionSetting != null) {
      lastExecutionInstant = Instant.ofEpochMilli(Long.parseLong(lastExecutionSetting.getValue().toString()));

      LOG.debug("Execute Ixbus Notification Job, lastExecution was {}",lastExecutionInstant);

      long nowExecution = System.currentTimeMillis();

      List<DocumentEntity> documents = ixbusConnectorService.getFoldersInStatut("Signature");
      if (documents != null) {
        documents.addAll(ixbusConnectorService.getFoldersInStatut("Visa"));
        LOG.debug("{} folders to check for notifications", documents.size());

        Map<String, String> users = ixbusConnectorService.getAllUsers();

        documents.stream().filter(documentEntity -> {
          //keep only folders for which currentStep changes after the last execution of the job
          LocalDateTime localDateTime = LocalDateTime.parse(documentEntity.getStepEnAttente().getDateEnAttente(), dateTimeFormatter);
          ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
          Instant instantEnAttente = zonedDateTime.toInstant();
          return instantEnAttente.isAfter(lastExecutionInstant);
        }).forEach(documentEntity -> {
          String email = users.get(documentEntity.getStepEnAttente().getIdentifiant());
          if (email!=null) {
            String username = searchUser(email);
            if (username != null) {
              LOG.debug("Document {} to notify for username={} and email={}", String.valueOf(documentEntity), username, email);

              NotificationContext ctx = NotificationContextImpl.cloneInstance();
              ctx.append(IxbusNotificationPlugin.RECIPIENT, username);
              ctx.append(IxbusNotificationPlugin.DOCUMENT_NAME, documentEntity.getName());
              ctx.append(IxbusNotificationPlugin.STATUS, documentEntity.getStatus());
              ctx.append(IxbusNotificationPlugin.NATURE, documentEntity.getNature());
              ctx.append(IxbusNotificationPlugin.TARGET_URL, documentEntity.getTargetUrl());
              ctx.getNotificationExecutor()
                 .with(ctx.makeCommand(PluginKey.key(IxbusNotificationPlugin.ID)))
                 .execute(ctx);

            }
          }
        });

        settingService.set(Context.GLOBAL, Scope.APPLICATION, IXBUS_NOTIFICATION_LAST_EXECUTION, new SettingValue<>(nowExecution));
      }
    } else {
      settingService.set(Context.GLOBAL, Scope.APPLICATION, IXBUS_NOTIFICATION_LAST_EXECUTION, new SettingValue<>(System.currentTimeMillis()));

    }
  }

  private String searchUser(String email) {
    OrganizationService organizationService = CommonsUtils.getService(OrganizationService.class);

    Query userQuery = new Query();
    userQuery.setEmail(email);
    String username = null;
    try {
      ListAccess<User> matchUsers = organizationService.getUserHandler().findUsersByQuery(userQuery);
      if (matchUsers.getSize() > 0) {
        User user = matchUsers.load(0, 1)[0];
        username = user.getUserName();
      }

    } catch (Exception e) {
      LOG.error("Unable to read user with email {}", email, e);
    }
    return username;
  }
}
