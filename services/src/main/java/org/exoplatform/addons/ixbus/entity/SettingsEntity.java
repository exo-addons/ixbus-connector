package org.exoplatform.addons.ixbus.entity;

public class SettingsEntity {
  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public SettingsEntity(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  private String serverUrl;
}
