package Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

public class RegisterController extends RegisterDAO{
    @FXML private TextField txtEmail, txtUsername, txtPassword;
    @FXML private Label labelRegister;
    @FXML private Button buttonBack;

    @FXML void Register(ActionEvent actionEvent) throws Exception{
        try{
            if(checkUserExists(txtUsername.getText())){
                labelRegister.setText("Istnieje już użytkownik o podanej nazwie");
            }else{
                boolean redirect = false;
                addUser(txtUsername.getText(), txtEmail.getText(), txtPassword.getText());
                System.out.println("Password incorrect!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Rejestracja");
                alert.setHeaderText("Rejestracja przebiegła pomyślnie!");
                alert.setContentText("Zostaniesz przekierowany na strone logowania!");
                alert.showAndWait();
                if(alert.isShowing()){
                    redirect = false;
                }else{
                    redirect = true;
                }
                if(redirect){
                    Stage stage = (Stage) labelRegister.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("../Login/Login.fxml"));
                    Scene scene = new Scene(root, 1024, 768);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML void backToMainPage(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../sample/sample.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

}
