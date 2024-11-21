package org.exoplatform.addons.ixbus.notification;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.annotation.TemplateConfig;
import org.exoplatform.commons.api.notification.annotation.TemplateConfigs;
import org.exoplatform.commons.api.notification.channel.template.AbstractTemplateBuilder;
import org.exoplatform.commons.api.notification.channel.template.TemplateProvider;
import org.exoplatform.commons.api.notification.model.MessageInfo;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.api.notification.service.template.TemplateContext;
import org.exoplatform.commons.notification.template.TemplateUtils;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.social.notification.plugin.SocialNotificationUtils;

import java.io.Writer;

@TemplateConfigs(templates = {
    @TemplateConfig(pluginId = IxbusNotificationPlugin.ID, template = "war:/notifications/templates/mail/IxbusNotificationPlugin.gtmpl"),
})
public class MailTemplateProvider extends TemplateProvider {

  public MailTemplateProvider(InitParams initParams) {
    super(initParams);
    this.templateBuilders.put(PluginKey.key(IxbusNotificationPlugin.ID), ixbusNotification);
  }

  /** Defines the template builder for MfaAdminRevocationRequestPlugin*/
  private AbstractTemplateBuilder ixbusNotification = new AbstractTemplateBuilder() {
    @Override
    protected MessageInfo makeMessage(NotificationContext ctx) {
      MessageInfo messageInfo = new MessageInfo();

      NotificationInfo notification = ctx.getNotificationInfo();

      String language = getLanguage(notification);
      TemplateContext templateContext = new TemplateContext(notification.getKey().getId(), language);
      SocialNotificationUtils.addFooterAndFirstName(notification.getTo(), templateContext);

      templateContext.put("DOCUMENT_NAME", notification.getValueOwnerParameter("documentName"));
      templateContext.put("STATUS", notification.getValueOwnerParameter("status"));
      templateContext.put("NATURE", notification.getValueOwnerParameter("nature"));
      templateContext.put("TARGET_URL", notification.getValueOwnerParameter("targetUrl"));

      String subject = TemplateUtils.processSubject(templateContext);
      String body = TemplateUtils.processGroovy(templateContext);
      //binding the exception throws by processing template
      ctx.setException(templateContext.getException());

      return messageInfo.subject(subject).body(body).end();
    }

    @Override
    protected boolean makeDigest(NotificationContext ctx, Writer writer) {
      return false;
    }
  };
}
