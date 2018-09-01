package Profile;

import DbConnection.ConnectionManager;
import Login.LoginController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {

    /**
     * Variables
     */
    Connection connection               = ConnectionManager.getConnection();
    LoginController loginController     = new LoginController();

    private static String age;
    private static Integer weight;
    private static Integer height;
    private static Integer kcal;
    private static String sex;

    /**
     * Getters & setters
     */
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getKcal() {
        return kcal;
    }
    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Functions
     */
    /** Ustawianie wszystkich wartości przy otworzeniu profilu */
    protected void setAllAttributes(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND id_user = ?");
            preparedStatement.setString(1, loginController.getUsername());
            preparedStatement.setString(2, loginController.getUserId().toString());

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                setAge(resultSet.getString("age"));
                setWeight(resultSet.getInt("weight"));
                setHeight(resultSet.getInt("height"));
                setKcal(resultSet.getInt("kcal_demand"));

                if(resultSet.getString("sex").equals("Female")){
                    setSex("Female");
                }else{
                    setSex("Male");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Zapis danych do bazy */
    protected void insertToProfile(String sex, Integer age, Integer weight, Integer height, Integer kcalDemand){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("UPDATE users SET sex = ?, age = ?, weight = ?, height = ?, kcal_demand = ? WHERE username = ? AND id_user = ?");
            preparedStatement.setString(1, sex);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, height);
            preparedStatement.setInt(5, kcalDemand);
            preparedStatement.setString(6, loginController.getUsername());
            preparedStatement.setInt(7, loginController.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
