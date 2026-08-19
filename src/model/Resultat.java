package model;

public class Resultat {
    private int idResultat;
    private Athlete athlete;
    private Competition competition;
    private double score;
    private int rang; //1 = or; 2=Argent; 3=Bronze

    public Resultat() {
    }


    public Resultat(int idResultat, Athlete athlete, Competition competition, double score, int rang) {
        this.idResultat = idResultat;
        this.athlete = athlete;
        this.competition = competition;
        this.score = score;
        this.rang = rang;
    }

    public Resultat(Athlete athlete, Competition competition, double score, int rang) {
        this.athlete = athlete;
        this.competition = competition;
        this.score = score;
        this.rang = rang;
    }


    public int getIdResultat() {
        return idResultat;
    }

    public void setIdResultat(int idResultat) {
        this.idResultat = idResultat;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    /**
     * Déduit le type de médaille à partir du rang.
     * Utile pour le module "Tableau des médailles".
     */
    public String getMedaille() {
        switch (rang) {
            case 1:
                return "Or";
            case 2:
                return "Argent";
            case 3:
                return "Bronze";
            default:
                return "Aucune";
        }
    }

    @Override
    public String toString() {
        return "Resultat{" +
                "idResultat=" + idResultat +
                ", athlete=" + (athlete != null ? athlete.getNom() + " " + athlete.getPrenom() : "null") +
                ", competition=" + (competition != null ? competition.getNomCompetition() : "null") +
                ", score=" + score +
                ", rang=" + rang +
                ", medaille=" + getMedaille() +
                '}';
    }
}
