package model;


public class Pays {

    private int idPays;
    private String nomPays;
    private String continent;

    public Pays() {
    }

    public Pays(int idPays, String nomPays, String continent) {
        this.idPays = idPays;
        this.nomPays = nomPays;
        this.continent = continent;
    }

    public Pays(String nomPays, String continent) {
        this.nomPays = nomPays;
        this.continent = continent;
    }

    public int getIdPays() {
        return idPays;
    }

    public void setIdPays(int idPays) {
        this.idPays = idPays;
    }

    public String getNomPays() {
        return nomPays;
    }

    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "idPays=" + idPays +
                ", nomPays='" + nomPays + '\'' +
                ", continent='" + continent + '\'' +
                '}';
    }
}