package dao;

import model.Athlete;
import model.Pays;
import model.Discipline;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AthleteDAO {


    private static final String SELECT_BASE =
            "SELECT a.idAthlete, a.nom, a.prenom, a.sexe, a.dateNaissance, " +
                    "p.idPays, p.nomPays, p.continent, " +
                    "d.idDiscipline, d.nomDiscipline, d.description " +
                    "FROM athlete a " +
                    "JOIN pays p ON a.idPays = p.idPays " +
                    "JOIN discipline d ON a.idDiscipline = d.idDiscipline";


    public boolean ajouter(Athlete a) {
        String sql = "INSERT INTO athlete (nom, prenom, sexe, dateNaissance, idPays, idDiscipline) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getSexe());
            ps.setDate(4, Date.valueOf(a.getDateNaissance()));
            ps.setInt(5, a.getPays().getIdPays());
            ps.setInt(6, a.getDiscipline().getIdDiscipline());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setIdAthlete(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'athlète : " + e.getMessage());
        }

        return false;
    }


    public boolean modifier(Athlete a) {
        String sql = "UPDATE athlete SET nom = ?, prenom = ?, sexe = ?, dateNaissance = ?, idPays = ?, idDiscipline = ? WHERE idAthlete = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getSexe());
            ps.setDate(4, Date.valueOf(a.getDateNaissance()));
            ps.setInt(5, a.getPays().getIdPays());
            ps.setInt(6, a.getDiscipline().getIdDiscipline());
            ps.setInt(7, a.getIdAthlete());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'athlète : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un athlète à partir de son id.
     */
    public boolean supprimer(int idAthlete) {
        String sql = "DELETE FROM athlete WHERE idAthlete = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idAthlete);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'athlète : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche un athlète par son id, avec son Pays et sa Discipline complets.
     */
    public Athlete rechercherParId(int idAthlete) {
        String sql = SELECT_BASE + " WHERE a.idAthlete = ?";
        Athlete athlete = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idAthlete);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    athlete = mapResultSetToAthlete(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'athlète : " + e.getMessage());
        }

        return athlete;
    }

    /**
     * Recherche des athlètes par nom ou prénom (recherche partielle).
     */
    public List<Athlete> rechercherParNom(String motCle) {
        String sql = SELECT_BASE + " WHERE a.nom LIKE ? OR a.prenom LIKE ?";
        List<Athlete> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, "%" + motCle + "%");
            ps.setString(2, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapResultSetToAthlete(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'athlète : " + e.getMessage());
        }

        return resultats;
    }

    /**
     * Retourne la liste complète des athlètes, avec leur Pays et Discipline.
     */
    public List<Athlete> afficherTous() {
        List<Athlete> athletes = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                athletes.add(mapResultSetToAthlete(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des athlètes : " + e.getMessage());
        }

        return athletes;
    }

    /**
     * Retourne la liste des athlètes appartenant à un pays donné.
     * Utile pour le tableau des médailles (regrouper par pays).
     */
    public List<Athlete> afficherParPays(int idPays) {
        String sql = SELECT_BASE + " WHERE p.idPays = ?";
        List<Athlete> athletes = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idPays);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    athletes.add(mapResultSetToAthlete(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des athlètes par pays : " + e.getMessage());
        }

        return athletes;
    }

    /**
     * Convertit une ligne du ResultSet (résultat du JOIN) en objet Athlete complet,
     * avec ses objets Pays et Discipline déjà construits.
     */
    private Athlete mapResultSetToAthlete(ResultSet rs) throws SQLException {
        Pays pays = new Pays(
                rs.getInt("idPays"),
                rs.getString("nomPays"),
                rs.getString("continent")
        );

        Discipline discipline = new Discipline(
                rs.getInt("idDiscipline"),
                rs.getString("nomDiscipline"),
                rs.getString("description")
        );

        LocalDate dateNaissance = rs.getDate("dateNaissance").toLocalDate();

        return new Athlete(
                rs.getInt("idAthlete"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("sexe"),
                dateNaissance,
                pays,
                discipline
        );
    }
}
