package model;
import java.time.LocalDate;

public class Athlete {
    private int idAthlete;
    private String nom;
    private String prenom;
    private String sexe;
    private LocalDate dateNaissance;
    private Pays pays;
    private Discipline discipline;

    public Athlete() {
    }

    public Athlete(int idAthlete, String nom, String prenom, String sexe, LocalDate dateNaissance, Pays pays, Discipline discipline) {
        this.idAthlete = idAthlete;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.pays = pays;
        this.discipline = discipline;
    }

    public Athlete(String nom, String prenom, String sexe, LocalDate dateNaissance, Pays pays, Discipline discipline) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.pays = pays;
        this.discipline = discipline;
    }

    public int getIdAthlete() {
        return idAthlete;
    }

    public void setIdAthlete(int idAthlete) {
        this.idAthlete = idAthlete;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public String toString() {
        return "Athlete{" +
                "idAthlete=" + idAthlete +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", pays=" + (pays != null ? pays.getNomPays() : "null") +
                ", discipline=" + (discipline != null ? discipline.getNomDiscipline() : "null") +
                '}';
    }
}
