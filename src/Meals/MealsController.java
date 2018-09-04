package Meals;

import Login.LoginDAO;
import Products.*;
import Recipes.Recipes;
import Recipes.RecipesModalController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Posiłki */
public class MealsController extends RecipesModalController{

    /**
     *  Variables
     */
    LoginDAO loginDAO                   = new LoginDAO();
    MealsDAO mealsDAO                   = new MealsDAO();
    ProductsDAO productsDAO             = new ProductsDAO();

    List<String> recipesToMealsList     = new ArrayList<>();    //Lista przepisów
    List<String> productsToMealsList    = new ArrayList<>();    //Lista produktów
    List<Integer> productsToMealsKcal   = new ArrayList<>();    //Pomocnicza lista dla ustawiania kalorii produktu z tabeli produktów

    ObservableList<Food> diaryList      = mealsDAO.getAllRecords(); //Lista przepisow i produktow w posiłkach

    Integer kcalSum = 0;

    @FXML Button buttonBack, buttonDeleteAll, buttonDeleteOne;
    @FXML TextField tfKcalSum, tfKcalRemain;
    @FXML public  TableColumn<Food, String> colName;
    @FXML public  TableColumn<Food, Integer> colKcal;
    @FXML public  TableView<Food>  mealsTable;

    /**
     * Constructors
     */
    public MealsController() throws SQLException, ClassNotFoundException {
    }

    /**
     * Functions
     */
    public void initialize() throws ClassNotFoundException, SQLException {
        /* Inicjalizacja tabeli produktów */
        colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
        colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
        colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
        colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
        ObservableList<Products> productsList = productsDAO.getAllRecords();
        populateProductsTable(productsList);

        if (mealsDAO.getEatenKcal().equals(0)) {
            kcalSum = 0;
        } else {
            kcalSum = mealsDAO.getEatenKcal();
        }
        tfKcalSum.setText(mealsDAO.getEatenKcal().toString());
        tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - Integer.parseInt(tfKcalSum.getText())));

        /* Operacje przy kliknieciu na dany wiersz w tabeli produktów*/
        productsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(productsTable.getSelectionModel().getSelectedItem() != null){
                    Products selectedProduct = productsTable.getSelectionModel().getSelectedItem();
                    tfNameProduct.setText(selectedProduct.getName());
                    tfKcalProduct.setText(String.valueOf(selectedProduct.getKcal()));
                    tfAmountProduct.setText(String.valueOf(selectedProduct.getAmount()));
                    tfNameProduct.setEditable(false);
                    tfKcalProduct.setEditable(false);
                    Integer amount, kcal;
                    amount = Integer.parseInt(tfAmountProduct.getText());
                    kcal = Integer.parseInt(tfKcalProduct.getText());
                    tfAmountProduct.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue != null && newValue != "" && !newValue.isEmpty()) {
                            Integer newKcal;
                            newKcal = (Integer.parseInt(newValue) * kcal) / amount;
                            tfKcalProduct.setText(String.valueOf(newKcal));
                        }
                    });
                }
            }
        });
        /* Inicjalizacja tabeli przepisow */
        colRecName.setCellValueFactory(cellData -> cellData.getValue().getRecipeName());
        colRecAllKcal.setCellValueFactory(cellData -> cellData.getValue().getRecipeAllKcal().asObject());
        colRecUsername.setCellValueFactory(cellData -> cellData.getValue().getRecipeUsername());
        ObservableList<Recipes> recipesList = getAllRecords();
        populateRecipesTable(recipesList);

        /* Inicjalizacja tabeli posiłkow */
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameProperty()));
        colKcal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAllKcalProperty()).asObject());
        populateMealsTable(diaryList);
    }

    /** Zapełnianie tabeli */
    protected void populateMealsTable(ObservableList<Food> diaryList){
        mealsTable.setItems(diaryList);
    }

    /** Dodawanie produktu do posilku */
    @FXML void addProductToMeals(ActionEvent event) throws SQLException, ClassNotFoundException {
        Food food = new Food();
        String name = tfNameProduct.getText();
        Integer kcal = Integer.parseInt(tfKcalProduct.getText());
        food.setNameProperty(name);
        food.setAllKcalProperty(kcal);
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameProperty()));
        colKcal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAllKcalProperty()).asObject());
        diaryList.add(food);
        populateMealsTable(diaryList);
        kcalSum += Integer.parseInt(String.valueOf(colKcal.getCellObservableValue(food).getValue()));
        tfKcalSum.setText(kcalSum.toString());
        tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
        if(Integer.parseInt(tfKcalRemain.getText()) < 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Uwaga");
            alert.setContentText("Przekroczono ustawioną pule kalorii");
            alert.showAndWait();
        }
        productsToMealsList.add(colName.getCellObservableValue(food).getValue());
        productsToMealsKcal.add(colKcal.getCellObservableValue(food).getValue());
    }

    /** Dodawanie przepisu do posilku */
    @FXML void addRecipeToMeals(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(recipesTable.getSelectionModel().getSelectedItem() != null) {
            Food food = new Food();
            Recipes selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();
            String name = selectedRecipe.getName();
            Integer kcal = selectedRecipe.getAllKcal();
            food.setNameProperty(name);
            food.setAllKcalProperty(kcal);
            colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameProperty()));
            colKcal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAllKcalProperty()).asObject());
            diaryList.add(food);
            populateMealsTable(diaryList);
            kcalSum += Integer.parseInt(String.valueOf(colKcal.getCellObservableValue(food).getValue()));
            tfKcalSum.setText(kcalSum.toString());
            tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
            if(Integer.parseInt(tfKcalRemain.getText()) < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Uwaga");
                alert.setContentText("Przekroczono ustawioną pule kalorii");
                alert.showAndWait();
            }
            recipesToMealsList.add(colName.getCellObservableValue(food).getValue());
        }
    }

    /** Zapisywanie posilkow */
    @FXML void saveMeals(ActionEvent event) throws SQLException {
        if(!recipesToMealsList.isEmpty() || !productsToMealsList.isEmpty()){
            boolean alerted = false;
            if(!recipesToMealsList.isEmpty()){
                for(int i = 0; i < recipesToMealsList.size(); i++){
                    mealsDAO.saveRecipesInMeals(recipesToMealsList.get(i).toString());
                }
                if(!alerted){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Powiadomienie");
                    alert.setContentText("Zapisano!");
                    alert.showAndWait();
                    alerted = true;
                }
            }else{
                System.out.println("Brak przepisow do dodania");
            }

            if(!productsToMealsList.isEmpty()){
                for(int i = 0; i < productsToMealsList.size(); i++){
                    mealsDAO.saveProductsInMeals(productsToMealsList.get(i).toString(), productsToMealsKcal.get(i));
                }
                if(!alerted){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Powiadomienie");
                    alert.setContentText("Zapisano!");
                    alert.showAndWait();
                    alerted = true;
                }
            }else{
                System.out.println("Brak produktow do dodania");
            }
            mealsDAO.setEatenKcal(Integer.parseInt(tfKcalSum.getText()));
        }else if(mealsTable.getItems().isEmpty()){
            mealsDAO.deleteAllFoodFromMeals();
            mealsDAO.setEatenKcal(0);

        }
    }

    /** Usuwanie wybranego jedzenia z tabeli posilkow */
    @FXML void deleteFood(ActionEvent event){
        if(event.getSource() == buttonDeleteAll){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Uwaga");
            alert.setContentText("Czy na pewno chcesz usunąć wszystkie posiłki?");
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()){
                System.out.println("Wyjscie");
            }else if(result.get() == ButtonType.OK){
                mealsTable.getItems().clear();
                kcalSum = 0;
                tfKcalSum.setText(kcalSum.toString());
                tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
            }else if(result.get() == ButtonType.CANCEL){
                System.out.println("canceled");
            }
        }else if(event.getSource() == buttonDeleteOne){
                Food selected = mealsTable.getSelectionModel().getSelectedItem();
                kcalSum -= selected.getAllKcalProperty();
                tfKcalSum.setText(kcalSum.toString());
                tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
                mealsTable.getItems().removeAll(mealsTable.getSelectionModel().getSelectedItem());
            }

    }

    /** Powrót do poprzedniej strony */
    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }
}
