package service;

import model.Pays;


public class BilanMedailles {

    private Pays pays;
    private int nbOr;
    private int nbArgent;
    private int nbBronze;

    public BilanMedailles(Pays pays) {
        this.pays = pays;
        this.nbOr = 0;
        this.nbArgent = 0;
        this.nbBronze = 0;
    }

    public Pays getPays() {
        return pays;
    }

    public int getNbOr() {
        return nbOr;
    }

    public void incrementerOr() {
        this.nbOr++;
    }

    public int getNbArgent() {
        return nbArgent;
    }

    public void incrementerArgent() {
        this.nbArgent++;
    }

    public int getNbBronze() {
        return nbBronze;
    }

    public void incrementerBronze() {
        this.nbBronze++;
    }

    /**
     * Retourne le total de médailles (Or + Argent + Bronze), utilisé pour le classement.
     */
    public int getTotal() {
        return nbOr + nbArgent + nbBronze;
    }

    @Override
    public String toString() {
        return String.format("%-20s Or:%-3d Argent:%-3d Bronze:%-3d Total:%-3d",
                pays.getNomPays(), nbOr, nbArgent, nbBronze, getTotal());
    }
}