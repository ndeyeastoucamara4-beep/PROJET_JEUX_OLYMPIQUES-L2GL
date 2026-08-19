package model;

public class Discipline {
    private int idDiscipline;
    private String nomDiscipline;
    private String description;

    public Discipline() {
    }

    public Discipline(int idDiscipline, String nomDiscipline, String description) {
        this.idDiscipline = idDiscipline;
        this.nomDiscipline = nomDiscipline;
        this.description = description;
    }

    public Discipline(String nomDiscipline, String description) {
        this.nomDiscipline = nomDiscipline;
        this.description = description;
    }

    public int getIdDiscipline() {
        return idDiscipline;
    }

    public void setIdDiscipline(int idDiscipline) {
        this.idDiscipline = idDiscipline;
    }

    public String getNomDiscipline() {
        return nomDiscipline;
    }

    public void setNomDiscipline(String nomDiscipline) {
        this.nomDiscipline = nomDiscipline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "idDiscipline=" + idDiscipline +
                ", nomDiscipline='" + nomDiscipline + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
