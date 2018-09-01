package Products;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

/** Produkty */
public class ProductsController extends ProductsDAO{

    /**
     * Variables
     */
    @FXML private TextField tfNameAdd, tfKcal, tfAmount, tfNameSearch;
    @FXML private Button buttonAdd, buttonBack;

    @FXML protected TableView productsTable;                        // Tabela produktów
    @FXML protected TableColumn<Products, String> colProdName;      // Kolumna Nazwa
    @FXML protected TableColumn<Products, Integer> colProdKcal;     // Kolumna Kcal
    @FXML protected TableColumn<Products, Integer> colProdAmount;   // Kolumna Ilość
    @FXML protected TableColumn<Products, String> colProdUsername;  // Kolumna Dodane przez


    /**
     * Functions
     */
     public void initialize() throws ClassNotFoundException, SQLException{
         /* Inicjalizacja tabeli produktów */
         colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
         colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
         colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
         colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
         ObservableList<Products> productsList = getAllRecords();
         populateTable(productsList);
    }

    /** Zapelnianie tabeli produktow */
    protected void populateTable(ObservableList<Products> productsList){
        productsTable.setItems(productsList);
    }

    /** Dodawanie nowego produktu */
    @FXML void addProduct(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(event.getSource() == buttonAdd) {
            if(!(tfNameAdd.getText().isEmpty() && tfKcal.getText().isEmpty() && tfAmount.getText().isEmpty())){
                if(!validateIfProductExist(tfNameAdd.getText())){
                    insertNewProduct(tfNameAdd.getText(), tfKcal.getText(), tfAmount.getText());
                    tfNameAdd.setText("");
                    tfKcal.setText("");
                    tfAmount.setText("");
                    ObservableList<Products> productsList = getAllRecords();
                    populateTable(productsList);
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Błąd");
                    alert.setContentText("Istnieje już w bazie produkt o podanej nazwie");
                    alert.showAndWait();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText("Puste pola!");
                alert.setContentText("Sprawdź czy wprowadzono wszystkie potrzebne dane");
                alert.showAndWait();
            }


        }
    }

    /** Wyszukiwanie produktu po nazwie */
    @FXML void searchProduct(ActionEvent event) throws ClassNotFoundException, SQLException{
        ObservableList<Products> list = searchProduct(tfNameSearch.getText());
        if(list.size() > 0) {
            populateTable(list);
        }
    }

    /** Wyswietlanie wszystkich produktow */
    @FXML void displayAllProducts(ActionEvent event) throws ClassNotFoundException, SQLException{
        ObservableList<Products> productsList = getAllRecords();
        populateTable(productsList);
    }

    /** Powrot do poprzedniej strony */
    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }



}
