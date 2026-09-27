package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_screen);

        //Gets intent from SuggestedRecipes and displays the recipe name, ingredients, and instructions
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        TextView recipeNameTxt = findViewById(R.id.recipeNameTxt);
        TextView methodText = findViewById(R.id.editTextText2);

        String name = intent.getStringExtra("recipe_name");
        String instructions = intent.getStringExtra("recipe_instructions");
        String ingredients = intent.getStringExtra("recipe_ingredients");

        if (name != null) {
            recipeNameTxt.setText(name);
        }

        if (instructions != null && ingredients != null) {
            methodText.setText("Ingredients:\n" + ingredients + "\n\nMethod:\n" + instructions);
        } else if (instructions != null) {
            methodText.setText(instructions);
        }
    }
}
