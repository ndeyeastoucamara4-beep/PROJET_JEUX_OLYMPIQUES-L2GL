package service;

import model.Utilisateur;


public interface IAuthService {


    Utilisateur seConnecter(String login, String motDePasse);

    void seDeconnecter();

    boolean estConnecte();
    Utilisateur getUtilisateurConnecte();

    boolean estAdmin();
}
