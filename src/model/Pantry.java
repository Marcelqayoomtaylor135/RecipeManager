package model;

import java.util.ArrayList;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Pantry {
        
    private String name;
    
    private ObservableList<PantryItem> items = FXCollections.observableArrayList();
    private ObservableList<PantryItem> current = FXCollections.observableArrayList();
    
    private Recipes recipes;
     
    private ObservableList<Recipe> availableRecipes = FXCollections.observableArrayList();
    private ObservableList<Recipe> currentAvailableRecipes = FXCollections.observableArrayList();    
    
 
    
    public Pantry(String name){
        
        this.name = name;
        
        recipes = new Recipes();        
        //availableRecipes = recipes.filterOnItems(items); // new        
        items.addListener(new ListChangeListener<PantryItem>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends PantryItem> p) {
                System.out.println("\n" +  "Item " + p + " has either been added or removed from pantry");
                availableRecipes.clear();
                currentAvailableRecipes.clear();
                if (recipes.filterOnItems(items) != null){
                    availableRecipes.addAll(recipes.filterOnItems(items));                    
                    currentAvailableRecipes.addAll(availableRecipes);
                }
                current.clear();
                current.addAll(items);
            }
        });
        
        //items.add(new PantryItem("Vodka"));
        //items.add(new PantryItem("Cranberry Juice"));
    }
    
    
    
    public PantryItem add(String ingredient){
        
        PantryItem ing = null;
        boolean found = false;
        for (PantryItem p : items){
            if (p.hasName(ingredient)){
                found = true;
                System.out.println("That ingredient already exists, try again");
                break;
            }
        }
        if (!found){
          ing = new PantryItem(ingredient);
          items.add(ing);
        }
                 // temp, must replace with physical error sign       
           
        return ing; 

    }
    
    public PantryItem getItem(String name){
                
        for (PantryItem p : items){
            
            String pantryItemName = p.getName().toLowerCase();
                                    
            if (name.toLowerCase().contains(pantryItemName) || pantryItemName.contains(name.toLowerCase())){ 
                System.out.println("RETURNING P");
                return p; 
            }             
        }
        
        System.out.println("RETURNING NULL");
        return null;
        
    } 
    
    public String getName(){ return this.name; }
    
    public void deduct(PantryItem p){ // used to have argument amount too
        if (p != null){
            try {
                System.out.println("Amount deducted is more than owned, deleting item completely"); 
                items.remove(p);            
            } catch (Exception e) {
                System.out.println("An error has occured trying to add the ingredient to the pantry");
            }
        } else {
            System.out.println("System couldn't find item, try again");
        }        
    }

    public void deduct(String ingName) {
        
        PantryItem ing = getItem(ingName.toLowerCase());
        
        if (ing != null){
            
            System.out.println("amount deducted is more than owned, deleting item completely"); 
            items.remove(ing);
            
        } else {
            System.out.println("System couldn't find item, try again");
        }
        
    }
    
/*
    
    private void cook(Recipe recipe){
        
        if (recipe != null){
            ArrayList<RecipeItem> ingredients = recipe.getIngredients();
            if (confirmation() == 'y'){
                if (hasIngredients(recipe)){
                    for (RecipeItem i : ingredients){
                        for (PantryItem p : items){
                            if (i.getName().toLowerCase().contains(p.getName().toLowerCase())){ // "contains" checks whether string is within the other. E.g. "salt" would be coutned as "sea salt"
                                deduct(p);
                                break;
                            } 
                        }
                    }   
                } else {
                    System.out.println("You don't have enough ingredients to make this recipe");
                }
            }         
        } else {
            System.out.println("recipe not found");
        }

    } */
    
    private Recipe getRandomRecipe(){
        return recipes.getRecipe(getRandom(0,recipes.getSize()-1));
    }
    
    private int getRandom(int min, int max){
        Random random = new Random();
        return random.ints(min,max).findFirst().getAsInt();
    }
    
    public boolean hasIngredients(Recipe r){
        
        ArrayList<RecipeItem> ingredients = r.getIngredients();
        ArrayList<String> missingIngredients = new ArrayList<String>();
        
        if (items.size() > 0){
            for (RecipeItem i : ingredients){
                boolean found = false;
                for (PantryItem p : items){
                    if (i.getName().toLowerCase().contains(p.getName().toLowerCase())){
                        found = true;
                        break;
                    } 
                }
                if (found) {System.out.print("Found " + i.getName() + ", ");} else {
                    missingIngredients.add("missing: " + i.getName());
                }
            } 
        } else {
            System.out.println("no items in pantry");
            return false;
        }
        
        if (missingIngredients.size() > 0){
            for (String s : missingIngredients){
                System.out.println(s);
            }
            return false;
        } 
        return true;
    }
    
    
    private void show(){
        if (!items.isEmpty()){
            for (PantryItem p : items){
                System.out.println(p.getName());
            }            
        } else {
            System.out.println("No items in the pantry");
        }

    }
    
    private void help(){
        System.out.println("bad input, try again");
    }
    
    public ObservableList<PantryItem> getItems(){
        return current;
    }
    
    public ObservableList<Recipe> getAvailableRecipes(){
        return currentAvailableRecipes;// temp comment out
        //return recipes.getRecipeList();
        
    }
    
    public Recipes getAllRecipes(){
        return recipes;
    }
    
    public void filterList(String name) {

        System.out.println("working with: " + name);
        
        ObservableList<PantryItem> temp = FXCollections.observableArrayList();

        items.forEach((PantryItem p) -> {
            try {
                System.out.println("comparing " + p.getName());
                if (p.hasName(name)) {
                    System.out.println(p.getName() + " has name " + name);
                    temp.add(p);
                }
            } catch (NumberFormatException e) {
            }
        });
        //System.out.println("temp contains: " + temp.toString()); 
        // hi hi hi 
        
         
        
        this.current.clear();
        this.current.addAll(temp);
    }
    
    public void filterRecipeList(String name){
        
        ObservableList<Recipe> temp = FXCollections.observableArrayList();
        
        availableRecipes.forEach((Recipe r) -> {
            try {
                if (r.hasName(name)) {
                    System.out.println(r.getName() + " has name " + name);
                    temp.add(r);
                }
            } catch (NumberFormatException e) {
            }
        });
        
        currentAvailableRecipes.clear();
        currentAvailableRecipes.addAll(temp);
        
    }
    
}
