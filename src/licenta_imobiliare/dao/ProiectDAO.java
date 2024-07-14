package licenta_imobiliare.dao;

import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Parcare;
import licenta_imobiliare.model.Proiect;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProiectDAO {



    public void adaugaProiect(Proiect proiect) {
        String newId = IDGenerator.generateCustomIdWithProjectName(proiect.getNumeProiect());
        proiect.setIdProiect(newId);
        String query = "INSERT INTO Proiecte (idProiect, numeProiect, adresaProiect) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, proiect.getIdProiect());
            statement.setString(2, proiect.getNumeProiect());
            statement.setString(3, proiect.getAdresaProiect());
            statement.executeUpdate();


            ApartamentDAO apartamentDAO = new ApartamentDAO();
            for (Apartament apartament : proiect.getApartamente()) {
                apartament.setIdProiect(proiect.getIdProiect());
                apartamentDAO.adaugaApartament(apartament);
            }


            ParcareDAO parcareDAO = new ParcareDAO();
            for (Parcare parcare : proiect.getParcari()) {
                parcare.setIdProiect(proiect.getIdProiect());
                parcareDAO.adaugaParcare(parcare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Proiect getProiect(String idProiect) {
        System.out.println("Searching for project with id: " + idProiect); // Debug statement
        String query = "SELECT * FROM Proiecte WHERE idProiect = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Proiect proiect = new Proiect();
                proiect.setIdProiect(resultSet.getString("idProiect"));
                proiect.setNumeProiect(resultSet.getString("numeProiect"));
                proiect.setAdresaProiect(resultSet.getString("adresaProiect"));

                ApartamentDAO apartamentDAO = new ApartamentDAO();
                List<Apartament> apartamente = apartamentDAO.getApartamenteByProiect(idProiect);
                proiect.setApartamente(apartamente);

                ParcareDAO parcareDAO = new ParcareDAO();
                List<Parcare> parcari = parcareDAO.getParcariByProiect(idProiect);
                proiect.setParcari(parcari);

                return proiect;
            } else {
                System.out.println("No project found with id: " + idProiect); // Debug statement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Proiect> getToateProiectele() {
        List<Proiect> proiecte = new ArrayList<>();
        String query = "SELECT * FROM Proiecte";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Proiect proiect = new Proiect();
                proiect.setIdProiect(resultSet.getString("idProiect"));
                proiect.setNumeProiect(resultSet.getString("numeProiect"));
                proiect.setAdresaProiect(resultSet.getString("adresaProiect"));

                // Obținerea apartamentelor aferente proiectului
                ApartamentDAO apartamentDAO = new ApartamentDAO();
                List<Apartament> apartamente = apartamentDAO.getApartamenteByProiect(proiect.getIdProiect());
                proiect.setApartamente(apartamente);

                // Obținerea parcărilor aferente proiectului
                ParcareDAO parcareDAO = new ParcareDAO();
                List<Parcare> parcari = parcareDAO.getParcariByProiect(proiect.getIdProiect());
                proiect.setParcari(parcari);

                proiecte.add(proiect);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiecte;
    }


    public void actualizeazaProiect(Proiect proiect) {
        String query = "UPDATE Proiecte SET numeProiect = ?, adresaProiect = ? WHERE idProiect = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, proiect.getNumeProiect());
            statement.setString(2, proiect.getAdresaProiect());
            statement.setString(3, proiect.getIdProiect());
            statement.executeUpdate();


            ApartamentDAO apartamentDAO = new ApartamentDAO();
            for (Apartament apartament : proiect.getApartamente()) {
                apartamentDAO.actualizeazaApartament(apartament);
            }

            ParcareDAO parcareDAO = new ParcareDAO();
            for (Parcare parcare : proiect.getParcari()) {
                parcareDAO.actualizeazaParcare(parcare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Proiect> cautaProiecteDupaNume(String numeProiect) {
        List<Proiect> proiecte = new ArrayList<>();
        String query = "SELECT * FROM Proiecte WHERE numeProiect LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + numeProiect + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Proiect proiect = new Proiect();
                proiect.setIdProiect(resultSet.getString("idProiect"));
                proiect.setNumeProiect(resultSet.getString("numeProiect"));
                proiect.setAdresaProiect(resultSet.getString("adresaProiect"));
                ApartamentDAO apartamentDAO = new ApartamentDAO();
                List<Apartament> apartamente = apartamentDAO.getApartamenteByProiect(proiect.getIdProiect());
                proiect.setApartamente(apartamente);
                ParcareDAO parcareDAO = new ParcareDAO();
                List<Parcare> parcari = parcareDAO.getParcariByProiect(proiect.getIdProiect());
                proiect.setParcari(parcari);

                proiecte.add(proiect);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiecte;
    }

    public void stergeProiect(String idProiect) {
        String queryApartamente = "DELETE FROM Apartamente WHERE idProiect = ?";
        String queryParcari = "DELETE FROM Parcari WHERE idProiect = ?";
        String queryProiect = "DELETE FROM Proiecte WHERE idProiect = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statementApartamente = connection.prepareStatement(queryApartamente);
             PreparedStatement statementParcari = connection.prepareStatement(queryParcari);
             PreparedStatement statementProiect = connection.prepareStatement(queryProiect)) {

            statementApartamente.setString(1, idProiect);
            statementApartamente.executeUpdate();

            statementParcari.setString(1, idProiect);
            statementParcari.executeUpdate();

            statementProiect.setString(1, idProiect);
            statementProiect.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void stergeToateProiectele() {
        String query = "DELETE FROM Proiecte";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toate proiectele au fost șterse.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ProiectDAO proiectDAO = new ProiectDAO();
        proiectDAO.stergeToateProiectele();
    }


}

