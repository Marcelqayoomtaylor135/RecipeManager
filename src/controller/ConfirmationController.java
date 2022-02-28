/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Recipe;


public class ConfirmationController extends Controller<Recipe> {
        
    @FXML private Button confirmBtn;
    
    @FXML private Button denyBtn;
    
    public void initialize(){}
    
    @FXML
    private void confirm(ActionEvent e) throws IOException {
        
        Stage checklistStage = new Stage();
        ViewLoader.showStage(model, "/view/ingredient_checklist.fxml", "Ingredient Checklist", checklistStage, sm, pantry);
        stage.close();
        
    }
    
    @FXML private void decline(ActionEvent e) throws IOException {
        
        stage.close();
        ViewLoader.showStage(model, "/view/recipe.fxml", "Recipe: " + model.getTitle(), stage, sm, pantry);

    }
    
    @FXML
    private void exit(ActionEvent e) throws IOException {
        
        stage.close();
        
    }
 
}
