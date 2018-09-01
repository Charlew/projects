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

/** Modal dodawania nowego przepisu */
public class RecipesModalController extends RecipesController{

    /**
     * Variables
     */
    List<String> products   = new ArrayList<>();
    Integer kcalSum         = 0;

    /* Tabelka produktow */
    @FXML protected TableView<Products> productsTable;
    @FXML protected TableColumn<Products, String> colProdName;
    @FXML protected TableColumn<Products, Integer> colProdKcal;
    @FXML protected TableColumn<Products, Integer> colProdAmount;
    @FXML protected TableColumn<Products, String> colProdUsername;

    /* Pola dla produktow */
    @FXML protected TextField tfNameProduct;
    @FXML protected TextField tfKcalProduct;
    @FXML protected TextField tfAmountProduct;

    /* Tabelka produktow do przepisu */
    @FXML private TableView<ProductsToRecipe> productsToRecipesTable;
    @FXML private TableColumn<ProductsToRecipe, String> colProdToRecName;
    @FXML private TableColumn<ProductsToRecipe, Integer> colProdToRecKcal;
    @FXML private TableColumn<ProductsToRecipe, Integer> colProdToRecAmount;
    @FXML private TextField tfKcalSum;
    @FXML private TextField tfNameRecipe;
    @FXML private TextArea taDescription;

    public void initialize() throws SQLException, ClassNotFoundException {
        /* Inicjalizacja tabeli produtków */
        colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
        colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
        colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
        colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
        ObservableList<Products> productsList = ProductsDAO.getAllRecords();
        populateProductsTable(productsList);

        /* Operacje przy kliknieciu na dany wiersz w tabeli produktów */
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
    }

    /** Zapełnianie tabeli produktów */
    protected void populateProductsTable(ObservableList<Products> productsList){
        productsTable.setItems(productsList);
    }

    /** Dodawanie produktu do tabeli "Produktow do przepisu" */
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

    /** Usuwanie wybranego wiersza z tabeli "Produktow do przepisu" */
    @FXML void deleteRow(ActionEvent event){
        ProductsToRecipe selected = productsToRecipesTable.getSelectionModel().getSelectedItem();
        kcalSum -= selected.kcal;
        tfKcalSum.setText(kcalSum.toString());
        productsToRecipesTable.getItems().removeAll(productsToRecipesTable.getSelectionModel().getSelectedItem());
        products.remove(selected.name);
    }

    /** Usuwanie wszystkich produktów */
    @FXML void deleteAllProducts(ActionEvent event){
        kcalSum = 0;
        tfKcalSum.setText(kcalSum.toString());
        productsToRecipesTable.getItems().clear();
        products.clear();
    }

    /** Dodawanie nowego przepisu */
    @FXML void insertRecipe(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(tfNameRecipe.getText().isEmpty() || taDescription.getText().isEmpty() || products.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText("Puste pola!");
            alert.setContentText("Sprawdź czy wprowadzono wszystkie potrzebne dane");
            alert.showAndWait();
        }else{
            if(!validateIfRecipeExists(tfNameRecipe.getText())){
                addRecipe(tfNameRecipe.getText(), Integer.parseInt(tfKcalSum.getText()), taDescription.getText());

                for(int i = 0; i < products.size(); i++){
                    addRecipesProducts( products.get(i).toString());
                }
                Stage stage = (Stage) tfNameRecipe.getScene().getWindow();
                stage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setContentText("Istnieje już w bazie przepis o podanej nazwie");
                alert.showAndWait();
            }
        }
    }
}
