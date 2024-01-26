package ca.ulaval.glo2003.entity;

public class Proprietaire {
    private int noProprietaire;
    private String nom;
    private String prenom;
    private String telephone;

    public Proprietaire( String nom, String prenom, String telephone) {

        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public int getNoProprietaire() {
        return noProprietaire;
    }

    public void setNoProprietaire(int noProprietaire) {
        this.noProprietaire = noProprietaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Proprietaire{" +
                "noProprietaire=" + noProprietaire +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
