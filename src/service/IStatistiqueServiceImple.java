package service;

import dao.PaysDAO;
import dao.AthleteDAO;
import dao.DisciplineDAO;
import dao.CompetitionDAO;
import dao.ResultatDAO;

/**
 * Implémentation de IStatistiqueService.
 * Calcule les statistiques en s'appuyant sur les DAO existants (afficherTous().size()).
 * Aucune requête SQL directe ici : cette classe ne fait que réutiliser les DAO,
 * conformément au principe de séparation des responsabilités.
 */
public class IStatistiqueServiceImple implements IStatistiqueService {

    private final PaysDAO paysDAO;
    private final AthleteDAO athleteDAO;
    private final DisciplineDAO disciplineDAO;
    private final CompetitionDAO competitionDAO;
    private final ResultatDAO resultatDAO;

    public IStatistiqueServiceImple() {
        this.paysDAO = new PaysDAO();
        this.athleteDAO = new AthleteDAO();
        this.disciplineDAO = new DisciplineDAO();
        this.competitionDAO = new CompetitionDAO();
        this.resultatDAO = new ResultatDAO();
    }

    @Override
    public int nombrePays() {
        return paysDAO.afficherTous().size();
    }

    @Override
    public int nombreAthletes() {
        return athleteDAO.afficherTous().size();
    }

    @Override
    public int nombreDisciplines() {
        return disciplineDAO.afficherTous().size();
    }

    @Override
    public int nombreCompetitions() {
        return competitionDAO.afficherTous().size();
    }

    @Override
    public int nombreResultats() {
        return resultatDAO.afficherTous().size();
    }

    @Override
    public void afficherStatistiques() {
        System.out.println("========================================");
        System.out.println("        STATISTIQUES GENERALES");
        System.out.println("========================================");
        System.out.println("Nombre de pays          : " + nombrePays());
        System.out.println("Nombre d'athletes        : " + nombreAthletes());
        System.out.println("Nombre de disciplines    : " + nombreDisciplines());
        System.out.println("Nombre de competitions   : " + nombreCompetitions());
        System.out.println("Nombre de resultats      : " + nombreResultats());
        System.out.println("========================================");
    }
}