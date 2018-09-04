package Meals;

import DbConnection.ConnectionManager;
import Login.LoginDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MealsDAO {
    LoginDAO loginDAO = new LoginDAO();

    /**
     * Functions
     */
    /** Ustawienie ilosci zjedzonych kcal dla usera */
    protected void setEatenKcal(Integer kcal) throws SQLException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("UPDATE users SET eaten_kcal = ? WHERE id_user = ?");
            preparedStatement.setInt(1, kcal);
            preparedStatement.setInt(2, loginDAO.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
        }
    }

    /** Pobranie ilości zjedzonych kcal przez usera */
    protected Integer getEatenKcal() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer eatenKcal = 0;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT eaten_kcal FROM users WHERE id_user = ?");
            preparedStatement.setInt(1, loginDAO.getUserId());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                eatenKcal = resultSet.getInt("eaten_kcal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        return eatenKcal;
    }

    /** Pobranie jedzenia z posilkow dla danegu usera */
    protected  ObservableList<Food> getAllRecords() throws ClassNotFoundException, SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM meals WHERE id_user = ?");
            preparedStatement.setInt(1, loginDAO.getUserId());
            resultSet = preparedStatement.executeQuery();
            ObservableList<Food> diaryList = getRecipesObjects(resultSet);
            return diaryList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        return null;
    }

    protected static ObservableList<Food> getRecipesObjects(ResultSet resultSet) throws ClassNotFoundException{
        try {
            ObservableList<Food> diaryList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Food food = new Food();
                if(resultSet.getString("recipe_name") != null){
                    food.setNameProperty(resultSet.getString("recipe_name"));
                }else{
                    food.setNameProperty(resultSet.getString("product_name"));
                }
                food.setAllKcalProperty(resultSet.getInt("kcal"));

                diaryList.add(food);
            }
            return diaryList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Zapisanie przepisu w posilkach */
    protected void saveRecipesInMeals(String name) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT * FROM recipes WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                preparedStatement = ConnectionManager.getConnection()
                        .prepareStatement("INSERT INTO meals SET id_user = ?, id_recipe = ?, recipe_name = ?, kcal = ?");
                preparedStatement.setInt(1, loginDAO.getUserId());
                preparedStatement.setInt(2, resultSet.getInt("id_recipe"));
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, resultSet.getInt("all_kcal"));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    /** Zapisanie produktu w posilkach */
    protected void saveProductsInMeals(String name, Integer kcal) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT * FROM products WHERE name = ?");
            preparedStatement.setString(1, name.toString());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getInt("id_prod"));
                System.out.println(name);
                preparedStatement = ConnectionManager.getConnection()
                        .prepareStatement("INSERT INTO meals SET id_user = ?, id_product = ?, product_name = ?, kcal = ?");
                preparedStatement.setInt(1, loginDAO.getUserId());
                preparedStatement.setInt(2, resultSet.getInt("id_prod"));
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, kcal);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    /** Usuwanie jedzenia z bazy danych */
    protected void deleteAllFoodFromMeals() throws SQLException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("DELETE FROM meals WHERE id_user = ?");
            preparedStatement.setInt(1, loginDAO.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
        }
    }


}
