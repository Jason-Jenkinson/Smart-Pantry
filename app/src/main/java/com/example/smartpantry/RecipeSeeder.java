package com.example.smartpantry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

//This insets a set of recipes, 19 to be exact into the database.
class RecipeSeeder {

    private static long insertRecipe(SQLiteDatabase db, String name, String instructions) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_R_NAME, name);
        cv.put(DBHelper.COL_R_INSTRUCTIONS, instructions);
        return db.insert(DBHelper.TABLE_RECIPES, null, cv);
    }

    private static void insertIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_RI_RECIPE_ID, recipeId);
        cv.put(DBHelper.COL_RI_NAME, name);
        cv.put(DBHelper.COL_RI_QUANTITY, qty);
        cv.put(DBHelper.COL_RI_UNIT, unit);
        db.insert(DBHelper.TABLE_RECIPE_INGREDIENTS, null, cv);
    }

    static void seed(SQLiteDatabase db) {
        long id;

        id = insertRecipe(db, "Scrambled Eggs",
                "1. Crack eggs into a bowl and whisk with a splash of milk.\n" +
                        "2. Melt butter in a pan over medium heat.\n" +
                        "3. Pour in eggs and stir gently until softly set.\n" +
                        "4. Season with salt and pepper and serve.");
        insertIngredient(db, id, "eggs", 3, "items");
        insertIngredient(db, id, "milk", 30, "ml");
        insertIngredient(db, id, "butter", 10, "grams");
        insertIngredient(db, id, "salt", 0.3, "grams");

        id = insertRecipe(db, "Tomato Pasta",
                "1. Boil pasta until al dente and drain.\n" +
                        "2. Heat olive oil, add garlic and chopped tomatoes.\n" +
                        "3. Simmer into a sauce and season with salt.\n" +
                        "4. Toss pasta through the sauce and serve.");
        insertIngredient(db, id, "pasta", 200, "grams");
        insertIngredient(db, id, "tomatoes", 3, "items");
        insertIngredient(db, id, "garlic", 2, "items");
        insertIngredient(db, id, "olive oil", 15, "ml");
        insertIngredient(db, id, "salt", 0.3, "grams");

        id = insertRecipe(db, "Grilled Cheese Sandwich",
                "1. Butter one side of each bread slice.\n" +
                        "2. Place cheese between the unbuttered sides.\n" +
                        "3. Grill in a pan until golden on both sides.\n" +
                        "4. Slice and serve warm.");
        insertIngredient(db, id, "bread", 2, "items");
        insertIngredient(db, id, "cheese", 2, "slices");
        insertIngredient(db, id, "butter", 10, "grams");

        id = insertRecipe(db, "Fried Rice",
                "1. Heat oil in a wok or large pan.\n" +
                        "2. Add cooked rice and break up any clumps.\n" +
                        "3. Stir in soy sauce and vegetables and fry until hot.\n" +
                        "4. Push rice aside, scramble in an egg, then mix together.");
        insertIngredient(db, id, "rice", 300, "grams");
        insertIngredient(db, id, "eggs", 2, "items");
        insertIngredient(db, id, "soy sauce", 20, "ml");
        insertIngredient(db, id, "carrot", 1, "items");
        insertIngredient(db, id, "oil", 15, "ml");

        id = insertRecipe(db, "Vegetable Omelette",
                "1. Whisk eggs with salt and pepper.\n" +
                        "2. Saute chopped vegetables in a pan until soft.\n" +
                        "3. Pour in eggs and cook until set.\n" +
                        "4. Fold in half and serve.");
        insertIngredient(db, id, "eggs", 3, "items");
        insertIngredient(db, id, "onion", 1, "items");
        insertIngredient(db, id, "bell pepper", 1, "items");
        insertIngredient(db, id, "salt", 0.3, "grams");

        id = insertRecipe(db, "Pancakes",
                "1. Whisk flour, milk, and eggs into a smooth batter.\n" +
                        "2. Heat a lightly oiled pan over medium heat.\n" +
                        "3. Pour batter and cook until bubbles form, then flip.\n" +
                        "4. Cook the other side until golden and serve.");
        insertIngredient(db, id, "flour", 200, "grams");
        insertIngredient(db, id, "milk", 250, "ml");
        insertIngredient(db, id, "eggs", 2, "items");
        insertIngredient(db, id, "sugar", 20, "grams");

        id = insertRecipe(db, "Banana Smoothie",
                "1. Peel and slice the banana.\n" +
                        "2. Add banana, milk, and yogurt to a blender.\n" +
                        "3. Blend until smooth.\n" +
                        "4. Pour into a glass and serve chilled.");
        insertIngredient(db, id, "banana", 2, "items");
        insertIngredient(db, id, "milk", 200, "ml");
        insertIngredient(db, id, "yogurt", 100, "grams");

        id = insertRecipe(db, "Chicken Stir Fry",
                "1. Slice chicken into strips and season.\n" +
                        "2. Stir fry chicken in hot oil until cooked through.\n" +
                        "3. Add vegetables and soy sauce, fry a few more minutes.\n" +
                        "4. Serve hot, on its own or with rice.");
        insertIngredient(db, id, "chicken breast", 300, "grams");
        insertIngredient(db, id, "bell pepper", 1, "items");
        insertIngredient(db, id, "soy sauce", 20, "ml");
        insertIngredient(db, id, "oil", 15, "ml");

        id = insertRecipe(db, "Tuna Salad",
                "1. Drain the tin of tuna and flake into a bowl.\n" +
                        "2. Add mayonnaise, chopped onion, and a squeeze of lemon.\n" +
                        "3. Mix well and season to taste.\n" +
                        "4. Serve on its own or with bread.");
        insertIngredient(db, id, "tuna", 1, "tin");
        insertIngredient(db, id, "mayonnaise", 30, "grams");
        insertIngredient(db, id, "onion", 1, "items");
        insertIngredient(db, id, "lemon", 1, "items");

        id = insertRecipe(db, "Peanut Butter Toast",
                "1. Toast the bread slices.\n" +
                        "2. Spread peanut butter evenly over each slice.\n" +
                        "3. Optionally top with banana slices.\n" +
                        "4. Serve immediately.");
        insertIngredient(db, id, "bread", 2, "items");
        insertIngredient(db, id, "peanut butter", 30, "grams");
        insertIngredient(db, id, "banana", 1, "items");

        id = insertRecipe(db, "Vegetable Soup",
                "1. Saute onion and garlic in a pot until fragrant.\n" +
                        "2. Add chopped vegetables and stock.\n" +
                        "3. Simmer for 20 minutes until vegetables are tender.\n" +
                        "4. Season to taste and serve hot.");
        insertIngredient(db, id, "onion", 1, "items");
        insertIngredient(db, id, "carrot", 2, "items");
        insertIngredient(db, id, "potato", 2, "items");
        insertIngredient(db, id, "vegetable stock", 500, "ml");

        id = insertRecipe(db, "Cheese Quesadilla",
                "1. Sprinkle grated cheese over half a tortilla.\n" +
                        "2. Fold the tortilla and cook in a dry pan.\n" +
                        "3. Flip once golden and cheese has melted.\n" +
                        "4. Slice into wedges and serve.");
        insertIngredient(db, id, "tortilla", 2, "items");
        insertIngredient(db, id, "cheese", 60, "grams");

        id = insertRecipe(db, "Garlic Bread",
                "1. Mix softened butter with crushed garlic and parsley.\n" +
                        "2. Spread over sliced bread or a baguette.\n" +
                        "3. Bake at 180C for about 10 minutes.\n" +
                        "4. Serve warm.");
        insertIngredient(db, id, "bread", 1, "items");
        insertIngredient(db, id, "butter", 40, "grams");
        insertIngredient(db, id, "garlic", 3, "items");

        id = insertRecipe(db, "Rice and Beans",
                "1. Cook rice according to package instructions.\n" +
                        "2. Heat beans in a pot with a little onion and spices.\n" +
                        "3. Combine rice and beans, or serve side by side.\n" +
                        "4. Season to taste and serve.");
        insertIngredient(db, id, "rice", 200, "grams");
        insertIngredient(db, id, "beans", 400, "grams");
        insertIngredient(db, id, "onion", 1, "items");

        id = insertRecipe(db, "Oatmeal",
                "1. Bring milk or water to a simmer.\n" +
                        "2. Stir in oats and cook for 5 minutes.\n" +
                        "3. Sweeten with sugar or honey to taste.\n" +
                        "4. Top with banana slices and serve.");
        insertIngredient(db, id, "oats", 80, "grams");
        insertIngredient(db, id, "milk", 250, "ml");
        insertIngredient(db, id, "banana", 1, "items");

        id = insertRecipe(db, "Egg Fried Noodles",
                "1. Cook noodles according to package instructions and drain.\n" +
                        "2. Scramble eggs in a hot pan and set aside.\n" +
                        "3. Stir fry noodles with soy sauce, then mix in the eggs.\n" +
                        "4. Serve hot.");
        insertIngredient(db, id, "noodles", 200, "grams");
        insertIngredient(db, id, "eggs", 2, "items");
        insertIngredient(db, id, "soy sauce", 20, "ml");

        id = insertRecipe(db, "Caprese Salad",
                "1. Slice tomatoes and mozzarella into rounds.\n" +
                        "2. Arrange alternately on a plate with basil leaves.\n" +
                        "3. Drizzle with olive oil and season.\n" +
                        "4. Serve immediately.");
        insertIngredient(db, id, "tomatoes", 2, "items");
        insertIngredient(db, id, "mozzarella", 125, "grams");
        insertIngredient(db, id, "basil", 5, "leaves");
        insertIngredient(db, id, "olive oil", 10, "ml");

        id = insertRecipe(db, "Baked Potato",
                "1. Pierce the potato skin several times with a fork.\n" +
                        "2. Bake at 200C for about 45 minutes until soft.\n" +
                        "3. Cut open and top with butter and cheese.\n" +
                        "4. Season and serve hot.");
        insertIngredient(db, id, "potato", 2, "items");
        insertIngredient(db, id, "butter", 20, "grams");
        insertIngredient(db, id, "cheese", 40, "grams");

        id = insertRecipe(db, "Tomato Soup",
                "1. Saute onion and garlic until soft.\n" +
                        "2. Add chopped tomatoes and stock, then simmer.\n" +
                        "3. Blend until smooth.\n" +
                        "4. Season and serve with a swirl of cream if you like.");
        insertIngredient(db, id, "tomatoes", 5, "items");
        insertIngredient(db, id, "onion", 1, "items");
        insertIngredient(db, id, "vegetable stock", 400, "ml");

        id = insertRecipe(db, "Mac and Cheese",
                "1. Boil macaroni until al dente and drain.\n" +
                        "2. Melt butter, stir in flour, then whisk in milk to make a sauce.\n" +
                        "3. Stir in grated cheese until melted and smooth.\n" +
                        "4. Combine with macaroni and serve.");
        insertIngredient(db, id, "macaroni", 200, "grams");
        insertIngredient(db, id, "cheese", 100, "grams");
        insertIngredient(db, id, "milk", 200, "ml");
        insertIngredient(db, id, "butter", 20, "grams");
    }
}
