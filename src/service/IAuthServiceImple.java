package service;

import dao.UtilisateurDAO;
import model.Utilisateur;


public class IAuthServiceImple implements IAuthService {

    private final UtilisateurDAO utilisateurDAO;


    private Utilisateur utilisateurConnecte;

    public IAuthServiceImple() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    @Override
    public Utilisateur seConnecter(String login, String motDePasse) {
        if (login == null || login.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
            System.out.println("Le login et le mot de passe sont obligatoires.");
            return null;
        }

        Utilisateur utilisateur = utilisateurDAO.authentifier(login, motDePasse);

        if (utilisateur != null) {
            this.utilisateurConnecte = utilisateur;
            System.out.println("Connexion réussie. Bienvenue " + utilisateur.getNomComplet() + " !");
        } else {
            System.out.println("Login ou mot de passe incorrect.");
        }

        return utilisateur;
    }

    @Override
    public void seDeconnecter() {
        if (utilisateurConnecte != null) {
            System.out.println("Déconnexion de " + utilisateurConnecte.getNomComplet() + ".");
        }
        this.utilisateurConnecte = null;
    }

    @Override
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    @Override
    public boolean estAdmin() {
        return utilisateurConnecte != null && "ADMIN".equalsIgnoreCase(utilisateurConnecte.getRole());
    }
}