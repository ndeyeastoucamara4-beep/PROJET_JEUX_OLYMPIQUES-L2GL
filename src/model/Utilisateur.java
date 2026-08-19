package model;

public class Utilisateur {
    private int idUtilisateur;
    private String nomComplet;
    private String login;
    private String motDePasse;
    private String role; //"ADMIN" "GESTIONNAIRE"


    public Utilisateur() {
    }


    public Utilisateur(int idUtilisateur, String nomComplet, String login, String motDePasse, String role) {
        this.idUtilisateur = idUtilisateur;
        this.nomComplet = nomComplet;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // constructeur sans id util lors de l'ajout(id auto-genere par la BD)
    public Utilisateur(String nomComplet, String login, String motDePasse, String role) {
        this.nomComplet = nomComplet;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // La méthode toString est maintenant bien intégrée dans la classe
    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", nomComplet='" + nomComplet + '\'' +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} // Fin de la classe Utilisateur