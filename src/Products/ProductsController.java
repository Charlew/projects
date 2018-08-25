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

public class ProductsController extends ProductsDAO{

    @FXML private TextField tfNameAdd, tfKcal, tfAmount, tfNameSearch;
    @FXML private Button buttonAdd, buttonBack, buttonSearch;
    @FXML private Label labelUsername;

    @FXML protected TableColumn<Products, String> colProdName;
    @FXML protected TableColumn<Products, Integer> colProdKcal;
    @FXML protected TableColumn<Products, Integer> colProdAmount;
    @FXML protected TableColumn<Products, String> colProdUsername;
    @FXML protected TableView productsTable;

     public void initialize() {
         try {
            colProdName.setCellValueFactory(cellData -> cellData.getValue().getProductName());
            colProdKcal.setCellValueFactory(cellData -> cellData.getValue().getProductKcal().asObject());
            colProdAmount.setCellValueFactory(cellData -> cellData.getValue().getProductAmount().asObject());
            colProdUsername.setCellValueFactory(cellData -> cellData.getValue().getProductUsername());
            ObservableList<Products> productsList = getAllRecords();
            populateTable(productsList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void populateTable(ObservableList<Products> productsList){
        productsTable.setItems(productsList);
    }

    @FXML void buttonHandler(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(event.getSource() == buttonAdd) {
            if(!(tfNameAdd.getText().isEmpty() && tfKcal.getText().isEmpty() && tfAmount.getText().isEmpty())){
                insertNewProduct(tfNameAdd.getText(), tfKcal.getText(), tfAmount.getText());
                tfNameAdd.setText("");
                tfKcal.setText("");
                tfAmount.setText("");
                ObservableList<Products> productsList = getAllRecords();
                populateTable(productsList);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText("Puste pola!");
                alert.setContentText("Sprawdź czy wprowadzono wszystkie potrzebne dane");
                alert.showAndWait();
            }


        }
    }


    @FXML void searchProduct(ActionEvent event) throws ClassNotFoundException, SQLException{
        ObservableList<Products> list = searchProduct(tfNameSearch.getText());
        if(list.size() > 0) {
            populateTable(list);
        }
    }

    @FXML void displayAllProducts(ActionEvent event) throws ClassNotFoundException, SQLException{
        ObservableList<Products> productsList = getAllRecords();
        populateTable(productsList);
    }


    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }



}
