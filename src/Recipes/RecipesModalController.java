package Recipes;

import Products.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipesModalController extends RecipesController{
    List<String> products = new ArrayList<>();
    Integer kcalSum = 0;

    //Tabelka produktow
    @FXML private TableView<Products> productsTable;
    @FXML private TableColumn<Products, String> colProdName;
    @FXML private TableColumn<Products, Integer> colProdKcal;
    @FXML private TableColumn<Products, Integer> colProdAmount;
    @FXML private TableColumn<Products, String> colProdUsername;

    //Pola dla produktow
    @FXML private TextField tfNameProduct;
    @FXML private TextField tfKcalProduct;
    @FXML private TextField tfAmountProduct;

    //Tabelka produktow do przepisu
    @FXML private TableView<ProductsToRecipe> productsToRecipesTable;
    @FXML private TableColumn<ProductsToRecipe, String> colProdToRecName;
    @FXML private TableColumn<ProductsToRecipe, Integer> colProdToRecKcal;
    @FXML private TableColumn<ProductsToRecipe, Integer> colProdToRecAmount;
    @FXML private TextField tfKcalSum;
    @FXML private TextField tfNameRecipe;
    @FXML private TextArea taDescription;

    public void initialize() {
        try {
            //Products Table
            colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
            colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
            colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
            colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
            ObservableList<Products> productsList = ProductsDAO.getAllRecords();
            populateProductsTable(productsList);

            productsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(productsTable.getSelectionModel().getSelectedItem() != null){
                        Products selectedRecipe = productsTable.getSelectionModel().getSelectedItem();
                        tfNameProduct.setText(selectedRecipe.getName());
                        tfKcalProduct.setText(String.valueOf(selectedRecipe.getKcal()));
                        tfAmountProduct.setText(String.valueOf(selectedRecipe.getAmount()));
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateProductsTable(ObservableList<Products> productsList){
        productsTable.setItems(productsList);
    }

    @FXML void addProductToRecipe(ActionEvent event){
        colProdToRecName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProdToRecKcal.setCellValueFactory(new PropertyValueFactory<>("kcal"));
        colProdToRecAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        String name = tfNameProduct.getText();
        Integer kcal = Integer.parseInt(tfKcalProduct.getText());
        Integer amount = Integer.parseInt(tfAmountProduct.getText());

        ProductsToRecipe productsToRecipe = new ProductsToRecipe(name, kcal, amount);
        productsToRecipesTable.getItems().add(productsToRecipe);
        kcalSum += Integer.parseInt(String.valueOf(colProdToRecKcal.getCellObservableValue(productsToRecipe).getValue()));
        tfKcalSum.setText(kcalSum.toString());
        products.add(colProdToRecName.getCellObservableValue(productsToRecipe).getValue());
        System.out.println(products);
    }

    @FXML void deleteRow(ActionEvent event){
        ProductsToRecipe selected = productsToRecipesTable.getSelectionModel().getSelectedItem();
        kcalSum -= selected.kcal;
        tfKcalSum.setText(kcalSum.toString());
        productsToRecipesTable.getItems().removeAll(productsToRecipesTable.getSelectionModel().getSelectedItem());
        products.remove(selected.name);
    }
    @FXML void deleteAllProducts(ActionEvent event){
        kcalSum = 0;
        tfKcalSum.setText(kcalSum.toString());
        productsToRecipesTable.getItems().clear();
        products.clear();
    }

    @FXML void insertRecipe(ActionEvent event) throws SQLException, ClassNotFoundException {
        addRecipe(tfNameRecipe.getText(), Integer.parseInt(tfKcalSum.getText()), taDescription.getText());

        for(int i = 0; i < products.size(); i++){
            addRecipesProducts( products.get(i).toString());
        }
        Stage stage = (Stage) tfNameRecipe.getScene().getWindow();
        stage.close();
    }
}
