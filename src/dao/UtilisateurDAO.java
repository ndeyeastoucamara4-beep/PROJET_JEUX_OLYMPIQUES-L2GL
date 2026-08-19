package dao;

import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UtilisateurDAO {


    public Utilisateur authentifier(String login, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE login = ? AND motDePasse = ?";
        Utilisateur utilisateur = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, motDePasse);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
        }

        return utilisateur;
    }


    public boolean ajouter(Utilisateur u) {
        String sql = "INSERT INTO utilisateur (nomComplet, login, motDePasse, role) VALUES (?, ?, ?, ?)";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNomComplet());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getRole());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                // Récupère l'id généré automatiquement par MySQL (AUTO_INCREMENT)
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        u.setIdUtilisateur(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }

        return false;
    }


    public boolean modifier(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nomComplet = ?, login = ?, motDePasse = ?, role = ? WHERE idUtilisateur = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, u.getNomComplet());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getIdUtilisateur());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
        }

        return false;
    }


    public boolean supprimer(int idUtilisateur) {
        String sql = "DELETE FROM utilisateur WHERE idUtilisateur = ?";

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }

        return false;
    }


    public Utilisateur rechercherParId(int idUtilisateur) {
        String sql = "SELECT * FROM utilisateur WHERE idUtilisateur = ?";
        Utilisateur utilisateur = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }

        return utilisateur;
    }


    public Utilisateur rechercherParLogin(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        Utilisateur utilisateur = null;

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }

        return utilisateur;
    }


    public List<Utilisateur> afficherTous() {
        String sql = "SELECT * FROM utilisateur";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection cnx = Database.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des utilisateurs : " + e.getMessage());
        }

        return utilisateurs;
    }


    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("idUtilisateur"),
                rs.getString("nomComplet"),
                rs.getString("login"),
                rs.getString("motDePasse"),
                rs.getString("role")
        );
    }
}