package licenta_imobiliare.dao;

import licenta_imobiliare.model.Rezervare;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;
import licenta_imobiliare.model.Client;
import licenta_imobiliare.model.Apartament;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RezervareDAO {

    public void adaugaRezervare(Rezervare rezervare) {
        String newId = IDGenerator.generateUniqueId();
        rezervare.setIdRezervare(newId);
        String query = "INSERT INTO Rezervari (idRezervare, idApartament, idClient) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rezervare.getIdRezervare());
            statement.setString(2, rezervare.getApartament().getIdApartament());
            statement.setString(3, rezervare.getClient().getIdClient());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Rezervare getRezervare(String idRezervare) {
        String query = "SELECT * FROM Rezervari WHERE idRezervare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idRezervare);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Rezervare rezervare = new Rezervare();
                rezervare.setIdRezervare(resultSet.getString("idRezervare"));

                ClientDAO clientDAO = new ClientDAO();
                rezervare.setClient(clientDAO.getClient(resultSet.getString("idClient")));

                ApartamentDAO apartamentDAO = new ApartamentDAO();
                rezervare.setApartament(apartamentDAO.getApartament(resultSet.getString("idApartament")));

                rezervare.setDataVizionare(resultSet.getDate("dataVizionare"));
                rezervare.setOraVizionare(resultSet.getString("oraVizionare"));
                return rezervare;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Rezervare> getToateRezervarile() {
        List<Rezervare> rezervari = new ArrayList<>();
        String query = "SELECT * FROM Rezervari";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Rezervare rezervare = new Rezervare();
                rezervare.setIdRezervare(resultSet.getString("idRezervare"));

                ClientDAO clientDAO = new ClientDAO();
                rezervare.setClient(clientDAO.getClient(resultSet.getString("idClient")));

                ApartamentDAO apartamentDAO = new ApartamentDAO();
                rezervare.setApartament(apartamentDAO.getApartament(resultSet.getString("idApartament")));

                rezervare.setDataVizionare(resultSet.getDate("dataVizionare"));
                rezervare.setOraVizionare(resultSet.getString("oraVizionare"));
                rezervari.add(rezervare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezervari;
    }

    public void actualizeazaRezervare(Rezervare rezervare) {
        String query = "UPDATE Rezervari SET idClient = ?, idApartament = ?, dataVizionare = ?, oraVizionare = ? WHERE idRezervare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rezervare.getClient().getIdClient());
            statement.setString(2, rezervare.getApartament().getIdApartament());
            statement.setDate(3, new java.sql.Date(rezervare.getDataVizionare().getTime()));
            statement.setString(4, rezervare.getOraVizionare());
            statement.setString(5, rezervare.getIdRezervare());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stergeRezervare(String idRezervare) {
        String query = "DELETE FROM Rezervari WHERE idRezervare = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idRezervare);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Rezervare> getRezervariById(String id) {
        List<Rezervare> rezervari = new ArrayList<>();
        String query = "SELECT * FROM Rezervari WHERE idRezervare LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + id + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Rezervare rezervare = new Rezervare();
                rezervare.setIdRezervare(resultSet.getString("idRezervare"));

                ClientDAO clientDAO = new ClientDAO();
                rezervare.setClient(clientDAO.getClient(resultSet.getString("idClient")));

                ApartamentDAO apartamentDAO = new ApartamentDAO();
                rezervare.setApartament(apartamentDAO.getApartament(resultSet.getString("idApartament")));

                rezervare.setDataVizionare(resultSet.getDate("dataVizionare"));
                rezervare.setOraVizionare(resultSet.getString("oraVizionare"));
                rezervari.add(rezervare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezervari;
    }
}
