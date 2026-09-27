package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipes extends AppCompatActivity {

    private RecipeAdapter adapter;
    private RecipeAdapter almostThereAdapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_recipes_screen);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView almostRecyclerView = findViewById(R.id.recyclerView2);
        emptyText = findViewById(R.id.textView5);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        almostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        almostThereAdapter = new RecipeAdapter(new ArrayList<>());
        almostRecyclerView.setAdapter(almostThereAdapter);

        loadSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<Recipe> allRecipes = DBHelper.getInstance(getApplicationContext()).getAllRecipesWithIngredients();
        List<PantryItem> pantry = DBHelper.getInstance(getApplicationContext()).getAllPantryItems();
        List<Recipe> matches = RecipeMatcher.getSuggestedRecipes(allRecipes, pantry);
        List<Recipe> almostThere = RecipeMatcher.getSuggestedAlmostRecpes(allRecipes, pantry);

        android.util.Log.d("SuggestedRecipesDebug", "Total recipes: " + allRecipes.size() +
                ", Pantry items: " + pantry.size() + ", Matched recipes: " + matches.size() +
                ", Almost there recipes: " + almostThere.size());

        adapter.setItems(matches);
        almostThereAdapter.setItems(almostThere);

        if (matches.isEmpty() && almostThere.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("No recipes match your pantry yet - add more ingredients");
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

    private static class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
        private final List<Recipe> items;

        RecipeAdapter(List<Recipe> items) {
            this.items = items;
        }

        void setItems(List<Recipe> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.suggest_recipes_item_row, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
            Recipe recipe = items.get(position);
            holder.textView.setText(recipe.getName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), RecipeDetails.class);
                intent.putExtra("recipe_name", recipe.getName());
                intent.putExtra("recipe_instructions", recipe.getInstructions());

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    builder.append(recipe.getIngredients().get(i).getIngredientName())
                            .append(" - ")
                            .append(recipe.getIngredients().get(i).getQuantity())
                            .append(" ")
                            .append(recipe.getIngredients().get(i).getUnit());
                    if (i < recipe.getIngredients().size() - 1) {
                        builder.append("\n");
                    }
                }
                intent.putExtra("recipe_ingredients", builder.toString());
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class RecipeViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            RecipeViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textViewItem);
            }
        }
    }
}
