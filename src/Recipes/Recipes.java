package Recipes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Recipes {
    private IntegerProperty idRecipeProperty;
    private StringProperty nameProperty;
    private IntegerProperty allKcalProperty;
    private StringProperty descriptionProperty;
    private StringProperty usernameProperty;

    public Recipes(){
        this.nameProperty = new SimpleStringProperty();
        this.allKcalProperty = new SimpleIntegerProperty();
        this.descriptionProperty = new SimpleStringProperty();
        this.idRecipeProperty = new SimpleIntegerProperty();
        this.usernameProperty = new SimpleStringProperty();

    }
    //ID RECIPE
    public int getIdRecipe() {
        return idRecipeProperty.get();
    }
    public IntegerProperty idRecipePropertyProperty() {
        return idRecipeProperty;
    }
    public void setIdRecipeint (Integer idRecipeProperty) {
        this.idRecipeProperty.set(idRecipeProperty);
    }

    // NAME
    public String getName(){
        return nameProperty.get();
    }
    public void setNameProperty(String name){
        this.nameProperty.set(name);
    }
    public StringProperty getRecipeName(){
        return nameProperty;
    }

    // ALL KCAL
    public Integer getAllKcal(){
        return allKcalProperty.get();
    }
    public void setAllKcalProperty(Integer kcal){
        this.allKcalProperty.set(kcal);
    }
    public IntegerProperty getRecipeAllKcal() {
        return allKcalProperty;
    }

    // DESCRIPTION
    public String getDescription(){
        return descriptionProperty.get();
    }
    public void setDescriptionProperty(String description){
        this.descriptionProperty.set(description);
    }
    public StringProperty getRecipeDescription(){ return descriptionProperty;
    }

    //USERNAME
    public String getUsername() { return usernameProperty.get(); }
    public StringProperty getRecipeUsername() { return usernameProperty; }
    public void setUsernameProperty(String usernameProperty) { this.usernameProperty.set(usernameProperty); }
}
