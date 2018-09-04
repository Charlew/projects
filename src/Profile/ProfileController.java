package Profile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Profil */
public class ProfileController extends ProfileDAO{

    /**
     * Variables
     */
    @FXML private Button buttonBack;
    @FXML private TextField tfAge, tfWeight, tfHeight, tfKcal;
    @FXML private CheckBox checkboxMale, checkboxFemale;

    /**
     * Functions
     */
    public void initialize() throws SQLException {
        /* Inicjalizacja profilu */
        setAllAttributes();
        tfAge.setText(getAge());
        tfHeight.setText(getHeight().toString());
        tfWeight.setText(getWeight().toString());
        tfKcal.setText(getKcal().toString());

        if(getSex().equals("Female")){
            checkboxFemale.setSelected(true);
        }else{
            checkboxMale.setSelected(true);
        }
    }

    /** Obliczanie zapotrzebowania kalorycznego na podstawie wprowadzonych danych */
    @FXML void countKcal(ActionEvent event){
        Integer weightTmp = Integer.parseInt(tfWeight.getText());
        Integer heightTmp = Integer.parseInt(tfHeight.getText());
        Integer ageTmp    = Integer.parseInt(tfAge.getText());

        if(tfWeight.getText().isEmpty() || tfHeight.getText().isEmpty() || tfAge.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText("Wprowadź wszystkie potrzebne dane!");
        }else{
            if(checkboxFemale.isSelected()){
                double result = (66.5 + (13.7 * weightTmp) + (5 * heightTmp) - (6.8 * ageTmp)) * 1.2;
                System.out.println(result);
                String resultStr = Integer.toString((int)result);
                tfKcal.setText(resultStr);
            }else{
                double result = (655  + (9.6 * weightTmp) + (1.85 * heightTmp) - (4.7 * ageTmp)) * 1.2;
                String resultStr = Integer.toString((int)result);
                tfKcal.setText(resultStr);
            }
        }
    }

    /** Zapisanie danych profilu */
    @FXML void save(ActionEvent event) throws Exception {
        if(checkboxMale.isSelected()){
            insertToProfile("Male", Integer.parseInt(tfAge.getText()), Integer.parseInt(tfWeight.getText()), Integer.parseInt(tfHeight.getText()), Integer.parseInt(tfKcal.getText()));

            boolean redirect = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Powiadomienie");
            alert.setContentText("Zapisano!");
            alert.showAndWait();

            if(alert.isShowing()){
                redirect = false;
            }else{
                redirect = true;
            }
            if(redirect){
                backButtonHandle(event);
            }
        }else {
            insertToProfile("Female", Integer.parseInt(tfAge.getText()), Integer.parseInt(tfWeight.getText()), Integer.parseInt(tfHeight.getText()), Integer.parseInt(tfKcal.getText()));

            boolean redirect = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Powiadomienie");
            alert.setContentText("Zapisano!");
            alert.showAndWait();

            if(alert.isShowing()){
                redirect = false;
            }else{
                redirect = true;
            }
            if(redirect){
                backButtonHandle(event);
            }
        }
    }

    /** Powrót do poprzedniej strony */
    @FXML void backButtonHandle(ActionEvent event) throws Exception{
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../WelcomePage/Welcome.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    /** Ustawianie tylko jednej mozliwosci w checkboxach */
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
