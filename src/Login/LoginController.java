package Login;

import WelcomePage.WelcomeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class LoginController extends LoginDAO{
    @FXML private TextField txtUsername;
    @FXML PasswordField txtPassword;
    @FXML private Button buttonBack;

    @FXML void backToMainPage(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../sample/sample.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    @FXML void Login(ActionEvent actionEvent) throws Exception{
        try {
            if(isLogin(txtUsername.getText(), txtPassword.getText())){
                Stage primaryStage = (Stage) txtUsername.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../WelcomePage/Welcome.fxml").openStream());
                WelcomeController welcomeController = (WelcomeController)loader.getController();
                welcomeController.getUsernameLabel(txtUsername.getText());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();

            }else if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Rejestracja");
                alert.setHeaderText("Wprowadź wszystkie dane!");
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText("Niepowodzenie logowania");
                alert.setContentText("Niepoprawna nazwa użytkownika lub hasło!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}