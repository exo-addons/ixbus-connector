package org.exoplatform.addons.ixbus;

import org.exoplatform.addons.ixbus.entity.SettingsEntity;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import java.util.ArrayList;
import java.util.List;

public class IxbusConnectorService {

  private           String serverUrl ="";
  private           String apiKey    ="";
  protected final Log    LOG       = ExoLogger.getLogger(IxbusConnectorService.class);

  private String currentSessionToken="";


  public IxbusConnectorService(InitParams params) {
    if (params.getValueParam("serverUrl") != null) {
      this.serverUrl = params.getValueParam("serverUrl").getValue();
    }
    if (params.getValueParam("apiKey") != null) {
      this.apiKey = params.getValueParam("apiKey").getValue();
    }
    if (serverUrl.isEmpty() || apiKey.isEmpty()) {
      LOG.error("IxbusConnector service is not correctly configured. Check serverUrl and apiKey.");
    }
  }

  private void createSessionToken() {
    currentSessionToken = "123456789abcdef";
  }

  public String getUserIdentifier(String username) {
    return "fedcba987654321";
  }

  public int getCurrentUserFoldersCount() {
    return 3;
  }

  public int getCurrentUserActionsCount() {
    return 2;
  }

  public List<String> getCurrentUserFolders() {
    return new ArrayList<>();
  }

  public List<String> getCurrentUserActions() {
    return new ArrayList<>();
  }

  public SettingsEntity getSettings() {
    return new SettingsEntity(serverUrl);
  }
}
