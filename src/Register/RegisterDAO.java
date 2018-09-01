package Register;

import DbConnection.ConnectionManager;

import java.sql.*;

public class RegisterDAO {
    Connection connection;

    /**
     * Functions
     */
    /** Sprawdzanie czy istnieje juz taki user */
    public boolean checkUserExists(String username){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean usernameExists = false;

        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM users ORDER BY username desc");
            resultSet = preparedStatement.executeQuery();

            String value;
            while(resultSet.next()){
                value = resultSet.getString("username");
                if(value.equals(username)){
                    System.out.println("Istnieje juz taki uzytkownik w bazie danych");
                    usernameExists = true;
                }else{
                    usernameExists = false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return usernameExists;
    }

    /** Dodawanie nowego usera */
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
