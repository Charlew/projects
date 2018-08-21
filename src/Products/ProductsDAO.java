package Products;

import DbConnection.ConnectionManager;
import Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsDAO {
    LoginController loginController = new LoginController();

    protected  void insertNewProduct(String name, String kcal, String amount){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("INSERT IGNORE INTO products SET id_user = ?, name = ?, kcal = ?, amount = ?, username = ?;");
            preparedStatement.setString(1, loginController.getUserId().toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, kcal);
            preparedStatement.setString(4, amount);
            preparedStatement.setString(5, loginController.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Products> getAllRecords() throws ClassNotFoundException, SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products");
            resultSet = preparedStatement.executeQuery();
            ObservableList<Products> productsList = getProductsObjects(resultSet);
            return productsList;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Products> getProductsObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException{
        try{
            ObservableList<Products> productsList = FXCollections.observableArrayList();

            while(resultSet.next()){
                Products products = new Products();
                products.setNameProperty(resultSet.getString("name"));
                products.setKcalProperty(resultSet.getInt("kcal"));
                products.setAmountProperty(resultSet.getInt("amount"));
                products.setUsernameProperty(resultSet.getString("username"));
                productsList.add(products);
            }
            return productsList;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static ObservableList<Products> searchProduct(String name) throws ClassNotFoundException, SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
           preparedStatement = ConnectionManager.getConnection().prepareStatement("SELECT * FROM products WHERE name = ?");
           preparedStatement.setString(1, name);

           resultSet = preparedStatement.executeQuery();
           ObservableList<Products> list = getProductsObjects(resultSet);
           return list;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
