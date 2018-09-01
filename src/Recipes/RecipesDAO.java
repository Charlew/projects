package Recipes;

import DbConnection.ConnectionManager;
import Login.LoginController;
import Login.LoginDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipesDAO {

    /**
     * Variablees
     */
    LoginDAO loginDAO = new LoginDAO();
    private static String name;
    private static Integer allKcal;
    private static String description;

    /**
     * Getters * setters
     */
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

    /**
     * Functions
     */
    /** Pobieranie id danego przepisu */
    protected Integer getRecipeId(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer idRecipe = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT id_recipe FROM recipes WHERE id_user = ? AND name = ? AND all_kcal = ? AND description = ?");
            preparedStatement.setInt(1, loginDAO.getUserId());
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

    /** Pobieranie wszystkich przepisow z bazy i dodanie ich do ObservableList */
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

    /** Pobranie wszystkich skladnikow przepisu */
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
                preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products WHERE id_prod = ?");
                preparedStatement.setInt(1, resultSet.getInt("id_prod"));
                ResultSet resultSet1 = preparedStatement.executeQuery();
                if (resultSet1.next()) {
                    ingredients.add(resultSet1.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formattedString = ingredients.toString()
                .replace(",", "\n")
                .replace("[", "\t")
                .replace("]", "\t")
                .trim();

        return formattedString;
    }

    /** Walidacja przy dodawaniu nowego przepisu */
    protected Boolean validateIfRecipeExists(String name){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean found = false;
        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM recipes WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                found = true;
            }else{
                found = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    /** Dodawanie przepisu */
    protected void addRecipe(String name, Integer allKcal, String description){
        PreparedStatement preparedStatement = null;
        try{
             preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("INSERT INTO recipes SET id_user = ?, username = ?, name = ?, all_kcal = ?, description = ?");
            preparedStatement.setInt(1, loginDAO.getUserId());
            preparedStatement.setString(2, loginDAO.getUsername());
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

    /** Dodanie id przepisu i produktu do tabeli laczacej */
    protected void addRecipesProducts(String productName){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products WHERE name = ?");
            preparedStatement.setString(1, productName.toString());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
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

    /** Pobranie komentarzy dla wybranego przepisu */
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

    /** Dodawanie komentarza */
    protected void addComment(Integer idRecipe, String content){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("INSERT INTO comments SET id_recipe = ?, username = ?, content = ?");
            preparedStatement.setInt(1, idRecipe);
            preparedStatement.setString(2, loginDAO.getUsername());
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
