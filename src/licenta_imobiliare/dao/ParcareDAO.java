package licenta_imobiliare.dao;

import licenta_imobiliare.model.Parcare;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcareDAO {


    public void adaugaParcare(Parcare parcare) {
        String newId = IDGenerator.generateUniqueId();
        parcare.setIdParcare(newId);
        String query = "INSERT INTO Parcari (idParcare, numarLoc, pretVanzare, pretChirie, idProiect) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, parcare.getIdParcare());
            statement.setInt(2, parcare.getNumarLoc());
            statement.setDouble(3, parcare.getPretVanzare());
            statement.setDouble(4, parcare.getPretChirie());
            statement.setString(5, parcare.getIdProiect());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Parcare getParcare(String idParcare) {
        String query = "SELECT * FROM Parcari WHERE idParcare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idParcare);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Parcare parcare = new Parcare();
                parcare.setIdParcare(resultSet.getString("idParcare"));
                parcare.setNumarLoc(resultSet.getInt("numarLoc"));
                parcare.setPretVanzare(resultSet.getDouble("pretVanzare"));
                parcare.setPretChirie(resultSet.getDouble("pretChirie"));
                parcare.setIdProiect(resultSet.getString("idProiect"));
                return parcare;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Parcare> getToateParcarile() {
        List<Parcare> parcari = new ArrayList<>();
        String query = "SELECT * FROM Parcari";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Parcare parcare = new Parcare();
                parcare.setIdParcare(resultSet.getString("idParcare"));
                parcare.setNumarLoc(resultSet.getInt("numarLoc"));
                parcare.setPretVanzare(resultSet.getDouble("pretVanzare"));
                parcare.setPretChirie(resultSet.getDouble("pretChirie"));
                parcare.setIdProiect(resultSet.getString("idProiect"));
                parcari.add(parcare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parcari;
    }


    public void actualizeazaParcare(Parcare parcare) {
        String query = "UPDATE Parcari SET numarLoc = ?, pretVanzare = ?, pretChirie = ?, idProiect = ? WHERE idParcare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, parcare.getNumarLoc());
            statement.setDouble(2, parcare.getPretVanzare());
            statement.setDouble(3, parcare.getPretChirie());
            statement.setString(4, parcare.getIdProiect());
            statement.setString(5, parcare.getIdParcare());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stergeParcare(String idParcare) {
        String query = "DELETE FROM Parcari WHERE idParcare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idParcare);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Parcare> getParcariByProiect(String idProiect) {
        List<Parcare> parcari = new ArrayList<>();
        String query = "SELECT * FROM Parcari WHERE idProiect = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Parcare parcare = new Parcare();
                parcare.setIdParcare(resultSet.getString("idParcare"));
                parcare.setNumarLoc(resultSet.getInt("numarLoc"));
                parcare.setPretVanzare(resultSet.getDouble("pretVanzare"));
                parcare.setPretChirie(resultSet.getDouble("pretChirie"));
                parcare.setIdProiect(resultSet.getString("idProiect"));
                parcari.add(parcare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parcari;
    }
}
