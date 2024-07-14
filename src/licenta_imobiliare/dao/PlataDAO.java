package licenta_imobiliare.dao;

import licenta_imobiliare.model.Client;
import licenta_imobiliare.model.Factura;
import licenta_imobiliare.model.Plata;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlataDAO {


    public void adaugaPlata(Plata plata) {
        String newId = IDGenerator.generateCustomId("PLATA");
        plata.setIdPlata(newId);
        String query = "INSERT INTO Plati (idPlata, suma, data, idClient, idFactura) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, plata.getIdPlata());
            statement.setDouble(2, plata.getSuma());
            statement.setDate(3, new java.sql.Date(plata.getData().getTime()));
            statement.setString(4, plata.getClient().getIdClient());
            statement.setString(5, plata.getFactura().getIdFactura());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Plata> getPlataById(String idPlata) {
        List<Plata> plati = new ArrayList<>();
        String query = "SELECT * FROM Plati WHERE idPlata LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + idPlata + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Plata plata = mapResultSetToPlata(resultSet);
                plati.add(plata);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plati;
    }

    public List<Plata> getToatePlatile() {
        List<Plata> plati = new ArrayList<>();
        String query = "SELECT * FROM Plati";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Plata plata = mapResultSetToPlata(resultSet);
                plati.add(plata);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plati;
    }


    public void actualizeazaPlata(Plata plata) {
        String query = "UPDATE Plati SET suma = ?, data = ?, idClient = ?, idFactura = ? WHERE idPlata = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, plata.getSuma());
            statement.setDate(2, new java.sql.Date(plata.getData().getTime()));
            statement.setString(3, plata.getClient().getIdClient());
            statement.setString(4, plata.getFactura().getIdFactura());
            statement.setString(5, plata.getIdPlata());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stergePlata(String idPlata) {
        String query = "DELETE FROM Plati WHERE idPlata = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idPlata);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Plata> getPlatiByDateRange(Date startDate, Date endDate) {
        List<Plata> plati = new ArrayList<>();
        String query = "SELECT * FROM Plati WHERE data BETWEEN ? AND ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                plati.add(mapResultSetToPlata(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plati;
    }

    private Plata mapResultSetToPlata(ResultSet resultSet) throws SQLException {
        Plata plata = new Plata();
        plata.setIdPlata(resultSet.getString("idPlata"));
        plata.setSuma(resultSet.getDouble("suma"));
        plata.setData(resultSet.getDate("data"));

        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.getClient(resultSet.getString("idClient"));
        plata.setClient(client);

        FacturaDAO facturaDAO = new FacturaDAO();
        Factura factura = facturaDAO.getFacturaById(resultSet.getString("idFactura"));
        plata.setFactura(factura);

        return plata;
    }

    public void stergeToatePlatile() {
        String query = "DELETE FROM Plati";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toate plățile au fost șterse.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PlataDAO plataDAO = new PlataDAO();
        plataDAO.stergeToatePlatile();
    }
}
