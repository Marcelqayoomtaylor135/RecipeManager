package model;

import java.util.ArrayList;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Recipes {
    
    private ObservableList<Recipe> recipes = FXCollections.observableArrayList();
    
    public Recipes(){
        
        try {
            ArrayList<String> recipeUrls = scrapeRecipeLinks();
            for (String url : recipeUrls){
                recipes.add(new Recipe(url));
            }
            System.out.println("Number of recipes scraped w/o regard to pantry: " + recipeUrls.size());
        } catch (IOException e){
            System.out.println("An io error has occurred");
        }
        
    }    
    
    // JSoup version, in future, don't run this every time (takes too long), write a file
    // and let the user (fetch new recipes) by the click of a button 
    private ArrayList<String> scrapeRecipeLinks() throws IOException {
        
        ArrayList<String> recipeUrls = new ArrayList<String>();
        
        String absUrl = "https://www.recipetineats.com/recipes/";
        String relUrl = "https://www.recipetineats.com/recipes/?fwp_paged=";
      
        Document doc = Jsoup.connect(absUrl).get();
        int count = 1;
        
        // Cycle through each tab of the recipe pages until there is no more content
        while (count < 10){
            doc = Jsoup.connect(relUrl+count).get();
            Element main = doc.getElementById("genesis-content");
            Elements links = main.getElementsByClass("entry-image-link");
            
            for (Element recipe : links){
                Document recipeDoc = Jsoup.connect(recipe.attr("href")).get();
                Element recipeMain = recipeDoc.getElementById("recipe");
                if (recipeMain != null){
                    recipeUrls.add(recipe.attr("href")); // if the link actually is a recipe, then add it to the list                    
                }
            } 
            count++;
        }
        
        return recipeUrls;
        
    }
    
    private Recipe manualCreate(String name){
        
        ArrayList<RecipeItem> ingredients = new ArrayList<RecipeItem>();
        ArrayList<Category> categories = new ArrayList<Category>();
        String title = name;
        String timeReq = "30 Minutes";
        
        
        ingredients.add(new RecipeItem("Vodka"));
        ingredients.add(new RecipeItem("Cranberry Juice"));
        ingredients.add(new RecipeItem("Milk"));
        categories.add(new Category("Japanese"));
        
        return new Recipe(ingredients, categories, title, timeReq);
        
    }
    
    public Recipe getRecipe(int index){
        
        return recipes.get(index);
        
    }
    
    public ObservableList<Recipe> getRecipeList(){
        
        return recipes;
        
    }    
    public int getSize(){
        
        return recipes.size();
        
    }
    
    public String show(){
        
        String message = "";
        message += "\n" + "All recipes:";       
        
        for (int i = 0; i < recipes.size(); i++){
            message += ("\n" + (i+1) + ". " + getRecipe(i).getTitle());
        }
        
        return message;
        
    }
    
    public ObservableList<Recipe> filterOnItems(ObservableList<PantryItem> ingredients){
        
        System.out.println("\n " + "Available recipes:");
        
        ObservableList<Recipe> filteredRecipes = FXCollections.observableArrayList();
        
        if (recipes.size() > 0){
            for (int i = 0; i < recipes.size(); i++){
                if (recipes.get(i).canCook(ingredients)){
                    filteredRecipes.add(recipes.get(i));
                    System.out.println("\t" + (i+1) + ". " + recipes.get(i));
                }            
            }
        }

        if (filteredRecipes.size() > 0){
            return filteredRecipes;
        }
        
        System.out.println("Returning null");
        return null;
        
    } 
            
}