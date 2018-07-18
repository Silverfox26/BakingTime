package com.example.surface4pro.bakingtime.data;


public class Ingredient {

    private Float quantity;
    private String measure;
    private String ingredient;

    /**
     * No args constructor for use in serialization
     */
    public Ingredient() {
    }

    /**
     * Constructs an Ingredient object.
     *
     * @param measure    Unit of Measurement.
     * @param ingredient Name of the Ingredient.
     * @param quantity   Quantity of the Ingredient.
     */
    public Ingredient(Float quantity, String measure, String ingredient) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

}