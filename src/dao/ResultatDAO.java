package dao;

import model.Resultat;
import model.Athlete;
import model.Competition;
import model.Pays;
import model.Discipline;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResultatDAO {


    private static final String SELECT_BASE =
            "SELECT r.idResultat, r.score, r.rang, " +
                    "a.idAthlete, a.nom AS athNom, a.prenom AS athPrenom, a.sexe, a.dateNaissance, " +
                    "p.idPays, p.nomPays, p.continent, " +
                    "da.idDiscipline AS athIdDiscipline, da.nomDiscipline AS athNomDiscipline, da.description AS athDescription, " +
                    "c.idCompetition, c.nomCompetition, c.dateCompetition, c.lieu, " +
                    "dc.idDiscipline AS compIdDiscipline, dc.nomDiscipline AS compNomDiscipline, dc.description AS compDescription " +
                    "FROM resultat r " +
                    "JOIN athlete a ON r.idAthlete = a.idAthlete " +
                    "JOIN pays p ON a.idPays = p.idPays " +
                    "JOIN discipline da ON a.idDiscipline = da.idDiscipline " +
                    "JOIN competition c ON r.idCompetition = c.idCompetition " +
                    "JOIN discipline dc ON c.idDiscipline = dc.idDiscipline";

    /**
     * Enregistre un nouveau résultat en base.
     * L'objet Resultat doit déjà avoir un Athlete et une Competition existants (avec leur id).
     */
    public boolean ajouter(Resultat r) {
        String sql = "INSERT INTO resultat (idAthlete, idCompetition, score, rang) VALUES (?, ?, ?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getAthlete().getIdAthlete());
            ps.setInt(2, r.getCompetition().getIdCompetition());
            ps.setDouble(3, r.getScore());
            ps.setInt(4, r.getRang());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        r.setIdResultat(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement du résultat : " + e.getMessage());
        }

        return false;
    }

    /**
     * Modifie un résultat existant, identifié par son id.
     */
    public boolean modifier(Resultat r) {
        String sql = "UPDATE resultat SET idAthlete = ?, idCompetition = ?, score = ?, rang = ? WHERE idResultat = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, r.getAthlete().getIdAthlete());
            ps.setInt(2, r.getCompetition().getIdCompetition());
            ps.setDouble(3, r.getScore());
            ps.setInt(4, r.getRang());
            ps.setInt(5, r.getIdResultat());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du résultat : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un résultat à partir de son id.
     */
    public boolean supprimer(int idResultat) {
        String sql = "DELETE FROM resultat WHERE idResultat = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idResultat);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du résultat : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche un résultat par son id, avec Athlete et Competition complets.
     */
    public Resultat rechercherParId(int idResultat) {
        String sql = SELECT_BASE + " WHERE r.idResultat = ?";
        Resultat resultat = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idResultat);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultat = mapResultSetToResultat(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du résultat : " + e.getMessage());
        }

        return resultat;
    }

    /**
     * Retourne la liste complète des résultats.
     */
    public List<Resultat> afficherTous() {
        List<Resultat> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.add(mapResultSetToResultat(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des résultats : " + e.getMessage());
        }

        return resultats;
    }

    /**
     * Retourne le classement complet d'une compétition, trié par rang croissant
     * (1er, 2e, 3e...). Utilisé pour le module "Classement compétition".
     */
    public List<Resultat> classementParCompetition(int idCompetition) {
        String sql = SELECT_BASE + " WHERE c.idCompetition = ? ORDER BY r.rang ASC";
        List<Resultat> classement = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idCompetition);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    classement.add(mapResultSetToResultat(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors du classement de la compétition : " + e.getMessage());
        }

        return classement;
    }


    public List<Resultat> afficherResultatsMedailles() {
        String sql = SELECT_BASE + " WHERE r.rang IN (1, 2, 3) ORDER BY r.rang ASC";
        List<Resultat> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.add(mapResultSetToResultat(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul des médailles : " + e.getMessage());
        }

        return resultats;
    }


    private Resultat mapResultSetToResultat(ResultSet rs) throws SQLException {

        Pays pays = new Pays(
                rs.getInt("idPays"),
                rs.getString("nomPays"),
                rs.getString("continent")
        );


        Discipline disciplineAthlete = new Discipline(
                rs.getInt("athIdDiscipline"),
                rs.getString("athNomDiscipline"),
                rs.getString("athDescription")
        );


        LocalDate dateNaissance = rs.getDate("dateNaissance").toLocalDate();
        Athlete athlete = new Athlete(
                rs.getInt("idAthlete"),
                rs.getString("athNom"),
                rs.getString("athPrenom"),
                rs.getString("sexe"),
                dateNaissance,
                pays,
                disciplineAthlete
        );


        Discipline disciplineCompetition = new Discipline(
                rs.getInt("compIdDiscipline"),
                rs.getString("compNomDiscipline"),
                rs.getString("compDescription")
        );


        LocalDate dateCompetition = rs.getDate("dateCompetition").toLocalDate();
        Competition competition = new Competition(
                rs.getInt("idCompetition"),
                rs.getString("nomCompetition"),
                dateCompetition,
                rs.getString("lieu"),
                disciplineCompetition
        );


        return new Resultat(
                rs.getInt("idResultat"),
                athlete,
                competition,
                rs.getDouble("score"),
                rs.getInt("rang")
        );
    }
}
