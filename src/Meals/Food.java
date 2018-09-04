package Meals;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Klasa pomocnicza dla posilkow */
public class Food {

    /**
     * Variables
     */
    private StringProperty nameProperty;
    private IntegerProperty kcalProperty;

    /**
     * Constructors
     */
    public Food(){
        this.nameProperty = new SimpleStringProperty();
        this.kcalProperty = new SimpleIntegerProperty();
    }

    /**
     * Getters & setters
     */
    public String getNameProperty() {
        return nameProperty.get();
    }
    public StringProperty namePropertyProperty() {
        return nameProperty;
    }
    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public int getAllKcalProperty() {
        return kcalProperty.get();
    }
    public IntegerProperty allKcalProperty() {
        return kcalProperty;
    }
    public void setAllKcalProperty(int kcalProperty) {
        this.kcalProperty.set(kcalProperty);
    }
}
