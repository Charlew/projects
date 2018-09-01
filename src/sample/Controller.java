package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** Scena startowa */
public class Controller {

    /**
     * Variables
     */
    @FXML private Button logIn, signIn;
    @FXML void handleClose(MouseEvent event){ System.exit(0); }

    /**
     * Functions
     */
    /** Wybór opcji przejsica do innej sceny */
    public void goToPage(ActionEvent event) throws Exception {
        if(event.getSource() == logIn){
            Stage stage = (Stage) logIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../Login/Login.fxml"));

            Scene scene = new Scene(root, 1024, 768);
            stage.setScene(scene);
            stage.show();
        }
        if(event.getSource() == signIn){
            try{
                Stage stage = (Stage) signIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("../Register/Register.fxml"));

                Scene scene = new Scene(root, 1024, 768);
                stage.setScene(scene);
                stage.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
