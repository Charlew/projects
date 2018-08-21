package Register;

import DbConnection.ConnectionManager;

import java.sql.*;

public class RegisterDAO {
    Connection connection;

    public boolean checkUserExists(String username){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean usernameExists = false;

        try{
            connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(id_user INT NOT NULL AUTO_INCREMENT , username VARCHAR(24),email VARCHAR(24), password VARCHAR(24), PRIMARY KEY (ID))");

            preparedStatement = connection.prepareStatement("SELECT * FROM users ORDER BY username desc");
            resultSet = preparedStatement.executeQuery();

            String value;
            while(resultSet.next()){
                value = resultSet.getString("username");
                System.out.println(value);
                if(value.equals(username)){
                    System.out.println("Istnieje juz taki uzytkownik w bazie danych");
                    usernameExists = true;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return usernameExists;
    }

    public void addUser(String username, String email, String password) throws SQLException{
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO users(username, email, password) VALUES (?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
