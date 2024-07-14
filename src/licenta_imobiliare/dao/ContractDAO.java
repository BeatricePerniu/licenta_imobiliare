package licenta_imobiliare.dao;

import licenta_imobiliare.model.*;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {


    public void adaugaContract(Contract contract) {
        String newId;
        if (contract instanceof ContractDeInchiriere) {
            newId = IDGenerator.generateCustomId("CHIRIE");
        } else if (contract instanceof ContractDeVanzare) {
            newId = IDGenerator.generateCustomId("VANZARE");
        } else {
            throw new IllegalArgumentException("Tip de contract necunoscut");
        }

        contract.setIdContract(newId);
        String query = "INSERT INTO Contracte (idContract, dataInceput, idClient, idApartament, tipContract, pret) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contract.getIdContract());
            statement.setDate(2, new java.sql.Date(contract.getDataInceput().getTime()));
            statement.setString(3, contract.getClient().getIdClient());
            statement.setString(4, contract.getApartament().getIdApartament());
            if (contract instanceof ContractDeInchiriere) {
                statement.setString(5, "Inchiriere");
                statement.setDouble(6, ((ContractDeInchiriere) contract).getChirieLunara());
            } else if (contract instanceof ContractDeVanzare) {
                statement.setString(5, "Vanzare");
                statement.setDouble(6, ((ContractDeVanzare) contract).getPretVanzare());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Contract getContract(String idContract) {
        String query = "SELECT * FROM Contracte WHERE idContract = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idContract);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String tipContract = resultSet.getString("tipContract");
                if ("Inchiriere".equals(tipContract)) {
                    ContractDeInchiriere contract = new ContractDeInchiriere();
                    contract.setIdContract(resultSet.getString("idContract"));
                    contract.setDataInceput(resultSet.getDate("dataInceput"));

                    Client client = new Client();
                    client.setIdClient(resultSet.getString("idClient"));
                    contract.setClient(client);

                    Apartament apartament = new Apartament();
                    apartament.setIdApartament(resultSet.getString("idApartament"));
                    contract.setApartament(apartament);

                    contract.setChirieLunara(resultSet.getDouble("pret"));
                    return contract;
                } else if ("Vanzare".equals(tipContract)) {
                    ContractDeVanzare contract = new ContractDeVanzare();
                    contract.setIdContract(resultSet.getString("idContract"));
                    contract.setDataInceput(resultSet.getDate("dataInceput"));

                    Client client = new Client();
                    client.setIdClient(resultSet.getString("idClient"));
                    contract.setClient(client);

                    Apartament apartament = new Apartament();
                    apartament.setIdApartament(resultSet.getString("idApartament"));
                    contract.setApartament(apartament);

                    contract.setPretVanzare(resultSet.getDouble("pret"));
                    return contract;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Client> getClientsWithContracts() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT DISTINCT c.* FROM Clienti c INNER JOIN Contracte con ON c.idClient = con.idClient";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client();
                client.setIdClient(resultSet.getString("idClient"));
                client.setNume(resultSet.getString("nume"));
                client.setPrenume(resultSet.getString("prenume"));
                client.setCnp(resultSet.getString("cnp"));
                client.setDomiciliu(resultSet.getString("domiciliu"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }


    public List<Apartament> getApartmentsForClient(String clientId) {
        List<Apartament> apartments = new ArrayList<>();
        String query = "SELECT a.* FROM Apartamente a INNER JOIN Contracte con ON a.idApartament = con.idApartament WHERE con.idClient = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartments.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartments;
    }

    public List<Contract> getToateContractele() {
        List<Contract> contracte = new ArrayList<>();
        String query = "SELECT c.idContract, c.dataInceput, c.pret, c.tipContract, " +
                "cl.idClient, cl.nume, cl.prenume, a.idApartament, " +
                "p.idProiect, p.numeProiect " +
                "FROM Contracte c " +
                "JOIN Clienti cl ON c.idClient = cl.idClient " +
                "JOIN Apartamente a ON c.idApartament = a.idApartament " +
                "LEFT JOIN Proiecte p ON a.idProiect = p.idProiect";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String idContract = resultSet.getString("idContract");
                Date dataInceput = resultSet.getDate("dataInceput");
                double pret = resultSet.getDouble("pret");
                String tipContract = resultSet.getString("tipContract");

                Client client = new Client();
                client.setIdClient(resultSet.getString("idClient"));
                client.setNume(resultSet.getString("nume"));
                client.setPrenume(resultSet.getString("prenume"));

                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));

                Proiect proiect = new Proiect();
                proiect.setIdProiect(resultSet.getString("idProiect"));
                proiect.setNumeProiect(resultSet.getString("numeProiect"));

                Contract contract;
                if ("Inchiriere".equals(tipContract)) {
                    contract = new ContractDeInchiriere(idContract, dataInceput, client, apartament, pret, proiect);
                } else {
                    contract = new ContractDeVanzare(idContract, dataInceput, client, apartament, pret, proiect);
                }

                contracte.add(contract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contracte;
    }


    public void stergeContract(String idContract) {
        String query = "DELETE FROM Contracte WHERE idContract = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idContract);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } public void stergeToateContractele() {
        String query = "DELETE FROM Contracte";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toate contractele au fost șterse.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ContractDAO contractDAO = new ContractDAO();
        contractDAO.stergeToateContractele();
    }

}
