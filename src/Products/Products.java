package Products;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Products {
    private StringProperty nameProperty;
    private IntegerProperty kcalProperty;
    private IntegerProperty amountProperty;
    private StringProperty usernameProperty;

    public Products(){
        this.nameProperty = new SimpleStringProperty();
        this.kcalProperty = new SimpleIntegerProperty();
        this.amountProperty = new SimpleIntegerProperty();
        this.usernameProperty = new SimpleStringProperty();
    }
    // NAME
    public String getName(){
        return nameProperty.get();
    }
    public void setNameProperty(String name){
        this.nameProperty.set(name);
    }
    public StringProperty getProductName(){
        return nameProperty;
    }

    // KCAL
    public Integer getKcal(){
        return kcalProperty.get();
    }
    public void setKcalProperty(Integer kcal){
        this.kcalProperty.set(kcal);
    }

    public IntegerProperty getProductKcal() {
        return kcalProperty;
    }

    // AMOUNT
    public Integer getAmount(){
        return amountProperty.get();
    }
    public void setAmountProperty(Integer amount){
        this.amountProperty.set(amount);
    }
    public IntegerProperty getProductAmount() {
        return amountProperty;
    }

    // USERNAME
    public String getUsername(){
        return usernameProperty.get();
    }
    public void setUsernameProperty(String username){
        this.usernameProperty.set(username);
    }
    public StringProperty getProductUsername(){
        return usernameProperty;
    }
}
