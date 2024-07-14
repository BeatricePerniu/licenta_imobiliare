package licenta_imobiliare.dao;

import licenta_imobiliare.model.Angajat;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AngajatDAO {

    public Angajat getAngajat(String idAngajat) {
        String query = "SELECT * FROM Angajati WHERE idAngajat = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAngajat);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Angajat angajat = mapResultSetToAngajat(resultSet);
                angajat.setDisponibilitate(getDisponibilitateAngajat(angajat.getIdAngajat()));
                return angajat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public HashMap<Date, List<String>> getDisponibilitateAngajat(String idAngajat) {
        HashMap<Date, List<String>> disponibilitate = new HashMap<>();
        String query = "SELECT * FROM DisponibilitateAngajati WHERE idAngajat = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAngajat);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date data = new Date(resultSet.getDate("dataDisponibilitate").getTime());
                String ora = resultSet.getString("oraDisponibilitate");
                disponibilitate.computeIfAbsent(data, k -> new ArrayList<>()).add(ora);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disponibilitate;
    }


    public void adaugaDisponibilitate(String idAngajat, Date data, String ora) {
        String query = "INSERT INTO DisponibilitateAngajati (idAngajat, dataDisponibilitate, oraDisponibilitate) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAngajat);
            statement.setDate(2, new java.sql.Date(data.getTime()));
            statement.setString(3, ora);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stergeDisponibilitate(String idAngajat, Date data, String ora) {
        String query = "DELETE FROM DisponibilitateAngajati WHERE idAngajat = ? AND dataDisponibilitate = ? AND oraDisponibilitate = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAngajat);
            statement.setDate(2, new java.sql.Date(data.getTime()));
            statement.setString(3, ora);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Angajat mapResultSetToAngajat(ResultSet resultSet) throws SQLException {
        Angajat angajat = new Angajat();
        angajat.setIdAngajat(resultSet.getString("idAngajat"));
        angajat.setNumeAngajat(resultSet.getString("numeAngajat"));
        angajat.setPrenumeAngajat(resultSet.getString("prenumeAngajat"));
        angajat.setCnpAngajat(resultSet.getString("cnpAngajat"));
        angajat.setNrTelAngajat(resultSet.getInt("nrTelAngajat"));
        angajat.setDataNastereAngajat(resultSet.getDate("dataNastereAngajat"));
        angajat.setEmailAngajat(resultSet.getString("emailAngajat"));
        angajat.setSectorAngajat(resultSet.getString("sectorAngajat"));
        return angajat;
    }


    public List<Angajat> getAllAngajati() {
        List<Angajat> angajati = new ArrayList<>();
        String query = "SELECT * FROM Angajati";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Angajat angajat = mapResultSetToAngajat(resultSet);
                angajat.setDisponibilitate(getDisponibilitateAngajat(angajat.getIdAngajat()));
                angajati.add(angajat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return angajati;
    }


    public void adaugaAngajat(Angajat angajat) {
        String newId = generateAngajatId(angajat.getNumeAngajat(), angajat.getPrenumeAngajat());
        angajat.setIdAngajat(newId);
        String query = "INSERT INTO Angajati (idAngajat, numeAngajat, prenumeAngajat, cnpAngajat, nrTelAngajat, dataNastereAngajat, emailAngajat, sectorAngajat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, angajat.getIdAngajat());
            statement.setString(2, angajat.getNumeAngajat());
            statement.setString(3, angajat.getPrenumeAngajat());
            statement.setString(4, angajat.getCnpAngajat());
            statement.setInt(5, angajat.getNrTelAngajat());
            statement.setDate(6, new java.sql.Date(angajat.getDataNastereAngajat().getTime()));
            statement.setString(7, angajat.getEmailAngajat());
            statement.setString(8, angajat.getSectorAngajat());
            statement.executeUpdate();


            for (HashMap.Entry<Date, List<String>> entry : angajat.getDisponibilitate().entrySet()) {
                Date data = entry.getKey();
                for (String ora : entry.getValue()) {
                    adaugaDisponibilitate(angajat.getIdAngajat(), data, ora);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateAngajatId(String nume, String prenume) {
        String idPart1 = (nume.length() >= 3 ? nume.substring(0, 3) : nume).toUpperCase();
        String idPart2 = (prenume.length() >= 3 ? prenume.substring(0, 3) : prenume).toUpperCase();
        String uniqueCode = IDGenerator.generateUniqueId().substring(0, 8).toUpperCase();
        return idPart1 + idPart2 + uniqueCode;
    }


    public void actualizeazaAngajat(Angajat angajat) {
        String query = "UPDATE Angajati SET numeAngajat = ?, prenumeAngajat = ?, cnpAngajat = ?, nrTelAngajat = ?, dataNastereAngajat = ?, emailAngajat = ?, sectorAngajat = ? WHERE idAngajat = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, angajat.getNumeAngajat());
            statement.setString(2, angajat.getPrenumeAngajat());
            statement.setString(3, angajat.getCnpAngajat());
            statement.setInt(4, angajat.getNrTelAngajat());
            statement.setDate(5, new java.sql.Date(angajat.getDataNastereAngajat().getTime()));
            statement.setString(6, angajat.getEmailAngajat());
            statement.setString(7, angajat.getSectorAngajat());
            statement.setString(8, angajat.getIdAngajat());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stergeAngajat(String idAngajat) {
        String query = "DELETE FROM Angajati WHERE idAngajat = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAngajat);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Angajat getAngajatByNume(String nume, String prenume) {
        String query = "SELECT * FROM Angajati WHERE numeAngajat = ? AND prenumeAngajat = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nume);
            statement.setString(2, prenume);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Angajat angajat = mapResultSetToAngajat(resultSet);
                angajat.setDisponibilitate(getDisponibilitateAngajat(angajat.getIdAngajat()));
                return angajat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stergeTotiAngajatii() {
        String query = "DELETE FROM Angajati";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toți angajații au fost șterși.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AngajatDAO angajatDAO = new AngajatDAO();
        angajatDAO.stergeTotiAngajatii();
    }
}
