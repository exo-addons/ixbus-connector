package org.exoplatform.addons.ixbus.notification;

import io.meeds.pwa.model.PwaNotificationMessage;
import io.meeds.pwa.plugin.PwaNotificationPlugin;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.ResourceBundleService;
import org.springframework.beans.factory.annotation.Autowired;

public class IxbusPwaNotificationPlugin implements PwaNotificationPlugin {

  private static final String   TITLE_LABEL_KEY = "pwa.notification.IxbusPwaNotificationPlugin.title";
  private static final String   BODY_LABEL_KEY = "pwa.notification.IxbusPwaNotificationPlugin.body";

  @Autowired
  private ResourceBundleService resourceBundleService;

  @Override
  public String getId() {
    return IxbusNotificationPlugin.ID;
  }

  @Override
  public PwaNotificationMessage process(NotificationInfo notification, LocaleConfig localeConfig) {
    PwaNotificationMessage notificationMessage = new PwaNotificationMessage();
    notificationMessage.setTitle(resourceBundleService.getSharedString(TITLE_LABEL_KEY, localeConfig.getLocale())
                                                      .replace("{0}", notification.getValueOwnerParameter("documentName"))
                                                      .replace("{1}",notification.getValueOwnerParameter("status")));
    notificationMessage.setBody(resourceBundleService.getSharedString(BODY_LABEL_KEY, localeConfig.getLocale())+" : "+notification.getValueOwnerParameter("nature"));
    notificationMessage.setUrl(notification.getValueOwnerParameter("targetUrl"));
    return notificationMessage;
  }

}
