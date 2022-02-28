package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeItem;

public class RecipeCardController extends Controller<Recipe> {
        
    @FXML private Image image;
    @FXML private ImageView imageView;
    
    @FXML ListView<RecipeItem> ownedIngredientsLv;
    @FXML ListView<RecipeItem> missingIngredientsLv;
    
   @FXML private Label title;
   @FXML private Label description;
   @FXML private Hyperlink link;
       
    public void initialize(){
        
        image = new Image(model.getImageUrl());
        
        imageView.setImage(image);
        
        title.setText(model.getTitle());
        description.setText(model.getDescription());
        link.setText(model.getRecipeUrl());
                
        ownedIngredientsLv.setItems(model.getOwnedIngredients());
        missingIngredientsLv.setItems(model.getMissingIngredients());
        
    }
    
    @FXML 
    void openLink(ActionEvent event) throws URISyntaxException, IOException {
        
        System.out.println("Link clicked");
        Desktop.getDesktop().browse(new URI(link.getText()));
        
    }

    @FXML
    private void handleCook(ActionEvent event) throws IOException {
        
        // Sends a confirmation to the user if they don't have all the ingredients (e.g. you sure you want to continue?)
        if (model.getMissingIngredients().size() > 0){
            Stage confirmationStage = new Stage();
            ViewLoader.showStage(model, "/view/confirmation.fxml", "Confirmation", confirmationStage, sm, pantry);
            stage.close();
            return;
        }
        
        Stage checklistStage = new Stage();
        ViewLoader.showStage(model, "/view/ingredient_checklist.fxml", "Ingredient Checklist", checklistStage, sm, pantry);
        stage.close();
        
    }
 
}
