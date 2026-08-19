package service;

import model.Athlete;

import java.util.List;


public interface IAthleteService {

    boolean ajouterAthlete(Athlete athlete);

    boolean modifierAthlete(Athlete athlete);

    boolean supprimerAthlete(int idAthlete);

    Athlete rechercherAthleteParId(int idAthlete);

    List<Athlete> rechercherAthleteParNom(String motCle);

    List<Athlete> afficherTousAthletes();

    List<BilanMedailles> calculerTableauMedailles();
}
