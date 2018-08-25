package Diary;

import Login.LoginController;
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

public class DiaryController extends RecipesModalController{
    LoginDAO loginDAO = new LoginDAO();
    DiaryDAO diaryDAO = new DiaryDAO();
    ProductsDAO productsDAO = new ProductsDAO();
    List<String> recipesToDiaryList = new ArrayList<>();
    List<String> productsToDiaryList = new ArrayList<>();
    List<Integer> productsToDiaryKcal = new ArrayList<>();
    ObservableList<Food> diaryList = diaryDAO.getAllRecords();

    Integer kcalSum = 0;
    @FXML Button buttonBack;
    @FXML TextField tfKcalSum, tfKcalRemain;
    @FXML public  TableColumn<Food, String> colName;
    @FXML public  TableColumn<Food, Integer> colKcal;
    @FXML public  TableView<Food>  diaryTable;

    public DiaryController() throws SQLException, ClassNotFoundException {
    }

    public void initialize() throws ClassNotFoundException, SQLException {
        //Products Table
        colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
        colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
        colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
        colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
        ObservableList<Products> productsList = productsDAO.getAllRecords();
        populateProductsTable(productsList);

        if (diaryDAO.getEatenKcal().equals(0)) {
            kcalSum = 0;
        } else {
            kcalSum = diaryDAO.getEatenKcal();
        }
        tfKcalSum.setText(diaryDAO.getEatenKcal().toString());
        tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - Integer.parseInt(tfKcalSum.getText())));

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
        //Recipes Table
        colRecName.setCellValueFactory(cellData -> cellData.getValue().getRecipeName());
        colRecAllKcal.setCellValueFactory(cellData -> cellData.getValue().getRecipeAllKcal().asObject());
        colRecUsername.setCellValueFactory(cellData -> cellData.getValue().getRecipeUsername());
        ObservableList<Recipes> recipesList = getAllRecords();
        populateRecipesTable(recipesList);

        //Food Table
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameProperty()));
        colKcal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAllKcalProperty()).asObject());
        populateDiaryTable(diaryList);
    }

    protected void populateDiaryTable(ObservableList<Food> diaryList){
        diaryTable.setItems(diaryList);
    }


    @FXML void addProductToDiary(ActionEvent event) throws SQLException, ClassNotFoundException {
        Food food = new Food();
        String name = tfNameProduct.getText();
        Integer kcal = Integer.parseInt(tfKcalProduct.getText());
        food.setNameProperty(name);
        food.setAllKcalProperty(kcal);
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameProperty()));
        colKcal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAllKcalProperty()).asObject());
        diaryList.add(food);
        populateDiaryTable(diaryList);
        kcalSum += Integer.parseInt(String.valueOf(colKcal.getCellObservableValue(food).getValue()));
        tfKcalSum.setText(kcalSum.toString());
        tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
        if(Integer.parseInt(tfKcalRemain.getText()) < 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Uwaga");
            alert.setContentText("Przekroczono ustawioną pule kalorii");
            alert.showAndWait();
        }
        productsToDiaryList.add(colName.getCellObservableValue(food).getValue());
        productsToDiaryKcal.add(colKcal.getCellObservableValue(food).getValue());
    }

    @FXML void addRecipeToDiary(ActionEvent event) throws SQLException, ClassNotFoundException {
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
            populateDiaryTable(diaryList);
            kcalSum += Integer.parseInt(String.valueOf(colKcal.getCellObservableValue(food).getValue()));
            tfKcalSum.setText(kcalSum.toString());
            tfKcalRemain.setText(String.valueOf(loginDAO.getKcalDemand() - kcalSum));
            if(Integer.parseInt(tfKcalRemain.getText()) < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Uwaga");
                alert.setContentText("Przekroczono ustawioną pule kalorii");
                alert.showAndWait();
            }
            recipesToDiaryList.add(colName.getCellObservableValue(food).getValue());
        }
    }

    @FXML void saveDiary(ActionEvent event){
        if(!recipesToDiaryList.isEmpty()){
            for(int i = 0; i < recipesToDiaryList.size(); i++){
                diaryDAO.saveRecipesInDiary(recipesToDiaryList.get(i).toString());
            }
        }else{
            System.out.println("Brak przepisow do dodania");
        }

        if(!productsToDiaryList.isEmpty()){
            for(int i = 0; i < productsToDiaryList.size(); i++){
                diaryDAO.saveProductsInDiary(productsToDiaryList.get(i).toString(), productsToDiaryKcal.get(i));
            }
        }else{
            System.out.println("Brak produktow do dodania");
        }
        diaryDAO.setEatenKcal(Integer.parseInt(tfKcalSum.getText()));
    }

    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }
}
