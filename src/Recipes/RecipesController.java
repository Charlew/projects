package Recipes;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class RecipesController extends RecipesDAO{

    @FXML private Button buttonBack;

    //Recipes
    @FXML protected TableColumn<Recipes, String> colRecName;
    @FXML protected TableColumn<Recipes, Integer> colRecAllKcal;
    @FXML protected TableColumn<Recipes, String> colRecUsername;
    @FXML private TextArea taIngredients;
    @FXML private TextArea taDescription;
    @FXML private TextArea taComment;
    @FXML private TextArea taAllComments;
    @FXML protected TableView<Recipes> recipesTable;

    public void initialize() throws SQLException, ClassNotFoundException {
        try {
            //Recipes Table
            colRecName.setCellValueFactory(cellData -> cellData.getValue().getRecipeName());
            colRecAllKcal.setCellValueFactory(cellData -> cellData.getValue().getRecipeAllKcal().asObject());
            colRecUsername.setCellValueFactory(cellData -> cellData.getValue().getRecipeUsername());
            ObservableList<Recipes> recipesList = getAllRecords();
            populateRecipesTable(recipesList);
            taDescription.setEditable(false);
            taIngredients.setEditable(false);
            taAllComments.setEditable(false);

            recipesTable.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(recipesTable.getSelectionModel().getSelectedItem() != null){
                        Recipes selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
                        taDescription.setText(selectedRecipe.getDescription());
                        taIngredients.setText(getIngredients(selectedRecipe.getIdRecipe()));
                        taAllComments.setText(getAllComments(selectedRecipe.getIdRecipe()));
                    }
                }
            });

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void populateRecipesTable(ObservableList<Recipes> recipesList){
        recipesTable.setItems(recipesList);
    }

    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    @FXML void addNewRecipe(ActionEvent event) throws Exception{
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("../Recipes/RecipesModal.fxml").openStream());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML void addComment(ActionEvent event){
        if(recipesTable.getSelectionModel().getSelectedItem() != null) {
            Recipes selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
            if(taComment.getText() != null){
                addComment(selectedRecipe.getIdRecipe(), taComment.getText());
                taAllComments.setText(getAllComments(selectedRecipe.getIdRecipe()));
            }
        }
    }
}
