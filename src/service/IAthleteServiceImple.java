package service;

import dao.AthleteDAO;
import dao.ResultatDAO;
import model.Athlete;
import model.Resultat;
import model.Pays;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class IAthleteServiceImple implements IAthleteService {

    private final AthleteDAO athleteDAO;
    private final ResultatDAO resultatDAO;

    public IAthleteServiceImple() {
        this.athleteDAO = new AthleteDAO();
        this.resultatDAO = new ResultatDAO();
    }

    @Override
    public boolean ajouterAthlete(Athlete athlete) {
        if (athlete.getPays() == null || athlete.getDiscipline() == null) {
            System.out.println("L'athlète doit avoir un pays et une discipline assignés.");
            return false;
        }
        return athleteDAO.ajouter(athlete);
    }

    @Override
    public boolean modifierAthlete(Athlete athlete) {
        return athleteDAO.modifier(athlete);
    }

    @Override
    public boolean supprimerAthlete(int idAthlete) {
        return athleteDAO.supprimer(idAthlete);
    }

    @Override
    public Athlete rechercherAthleteParId(int idAthlete) {
        return athleteDAO.rechercherParId(idAthlete);
    }

    @Override
    public List<Athlete> rechercherAthleteParNom(String motCle) {
        return athleteDAO.rechercherParNom(motCle);
    }

    @Override
    public List<Athlete> afficherTousAthletes() {
        return athleteDAO.afficherTous();
    }

    @Override
    public List<BilanMedailles> calculerTableauMedailles() {
        // Récupère uniquement les résultats médaillés (rang 1, 2 ou 3)
        List<Resultat> resultatsMedailles = resultatDAO.afficherResultatsMedailles();

        // LinkedHashMap conserve l'ordre d'insertion, pratique pour un affichage stable
        Map<Integer, BilanMedailles> bilanParPays = new LinkedHashMap<>();

        for (Resultat resultat : resultatsMedailles) {
            Pays pays = resultat.getAthlete().getPays();
            int idPays = pays.getIdPays();

            // Si le pays n'a pas encore de bilan, on en crée un nouveau
            bilanParPays.putIfAbsent(idPays, new BilanMedailles(pays));

            BilanMedailles bilan = bilanParPays.get(idPays);

            switch (resultat.getRang()) {
                case 1 -> bilan.incrementerOr();
                case 2 -> bilan.incrementerArgent();
                case 3 -> bilan.incrementerBronze();
                default -> {

                }
            }
        }

        // Transforme la Map en liste, triée par nombre d'Or décroissant, puis Argent, puis Bronze
        List<BilanMedailles> classement = new ArrayList<>(bilanParPays.values());
        classement.sort(
                Comparator.comparingInt(BilanMedailles::getNbOr)
                        .thenComparingInt(BilanMedailles::getNbArgent)
                        .thenComparingInt(BilanMedailles::getNbBronze)
                        .reversed()
        );

        return classement;
    }
}