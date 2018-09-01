package Login;

import DbConnection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    /**
     * Variables
     */
    Connection connection;
    private static String username;
    private static Integer userId;
    private static Integer kcalDemand;

    /**
     * Getters & setters
     */
    public Integer getKcalDemand() { return this.kcalDemand; }
    public void setKcalDemand(Integer kcalDemand) { this.kcalDemand = kcalDemand; }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }
    public Integer getUserId(){
        return this.userId;
    }

    /**
     * Functions
     */

    /** Walidacja wprowadzanych danych do zalogowania */
    protected boolean isLogin(String username, String password) throws SQLException {
        boolean status = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT id_user, username, kcal_demand, eaten_kcal FROM users WHERE username = ? AND password = ? ");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                setUsername(username);
                setUserId(resultSet.getInt("id_user"));
                setKcalDemand(resultSet.getInt("kcal_demand"));
                System.out.println("User authenticated successfully " + getUsername() + " ID " + getUserId());
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        return status;
    }
}
