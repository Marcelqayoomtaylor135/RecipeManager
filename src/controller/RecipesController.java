package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Pantry;
import model.Recipe;
import model.User;

public class RecipesController extends Controller<Pantry> {
    
    public User primaryUser = this.primaryUser;
    
    @FXML private ListView<Recipe> recipesLv;
    private ObservableList<Recipe> availableRecipes;     
    @FXML private Button openRecipeBtn;
    @FXML private TextField recipeSearchTf;

    public void initialize(){

        recipesLv.getSelectionModel().selectedItemProperty().addListener((obs, oldRecipe, newRecipe) -> {
            
            openRecipeBtn.setDisable(false);
            
        });
        
        recipeSearchTf.textProperty().addListener(
            (observable, oldText, newText) -> {
                
                model.filterRecipeList(newText.toLowerCase());
                
        });
        
        recipesLv.setCellFactory(new Callback<ListView<Recipe>, ListCell<Recipe>>(){
            
            @Override
            public ListCell<Recipe> call(ListView<Recipe> param){
                ListCell<Recipe> cell = new ListCell<Recipe>(){
                    
                    @Override
                    protected void updateItem(Recipe recipe, boolean empty){
                        super.updateItem(recipe, empty);
                        if (recipe != null){
                            setText(recipe.getName());
                            if (recipe.getMissingIngredients().size() == 0){
                                setTextFill(javafx.scene.paint.Color.GREEN);
                            } else {
                                setTextFill(javafx.scene.paint.Color.DARKORANGE);
                            }
                            
                        }  else {
                            setText("");
                        }
                    }                      
                };
            return cell;
                        
            }
            
        });
                
    }
    
    public Pantry getPantry(){
        return model;
    } 
    
    @FXML private void handleRecipe(ActionEvent e) throws IOException{
        Stage recipeStage = new Stage();
        recipeStage.centerOnScreen();
        ViewLoader.showStage(getSelectedRecipe(), "/view/recipe.fxml", "Recipe: " + getSelectedRecipe().getTitle(), recipeStage, sm, pantry);
    }
    
    public Recipe getSelectedRecipe() {
        return recipesLv.getSelectionModel().getSelectedItem();
    }
        
}
