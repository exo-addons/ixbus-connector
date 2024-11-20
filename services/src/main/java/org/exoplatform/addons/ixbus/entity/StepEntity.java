package org.exoplatform.addons.ixbus.entity;

public class StepEntity {
  public String getIdentifiant() {
    return identifiant;
  }

  public void setIdentifiant(String identifiant) {
    this.identifiant = identifiant;
  }

  public String getStatut() {
    return statut;
  }

  public void setStatut(String statut) {
    this.statut = statut;
  }

  public String getDateEnAttente() {
    return dateEnAttente;
  }

  public void setDateEnAttente(String dateEnAttente) {
    this.dateEnAttente = dateEnAttente;
  }

  private String identifiant;
  private String statut;
  private String dateEnAttente;

  @Override
  public String toString() {
    return "StepEntity{" +
        "identifiant='" + identifiant + '\'' +
        ", statut='" + statut + '\'' +
        ", dateEnAttente='" + dateEnAttente + '\'' +
        '}';
  }
}
