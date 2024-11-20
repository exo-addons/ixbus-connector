package org.exoplatform.addons.ixbus.entity;

public class DocumentEntity {

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public String getReferentFirstName() {
    return referentFirstName;
  }

  public void setReferentFirstName(String referentFirstName) {
    this.referentFirstName = referentFirstName;
  }

  public String getReferentLastName() {
    return referentLastName;
  }

  public void setReferentLastName(String referentLastName) {
    this.referentLastName = referentLastName;
  }

  public String getNature() {
    return nature;
  }

  public void setNature(String nature) {
    this.nature = nature;
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  private String name;
  private String action;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  private String status;
  private String creationDate;
  private String dueDate;
  private String referentFirstName;
  private String referentLastName;
  private String nature;
  private String targetUrl;
  private String id;

  public StepEntity getStepEnAttente() {
    return stepEnAttente;
  }

  public void setStepEnAttente(StepEntity stepEnAttente) {
    this.stepEnAttente = stepEnAttente;
  }

  private StepEntity stepEnAttente;

  @Override
  public String toString() {
    return "DocumentEntity{" +
        "name='" + name + '\'' +
        ", action='" + action + '\'' +
        ", status='" + status + '\'' +
        ", creationDate='" + creationDate + '\'' +
        ", dueDate='" + dueDate + '\'' +
        ", referentFirstName='" + referentFirstName + '\'' +
        ", referentLastName='" + referentLastName + '\'' +
        ", nature='" + nature + '\'' +
        ", targetUrl='" + targetUrl + '\'' +
        ", id='" + id + '\'' +
        ", stepEnAttente=" + stepEnAttente +
        '}';
  }
}
