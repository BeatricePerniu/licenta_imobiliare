package licenta_imobiliare.dao;

import licenta_imobiliare.model.*;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private Connection connection;

    public FacturaDAO() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }


    public void adaugaFactura(Factura factura) {
        String newId = IDGenerator.generateCustomId("FACTURA");
        factura.setIdFactura(newId);
        String query = "INSERT INTO Facturi (idFactura, dataEmiterii, idClient, idApartament, pretApartament, tva, estePlatita, dataScadenta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, factura.getIdFactura());
            statement.setDate(2, new java.sql.Date(factura.getDataEmiterii().getTime()));
            statement.setString(3, factura.getClient().getIdClient());
            statement.setString(4, factura.getApartament().getIdApartament());
            statement.setDouble(5, factura.getPretApartament());
            statement.setDouble(6, factura.getTva());
            statement.setBoolean(7, factura.isEstePlatita());
            statement.setDate(8, new java.sql.Date(factura.getDataScadenta().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Factura> getFacturiByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi WHERE dataEmiterii BETWEEN ? AND ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }


    public Factura getFacturaById(String idFactura) throws SQLException {
        String query = "SELECT * FROM Facturi WHERE idFactura = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idFactura);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToFactura(resultSet);
            }
        }
        return null;
    }


    public List<Factura> getToateFacturile() {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }

    public void actualizeazaFactura(Factura factura) {
        String query = "UPDATE Facturi SET dataEmiterii = ?, idClient = ?, idApartament = ?, pretApartament = ?, tva = ?, estePlatita = ?, dataScadenta = ? WHERE idFactura = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(factura.getDataEmiterii().getTime()));
            statement.setString(2, factura.getClient().getIdClient());
            statement.setString(3, factura.getApartament().getIdApartament());
            statement.setDouble(4, factura.getPretApartament());
            statement.setDouble(5, factura.getTva());
            statement.setBoolean(6, factura.isEstePlatita());
            statement.setDate(7, new java.sql.Date(factura.getDataScadenta().getTime()));
            statement.setString(8, factura.getIdFactura());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Factura> getFacturiByMonthAndYear(int month, int year) {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi WHERE ";

        if (month != -1) {
            query += "MONTH(dataEmiterii) = ? AND ";
        }

        if (year != -1) {
            query += "YEAR(dataEmiterii) = ? AND ";
        }

        query = query.substring(0, query.length() - 4); // Remove the last "AND"

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int paramIndex = 1;
            if (month != -1) {
                statement.setInt(paramIndex++, month);
            }

            if (year != -1) {
                statement.setInt(paramIndex++, year);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }


    public List<Factura> getFacturiByExactDate(int day, int month, int year) {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi WHERE DAY(dataEmiterii) = ? AND MONTH(dataEmiterii) = ? AND YEAR(dataEmiterii) = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, day);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }


    public List<Factura> getFacturiById(String id) {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi WHERE idFactura LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + id + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }


    private Factura mapResultSetToFactura(ResultSet resultSet) throws SQLException {
        Factura factura = new Factura();
        factura.setIdFactura(resultSet.getString("idFactura"));
        factura.setDataEmiterii(resultSet.getDate("dataEmiterii"));


        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.getClient(resultSet.getString("idClient"));
        factura.setClient(client);


        ApartamentDAO apartamentDAO = new ApartamentDAO();
        Apartament apartament = apartamentDAO.getApartament(resultSet.getString("idApartament"));
        factura.setApartament(apartament);

        factura.setPretApartament(resultSet.getDouble("pretApartament"));
        factura.setTva(resultSet.getDouble("tva"));
        factura.setEstePlatita(resultSet.getBoolean("estePlatita"));
        factura.setDataScadenta(resultSet.getDate("dataScadenta"));

        return factura;
    }

    public List<Factura> getFacturiByClientId(String clientId) {
        List<Factura> facturi = new ArrayList<>();
        String query = "SELECT * FROM Facturi WHERE idClient = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Factura factura = mapResultSetToFactura(resultSet);
                facturi.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturi;
    }
    public void stergeToateFacturile() {
        String query = "DELETE FROM Facturi";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toate facturile au fost șterse.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        FacturaDAO facturaDAO = new FacturaDAO();
        facturaDAO.stergeToateFacturile();
    }
}
