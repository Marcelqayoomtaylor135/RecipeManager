package model;

import java.util.ArrayList;
import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Recipe {
    
    private ArrayList<RecipeItem> ingredients;
    private ArrayList<Category> categories;
    private String title;
    private String description;
    private String imageUrl;
    private String timeReq;
    private String recipeUrl;
    
    private ObservableList<RecipeItem> missingIngredients = FXCollections.observableArrayList();
    private ObservableList<RecipeItem> ownedIngredients = FXCollections.observableArrayList();
    
    public Recipe(ArrayList<RecipeItem> ingredients, ArrayList<Category> categories, String title, String timeReq){

        this.ingredients = ingredients;
        this.categories = categories;
        this.title = title;
        this.timeReq = timeReq;
        this.imageUrl = "https://recipetineats.com/wp-content/uploads/2019/10/Cosmopolitan-cocktail_0.jpg";
        this.description = "Cosmopolitan Cocktails are a classic, elegant vodka cocktail made famous by the ladies of Sex and the City! Not to sweet, not too tart, super quick to make!"; // temp
        this.recipeUrl = "https://www.recipetineats.com/cosmopolitan-cocktails/"; // temp
    
    }
    
    // Designed for RecipeTinEats
    public Recipe(String url) throws IOException { 
        
       Document doc = Jsoup.connect(url).get();
       
       recipeUrl = url;
       
       imageUrl = doc.getElementsByClass("wp-block-image").first().getElementsByTag("img").attr("src");
       
       String title = doc.title();
       String[] splitTitle = title.split(" [|]");
       title = splitTitle[0];
       this.title = title;
       
       Elements metas = doc.getElementsByTag("meta");
       
       for (Element e : metas){
           if (e.attr("name").equals("description")){
               description = e.attr("content");
               break;
           }
       }

       this.timeReq = "placeholder";
       
       Elements ingredients = doc.getElementsByClass("wprm-recipe-ingredient");
       Elements ingredientNames = doc.getElementsByClass("wprm-recipe-ingredient-name");
       
       Set<String> uniqueIngredients = new HashSet<String>(); // Distinct values and orders it
       ArrayList<RecipeItem> finalIngredients = new ArrayList<RecipeItem>();
       
       for (Element ingName : ingredientNames) { uniqueIngredients.add(ingName.text()); }
       
       for (String ingredient : uniqueIngredients) { 
           RecipeItem finalIngredient = new RecipeItem(ingredient); 
           finalIngredients.add(finalIngredient);
       }
       
       this.ingredients = finalIngredients;
       this.categories = new ArrayList<Category>();
        
    }
    
    public ArrayList<RecipeItem> getIngredients(){
        
        return ingredients;
        
    }
    
    public String getImageUrl(){
        
        if (imageUrl != null){
            return imageUrl;
        } else {
            System.out.println("url was null");
            return null;
        }
        
    }
    
    public String getRecipeUrl(){
        
        return recipeUrl;
        
    }
    
    public String getDescription(){
        
        if (description != null){
            return description;
        } else{
            String failure = "Failed to find recipe description";
            return failure;
        }
        
    }
    
    public boolean canCook(ObservableList<PantryItem> pantryIngredients){
        
        missingIngredients.clear();
        ownedIngredients.clear();
        
        if (pantryIngredients.size() > 0){
            for (RecipeItem i : ingredients){
                boolean found = false;
                for (PantryItem p : pantryIngredients){
                    if (i.getName().toLowerCase().contains(p.getName().toLowerCase()) || p.getName().toLowerCase().contains(i.getName().toLowerCase())){
                        found = true;            
                        if (ownedIngredients.size() > 0){
                            for (RecipeItem j : ownedIngredients){
                                if (!j.hasName(i.getName())){
                                    ownedIngredients.add(new RecipeItem(i.getName()));
                                    break;
                                }
                            }
                        } else {
                                ownedIngredients.add(new RecipeItem(i.getName()));
                        }
                        break;
                    } 
                }
                if (!found) {
                    missingIngredients.add(new RecipeItem(i.getName()));
                } 
            } 
        } else {
            System.out.println("no items in pantry");
            return false;
        }
                
        
        if (missingIngredients.size() > 6){ // 6 missing ing for close recipes

            return false;
        }  
        
        if (ownedIngredients.size() < 1){
            
            return false;
            
        }
        
        return true;
        
    }
    
    public String getName(){
        
        return title;
        
    }
    
    public boolean hasName(String name){
        
        return getName().toLowerCase().contains(name.toLowerCase().trim());
        
    }
    
    public String toString(){
        
        return title;

    }
        
    public String getTitle() {return title;}    
    
    public ObservableList<RecipeItem> getMissingIngredients(){
        
        return missingIngredients;
        
    }
    
     public ObservableList<RecipeItem> getOwnedIngredients(){
         
        return ownedIngredients;
        
    }
     
}
