package dao;

import model.Pays;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PaysDAO {

    public boolean ajouter(Pays p) {
        String sql = "INSERT INTO pays (nomPays, continent) VALUES (?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNomPays());
            ps.setString(2, p.getContinent());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdPays(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du pays : " + e.getMessage());
        }

        return false;
    }

    /**
     * Modifie un pays existant, identifié par son id.
     */
    public boolean modifier(Pays p) {
        String sql = "UPDATE pays SET nomPays = ?, continent = ? WHERE idPays = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, p.getNomPays());
            ps.setString(2, p.getContinent());
            ps.setInt(3, p.getIdPays());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du pays : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un pays à partir de son id.
     * Attention : supprime aussi en cascade tous les athlètes liés à ce pays (ON DELETE CASCADE).
     */
    public boolean supprimer(int idPays) {
        String sql = "DELETE FROM pays WHERE idPays = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idPays);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du pays : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche un pays par son id.
     * Retourne null si aucun pays ne correspond.
     */
    public Pays rechercherParId(int idPays) {
        String sql = "SELECT * FROM pays WHERE idPays = ?";
        Pays pays = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idPays);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pays = mapResultSetToPays(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du pays : " + e.getMessage());
        }

        return pays;
    }

    /**
     * Recherche des pays par leur nom (recherche partielle, insensible à la casse).
     */
    public List<Pays> rechercherParNom(String nomPays) {
        String sql = "SELECT * FROM pays WHERE nomPays LIKE ?";
        List<Pays> resultats = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, "%" + nomPays + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapResultSetToPays(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du pays : " + e.getMessage());
        }

        return resultats;
    }

    /**
     * Retourne la liste complète des pays.
     */
    public List<Pays> afficherTous() {
        String sql = "SELECT * FROM pays";
        List<Pays> paysList = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paysList.add(mapResultSetToPays(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des pays : " + e.getMessage());
        }

        return paysList;
    }


    private Pays mapResultSetToPays(ResultSet rs) throws SQLException {
        return new Pays(
                rs.getInt("idPays"),
                rs.getString("nomPays"),
                rs.getString("continent")
        );
    }
}