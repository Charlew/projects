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
import java.util.Date;
import java.util.List;

public class RecipesDAO {

    /**
     * Variablees
     */
    LoginDAO loginDAO = new LoginDAO();
    private static String name;
    private static Integer allKcal;
    private static String description;
    private static String content;
    private static String username;
    private static String comment;
    private static Date date = new Date();

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

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

    public Date getCommentDate(){
        return this.date;
    }
    public void setCommentDate(Date date){
        this.date = date;
    }

    /**
     * Functions
     */
    /** Pobieranie id danego przepisu */
    protected Integer getRecipeId() throws SQLException {
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
        }finally {
            preparedStatement.close();
            resultSet.close();
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
        }finally {
            preparedStatement.close();
            resultSet.close();
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
    protected String getIngredients(Integer idRecipe) throws SQLException {
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
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        formattedString = ingredients.toString()
                .replace(",", "\n")
                .replace("[", "\t")
                .replace("]", "\t")
                .trim();

        return formattedString;
    }

    /** Walidacja przy dodawaniu nowego przepisu */
    protected Boolean validateIfRecipeExists(String name) throws SQLException {
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
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        return found;
    }

    /** Dodawanie przepisu */
    protected void addRecipe(String name, Integer allKcal, String description) throws SQLException {
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
        }finally {
            preparedStatement.close();
        }
    }

    /** Dodanie id przepisu i produktu do tabeli laczacej */
    protected void addRecipesProducts(String productName) throws SQLException {
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
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
    }



    /** Pobranie komentarzy dla wybranego przepisu */
    protected String getAllComments(int idRecipe) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> allComments = new ArrayList<>();

        try{
            preparedStatement = ConnectionManager.getConnection()
                    .prepareStatement("SELECT * FROM comments WHERE id_recipe = ?");
            preparedStatement.setInt(1, idRecipe);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                setUsername(resultSet.getString("username"));
                setContent(resultSet.getString("content"));
                setCommentDate(resultSet.getDate("date"));
                setComment(getCommentDate() + " | " +  getUsername() + " napisał(a):\n" + getContent() + "\n________________________________________________________________\n");

                allComments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
        return allComments.toString()
                .replace("[", "")
                .replace(",", "")
                .replace("]", "")
                .trim();

    }

    /** Dodawanie komentarza */
    protected void addComment(Integer idRecipe, String content) throws SQLException {
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
        }finally {
            preparedStatement.close();
        }
    }
}
