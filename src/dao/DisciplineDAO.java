package dao;

import model.Discipline;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DisciplineDAO {

    public boolean ajouter(Discipline d) {
        String sql = "INSERT INTO discipline (nomDiscipline, description) VALUES (?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getNomDiscipline());
            ps.setString(2, d.getDescription());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        d.setIdDiscipline(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la discipline : " + e.getMessage());
        }

        return false;
    }

    /**
     * Modifie une discipline existante, identifiée par son id.
     */
    public boolean modifier(Discipline d) {
        String sql = "UPDATE discipline SET nomDiscipline = ?, description = ? WHERE idDiscipline = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, d.getNomDiscipline());
            ps.setString(2, d.getDescription());
            ps.setInt(3, d.getIdDiscipline());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la discipline : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime une discipline à partir de son id.
     * Attention : supprime aussi en cascade les athlètes et compétitions liés (ON DELETE CASCADE).
     */
    public boolean supprimer(int idDiscipline) {
        String sql = "DELETE FROM discipline WHERE idDiscipline = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idDiscipline);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la discipline : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche une discipline par son id.
     * Retourne null si aucune discipline ne correspond.
     */
    public Discipline rechercherParId(int idDiscipline) {
        String sql = "SELECT * FROM discipline WHERE idDiscipline = ?";
        Discipline discipline = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idDiscipline);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    discipline = mapResultSetToDiscipline(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la discipline : " + e.getMessage());
        }

        return discipline;
    }

    /**
     * Recherche des disciplines par leur nom (recherche partielle).
     */
    public List<Discipline> rechercherParNom(String nomDiscipline) {
        String sql = "SELECT * FROM discipline WHERE nomDiscipline LIKE ?";
        List<Discipline> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, "%" + nomDiscipline + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapResultSetToDiscipline(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la discipline : " + e.getMessage());
        }

        return resultats;
    }

    /**
     * Retourne la liste complète des disciplines.
     */
    public List<Discipline> afficherTous() {
        String sql = "SELECT * FROM discipline";
        List<Discipline> disciplines = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                disciplines.add(mapResultSetToDiscipline(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des disciplines : " + e.getMessage());
        }

        return disciplines;
    }

    private Discipline mapResultSetToDiscipline(ResultSet rs) throws SQLException {
        return new Discipline(
                rs.getInt("idDiscipline"),
                rs.getString("nomDiscipline"),
                rs.getString("description")
        );
    }
}