package Recipes;

public class ProductsToRecipe {
    public String name;
    public Integer kcal;
    public Integer amount;

    public ProductsToRecipe(String name, Integer kcal, Integer amount) {
        this.name = name;
        this.kcal = kcal;
        this.amount = amount;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}