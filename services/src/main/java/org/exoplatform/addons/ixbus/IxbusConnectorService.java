package org.exoplatform.addons.ixbus;

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
    if (params.getValueParam("exo.addons.ixbus.api.url") != null) {
      this.serverUrl = params.getValueParam("exo.addons.ixbus.api.url").getValue();
    }
    if (params.getValueParam("exo.addons.ixbus.api.key") != null) {
      this.apiKey = params.getValueParam("exo.addons.ixbus.api.key").getValue();
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

  public List<String> getCurrentUserFolders() {
    return new ArrayList<>();
  }



}
