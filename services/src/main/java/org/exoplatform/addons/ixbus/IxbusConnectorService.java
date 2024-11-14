package org.exoplatform.addons.ixbus;

import org.exoplatform.addons.ixbus.entity.DocumentEntity;
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

  public List<DocumentEntity> getCurrentUserFolders() {
    return generateContent(3);
  }

  public List<DocumentEntity> getCurrentUserActions() {
    return generateContent(2);
  }

  public SettingsEntity getSettings() {
    SettingsEntity settings = new SettingsEntity();
    settings.setCreateUrl(serverUrl+"/parapheur/preparer");
    return settings;
  }

  private List<DocumentEntity> generateContent(int size) {
    List<DocumentEntity> results = new ArrayList<>();
    for(int i = 0 ; i<size; i++) {
      DocumentEntity document = new DocumentEntity();
      document.setId("C51A70FDAF6FF186624D991B49E4CC76");
      document.setName("Test Document Name which can be quite long");
      document.setAction("viser");
      document.setCreationDate("2022-09-13T12:20:59");
      document.setDueDate("2022-10-13T12:20:59");
      document.setReferentFirstName("Morgan");
      document.setReferentLastName("Argondicco");
      document.setNature("Annonce présidentielle");
      results.add(document);
    }
    return results;
  }
}
