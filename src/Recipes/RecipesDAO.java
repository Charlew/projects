package Recipes;

import DbConnection.ConnectionManager;
import Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipesDAO {

    LoginController loginController = new LoginController();
    private static String name;
    private static Integer allKcal;
    private static String description;


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Integer getAllKcal(){
        return allKcal;
    }

    public void setAllKcal(Integer allKcal){
        this.allKcal = allKcal;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    protected Integer getRecipeId(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer idRecipe = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT id_recipe FROM recipes WHERE id_user = ? AND name = ? AND all_kcal = ? AND description = ?");
            preparedStatement.setInt(1, loginController.getUserId());
            preparedStatement.setString(2, getName());
            preparedStatement.setInt(3, getAllKcal());
            preparedStatement.setString(4, getDescription());
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                idRecipe = resultSet.getInt("id_recipe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idRecipe;
    }

    protected static ObservableList<Recipes> getAllRecords() throws ClassNotFoundException, SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM recipes");
            resultSet = preparedStatement.executeQuery();
            ObservableList<Recipes> recipesList = getRecipesObjects(resultSet);
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static ObservableList<Recipes> getRecipesObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException {
        try {
            ObservableList<Recipes> recipesList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Recipes recipes = new Recipes();
                recipes.setIdRecipeint(resultSet.getInt("id_recipe"));
                recipes.setUsernameProperty(resultSet.getString("username"));
                recipes.setNameProperty(resultSet.getString("name"));
                recipes.setAllKcalProperty(resultSet.getInt("all_kcal"));
                recipes.setDescriptionProperty(resultSet.getString("description"));
                recipesList.add(recipes);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getIngredients(Integer idRecipe) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> ingredients = new ArrayList<String>();
        String formattedString;

        try {
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM recipes_products WHERE id_recipe = ?");
            preparedStatement.setInt(1, idRecipe);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products WHERE id_prod = ?");
                preparedStatement1.setInt(1, resultSet.getInt("id_prod"));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    ingredients.add(resultSet1.getString("name") + ": " + resultSet1.getInt("amount") + "g");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formattedString = ingredients.toString()
                .replace(",", "\n")  //remove the commas
                .replace("[", "\t")  //remove the right bracket
                .replace("]", "\t")  //remove the left bracket
                .trim();

        return formattedString;
    }

    protected void addRecipe(String name, Integer allKcal, String description){
        PreparedStatement preparedStatement = null;
        try{
             preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("INSERT INTO recipes SET id_user = ?, username = ?, name = ?, all_kcal = ?, description = ?");
            preparedStatement.setInt(1, loginController.getUserId());
            preparedStatement.setString(2, loginController.getUsername());
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, allKcal);
            preparedStatement.setString(5, description);

            setName(name);
            setAllKcal(allKcal);
            setDescription(description);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void addRecipesProducts(String productName){
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products WHERE name = ?");
            preparedStatement.setString(1, productName.toString());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                System.out.println("NAME:" + resultSet.getString("name"));
                preparedStatement = ConnectionManager.getConnection()
                        .prepareStatement("INSERT IGNORE INTO recipes_products SET id_recipe = ?, id_prod = ?;");
                preparedStatement.setInt(1, getRecipeId());
                preparedStatement.setInt(2, resultSet.getInt("id_prod"));
                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String getAllComments(int idRecipe){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String content = null, username = null, comment = null;
        List<String> allComments = new ArrayList<>();
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT * FROM comments WHERE id_recipe = ?");
            preparedStatement.setInt(1, idRecipe);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                username = resultSet.getString("username");
                content = resultSet.getString("content");
                comment = username + " napisał(a):\n" + content + "\n________________________________________________________________\n";
                allComments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allComments.toString()
                .replace("[", "")
                .replace(",", "")
                .replace("]", "")
                .trim();

    }

    protected void addComment(Integer idRecipe, String content){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("INSERT INTO comments SET id_recipe = ?, username = ?, content = ?");
            preparedStatement.setInt(1, idRecipe);
            preparedStatement.setString(2, loginController.getUsername());
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String showRecipeUsername(Integer idRecipe){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT username FROM recipes WHERE id_recipe = ?");
            preparedStatement.setInt(1, idRecipe);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}