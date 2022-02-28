package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.User;

public class DashboardController extends Controller<User> {
    
    @FXML private void handlePantry(ActionEvent e) throws IOException{
        
        Stage pantryStage = new Stage();
        pantryStage.setX(0);
        pantryStage.setY(stage.getY());
        ViewLoader.showStage(model.getPantry(), "/view/pantry.fxml", "Virtual Pantry", pantryStage, sm, pantry);
    
    }
    
    @FXML private void handleRecipes(ActionEvent e) throws IOException{
        
        Stage recipeStage = new Stage();
        recipeStage.setWidth(600); // UPDATE: make these values responsive
        recipeStage.setHeight(550);
        recipeStage.setX(Screen.getPrimary().getBounds().getMaxX()-600);
        recipeStage.setY(stage.getY());
        System.out.println(recipeStage.getWidth());
        //recipeStage.setX(Screen.getPrimary().getBounds().getMaxX());
        ViewLoader.showStage(model.getPantry(), "/view/recipes.fxml", "Virtual Pantry - Recipes", recipeStage, sm, pantry);
    
    }
        
    @FXML private void handleExit(ActionEvent e) throws IOException{
        
        Platform.exit();
        
    }
        
    public User getUser(){
        
        return model;
        
    }
    
}
