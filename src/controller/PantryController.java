package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Pantry;
import model.PantryItem;

public class PantryController extends Controller<Pantry> {
        
    @FXML private ListView<PantryItem> pantryItemLv;
    private ObservableList<PantryItem> items; 
    
    @FXML private Button deleteButton;
    
    @FXML private TextField ingredientTf;
    @FXML private TextField ingredientSearchTf;

    public void initialize(){
        
        ingredientSearchTf.textProperty().addListener(
            (observable, oldText, newText) -> {
                model.filterList(newText.toLowerCase()); 
        });
        
        pantryItemLv.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            deleteButton.setDisable(false);
        }); 
 
    }
    
    @FXML private void handlePantry(ActionEvent e) throws IOException{
        
        ViewLoader.showStage(model, "/view/pantry.fxml", "Virtual Pantry", stage, sm, pantry);
        
    }
    
    @FXML private void handleAdd(ActionEvent e) throws IOException{
        
        String ingredient = ingredientTf.getText();
        PantryItem ing = model.add(ingredient);
        
        if (ing != null){
            sm.insertIngredient(ing, model);

        }
        
        ingredientTf.setText("");
            
    }
    
    private PantryItem getSelectedItem() {
        
        System.out.println(pantryItemLv.getSelectionModel().getSelectedItem());
        return pantryItemLv.getSelectionModel().getSelectedItem();
        
    }
    
    @FXML private void handleDelete(ActionEvent e){
        
        PantryItem ing = getSelectedItem();
        
        if (ing != null){
            model.deduct(ing);
            sm.deductIngredient(ing);
            
        }
        
        deleteButton.setDisable(true);
        
    }
    
    public Pantry getPantry(){
        
        return model;
        
    }
    
}
