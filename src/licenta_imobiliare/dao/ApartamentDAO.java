package licenta_imobiliare.dao;

import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Proiect;
import licenta_imobiliare.util.DatabaseConnection;
import licenta_imobiliare.util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApartamentDAO {
    public List<Apartament> getApartamenteByProiectSiCamereSiPret(String idProiect, int camere, String sortarePret, boolean chirie) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE idProiect = ? AND (camere = ? OR ? = 0) AND chirie = ?";

        if (sortarePret.equals("Crescător")) {
            query += " ORDER BY pret ASC";
        } else if (sortarePret.equals("Descrescător")) {
            query += " ORDER BY pret DESC";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            statement.setInt(2, camere);
            statement.setInt(3, camere);
            statement.setBoolean(4, chirie);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }


    public boolean hasActiveContract(String idApartament) {
        String query = "SELECT COUNT(*) FROM contracte WHERE idApartament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idApartament);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean stergeApartament(String idApartament) {
        if (hasActiveContract(idApartament)) {
            return false;
        }
        String query = "DELETE FROM Apartamente WHERE idApartament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idApartament);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Apartament> getApartamenteByProiectSiCamereSiPretsiChirie(String idProiect, int camere, String sortarePret, boolean chirie) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE idProiect = ? AND (camere = ? OR ? = 0) AND chirie = ?";

        if (sortarePret.equals("Crescător")) {
            query += " ORDER BY pret ASC";
        } else if (sortarePret.equals("Descrescător")) {
            query += " ORDER BY pret DESC";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            statement.setInt(2, camere);
            statement.setInt(3, camere);
            statement.setBoolean(4, chirie);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }

    public List<Apartament> getApartamenteByProiectSiCamereSiPret(String idProiect, int camere, String sortarePret) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE idProiect = ? AND (camere = ? OR ? = 0)";

        if (sortarePret.equals("Crescător")) {
            query += " ORDER BY pret ASC";
        } else if (sortarePret.equals("Descrescător")) {
            query += " ORDER BY pret DESC";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            statement.setInt(2, camere);
            statement.setInt(3, camere); // Adăugăm acest parametru pentru a permite afișarea tuturor apartamentelor când camere este 0
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setIdProiect(resultSet.getString("idProiect"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }
    public Apartament getApartamentById(String idApartament) throws SQLException {
        String query = "SELECT * FROM Apartamente WHERE idApartament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idApartament);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToApartament(resultSet);
            }
        }
        return null;
    }
    private Apartament mapResultSetToApartament(ResultSet resultSet) throws SQLException {
        Apartament apartament = new Apartament();
        apartament.setIdApartament(resultSet.getString("idApartament"));
        apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
        apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
        apartament.setPret(resultSet.getInt("pret"));
        apartament.setEtaj(resultSet.getInt("etaj"));
        apartament.setChirie(resultSet.getBoolean("chirie"));
        apartament.setIdProiect(resultSet.getString("idProiect"));
        apartament.setHol(resultSet.getInt("hol"));
        apartament.setCamere(resultSet.getInt("camere"));
        apartament.setBaie(resultSet.getInt("baie"));
        apartament.setBucatarie(resultSet.getInt("bucatarie"));
        return apartament;
    }

    public Apartament getApartament(String idApartament) {
        String query = "SELECT * FROM Apartamente WHERE idApartament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idApartament);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setIdProiect(resultSet.getString("idProiect"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                return apartament;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Apartament> getToateApartamentele() {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }



    public void adaugaApartament(Apartament apartament) {
        String sql = "INSERT INTO apartamente (idApartament, pret, suprafataUtila, suprafataTotala, etaj, hol, camere, baie, bucatarie, chirie, idProiect) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String idApartament = generateApartamentId(apartament.getProiect().getNumeProiect());
            apartament.setIdApartament(idApartament);
            pstmt.setString(1, idApartament);
            pstmt.setInt(2, apartament.getPret());
            pstmt.setInt(3, apartament.getSuprafataUtila());
            pstmt.setInt(4, apartament.getSuprafataTotala());
            pstmt.setInt(5, apartament.getEtaj());
            pstmt.setInt(6, apartament.getHol());
            pstmt.setInt(7, apartament.getCamere());
            pstmt.setInt(8, apartament.getBaie());
            pstmt.setInt(9, apartament.getBucatarie());
            pstmt.setBoolean(10, apartament.isChirie());

            Proiect proiect = apartament.getProiect();
            if (proiect != null) {
                pstmt.setString(11, proiect.getIdProiect());
            } else {
                pstmt.setNull(11, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String generateApartamentId(String numeProiect) {
        String idPart1 = (numeProiect.length() >= 3 ? numeProiect.substring(0, 3) : numeProiect).toUpperCase();
        String uniqueCode = IDGenerator.generateUniqueId().substring(0, 8).toUpperCase();
        return idPart1 + uniqueCode;
    }


    public void actualizeazaApartament(Apartament apartament) {
        String sql = "UPDATE apartamente SET pret = ?, suprafataUtila = ?, suprafataTotala = ?, etaj = ?, hol = ?, camere = ?, baie = ?, bucatarie = ?, chirie = ?, idProiect = ? WHERE idApartament = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, apartament.getPret());
            pstmt.setInt(2, apartament.getSuprafataUtila());
            pstmt.setInt(3, apartament.getSuprafataTotala());
            pstmt.setInt(4, apartament.getEtaj());
            pstmt.setInt(5, apartament.getHol());
            pstmt.setInt(6, apartament.getCamere());
            pstmt.setInt(7, apartament.getBaie());
            pstmt.setInt(8, apartament.getBucatarie());
            pstmt.setBoolean(9, apartament.isChirie());

            Proiect proiect = apartament.getProiect();
            if (proiect != null) {
                pstmt.setString(10, proiect.getIdProiect());
            } else {
                pstmt.setNull(10, java.sql.Types.VARCHAR);
            }

            pstmt.setString(11, apartament.getIdApartament());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Apartament> getApartamenteByChirie(boolean chirie) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE chirie = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, chirie);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }


    public List<Apartament> getApartamenteVanzare() {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE chirie = false";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }



    public List<Apartament> getApartamenteByProiect(String idProiect) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE idProiect = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = new Apartament();
                apartament.setIdApartament(resultSet.getString("idApartament"));
                apartament.setSuprafataUtila(resultSet.getInt("suprafataUtila"));
                apartament.setSuprafataTotala(resultSet.getInt("suprafataTotala"));
                apartament.setPret(resultSet.getInt("pret"));
                apartament.setEtaj(resultSet.getInt("etaj"));
                apartament.setChirie(resultSet.getBoolean("chirie"));
                apartament.setIdProiect(resultSet.getString("idProiect"));
                apartament.setHol(resultSet.getInt("hol"));
                apartament.setCamere(resultSet.getInt("camere"));
                apartament.setBaie(resultSet.getInt("baie"));
                apartament.setBucatarie(resultSet.getInt("bucatarie"));
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }

    public List<Apartament> getApartamenteDeVanzareByProiectSiCamereSiPret(String idProiect, int numarCamere, String sortarePret) {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE idProiect = ? AND chirie = TRUE";

        if (numarCamere > 0) {
            query += " AND camere = " + numarCamere;
        }

        if ("Crescător".equals(sortarePret)) {
            query += " ORDER BY pret ASC";
        } else if ("Descrescător".equals(sortarePret)) {
            query += " ORDER BY pret DESC";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idProiect);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Apartament apartament = mapResultSetToApartament(resultSet);
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }


    public List<Apartament> getApartamenteDeVanzare() {
        List<Apartament> apartamente = new ArrayList<>();
        String query = "SELECT * FROM Apartamente WHERE chirie = TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Apartament apartament = mapResultSetToApartament(resultSet);
                apartamente.add(apartament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartamente;
    }

    public void stergeToateApartamentele() {
        String query = "DELETE FROM Apartamente";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Toate apartamentele au fost șterse.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }public static void main(String[] args) {
        ApartamentDAO apartamentDAO = new ApartamentDAO();
        apartamentDAO.stergeToateApartamentele();
    }

}
