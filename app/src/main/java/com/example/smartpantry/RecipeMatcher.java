package com.example.smartpantry;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


 //This class implements the "STRICT MATCHING RULE" that makes sure the pantry contains all ingredients for the recipe.
 //If theres not enough of one ingredient, the recipe is not suggested
 //There are no partial matches shown.

 //It uses two rules
 //Ingredients names are compared without case sesitivity and no whitespace.
 //Pantry quantities are combined only when their units match the recipe's required unit.
public class RecipeMatcher {

    //This returns a list of all the reciped that can be made.
    public static List<Recipe> getSuggestedRecipes(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> suggestions = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMake(recipe, pantry)) {
                suggestions.add(recipe);
            }
        }
        return suggestions;
    }

    public static List<Recipe> getSuggestedAlmostRecpes(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> suggestions = new ArrayList<>();
        if (allRecipes == null) {
            return suggestions;
        }

        for (Recipe recipe : allRecipes) {
            if (recipe == null || canMake(recipe, pantry)) {
                continue;
            }

            int missingIngredients = 0;
            if (recipe.getIngredients() != null) {
                for (RecipeIngredient neededIngredient : recipe.getIngredients()) {
                    if (!pantryHasEnough(pantry, neededIngredient)) {
                        missingIngredients++;
                    }
                }
            }

            if (missingIngredients == 1) {
                suggestions.add(recipe);
            }
        }
        return suggestions;
    }

    //checks if theres the required ingredients with the right amounts the the pantry
    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        List<RecipeIngredient> required = recipe.getIngredients();
        if (required == null || required.isEmpty()) {
            return false;
        }
        for (RecipeIngredient need : required) {
            if (!pantryHasEnough(pantry, need)) {
                return false; //one missing ingredient disqualifies the whole recipe
            }
        }
        return true;
    }

    private static boolean pantryHasEnough(List<PantryItem> pantry, RecipeIngredient neededIngredient) {
        if (pantry == null || neededIngredient.getIngredientName() == null || neededIngredient.getUnit() == null) {
            return false;
        }

        double availableQuantity = 0;
        for (PantryItem item : pantry) {
            if (!namesMatch(item.getName(), neededIngredient.getIngredientName())) {
                continue;
            }
            if (unitsMatch(item.getUnit(), neededIngredient.getUnit())) {
                availableQuantity += item.getQuantity();
            }
        }
        return availableQuantity >= neededIngredient.getQuantity();
    }

    private static boolean namesMatch(String nameA, String nameB) {
        if (nameA == null || nameB == null) return false;
        return nameA.trim().equalsIgnoreCase(nameB.trim());
    }

    private static boolean unitsMatch(String unitA, String unitB) {
        if (unitA == null || unitB == null) return false;
        return normalizeUnit(unitA).equals(normalizeUnit(unitB));
    }

    //normalizer to ensure simple missmatches are classes as the same unit.
    private static String normalizeUnit(String unit) {
        String normalized = unit.trim().toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "g":
            case "gram":
            case "grams":
                return "grams";
            case "ml":
            case "milliliter":
            case "milliliters":
            case "millilitre":
            case "millilitres":
                return "ml";
            case "item":
            case "items":
            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
                return "items";
            default:
                return normalized;
        }
    }
}
