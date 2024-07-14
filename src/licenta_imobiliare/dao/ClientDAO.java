package licenta_imobiliare.dao;

import licenta_imobiliare.model.Client;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {


    public Client getClient(String idClient) {
        String query = "SELECT * FROM Clienti WHERE idClient = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idClient);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToClient(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }   public boolean hasContracts(String idClient) {
        String query = "SELECT COUNT(*) FROM Contracte WHERE idClient = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idClient);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Client getClientByNume(String nume, String prenume) {
        String query = "SELECT * FROM Clienti WHERE nume = ? AND prenume = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nume);
            statement.setString(2, prenume);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToClient(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Client> getAllClients() {
        List<Client> clienti = new ArrayList<>();
        String query = "SELECT * FROM Clienti";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clienti.add(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clienti;
    }


    public List<Client> getClientsWithContracts() {
        List<Client> clienti = new ArrayList<>();
        String query = "SELECT DISTINCT c.* FROM Clienti c JOIN Contracte ct ON c.idClient = ct.idClient";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clienti.add(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clienti;
    }


    public List<Client> cautaClient(String searchTerm) {
        List<Client> clienti = new ArrayList<>();
        String query = "SELECT * FROM Clienti WHERE nume LIKE ? OR prenume LIKE ? OR cnp LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchTerm + "%");
            statement.setString(2, "%" + searchTerm + "%");
            statement.setString(3, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                clienti.add(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clienti;
    }


    public void adaugaClient(Client client) {
        String newId = generateClientId(client.getNume(), client.getPrenume());
        client.setIdClient(newId);
        String query = "INSERT INTO Clienti (idClient, nume, prenume, cnp, serie, nrBuletin, dataNastere, domiciliu, email, telefon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getIdClient());
            statement.setString(2, client.getNume());
            statement.setString(3, client.getPrenume());
            statement.setString(4, client.getCnp());
            statement.setString(5, client.getSerie());
            statement.setInt(6, client.getNrBuletin());
            statement.setDate(7, new java.sql.Date(client.getDataNastere().getTime()));
            statement.setString(8, client.getDomiciliu());
            statement.setString(9, client.getEmail());
            statement.setInt(10, client.getTelefon());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void actualizeazaClient(Client client) {
        String query = "UPDATE Clienti SET nume = ?, prenume = ?, cnp = ?, serie = ?, nrBuletin = ?, dataNastere = ?, domiciliu = ?, email = ?, telefon = ? WHERE idClient = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getNume());
            statement.setString(2, client.getPrenume());
            statement.setString(3, client.getCnp());
            statement.setString(4, client.getSerie());
            statement.setInt(5, client.getNrBuletin());
            statement.setDate(6, new java.sql.Date(client.getDataNastere().getTime()));
            statement.setString(7, client.getDomiciliu());
            statement.setString(8, client.getEmail());
            statement.setInt(9, client.getTelefon());
            statement.setString(10, client.getIdClient());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stergeClient(String idClient) {
        String query = "DELETE FROM Clienti WHERE idClient = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idClient);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Client mapResultSetToClient(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setIdClient(resultSet.getString("idClient"));
        client.setNume(resultSet.getString("nume"));
        client.setPrenume(resultSet.getString("prenume"));
        client.setCnp(resultSet.getString("cnp"));
        client.setSerie(resultSet.getString("serie"));
        client.setNrBuletin(resultSet.getInt("nrBuletin"));
        client.setDataNastere(resultSet.getDate("dataNastere"));
        client.setDomiciliu(resultSet.getString("domiciliu"));
        client.setEmail(resultSet.getString("email"));
        client.setTelefon(resultSet.getInt("telefon"));
        return client;
    }


    private String generateClientId(String nume, String prenume) {
        String idPart1 = (nume.length() >= 3 ? nume.substring(0, 3) : nume).toUpperCase();
        String uniqueCode = IDGenerator.generateUniqueId().substring(0, 8).toUpperCase();
        return idPart1 + uniqueCode;
    }

    public void stergeTotiClientii() {


        String query = "DELETE FROM Clienti";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toți clienții au fost șterși.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.stergeTotiClientii();
    }
}
