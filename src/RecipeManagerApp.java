import au.edu.uts.ap.javafx.ViewLoader;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import model.Pantry;
import model.User;
import storage.StorageManager;

public class RecipeManagerApp extends Application {
        
    User user;
    private final String dbName = "userdata.db";
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        StorageManager sm = new StorageManager();
        
        User user = null;
        Pantry pantry = null;
        
        // Checks if database doesn't exist and therefore creates it
        if (!sm.fileExists(dbName)){
            
            System.out.println("DB doesn't exist, creating it now");
            
            // If db doesn't exist, built it by creating all the tables
            String ingredientsSql = "IngredientID integer PRIMARY KEY AUTOINCREMENT, \n" 
                                 + " name text NOT NULL, \n"
                                 + " PantryID integer, \n"
                                 + " CONSTRAINT PantryID \n"
                                 + " FOREIGN KEY (PantryID) \n"
                                 + " REFERENCES pantries(PantryID)"
                                 + ");";
            
            String pantrySql = "PantryID integer PRIMARY KEY AUTOINCREMENT, \n"
                    + " name text NOT NULL"
                    + ");";
                        
            String usersSql = "UserID integer PRIMARY KEY AUTOINCREMENT,\n"  
                   + " username text NOT NULL,\n"  
                   + " PantryID integer, \n"
                   + " CONSTRAINT PantryID \n"
                   + " FOREIGN KEY (PantryID) \n"
                   + " REFERENCES pantries(PantryID)"
                   + ");";
            
            try {
                sm.createNewTable("ingredients", ingredientsSql, dbName);

                sm.createNewTable("pantries", pantrySql, dbName);

                sm.createNewTable("users", usersSql, dbName);          
                                
            } catch (SQLException e) {
                e.printStackTrace();
            }
                        
        } else {

            System.out.println("Database Exists");
            
        }
        
        String username = System.getProperty("user.name");
                
        // if user doesn't exist create one and send it to the database, else just get it from the database
        if (!sm.userExists(username)){
            System.out.println("USER HAS NOT BEEN CREATED");
            pantry = new Pantry(username+"'s Pantry");
            user = new User(username, pantry);
            sm.insertPantry(user, "pantries");
            sm.insert(user, "users");
        } else { 
            try {
                pantry = new Pantry(username+"'s Pantry");
                sm.load(pantry, sm.getPantryKey(pantry));
                user = new User(username, pantry);
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        ViewLoader.showStage(user,  "/view/session.fxml", "Virtual Pantry - Dashboard", stage, sm, pantry);

    }
    
}


/*

JOURNAL

19/11/2021
Started the project, set up the project organisation, planned the project, and established the 
initial model files.

21/11/2021
Expanded the pantry system to add, deduct and show ingredients from the pantry 
(with exact quanaties). This system is fully functional and is working as intended
for the final product.

I also started working on the recipe system. Now, recipes can be made (hard coded) and 
the user can select them (currently this just prints the recipe)

Next I will make it so selecting the recipe will "cook" it, deducting the
recipe's ingredient amounts from those of the pantry


24/11/2021

Idk how i've done this. I'm drunk but i've solved all of my bugs and completed the cook function
The next step is to gather recipes from a site. holy shit thats exciting. i could be wrong

25/11/2021
I've imported a package called Jsoup that is a tool for scraping html from webpages.
I've successfully managed to scrape and collect all the recipes off my target website and create an array of urls
Next step is to go through and make recipes in the system. This will require a for loop for each recipe and then another for adding each ingredient. 
It might be a bit messy.

Ok update, it's messy. I've managed to loop through each ingredient. However, i'm trying to figure out how I should store ingredients. 
Some ingredients are just listed as "2 tablespoons". At first i thought i should convert that to grams. However, i realise now that different ingredients will have different masses.
E.g. 20g of water might be 1 tbsp, while 1 tsbp of gravy might be 30g. There are converters online tho.

26/11/2021 
I've decided that im gonna reduce the scope of the project. Instead of calculating the amount needed per 
ingredient and deducting from the pantry, im just gonna check whether the user has the ingredients or not. 
This is because I cant track ingredient deductions when the user cooks recipes outside of the app. 
Also, people won't want to measure the ingredients that they have when beginning (since everything is already opened)

Now that we're just checking whether the user has the ingredient or not, amount is not needed. However, now when a user cooks a dish, how does the pantry update.
I.e. I use the rest of my soy sauce in a recipe, how will the suggested recipes update to remove soy sauce. 
Currently, im thinking to have a "review" system after choosing a dish. When the recipe is selected, the app will ask: "did/will you run out of x ingredient after cooking"
and update it that way. 

27/11/2021
Things are going well. Recipes are successfully scraped and created as individual recipes (as well as individual ingredients). 
I'm struggling to find next steps. Things are working as planned. 
    1. Users can add/deduct/show ingredients to their pantry
    2. Recipes are scraped and added with individual ingredients
    3. If a recipe is chosen, it deducts pantry ingredients
    4. Random recipe can be chosen

The next step is to only show available recipes when "cook" is selected.
Did it.

Next step is legit error checking then planning, styling, building the front end. 
Also fixing the ingredient search to be more precise

17/11/2021

Started the front end. My goal is to have a very simplistic deign just to get out a minimum viable product.
Having some trouble doing the login and signup system. 
Having trouble getting the Users instance from the system to run the function of logging in (i.e. checking deets) for differing methods
Will figure it out

20/11/2021

Figured out my recent problem, just had some fxml issues.
Now I am looking for my next steps. From here I need to do the dashboard and all the subsequent pages.
This will take me a long time to look pretty. Might just make a bad dashboard and work on the pantry first.

21/11

Started on the pantry. Currently, the list view is working with the pantry's ingredients.
I have made it so you can add ingredients to the list. I next need to make it so that the TF clears when entered.
Also, i want to make a button in each list view item that has a red 'x' button to remove ingredients. 
Temp solution could just be a overall delete button that appears when an item is selected and deletes the selected item.

13/01/2022

Changes i've made in bowral. Made a obs list of "available recipes"
added a listener to it for the items
made sure that it checks when the items are available
made seperate stages to demonstrate functionality
shows only available recipes

also prepared a "open recipe" button so that a recipe page will open up

14/01/2022

I've started to implement a search bar for the pantry items.
It seems to be working. However, i get an error every time i add an item.
Can't tell if i didnt notice this before or not.
I think it bugs out because it checks for available recipes

Ok i fixed it. When i added an ingredient, it checked what recipes were available passing through null rather than an empty list. 
I made it to check whether the list was null before proceeding.

Now however, list doesnt return to it original state before searching.
It also updates the recipes as you search which isnt ideal.

Fixed both issues. Made a new list called current. Added current to follow the changes of "items" via a change listener.
Also made the fxml lv link to current. 
Changed filter list to only affect current.

15/01/2022

Today i designed and built the recipe cards for each recipe. They look quite good. 
Right now im stuck on trying to distinguish the owned and missing ingredients into two seperate lists and displaying them on the recipe card.
My current thought is to give the recipe two lists, owned and missing ingredients. 
Then, when place a change listener on the items list to update the recipes.
Downsides of the strategy is that every single recipe will be updating when one item gets changed, very resource intensive.


16/01/2022
I've realised that recipe's have a "canCook" method that finds missing ingredients. So, by making missingIng and OwnedIng observable lists,
I can update them whenever canCook is called. CanCook method is accessible by the user from pantry, recipes, recipe. 

Finished it. Did exactly what I described above. Also made the "ownedIng" and "missingIng" green and red respectively. 
Also made it so the link actually worked.


17/01/2022

Today my goals are too:
    1. Research save file system - DONE
    2. Re-implement the delete button - DONE

I've made it so recipes come up in the list when you are close to being able to make it. 
I want to next colour code the list so that green represents can cook now, and orange represents nearly there.

One bug i've noticed. You can add items that already exist in the pantry. Shouldn't be too hard of a bug.
Fixed it. Made sure that the item checks for duplicates before adding. Also changed Recipe.hasName() to 
include "equals" rather than "contains". This may cause a problem of people adding redundant items like
pinapple and pinapple slices but hey, that's a future me problem.


Future goals:
    1. Implement save file system
    2. Add a bulk add feature
    3. Add a "did you run out of anything?" after "cooking" a recipe
    4. Design a way for all recipes to be shown and colour coded based on availability

20/01/2022

Today I planned and started the cook functionality for recipe cards. I will next setup a new view
that promps individuals if they still want to cook (if they dont have enough ingredients according to pantry).

Once confirmed, or if the user has all ingredients, the cook function will run and display a menu. This menu
will contain a checklist of whether the user they ran out of the required ingredients. One problem i see is that 
users might get confused with ingredients they still have as the checkbox uses ticks. If i inverse it (e.g. what do you
have remaining) more clicks are most likely going to occur, resulting in a worse user experience. If i can change 
the checkbox icon from ticks, this would be ideal. 

21/01/2022
Made the basic shell of the ingredient checklist. Essentially, i make a simple vbox. When the scene is initialised,
I loop through each ingredient,make each one their own checkbox, and add it to to the vbox. 

Next, I need to style the vbox. Also, when a continue button is clicked, i need to loop through the checkboxes to see 
which ones are selected. Then, remove those corresponding ingredients from the pantry.

24/01/2022

Fleshed out the design of the ingredient checklist.
Discovered how to work on elements in a VBox (or any layout) by getting its nodes and casting to the item i want.

When the "submit" button is pressed, the program reads the checkboxes that are ticked, and makes an array of those object names

Next, I want to delete those objects from the pantry. However, I am unable to access the Pantry's deduct method from the Recipe model.

Also made a confirmation page, to ask if a user wants to continue the cooking process when their recipes says they don't have enough ingredients.

25/01/2022

Set the cell value factory so's that the recipe availability is based off the colour. E.g. if you have no 
missing ingredients, the recipe is green. Otherwise, near available recipes are orange.

I need to next finish off the cook functionality, as i am yet to figure out how to reach a method from
the pantry in the ingredient checklist

28/01/2022

Today I've planned out what I'm going to do over the bowral break (just the weekend), I'm going to implement the
save / load system.

From my research so far, it seems I need to use XML's "nested object serialization" as my program uses multiple objects
stemming from the user object.

However, as this needs a lot of research and practice, i shall do it over the weekend as I dont need my program files
and instead can make a test project to practice on 

Link: http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#start


31/01/2022

Decided to go against the xml method as it didnt seem to work. 
Am trying to go with a simple file i/o and writing objects to it. However, I seem to be having issues.

The issues specifically is that the user says its being written to the file, but then no data is going through somehow.

Will pick it up next session.

1/02/2022 [8:30-10:30]

Today, I spent the morning session studying SQLite, a serverless, lightweight SQL database.
In doing so, I practiced integrating SQLite into java and netbeans, creating a database,
creating a table, and adding values to it. 

This was all done successfully in a test project.

Next session I will apply these newly found skills to the other project.
However, before coding, I will need to design the database and how the tables will relate.
Perhaps even make an ERD or whatever the diagram was called in DBFund.

1/02/2022 [1.5hrs]

Today, I spent the time transferring my new database skills over to the main project. 

Now, when a user is created through the signup form, they are added to the database.

The database is stored locally on the pc.

Next time, I will make it so the login form checks if the user exists and confirms the data to 
log in.

6/02/2022 [2.5hrs]

Today, I spent time selecting the data to see if a user already exists. This has been complete.

However, I discovered that now once the user is recognised, a instance of the user must be created 
for the session.

Next, I have to find out how to get the row as an object, and create a user using each data of each column

Then, I have to figure out how to get each pantry and following object saved to the database;

NVM i did more lol.

I added it so that the function for checking whether a user exists returns a row, so eventually,
it can use the data to make an instance of the user. However, I realised that the instance of the user
can't be created without a pantry and so on. So, I now need to start from the very end of the ERD and build
out each table for the smaller portions. Then I can work my way backwards.

This might get tricky.


7/02/2022 [45 mins]

Made login system work with database. However, a little stuck on how to store the objects.

Like do I serialize the objects and store them that way?

Or do I make a table for each object and link back to the parent object. Idk man


8/02/2022 [30 mins]

Made the "pantries" table. Whilst the tables are being created, im getting an error "[SQLITE_ERROR] SQL error or missing database (no such column: id)"

Will work on it next time.

9/02/2022 [40 mins] 

Fixed the bug as there was just a loose mispelling. 

New plan is to add pantries first, then add users, setting the users pantry id equal to that 
of the corresponding pantry entry. My method of finding the pantry id is through just making
the pantry have a name of "[user]'s pantry" and then checking for the user's name

im getting a bug saying connection has closed before user is been added but i cant see it rn


10/02/2022 [1 hour]

Good progress today. Fixed the db is closed issue as I was just structuring the close process wrong (needs
to be on each function rather than before).

Also, I made a count rows function which counts the rows of a table. 

I did this as I now set the user's pantryID (foreign key) as just to increment with the tables rows.
As they should be equivelent to the pantry's tables row count. However, if a row fails to be added to 
the pantry table, the database gets messed up. So this is just a temp solution.

Also, the foreign key constraint doesn't seem to be working (I can set the user's pantry id to whatever)

While it works in practice (if i set it correctly it recognises the entry in the pantry table), it can
easily mess up if inputted wrong. I think i just need to enable foreign key constraints somewhere. However,
my first attempt caused a crash. Shouldn't be too hard


11/02/2022 [2 Hours]

GOT THE FOREIGN KEYS WORKING. Basically, in the connect method I needed to setup "config", add "foreign keys: true"
and send that config as properties through the driver connect thingo. Works now!

When the user logs in, the applcation checks the database to see if the user exists. Then it uses the user's pantryID 
(foreign key) to find the related pantry, get the data from the pantry and create a local instance of both the pantry 
and the user.

From here, I have to repeat this process so that the pantry has foreign key to the ingredient list, and the recipes.
the recipes will then have their own foreign key to the recipe ingredients.

Coded the other tables to be created when the app begins. Next is to integrate them all throughout the controllers.

12/02/2022 [1.5 hrs]

Had an opiphany last night. As the app calculates the recipes available with the ingredients from the pantry
on startup, there is no need to store recipes or recipe ingredients in the database!!!!

This should mean I just have to do the pantry ingredients and it will work! Seems too good to be true

My thoughts surrounding the pantry to ingredients table. I might have to do it where I include a pantryID
to the ingredient themselves. My fear is that multiple ingredients will be inputted into the database overlapping.

The next option is to recognise pantry to ingredients as a many to many relation. I think this is the case.
I will have to create another table "PantryIngredientRelation" that includes the pantryID and each row has a different ingredient ID
that it belongs too. 

E.g. (1, 1) might mean in pantry 1, there is a carrot (or ingredient with id 1)

Yeah actually it defs would be M:N as multiple ingredients could be in multiple pantries in the database.

But then there's the argument that i could just make it for the desktop account user. 

Actually today Im gonna rewrite the start so that a user is created without needing to sign in, and its just the desktop user.
This will allow me to vastly simplify the database.

First, I need to make it so the front page goes straight to the dashboard. On launch, it should check if the database
has a entry for user. If not, create one using the desktop user's account details and make a new pantry.


OK, so I restructured the program to check whether the user exists in the database. If
not then it makes a new user with a blank pantry using the details of the computer's
profile (e.g. marcel, or admin). 

I also made it so that the ingredient table exists, and got rid of the recipe table and recipe item table.
I made it so when a ingredient is added to the pantry, the ingredient table gets a new row with that ingredient's name.

Next, I need to add the foreign key constraint so that ingredients match up to the pantry.
Also need to make it so ingredient entries are deleted when removed from pantry.

Then i need to make it so that on launch, the program gets all the items with the pantries FK
and then using a loop, add each instance of the ingredient to the instance of the pantry. Then it should be done!

Then I need to make it so users can log back in as currently it is waiting on the pantry to be complete.

13/02/22 [1.5 hrs]

my god, just spent 1.5 hours fixing one bug only to be dealt another.

RESULTSET CLOSED was the error. I learnt that this occured if closed too early (which turned out not to be the case)
or if the result set was empty (which it was as i inputted the wrong query)

Now however, I got an error that says SQLITE_CONSTRAINT_PRIMARYKEY on UNIQUE constraint failed: pantries.PantryID
Meaning that multiple entries are being inptuted with the same primary key which is bad as it is supposed to be unique. 
However, my gameplan was to have multiple entries anyway with the same foreign key which sucks.
It shouldnt be entering multiple times anyway as i only call it once. However, i suspect is has something 
to do with the double entry of the ingredient on the instance level. IDK why this happens but oh well, 
a job for next time 

14/02/22 [1.5 hrs] 

Fixed the bug, I accidently was trying to insert the data from "insertIngredient" into the pantry table instead of the ingredient table.

Now however, I had a bug that said the database was locked. I think this is because too many connections are open
and overloading the system. I've restructured the entire storagemanager script so that each "try" statement
has a "finally" block to close everything. However, I have got some new bugs, but dont think they're major.

Currently, the main bug says "resultset closed" on one function. Probably just returning an empty result set
but more analysis needs to be done. 

Will continue another time. 

15/02/22 [1hr 45mins]

Fixed the bug. I found that trying to use functions on result sets once sent dont work because they're closed.
Instead, I've discovered you have to get the data you want first before returning it.

But now, I got the ingredient table working! Even with multiple ingredients!

Found a new BUG: the ingredient dectector is too senstive. I added "A" as a test ingredient and it thinks
I have avocado lmao. I thought i fixed this. I just need to change it so the amount of letters is longer i guess.

I've made it so when a user starts the app it checks if they've used it before (data in db).
For some reason, it doesn't work the first time (if there's only one instance of the user) however,
does work for the second instance, not sure why. Just tested, its not because ingredients arent added.
Even if they are, the app just doesnt detect the first row of the db. LMAO IT JUST KINDA FIXED ITSELF.
Idek lmao

Working perfectly! On first launch, it creates a new user and adds to db. 
Then when adding ingredients, it adds them to the pantry instance and db.
I NEED TO MAKE DELETING ING AFFECT DB, shouldnt be hard.

Then i need to fix the cook function and the alpha version is done!



17/02/22 [1hr 45 mins]


Made the delete function also delete the item in database.

Made the cook function work by sending the pantry through the controller interface.

COOK FUNCTION FULLY WORKS, including removing the item from pantry and database

Removed test ingredient from pantry

Need to:
Update: Make session and dashboard stage the same
Update: Increase number of recipes
Update: Clean up code


18/02/22 [2 hrs]

Done: 
    1. Make session and dashboard stage the same
    2. Disabled "favourite" and "settings" button


Bugs Found:
    1. Filter by ingredient doesnt work unless fully typed (FIXED - changed hasName function to be 'contains' not 'equals')
    2. Seems that near recipes are shown without regard to ingredients. Need to make sure at least one matching ing exists
       (FIXED - made a if statement to make sure ownedIngredients is greater than 1 before sending it back from canCook method)
    3. PantryItems aren't being deleted unless they are exactly the same typed ingredient as in the recipe when cooked. E.g. (garlic wont be removed from garlic cloves when cooked)
       This seems to be an issue with the canCook in recipe model i believe, as the .contains function isnt working like normal


To Be Done:
    1. Need to create a legit error handler

Good progress today, cleaned up a lot of code and deleted old unnecesarry files. I found a bug today that needs to be fixed before launch.
See bug 3 for details. Shouldn't be too hard.


19/02/22

Done: 
    1. Fixed bug 3 from yesterday. I had to add an || (or) to the if statement to say if the string b contained string a as well, as sometimes
       the recipeitem was bigger than the pantry item and vice versa

Bugs Found:
    1. Whilst db saves all ingredients, it fails to load the very first one. Proabably an out of bounds error.
       (FIXED - added it manually before looping through while(rs.next()) )

To Do:
    1. Make an error handler for all database interactions

21/02/22

Done: 
    1. Positioned the windows to look good
    
Bugs Found:
    1. Recipe title cuts off if too long
    2. "unsalted butter" exists when beef is there. Make contains function stricter.


28/02/22

Update: Uni has begun and I got a lot of work to do. However, I pretty much finished the alpha
build of the program which is great.

Uploading to github

test


*/