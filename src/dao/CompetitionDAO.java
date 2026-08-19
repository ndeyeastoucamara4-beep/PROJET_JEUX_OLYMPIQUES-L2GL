package dao;

import model.Competition;
import model.Discipline;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CompetitionDAO {

    // Requête de base avec JOIN, réutilisée dans plusieurs méthodes
    private static final String SELECT_BASE =
            "SELECT c.idCompetition, c.nomCompetition, c.dateCompetition, c.lieu, " +
                    "d.idDiscipline, d.nomDiscipline, d.description " +
                    "FROM competition c " +
                    "JOIN discipline d ON c.idDiscipline = d.idDiscipline";


    public boolean ajouter(Competition c) {
        String sql = "INSERT INTO competition (nomCompetition, dateCompetition, lieu, idDiscipline) VALUES (?, ?, ?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNomCompetition());
            ps.setDate(2, Date.valueOf(c.getDateCompetition()));
            ps.setString(3, c.getLieu());
            ps.setInt(4, c.getDiscipline().getIdDiscipline());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCompetition(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la compétition : " + e.getMessage());
        }

        return false;
    }

    /**
     * Modifie une compétition existante, identifiée par son id.
     */
    public boolean modifier(Competition c) {
        String sql = "UPDATE competition SET nomCompetition = ?, dateCompetition = ?, lieu = ?, idDiscipline = ? WHERE idCompetition = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, c.getNomCompetition());
            ps.setDate(2, Date.valueOf(c.getDateCompetition()));
            ps.setString(3, c.getLieu());
            ps.setInt(4, c.getDiscipline().getIdDiscipline());
            ps.setInt(5, c.getIdCompetition());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la compétition : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime une compétition à partir de son id.
     * Attention : supprime aussi en cascade les résultats liés (ON DELETE CASCADE).
     */
    public boolean supprimer(int idCompetition) {
        String sql = "DELETE FROM competition WHERE idCompetition = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idCompetition);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la compétition : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche une compétition par son id, avec sa Discipline complète.
     */
    public Competition rechercherParId(int idCompetition) {
        String sql = SELECT_BASE + " WHERE c.idCompetition = ?";
        Competition competition = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idCompetition);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    competition = mapResultSetToCompetition(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la compétition : " + e.getMessage());
        }

        return competition;
    }

    /**
     * Recherche des compétitions par leur nom (recherche partielle).
     */
    public List<Competition> rechercherParNom(String nomCompetition) {
        String sql = SELECT_BASE + " WHERE c.nomCompetition LIKE ?";
        List<Competition> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, "%" + nomCompetition + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapResultSetToCompetition(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la compétition : " + e.getMessage());
        }

        return resultats;
    }

    /**
     * Retourne la liste complète des compétitions, avec leur Discipline.
     */
    public List<Competition> afficherTous() {
        List<Competition> competitions = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                competitions.add(mapResultSetToCompetition(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des compétitions : " + e.getMessage());
        }

        return competitions;
    }


    private Competition mapResultSetToCompetition(ResultSet rs) throws SQLException {
        Discipline discipline = new Discipline(
                rs.getInt("idDiscipline"),
                rs.getString("nomDiscipline"),
                rs.getString("description")
        );

        LocalDate dateCompetition = rs.getDate("dateCompetition").toLocalDate();

        return new Competition(
                rs.getInt("idCompetition"),
                rs.getString("nomCompetition"),
                dateCompetition,
                rs.getString("lieu"),
                discipline
        );
    }
}