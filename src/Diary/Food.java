package Diary;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Food {

    private StringProperty nameProperty;
    private IntegerProperty kcalProperty;

    public Food(){
        this.nameProperty = new SimpleStringProperty();
        this.kcalProperty = new SimpleIntegerProperty();
    }

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
