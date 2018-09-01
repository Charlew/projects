package Recipes;

/** Klasa pomocnicza dla dodawania do tabelki "Produktow do przepisu" */
public class ProductsToRecipe {

    /**
     * Variables
     */
    public String name;
    public Integer kcal;
    public Integer amount;

    /**
     * Constructors
     */
    public ProductsToRecipe(String name, Integer kcal, Integer amount) {
        this.name   = name;
        this.kcal   = kcal;
        this.amount = amount;
    }

    /**
     * Getters & setters
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}