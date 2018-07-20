package com.example.surface4pro.bakingtime.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private Float quantity;
    private String measure;
    private String ingredient;

    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };

    Ingredient(Parcel in) {
        this.quantity = ((Float) in.readValue((Float.class.getClassLoader())));
        this.measure = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredient = ((String) in.readValue((String.class.getClassLoader())));
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quantity);
        dest.writeValue(measure);
        dest.writeValue(ingredient);
    }

    public int describeContents() {
        return 0;
    }
}