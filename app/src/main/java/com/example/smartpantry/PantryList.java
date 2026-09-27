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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantryList extends AppCompatActivity {

    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_list_screen);

        //Sets up the list and open existing pantry items in the editor when clicked
        RecyclerView recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(new ArrayList<>(), item -> {
            Intent intent = new Intent(PantryList.this, AddIngredient.class);
            intent.putExtra(AddIngredient.EXTRA_ITEM_ID, item.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        //Opening the editor without an item ID creates a new pantry item
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(PantryList.this, AddIngredient.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Reload so additions or edits made in the editor are shown on return.
        List<PantryItem> items = DBHelper.getInstance(getApplicationContext()).getAllPantryItems();
        adapter.setItems(items);
    }

    private static class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
        private final List<PantryItem> items;
        private final OnItemClickListener listener;

        PantryAdapter(List<PantryItem> items, OnItemClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        void setItems(List<PantryItem> newItems) {
            //Replace the displayed data, then ask the RecyclerView to redraw.
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @Override
        public PantryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Use Android's built-in two-line row layout for each pantry item.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new PantryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PantryViewHolder holder, int position) {
            PantryItem item = items.get(position);
            //Show the item's name, quantity, unit, and expiry date if available.
            holder.text1.setText(item.getName());
            holder.text2.setText(item.getQuantity() + " " + item.getUnit() +
                    (item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "" : " • expires " + item.getExpiryDate()));
                holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class PantryViewHolder extends RecyclerView.ViewHolder {
            TextView text1;
            TextView text2;

            PantryViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }

        interface OnItemClickListener {
            void onItemClick(PantryItem item);
        }
    }
}
