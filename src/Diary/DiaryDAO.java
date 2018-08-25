package Diary;

import DbConnection.ConnectionManager;
import Login.LoginController;
import Products.ProductsDAO;
import Recipes.Recipes;
import Recipes.RecipesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiaryDAO {
    LoginController loginController = new LoginController();

    protected  ObservableList<Food> getAllRecords() throws ClassNotFoundException, SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM diary WHERE id_user = ?");
            preparedStatement.setInt(1, loginController.getUserId());
            resultSet = preparedStatement.executeQuery();
            ObservableList<Food> diaryList = getRecipesObjects(resultSet);
            return diaryList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static ObservableList<Food> getRecipesObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException {
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

    protected void saveRecipesInDiary(String name){
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT * FROM recipes WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                preparedStatement = ConnectionManager.getConnection()
                        .prepareStatement("INSERT INTO diary SET id_user = ?, id_recipe = ?, recipe_name = ?, kcal = ?");
                preparedStatement.setInt(1, loginController.getUserId());
                preparedStatement.setInt(2, resultSet.getInt("id_recipe"));
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, resultSet.getInt("all_kcal"));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void saveProductsInDiary(String name, Integer kcal){
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
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
                        .prepareStatement("INSERT INTO diary SET id_user = ?, id_product = ?, product_name = ?, kcal = ?");
                preparedStatement.setInt(1, loginController.getUserId());
                preparedStatement.setInt(2, resultSet.getInt("id_prod"));
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, kcal);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void setEatenKcal(Integer kcal){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("UPDATE users SET eaten_kcal = ? WHERE id_user = ?");
            preparedStatement.setInt(1, kcal);
            preparedStatement.setInt(2, loginController.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected  Integer getEatenKcal(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer eatenKcal = 0;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT eaten_kcal FROM users WHERE id_user = ?");
            preparedStatement.setInt(1, loginController.getUserId());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                eatenKcal = resultSet.getInt("eaten_kcal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eatenKcal;
    }


}
