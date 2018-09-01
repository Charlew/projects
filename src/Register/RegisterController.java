package Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

/** Rejestracja */
public class RegisterController extends RegisterDAO{

    /**
     * Variables
     */
    @FXML private TextField txtEmail, txtUsername;
    @FXML private PasswordField txtPassword, txtConfirmPassword;
    @FXML private Label labelRegister;
    @FXML private Button buttonBack;

    /**
     * Functions
     */
    /** Rejestracja nowego usera */
    @FXML void Register(ActionEvent actionEvent) throws Exception{
        try{
            if(txtEmail.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtConfirmPassword.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Rejestracja");
                alert.setHeaderText("Wprowadź wszystkie dane!");
                alert.showAndWait();
            }else if(txtConfirmPassword.getText() != txtPassword.getText()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Rejestracja");
                alert.setHeaderText("Podane hasła różnią się!");
                alert.showAndWait();
            } else if(checkUserExists(txtUsername.getText())){
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

    /** Powrót do poprzedniej strony */
    @FXML void backToMainPage(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../sample/sample.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

}
