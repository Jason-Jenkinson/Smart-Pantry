package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 3;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_P_ID = "_id";
    public static final String COL_P_NAME = "name";
    public static final String COL_P_QUANTITY = "quantity";
    public static final String COL_P_UNIT = "unit";
    public static final String COL_P_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_R_ID = "_id";
    public static final String COL_R_NAME = "name";
    public static final String COL_R_INSTRUCTIONS = "instructions";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "_id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "ingredient_name";
    public static final String COL_RI_QUANTITY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_P_NAME + " TEXT NOT NULL, " +
                COL_P_QUANTITY + " REAL NOT NULL, " +
                COL_P_UNIT + " TEXT, " +
                COL_P_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_R_NAME + " TEXT NOT NULL, " +
                COL_R_INSTRUCTIONS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_R_ID + "))");

        // Seed the recipe collection so the app is useful on first launch.
        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("UPDATE " + TABLE_RECIPE_INGREDIENTS +
                    " SET " + COL_RI_QUANTITY + " = 0.3, " + COL_RI_UNIT + " = 'grams'" +
                    " WHERE LOWER(" + COL_RI_NAME + ") = 'salt'" +
                    " AND LOWER(" + COL_RI_UNIT + ") = 'pinch'");
        }
    }


    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = pantryToContentValues(item);
        return db.insert(TABLE_PANTRY, null, cv);
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = pantryToContentValues(item);
        return db.update(TABLE_PANTRY, cv, COL_P_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_P_ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Nullable
    public PantryItem getPantryItemById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_P_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (c.moveToFirst()) {
            item = pantryFromCursor(c);
        }
        c.close();
        return item;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, COL_P_NAME + " COLLATE NOCASE ASC");
        while (c.moveToNext()) {
            list.add(pantryFromCursor(c));
        }
        c.close();
        return list;
    }

    private ContentValues pantryToContentValues(PantryItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_P_NAME, item.getName());
        cv.put(COL_P_QUANTITY, item.getQuantity());
        cv.put(COL_P_UNIT, item.getUnit());
        cv.put(COL_P_EXPIRY, item.getExpiryDate());
        return cv;
    }

    private PantryItem pantryFromCursor(Cursor c) {
        PantryItem item = new PantryItem();
        item.setId(c.getLong(c.getColumnIndexOrThrow(COL_P_ID)));
        item.setName(c.getString(c.getColumnIndexOrThrow(COL_P_NAME)));
        item.setQuantity(c.getDouble(c.getColumnIndexOrThrow(COL_P_QUANTITY)));
        item.setUnit(c.getString(c.getColumnIndexOrThrow(COL_P_UNIT)));
        item.setExpiryDate(c.getString(c.getColumnIndexOrThrow(COL_P_EXPIRY)));
        return item;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, COL_R_NAME + " COLLATE NOCASE ASC");
        while (c.moveToNext()) {
            list.add(recipeFromCursor(c));
        }
        c.close();
        return list;
    }

    @Nullable
    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_R_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Recipe recipe = null;
        if (c.moveToFirst()) {
            recipe = recipeFromCursor(c);
        }
        c.close();
        if (recipe != null) {
            recipe.setIngredients(getIngredientsForRecipe(recipe.getId()));
        }
        return recipe;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, COL_RI_NAME + " COLLATE NOCASE ASC");
        while (c.moveToNext()) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setId(c.getLong(c.getColumnIndexOrThrow(COL_RI_ID)));
            ri.setRecipeId(c.getLong(c.getColumnIndexOrThrow(COL_RI_RECIPE_ID)));
            ri.setIngredientName(c.getString(c.getColumnIndexOrThrow(COL_RI_NAME)));
            ri.setQuantity(c.getDouble(c.getColumnIndexOrThrow(COL_RI_QUANTITY)));
            ri.setUnit(c.getString(c.getColumnIndexOrThrow(COL_RI_UNIT)));
            list.add(ri);
        }
        c.close();
        return list;
    }

    public List<Recipe> getAllRecipesWithIngredients() {
        List<Recipe> recipes = getAllRecipes();
        for (Recipe r : recipes) {
            r.setIngredients(getIngredientsForRecipe(r.getId()));
        }
        return recipes;
    }

    private Recipe recipeFromCursor(Cursor c) {
        Recipe recipe = new Recipe();
        recipe.setId(c.getLong(c.getColumnIndexOrThrow(COL_R_ID)));
        recipe.setName(c.getString(c.getColumnIndexOrThrow(COL_R_NAME)));
        recipe.setInstructions(c.getString(c.getColumnIndexOrThrow(COL_R_INSTRUCTIONS)));
        return recipe;
    }
}