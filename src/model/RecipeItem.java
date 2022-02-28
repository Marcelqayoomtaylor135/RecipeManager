package model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecipeItem {
    
    private StringProperty name;
    private String amountReq;
    
    public RecipeItem(String name){ 
        
        this.name = new SimpleStringProperty();
        this.name.set(name);
        
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

    public String getAmountReq() {
        
        return amountReq;
    
    }

    public void setAmountReq(String amountReq) {
        
        this.amountReq = amountReq;
    
    }
    
    public boolean hasName(String name){
        
        return getName().toLowerCase().contains(name.toLowerCase().trim());
        
    }
    
    @Override
    public String toString(){
        
        return name.getValue();
        
    }
    
}
