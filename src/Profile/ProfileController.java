package Profile;

import DbConnection.ConnectionManager;
import Login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    Connection connection = ConnectionManager.getConnection();
    LoginController loginController = new LoginController();
    @FXML private Button buttonBack;
    @FXML private TextField tfAge, tfWeight, tfHeight, tfKcal;
    @FXML private CheckBox checkboxMale, checkboxFemale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND id_user = ?");
            preparedStatement.setString(1, loginController.getUsername());
            preparedStatement.setString(2, loginController.getUserId().toString());

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                tfAge.setText(resultSet.getString("age"));
                tfWeight.setText(resultSet.getString("weight"));
                tfHeight.setText(resultSet.getString("height"));
                tfKcal.setText(resultSet.getString("kcal_demand"));
                if(resultSet.getString("sex").equals("Female")){
                    checkboxFemale.setSelected(true);
                }else{
                    checkboxMale.setSelected(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(String sex, String age, String weight, String height, String kcalDemand){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("UPDATE users SET sex = ?, age = ?, weight = ?, height = ?, kcal_demand = ? WHERE username = ? AND id_user = ?");
            preparedStatement.setString(1, sex);
            preparedStatement.setString(2, age);
            preparedStatement.setString(3, weight);
            preparedStatement.setString(4, height);
            preparedStatement.setString(5, kcalDemand);
            preparedStatement.setString(6, loginController.getUsername());
            preparedStatement.setString(7, loginController.getUserId().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML void buttonCountKcalHandler(ActionEvent event){
        Integer weightTmp = Integer.parseInt(tfWeight.getText());
        Integer heightTmp = Integer.parseInt(tfHeight.getText());
        Integer ageTmp    = Integer.parseInt(tfAge.getText());
        if(checkboxFemale.isSelected()){
            double result = (66.5 + (13.7 * weightTmp) + (5 * heightTmp) - (6.8 * ageTmp)) * 1.2;
            System.out.println(result);
            String resultStr = Double.toString((int)result);
            tfKcal.setText(resultStr);
        }else{
            double result = (655  + (9.6 * weightTmp) + (1.85 * heightTmp) - (4.7 * ageTmp)) * 1.2;
            String resultStr = Double.toString((int)result);
            tfKcal.setText(resultStr);
        }
    }

    @FXML void buttonSaveHandler(ActionEvent event){
        if(checkboxMale.isSelected()){
            insertData("Male", tfAge.getText(), tfWeight.getText(), tfHeight.getText(), tfKcal.getText());
        }else {
            insertData("Female", tfAge.getText(), tfWeight.getText(), tfHeight.getText(), tfKcal.getText());
        }

    }

    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    @FXML void handleCheckBoxMale(){
        if(checkboxMale.isSelected()){
            checkboxFemale.setSelected(false);
        }
    }
    @FXML void handleCheckBoxFemale(){
        if(checkboxFemale.isSelected()){
            checkboxMale.setSelected(false);
        }
    }


}
