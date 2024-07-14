package licenta_imobiliare.dao;

import licenta_imobiliare.model.Utilizator;
import licenta_imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilizatorDAO {

    public Utilizator getUtilizatorByNume(String numeUtilizator) {
        String query = "SELECT * FROM utilizatori WHERE numeUtilizator = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, numeUtilizator);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Utilizator utilizator = new Utilizator();
                utilizator.setIdUtilizator(resultSet.getString("idUtilizator"));
                utilizator.setNumeUtilizator(resultSet.getString("numeUtilizator"));
                utilizator.setParolaUtilizator(resultSet.getString("parolaUtilizator"));
                return utilizator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
