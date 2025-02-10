//-----------------------------------------
// NAME		: Ahmed Abdelgalil
// STUDENT NUMBER	: 7924122
// COURSE		: COMP 2150 A01
// INSTRUCTOR	: Heather Matheson
// ASSIGNMENT	: assignment #1
// QUESTION	: question #1      
// 
// REMARKS: This program is a meal tracker. You can create a userprofile and keep track of your meals along with your calories.
//
//
//-----------------------------------------

import java.io.*;

// -------------------------------------------------------------------------
// MealTracker: Main class to manage food items and user profiles.
// -------------------------------------------------------------------------
public class MealTracker{
    private FoodNode foodLibrary; // Linked list holding all food items
    private UserNode userProfiles; // Linked list holding all users

    public MealTracker(){
        this.foodLibrary = null;
        this.userProfiles = null;
    }

    // ------------------------------------------------------
    // NewUser
    //
    // PURPOSE: Searches for a user library by username.
    // PARAMETERS: username - the username to find
    //
    // Returns: the NewUser if found, or null if not found
    // ------------------------------------------------------
    public NewUser findUser(String username){
        for(UserNode current = userProfiles; current != null; current = current.getNextNode()){
            if(current.getUserData().getUsername().equals(username)){
                return current.getUserData();
            }
        }
        return null;
    }

    // ------------------------------------------------------
    // addUser
    //
    // PURPOSE: Checks username then adds a new user if the username does not
    // already exist.
    // PARAMETERS: username - the new user's name
    // ------------------------------------------------------
    public void addUser(String username){
        if(findUser(username) == null){
            NewUser newUser = new NewUser(username);
            UserNode newUserNode = new UserNode(newUser);
            if(userProfiles == null){
                userProfiles = newUserNode;
            }
            else{
                UserNode current = userProfiles;
                while(current.getNextNode() != null){
                    current = current.getNextNode();
                }
                current.setNextNode(newUserNode);
            }
            System.out.println("NEW USER ADDED");
        }
        else{
            System.out.println("DUPLICATE USER NOT ADDED");
        }
    }

    // ------------------------------------------------------
    // findFood
    //
    // PURPOSE: Looks in the food library using item description to see if it's
    // already there or not
    // PARAMETERS: description - the food's description
    //
    // Returns: the Food item if found, or null if not found
    // ------------------------------------------------------
    public Food findFood(String description){
        for(FoodNode current = foodLibrary; current != null; current = current.getNextNode()){
            if(current.getFoodItem().getDescription().equals(description)){
                return current.getFoodItem();
            }
        }
        return null;
    }

    // ------------------------------------------------------
    // getDescription
    //
    // PURPOSE: Gets the description after skipping a few spaces
    // PARAMETERS: line - the whole input line
    // wordIndex - the number of words to skip
    // Returns: the description we got
    // ------------------------------------------------------
    private String getDescription(String line, int wordIndex){
        int spaceCount = 0;
        int index = 0;
        while(index < line.length() && spaceCount < wordIndex){
            if(line.charAt(index) == ' '){
                spaceCount++;
            }
            index++;
        }
        return line.substring(index).trim();
    }

    // ------------------------------------------------------
    // myMethod
    //
    // PURPOSE: Adds or updates a food item in the food library.
    // PARAMETERS: newFood - the Food item to add or update
    // ------------------------------------------------------
    public void addFood(Food newFood){
        FoodNode prev = null;
        FoodNode current = foodLibrary;
        while(current != null){
            if(current.getFoodItem().getDescription().equals(newFood.getDescription())){
                // Remove old node
                if(prev == null){
                    foodLibrary = current.getNextNode();
                }
                else{
                    prev.setNextNode(current.getNextNode());
                }
                break;
            }
            prev = current;
            current = current.getNextNode();
        }

        FoodNode newFoodNode = new FoodNode(newFood);
        if(foodLibrary == null){
            foodLibrary = newFoodNode;
        }
        else{
            FoodNode temp = foodLibrary;
            while(temp.getNextNode() != null){
                temp = temp.getNextNode();
            }
            temp.setNextNode(newFoodNode);
        }
        System.out.println(current == null ? "NEW FOOD ADDED" : "FOOD UPDATED");
    }

    // ------------------------------------------------------
    // testParse
    //
    // PURPOSE: parses the line of the input, to get type, calories, etc
    // reads the input file to act upon it, EAT, print, etc.
    // PARAMETERS: line - the input line from the file
    // ------------------------------------------------------
    public void textParse(String line){
        String[] part = line.split(" ");
        switch(part[0]){
            case "NEWPROFILE":
                addUser(part[1]);
                break;
            case "NEWFOOD":
                // If we found a line that has the word SINGLE, take each part (Type, calories, description, etc.), create a new food and assign it's details to the food
                if(part[1].equals("SINGLE")){
                    String foodType = part[2];
                    int foodCalories = Integer.parseInt(part[3]);
                    switch(foodType){
                        case "FRUITVEG":
                            String fruitVegDesc = getDescription(line, Constants.FRUITVEG_DESC_INDEX);
                            addFood(new AddFruitVeg(part[2], foodCalories, part[4], fruitVegDesc));
                            break;
                        case "DAIRY":
                        case "MEAT":
                            String dairyMeatDesc = getDescription(line, Constants.DAIRY_MEAT_DESC_INDEX);
                            addFood(new AddDairyMeat(part[2],
                                    foodCalories,
                                    Float.parseFloat(part[4]),
                                    Float.parseFloat(part[5]),
                                    part[6],
                                    dairyMeatDesc));
                            break;
                        case "GRAIN":
                            String grainDesc = getDescription(line, Constants.GRAIN_DESC_INDEX);
                            addFood(new AddGrain(part[2],
                                    foodCalories,
                                    Float.parseFloat(part[4]),
                                    Float.parseFloat(part[5]),
                                    part[6],
                                    grainDesc));
                            break;
                        case "OTHER":
                            String otherDesc = getDescription(line, Constants.OTHER_DESC_INDEX);
                            addFood(new AddOther(part[2],
                                    foodCalories,
                                    Float.parseFloat(part[4]),
                                    Float.parseFloat(part[5]),
                                    Float.parseFloat(part[6]),
                                    otherDesc));
                            break;
                    }
                }
                break;
            // When we find the word EAT, look for the user first then add the food to his meal history and print it out
            case "EAT":
                NewUser currentUser = findUser(part[1]);
                String foodName = getDescription(line, 2);
                Food selectedFood = findFood(foodName);
                if(currentUser == null){
                    System.out.println("USER NOT FOUND");
                }
                else if(selectedFood == null){
                    System.out.println("FOOD NOT FOUND");
                }
                else{
                    if(selectedFood instanceof AddFruitVeg){
                        System.out.println(currentUser.getUsername() + " is eating a Fruit/Vegetable: "
                                + selectedFood.getDescription());
                    }
                    else if(selectedFood instanceof AddDairyMeat){
                        System.out.println(currentUser.getUsername() + " is eating a Dairy/Meat product: "
                                + selectedFood.getDescription());
                    }
                    else if(selectedFood instanceof AddGrain){
                        System.out.println(currentUser.getUsername() + " is eating a Grain product: "
                                + selectedFood.getDescription());
                    }
                    else if(selectedFood instanceof AddOther){
                        System.out.println(currentUser.getUsername() + " is eating an Other food: "
                                + selectedFood.getDescription());
                    }
                    currentUser.eat(selectedFood);
                    System.out.println("MEAL RECORDED");
                }
                break;

            // When the word PRINTSERVINGS is found, look for the user and print all the types of food they ate
            case "PRINTSERVINGS":
                currentUser = findUser(part[1]);
                if(currentUser == null){
                    System.out.println("USER NOT FOUND");
                }
                else{
                    int fruitVegCount = 0, dairyCount = 0, meatCount = 0, grainCount = 0, otherCount = 0;
                    for(FoodNode meal = currentUser.mealHistory; meal != null; meal = meal.getNextNode()){
                        Food f = meal.getFoodItem();
                        if(f instanceof AddFruitVeg){
                            fruitVegCount++;
                        }
                        else if(f instanceof AddDairyMeat){
                            if(f.getType().equals("DAIRY")){
                                dairyCount++;
                            }
                            else if(f.getType().equals("MEAT")){
                                meatCount++;
                            }
                        }
                        else if(f instanceof AddGrain){
                            grainCount++;
                        }
                        else if(f instanceof AddOther){
                            otherCount++;
                        }
                    }
                    System.out.println(currentUser.getUsername() + " has consumed " +
                            fruitVegCount + " fruit/veg, " +
                            dairyCount + " dairy, " +
                            meatCount + " meat, " +
                            grainCount + " grain, " +
                            otherCount + " other");
                }
                break;
            // When the word PRINTCALORIES is found, look for the user and print the total calories they ate
            case "PRINTCALORIES":
                currentUser = findUser(part[1]);
                if(currentUser == null){
                    System.out.println("USER NOT FOUND");
                }
                else{
                    System.out.println("TOTAL CALORIES = " + currentUser.getTotalCalories());
                }
                break;

            // When the word PRINTMEALS is found, look for the user and print all the meals they ate
            case "PRINTMEALS":
                currentUser = findUser(part[1]);
                if(currentUser == null){
                    System.out.println("USER NOT FOUND");
                }
                else{
                    currentUser.printMeals();
                }
                break;

            case "QUIT":
                System.out.println("DONE");
                System.exit(0);
                break;
        }
    }

    // ------------------------------------------------------
    // main
    //
    // PURPOSE: reads the input file and processes each line.
    // PARAMETERS: args - expects the input file name as the first argument.
    // ------------------------------------------------------
    public static void main(String[] args){
        // Command argument to open the input file
        if(args.length < 1){
            System.out.println("Usage: java MealTracker <input_file>");
            return;
        }

        String inputFileName = args[0];
        MealTracker tracker = new MealTracker();

        // Looking for lines that are not comments or null, when found send them to textParse to do the commands
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFileName))){
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                if(currentLine.startsWith("#")){
                    System.out.println(currentLine);
                }
                else{
                    tracker.textParse(currentLine);
                }
            }
        }
        catch(IOException e){
            System.out.println("Error reading file: " + inputFileName);
        }
    }
}


// -------------------------------------------------------------------------
// Constants: Named constants for description when parsing
// -------------------------------------------------------------------------
class Constants{
    public static final int FRUITVEG_DESC_INDEX = 5; // Skip first 5 words to get fruit and veg description
    public static final int DAIRY_MEAT_DESC_INDEX = 7; // Skip first 7 words to get dairy/meat description
    public static final int GRAIN_DESC_INDEX = 7; // Skip first 7 words to get grain description
    public static final int OTHER_DESC_INDEX = 7; // Skip first 7 words to het other food description
}

// -------------------------------------------------------------------------
// UserNode: A node for our linked list for the users.
// -------------------------------------------------------------------------
class UserNode{
    private NewUser userData; // The user object stored in this node
    private UserNode nextNode; // Reference to the next user node

    // Adding the user's data to the node and next node to null.
    public UserNode(NewUser userData){
        this.userData = userData;
        this.nextNode = null;
    }

    public NewUser getUserData(){
        return userData;
    }

    public UserNode getNextNode(){
        return nextNode;
    }

    public void setNextNode(UserNode nextNode){
        this.nextNode = nextNode;
    }
}

// -------------------------------------------------------------------------
// FoodNode: A node for our linked list of food items.
// -------------------------------------------------------------------------
class FoodNode{
    private Food foodItem; // The food item stored in this node
    private FoodNode nextNode; // Reference pointer to the next food node

    // Adding the food item to the node and next node to null.
    public FoodNode(Food foodItem){
        this.foodItem = foodItem;
        this.nextNode = null;
    }

    public Food getFoodItem(){
        return foodItem;
    }

    public FoodNode getNextNode(){
        return nextNode;
    }

    public void setNextNode(FoodNode nextNode){
        this.nextNode = nextNode;
    }
}

// -------------------------------------------------------------------------
// Food: The superclass for our food library.
// -------------------------------------------------------------------------
abstract class Food{
    private String type; // Food type, Fruit, Veg, Meat, Dairy, Grain, Other
    private int calories; // Calories for the food
    private String description; // The description of the food

    // Setting the data we get from the input to the Food super class
    public Food(String type, int calories, String description){
        this.type = type;
        this.calories = calories;
        this.description = description;
    }

    public int getCalories(){
        return calories;
    }

    protected void addCalories(int additionalCalories){
        calories += additionalCalories;
    }

    public String getType(){
        return type;
    }

    protected void setType(String type){
        this.type = type;
    }

    public String getDescription(){
        return description;
    }
}

// -------------------------------------------------------------------------
// AddFruitVeg: Class for our fruit and vegetable foods.
// -------------------------------------------------------------------------
class AddFruitVeg extends Food{
    private String produceType; // Type whether fruit or veg

    public AddFruitVeg(String displayType, int calories, String produceType, String description){
        super(produceType, calories, description);
        setType(displayType);
        this.produceType = produceType;
    }

    public String getProduceType(){
        return produceType;
    }
}

// -------------------------------------------------------------------------
// AddDairyMeat: Class for our dairy or meat foods.
// -------------------------------------------------------------------------
class AddDairyMeat extends Food{
    private String animalName; // Animal source
    private float protein; // Amount of protein
    private float fat; // Amount of fat

    public AddDairyMeat(String type, int calories, float protein, float fat, String animalName, String description){
        super(type, calories, description);
        this.protein = protein;
        this.fat = fat;
        this.animalName = animalName;
    }

    public String getAnimalName(){
        return animalName;
    }

    public float getProtein(){
        return protein;
    }

    public float getFat(){
        return fat;
    }
}

// -------------------------------------------------------------------------
// AddGrain: Class for grain foods.
// -------------------------------------------------------------------------
class AddGrain extends Food{
    private String plantName; // The plant source
    private float protein; // Amount of protein
    private float fat; // Amount of fat

    public AddGrain(String type, int calories, float protein, float fat, String plantName, String description){
        super(type, calories, description);
        this.protein = protein;
        this.fat = fat;
        this.plantName = plantName;
    }

    public String getPlantName(){
        return plantName;
    }

    public float getProtein(){
        return protein;
    }

    public float getFat(){
        return fat;
    }
}

// -------------------------------------------------------------------------
// AddOther: Class for other food types.
// -------------------------------------------------------------------------
class AddOther extends Food{
    private float protein; // Amount of protein
    private float fat; // Amount of fat
    private float sugar; // Amount of sugar

    public AddOther(String type, int calories, float protein, float fat, float sugar, String description){
        super(type, calories, description);
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
    }

    public float getProtein(){
        return protein;
    }

    public float getFat(){
        return fat;
    }

    public float getSugar(){
        return sugar;
    }
}

// -------------------------------------------------------------------------
// NewUser: User profile and history are saved here.
// -------------------------------------------------------------------------
class NewUser{
    private String username; // Username for the registering user
    public FoodNode mealHistory; // Linked list for the food items eaten

    public NewUser(String username){
        this.username = username;
        this.mealHistory = null;
    }

    public String getUsername(){
        return username;
    }

    // ------------------------------------------------------
    // eat
    //
    // PURPOSE: Adds food item that the user ate into their meal history.
    // PARAMETERS: foodItem
    // the food item that the user ate
    // ------------------------------------------------------
    public void eat(Food foodItem){
        if(mealHistory == null){
            mealHistory = new FoodNode(foodItem);
        }
        else{
            FoodNode currentNode = mealHistory;
            while(currentNode.getNextNode() != null){
                currentNode = currentNode.getNextNode();
            }
            currentNode.setNextNode(new FoodNode(foodItem));
        }
    }

    // ------------------------------------------------------
    // getTotalCalories
    //
    // PURPOSE: Calculates the total calories the user ate
    //
    // Returns: total calories the user has ate so far
    // ------------------------------------------------------
    public int getTotalCalories(){
        int totalCalories = 0;
        for(FoodNode currentNode = mealHistory; currentNode != null; currentNode = currentNode.getNextNode()){
            totalCalories += currentNode.getFoodItem().getCalories();
        }
        return totalCalories;
    }

    // Printing all meals in the user's history, using instanceof by food type.
    public void printMeals(){
        System.out.println("Serving history for " + username + ":");
        for(FoodNode currentNode = mealHistory; currentNode != null; currentNode = currentNode.getNextNode()){
            Food currentFood = currentNode.getFoodItem();
            if(currentFood instanceof AddFruitVeg){
                System.out.println("[Fruit/Vegetable] " + currentFood.getDescription() +
                        " (" + currentFood.getCalories() + " calories)");
            } else if(currentFood instanceof AddDairyMeat){
                System.out.println("[Dairy/Meat] " + currentFood.getDescription() +
                        " (" + currentFood.getCalories() + " calories)");
            } else if(currentFood instanceof AddGrain){
                System.out.println("[Grain] " + currentFood.getDescription() +
                        " (" + currentFood.getCalories() + " calories)");
            } else if(currentFood instanceof AddOther){
                System.out.println("[Other] " + currentFood.getDescription() +
                        " (" + currentFood.getCalories() + " calories)");
            }
        }
    }
}