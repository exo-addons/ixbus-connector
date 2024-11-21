package org.exoplatform.addons.ixbus.notification;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.ArgumentLiteral;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.BaseNotificationPlugin;
import org.exoplatform.container.xml.InitParams;

public class IxbusNotificationPlugin extends BaseNotificationPlugin {

  public static final ArgumentLiteral<String> DOCUMENT_NAME = new ArgumentLiteral<>(String.class,"ixbusName");
  public static final ArgumentLiteral<String> STATUS = new ArgumentLiteral<>(String.class,"ixbusStatus");
  public static final ArgumentLiteral<String> NATURE = new ArgumentLiteral<>(String.class,"ixbusNature");
  public static final ArgumentLiteral<String> RECIPIENT = new ArgumentLiteral<>(String.class,"ixBusRecipient");
  public static final ArgumentLiteral<String> TARGET_URL = new ArgumentLiteral<>(String.class,"ixBusTargetUrl");

  public static final String                ID="IxbusNotificationPlugin";


  public IxbusNotificationPlugin(InitParams initParams) {
    super(initParams);
  }

  @Override
  public String getId() {
    return this.ID;
  }

  @Override
  public boolean isValid(NotificationContext ctx) {
    return true;
  }

  @Override
  public NotificationInfo makeNotification(NotificationContext ctx) {
    @SuppressWarnings("unchecked")
    String recipient = ctx.value(RECIPIENT);
    String documentName = ctx.value(DOCUMENT_NAME);
    String status = ctx.value(STATUS);
    String nature = ctx.value(NATURE);
    String targetUrl = ctx.value(TARGET_URL);
    return NotificationInfo.instance()
                    .key(getId())
                    .with("documentName", documentName)
                    .with("status", status)
                    .with("nature", nature)
                    .with("targetUrl", targetUrl)
                    .to(recipient);
  }

}
