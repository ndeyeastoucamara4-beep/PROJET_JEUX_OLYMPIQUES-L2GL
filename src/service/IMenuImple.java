package service;

import dao.*;
import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Implémentation de IMenu.
 * Gère l'affichage de tous les menus et sous-menus en console, et fait le lien
 * entre la saisie utilisateur et les couches service/dao.
 */
public class IMenuImple implements IMenu {

    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Services
    private final IAuthService authService = new IAuthServiceImple();
    private final IAthleteService athleteService = new IAthleteServiceImple();
    private final IStatistiqueService statistiqueService = new IStatistiqueServiceImple();

    // DAO utilisés directement pour les modules simples (Pays, Discipline, Competition, Resultat, Utilisateur)
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final PaysDAO paysDAO = new PaysDAO();
    private final DisciplineDAO disciplineDAO = new DisciplineDAO();
    private final CompetitionDAO competitionDAO = new CompetitionDAO();
    private final ResultatDAO resultatDAO = new ResultatDAO();

    @Override
    public void demarrer() {
        System.out.println("===================================");
        System.out.println("JEUX OLYMPIQUES DE LA JEUNESSE 2026");
        System.out.println("===================================");

        while (true) {
            if (!authService.estConnecte()) {
                if (!ecranConnexion()) {
                    continue; // échec de connexion, on redemande
                }
            }
            afficherMenuPrincipal();
        }
    }

    // ============================================================
    // ECRAN DE CONNEXION
    // ============================================================

    private boolean ecranConnexion() {
        System.out.println("\n--- CONNEXION ---");
        System.out.print("Login : ");
        String login = sc.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = sc.nextLine();

        Utilisateur u = authService.seConnecter(login, motDePasse);
        return u != null;
    }

    // ============================================================
    // MENU PRINCIPAL
    // ============================================================

    private void afficherMenuPrincipal() {
        System.out.println("\n===================================");
        System.out.println("MENU PRINCIPAL - Connecté : " + authService.getUtilisateurConnecte().getNomComplet());
        System.out.println("===================================");
        System.out.println("1. Gestion des utilisateurs");
        System.out.println("2. Gestion des pays");
        System.out.println("3. Gestion des disciplines");
        System.out.println("4. Gestion des athletes");
        System.out.println("5. Gestion des competitions");
        System.out.println("6. Gestion des resultats");
        System.out.println("7. Statistiques");
        System.out.println("8. Deconnexion");
        System.out.println("9. Quitter");
        System.out.print("Votre choix : ");

        int choix = lireEntier();

        switch (choix) {
            case 1 -> menuUtilisateurs();
            case 2 -> menuPays();
            case 3 -> menuDisciplines();
            case 4 -> menuAthletes();
            case 5 -> menuCompetitions();
            case 6 -> menuResultats();
            case 7 -> statistiqueService.afficherStatistiques();
            case 8 -> authService.seDeconnecter();
            case 9 -> quitter();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void quitter() {
        System.out.println("Fermeture de l'application...");
        Database.fermerConnexion();
        System.exit(0);
    }

    // ============================================================
    // MODULE 1 : UTILISATEURS (accessible uniquement à l'admin)
    // ============================================================

    private void menuUtilisateurs() {
        if (!authService.estAdmin()) {
            System.out.println("Accès refusé : réservé aux administrateurs.");
            return;
        }

        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES UTILISATEURS ---");
            System.out.println("1. Ajouter utilisateur");
            System.out.println("2. Modifier utilisateur");
            System.out.println("3. Supprimer utilisateur");
            System.out.println("4. Rechercher utilisateur");
            System.out.println("5. Afficher utilisateurs");
            System.out.println("6. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom complet : ");
                    String nom = sc.nextLine();
                    System.out.print("Login : ");
                    String login = sc.nextLine();
                    System.out.print("Mot de passe : ");
                    String mdp = sc.nextLine();
                    System.out.print("Role (ADMIN/GESTIONNAIRE) : ");
                    String role = sc.nextLine();
                    boolean ok = utilisateurDAO.ajouter(new Utilisateur(nom, login, mdp, role));
                    System.out.println(ok ? "Utilisateur ajouté." : "Echec de l'ajout.");
                }
                case 2 -> {
                    System.out.print("Id de l'utilisateur à modifier : ");
                    int id = lireEntier();
                    Utilisateur u = utilisateurDAO.rechercherParId(id);
                    if (u == null) {
                        System.out.println("Utilisateur introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau nom complet : ");
                    u.setNomComplet(sc.nextLine());
                    System.out.print("Nouveau login : ");
                    u.setLogin(sc.nextLine());
                    System.out.print("Nouveau mot de passe : ");
                    u.setMotDePasse(sc.nextLine());
                    boolean ok = utilisateurDAO.modifier(u);
                    System.out.println(ok ? "Utilisateur modifié." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id de l'utilisateur à supprimer : ");
                    int id = lireEntier();
                    boolean ok = utilisateurDAO.supprimer(id);
                    System.out.println(ok ? "Utilisateur supprimé." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Login recherché : ");
                    Utilisateur u = utilisateurDAO.rechercherParLogin(sc.nextLine());
                    System.out.println(u != null ? u : "Aucun utilisateur trouvé.");
                }
                case 5 -> afficherListe(utilisateurDAO.afficherTous());
                case 6 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // ============================================================
    // MODULE 2 : PAYS
    // ============================================================

    private void menuPays() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES PAYS ---");
            System.out.println("1. Ajouter pays");
            System.out.println("2. Modifier pays");
            System.out.println("3. Supprimer pays");
            System.out.println("4. Rechercher pays");
            System.out.println("5. Liste des pays");
            System.out.println("6. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom du pays : ");
                    String nom = sc.nextLine();
                    System.out.print("Continent : ");
                    String continent = sc.nextLine();
                    boolean ok = paysDAO.ajouter(new Pays(nom, continent));
                    System.out.println(ok ? "Pays ajouté." : "Echec de l'ajout.");
                }
                case 2 -> {
                    System.out.print("Id du pays à modifier : ");
                    int id = lireEntier();
                    Pays p = paysDAO.rechercherParId(id);
                    if (p == null) {
                        System.out.println("Pays introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau nom : ");
                    p.setNomPays(sc.nextLine());
                    System.out.print("Nouveau continent : ");
                    p.setContinent(sc.nextLine());
                    boolean ok = paysDAO.modifier(p);
                    System.out.println(ok ? "Pays modifié." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id du pays à supprimer : ");
                    int id = lireEntier();
                    boolean ok = paysDAO.supprimer(id);
                    System.out.println(ok ? "Pays supprimé." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Nom recherché : ");
                    afficherListe(paysDAO.rechercherParNom(sc.nextLine()));
                }
                case 5 -> afficherListe(paysDAO.afficherTous());
                case 6 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // ============================================================
    // MODULE 3 : DISCIPLINES
    // ============================================================

    private void menuDisciplines() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES DISCIPLINES ---");
            System.out.println("1. Ajouter discipline");
            System.out.println("2. Modifier discipline");
            System.out.println("3. Supprimer discipline");
            System.out.println("4. Rechercher discipline");
            System.out.println("5. Afficher disciplines");
            System.out.println("6. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom de la discipline : ");
                    String nom = sc.nextLine();
                    System.out.print("Description : ");
                    String desc = sc.nextLine();
                    boolean ok = disciplineDAO.ajouter(new Discipline(nom, desc));
                    System.out.println(ok ? "Discipline ajoutée." : "Echec de l'ajout.");
                }
                case 2 -> {
                    System.out.print("Id de la discipline à modifier : ");
                    int id = lireEntier();
                    Discipline d = disciplineDAO.rechercherParId(id);
                    if (d == null) {
                        System.out.println("Discipline introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau nom : ");
                    d.setNomDiscipline(sc.nextLine());
                    System.out.print("Nouvelle description : ");
                    d.setDescription(sc.nextLine());
                    boolean ok = disciplineDAO.modifier(d);
                    System.out.println(ok ? "Discipline modifiée." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id de la discipline à supprimer : ");
                    int id = lireEntier();
                    boolean ok = disciplineDAO.supprimer(id);
                    System.out.println(ok ? "Discipline supprimée." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Nom recherché : ");
                    afficherListe(disciplineDAO.rechercherParNom(sc.nextLine()));
                }
                case 5 -> afficherListe(disciplineDAO.afficherTous());
                case 6 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // ============================================================
    // MODULE 4 : ATHLETES
    // ============================================================

    private void menuAthletes() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES ATHLETES ---");
            System.out.println("1. Ajouter athlete");
            System.out.println("2. Modifier athlete");
            System.out.println("3. Supprimer athlete");
            System.out.println("4. Rechercher athlete");
            System.out.println("5. Afficher athletes");
            System.out.println("6. Tableau des medailles");
            System.out.println("7. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom : ");
                    String nom = sc.nextLine();
                    System.out.print("Prenom : ");
                    String prenom = sc.nextLine();
                    System.out.print("Sexe (M/F) : ");
                    String sexe = sc.nextLine();
                    System.out.print("Date de naissance (jj/mm/aaaa) : ");
                    LocalDate date = LocalDate.parse(sc.nextLine(), dateFormat);

                    System.out.print("Id du pays : ");
                    Pays pays = paysDAO.rechercherParId(lireEntier());
                    System.out.print("Id de la discipline : ");
                    Discipline discipline = disciplineDAO.rechercherParId(lireEntier());

                    if (pays == null || discipline == null) {
                        System.out.println("Pays ou discipline introuvable. Ajout annulé.");
                        continue;
                    }

                    Athlete a = new Athlete(nom, prenom, sexe, date, pays, discipline);
                    boolean ok = athleteService.ajouterAthlete(a);
                    System.out.println(ok ? "Athlete ajouté." : "Echec de l'ajout.");
                }
                case 2 -> {
                    System.out.print("Id de l'athlete à modifier : ");
                    int id = lireEntier();
                    Athlete a = athleteService.rechercherAthleteParId(id);
                    if (a == null) {
                        System.out.println("Athlete introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau nom : ");
                    a.setNom(sc.nextLine());
                    System.out.print("Nouveau prenom : ");
                    a.setPrenom(sc.nextLine());
                    boolean ok = athleteService.modifierAthlete(a);
                    System.out.println(ok ? "Athlete modifié." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id de l'athlete à supprimer : ");
                    int id = lireEntier();
                    boolean ok = athleteService.supprimerAthlete(id);
                    System.out.println(ok ? "Athlete supprimé." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Nom ou prenom recherché : ");
                    afficherListe(athleteService.rechercherAthleteParNom(sc.nextLine()));
                }
                case 5 -> afficherListe(athleteService.afficherTousAthletes());
                case 6 -> afficherTableauMedailles();
                case 7 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void afficherTableauMedailles() {
        System.out.println("\n--- TABLEAU DES MEDAILLES ---");
        List<BilanMedailles> classement = athleteService.calculerTableauMedailles();
        if (classement.isEmpty()) {
            System.out.println("Aucun résultat médaillé pour le moment.");
            return;
        }
        int rang = 1;
        for (BilanMedailles bilan : classement) {
            System.out.println(rang + ". " + bilan);
            rang++;
        }
    }

    // ============================================================
    // MODULE 5 : COMPETITIONS
    // ============================================================

    private void menuCompetitions() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES COMPETITIONS ---");
            System.out.println("1. Ajouter competition");
            System.out.println("2. Modifier competition");
            System.out.println("3. Supprimer competition");
            System.out.println("4. Rechercher competition");
            System.out.println("5. Afficher competitions");
            System.out.println("6. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Nom de la competition : ");
                    String nom = sc.nextLine();
                    System.out.print("Date (jj/mm/aaaa) : ");
                    LocalDate date = LocalDate.parse(sc.nextLine(), dateFormat);
                    System.out.print("Lieu (Dakar/Diamniadio/Saly) : ");
                    String lieu = sc.nextLine();
                    System.out.print("Id de la discipline : ");
                    Discipline discipline = disciplineDAO.rechercherParId(lireEntier());

                    if (discipline == null) {
                        System.out.println("Discipline introuvable. Ajout annulé.");
                        continue;
                    }

                    boolean ok = competitionDAO.ajouter(new Competition(nom, date, lieu, discipline));
                    System.out.println(ok ? "Competition ajoutée." : "Echec de l'ajout.");
                }
                case 2 -> {
                    System.out.print("Id de la competition à modifier : ");
                    int id = lireEntier();
                    Competition c = competitionDAO.rechercherParId(id);
                    if (c == null) {
                        System.out.println("Competition introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau nom : ");
                    c.setNomCompetition(sc.nextLine());
                    System.out.print("Nouveau lieu : ");
                    c.setLieu(sc.nextLine());
                    boolean ok = competitionDAO.modifier(c);
                    System.out.println(ok ? "Competition modifiée." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id de la competition à supprimer : ");
                    int id = lireEntier();
                    boolean ok = competitionDAO.supprimer(id);
                    System.out.println(ok ? "Competition supprimée." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Nom recherché : ");
                    afficherListe(competitionDAO.rechercherParNom(sc.nextLine()));
                }
                case 5 -> afficherListe(competitionDAO.afficherTous());
                case 6 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // ============================================================
    // MODULE 6 : RESULTATS
    // ============================================================

    private void menuResultats() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES RESULTATS ---");
            System.out.println("1. Enregistrer resultat");
            System.out.println("2. Modifier resultat");
            System.out.println("3. Supprimer resultat");
            System.out.println("4. Classement competition");
            System.out.println("5. Afficher resultats");
            System.out.println("6. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Id de l'athlete : ");
                    Athlete athlete = athleteService.rechercherAthleteParId(lireEntier());
                    System.out.print("Id de la competition : ");
                    Competition competition = competitionDAO.rechercherParId(lireEntier());

                    if (athlete == null || competition == null) {
                        System.out.println("Athlete ou competition introuvable. Ajout annulé.");
                        continue;
                    }

                    System.out.print("Score : ");
                    double score = Double.parseDouble(sc.nextLine());
                    System.out.print("Rang : ");
                    int rang = lireEntier();

                    boolean ok = resultatDAO.ajouter(new Resultat(athlete, competition, score, rang));
                    System.out.println(ok ? "Resultat enregistré." : "Echec de l'enregistrement.");
                }
                case 2 -> {
                    System.out.print("Id du resultat à modifier : ");
                    int id = lireEntier();
                    Resultat r = resultatDAO.rechercherParId(id);
                    if (r == null) {
                        System.out.println("Resultat introuvable.");
                        continue;
                    }
                    System.out.print("Nouveau score : ");
                    r.setScore(Double.parseDouble(sc.nextLine()));
                    System.out.print("Nouveau rang : ");
                    r.setRang(lireEntier());
                    boolean ok = resultatDAO.modifier(r);
                    System.out.println(ok ? "Resultat modifié." : "Echec de la modification.");
                }
                case 3 -> {
                    System.out.print("Id du resultat à supprimer : ");
                    int id = lireEntier();
                    boolean ok = resultatDAO.supprimer(id);
                    System.out.println(ok ? "Resultat supprimé." : "Echec de la suppression.");
                }
                case 4 -> {
                    System.out.print("Id de la competition : ");
                    afficherListe(resultatDAO.classementParCompetition(lireEntier()));
                }
                case 5 -> afficherListe(resultatDAO.afficherTous());
                case 6 -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // ============================================================
    // UTILITAIRES
    // ============================================================

    /**
     * Lit un entier saisi par l'utilisateur, en gérant les erreurs de saisie
     * (texte au lieu d'un nombre) sans faire planter le programme.
     */
    private int lireEntier() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez saisir un nombre valide : ");
            }
        }
    }

    /**
     * Affiche une liste d'objets, un par ligne, en utilisant leur toString().
     */
    private <T> void afficherListe(List<T> liste) {
        if (liste.isEmpty()) {
            System.out.println("Aucun élément trouvé.");
            return;
        }
        for (T element : liste) {
            System.out.println(element);
        }
    }
}