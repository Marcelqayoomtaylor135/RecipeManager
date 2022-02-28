package model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PantryItem {
    
    private StringProperty name;
    private double amountOwned;

    public PantryItem(String name){ 
        
        this.name = new SimpleStringProperty();
        this.name.set(name);
        System.out.println("New ingredient created");
        
    }
    
     public ReadOnlyStringProperty nameProperty() {
        return name;
    }
     
    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getAmountOwned() {
        return amountOwned;
    }

    public void setAmountOwned(double amountOwned) {
        this.amountOwned = amountOwned;
    }
    
    public boolean hasName(String name){
        return this.getName().toLowerCase().contains(name.toLowerCase().trim());
    }
    
    @Override
    public String toString(){
        return name.getValue();
    }
        
}
