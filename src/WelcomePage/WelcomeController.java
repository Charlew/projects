package WelcomePage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

/** Głowna scena (Powitalna) */
public class WelcomeController{

    /**
     * Variables
     */
    @FXML private Label labelWelcome;
    @FXML private Button buttonProducts, buttonProfile, buttonRecipes, buttonDiary, buttonLogout;

    /**
     * Functions
     */
    /** Wyswietlanie username */
    public void getUsernameLabel(String user) {
         labelWelcome.setText("Witaj " + user + "!");
     }

    /** Wybór opcji przejsica do innej sceny */
    public void goTo(ActionEvent event) throws IOException {
        try{
            if(event.getSource() == buttonProducts){
                Stage primaryStage = (Stage) buttonProducts.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../Products/Products.fxml").openStream());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();
            }else if(event.getSource() == buttonProfile){
                Stage primaryStage = (Stage) buttonProfile.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../Profile/Profile.fxml").openStream());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();
            }else if(event.getSource() == buttonDiary) {
                Stage primaryStage = (Stage) buttonDiary.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../Diary/Diary.fxml").openStream());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();
            }else if(event.getSource() == buttonRecipes) {
                Stage primaryStage = (Stage) buttonRecipes.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../Recipes/Recipes.fxml").openStream());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();
            }else if(event.getSource() == buttonLogout) {
                Stage primaryStage = (Stage) buttonLogout.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("../Login/Login.fxml").openStream());
                Scene scene = new Scene(root, 1024, 768);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
