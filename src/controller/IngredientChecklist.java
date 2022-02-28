package controller;

import au.edu.uts.ap.javafx.Controller;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import model.Recipe;
import model.RecipeItem;
import javafx.scene.control.CheckBox;
import model.PantryItem;

public class IngredientChecklist extends Controller<Recipe> {
        
    @FXML private VBox checklistVBox;
    
    private int elementCounter = 0;
    
    public void initialize(){
        
        for (RecipeItem r : model.getIngredients()){
            elementCounter++;
            System.out.println("checking " + r.getName());
            checklistVBox.getChildren().add(new CheckBox(r.getName()));
        }
            
    }
    
    @FXML
    private void submitCook(ActionEvent e) throws IOException {
        
        ArrayList<String> emptyIng = new ArrayList<String>();
        
        for(Node nodeIn:checklistVBox.getChildren()){
            if(nodeIn instanceof CheckBox){
                if (((CheckBox) nodeIn).isSelected()){
                    String tempIng = ((CheckBox) nodeIn).getText();
                    if (!emptyIng.contains(tempIng)){
                        emptyIng.add(tempIng);
                    }                                            
                }
            }
        }
        
        for (String ing : emptyIng){
            PantryItem ingredient = pantry.getItem(ing);
            if (ingredient != null){
                pantry.deduct(ingredient);
                sm.deductIngredient(ingredient);
            } else {
                System.out.println("ing was null, check getItem method");
            }
        }
        
        stage.close();
         
    }
 
}
