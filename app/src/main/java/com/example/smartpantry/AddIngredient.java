package com.example.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.model.PantryItem;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddIngredient extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "pantry_item_id";

    private EditText expiryDateEditText;
    private long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_screen);

        EditText ingredientNameEditText = findViewById(R.id.ETT_IngredientName);
        EditText quantityEditText = findViewById(R.id.ETT_Quantity);
        Spinner unitSpinner = findViewById(R.id.spinner);
        expiryDateEditText = findViewById(R.id.ETT_ExpiryDate);
        Button saveButton = findViewById(R.id.btn_Save);
        Button deleteButton = findViewById(R.id.btn_Delete);

        List<String> unitOptions = Arrays.asList("grams", "ml", "items", "slices", "leaves");
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unitOptions);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            PantryItem existingItem = DBHelper.getInstance(getApplicationContext()).getPantryItemById(itemId);
            if (existingItem == null) {
                Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            ((TextView) findViewById(R.id.textView2)).setText("Edit Ingredient");
            ingredientNameEditText.setText(existingItem.getName());
            quantityEditText.setText(String.valueOf(existingItem.getQuantity()));
            String existingUnit = existingItem.getUnit();
            int existingUnitPosition = unitOptions.indexOf(existingUnit);
            if (existingUnitPosition >= 0) {
                unitSpinner.setSelection(existingUnitPosition);
            }
            expiryDateEditText.setText(existingItem.getExpiryDate());
            saveButton.setText("Update");
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Delete ingredient?")
                    .setMessage("This will remove " + existingItem.getName() + " from your pantry.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        DBHelper.getInstance(getApplicationContext()).deletePantryItem(itemId);
                        Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .show());
        }

        expiryDateEditText.setFocusable(false);
        expiryDateEditText.setCursorVisible(false);
        expiryDateEditText.setOnClickListener(v -> showDatePicker());

        saveButton.setOnClickListener(v -> {
            String name = ingredientNameEditText.getText().toString().trim();
            String quantityText = quantityEditText.getText().toString().trim();
            String unit = unitSpinner.getSelectedItem() == null ? "" : unitSpinner.getSelectedItem().toString().trim();
            String expiry = expiryDateEditText.getText().toString().trim();

            if (name.isEmpty()) {
                ingredientNameEditText.setError("Ingredient name is required");
                ingredientNameEditText.requestFocus();
                return;
            }
            if (name.codePoints().anyMatch(Character::isDigit)) {
                ingredientNameEditText.setError("Ingredient name cannot contain numbers");
                ingredientNameEditText.requestFocus();
                return;
            }

            if (quantityText.isEmpty()) {
                quantityEditText.setError("Quantity is required");
                quantityEditText.requestFocus();
                return;
            }
            

            double quantity;
            try {
                quantity = Double.parseDouble(quantityText);
            } catch (NumberFormatException e) {
                quantityEditText.setError("Enter a valid quantity");
                quantityEditText.requestFocus();
                return;
            }

            if (quantity <= 0) {
                quantityEditText.setError("Quantity must be greater than zero");
                quantityEditText.requestFocus();
                return;
            }

            if (unit.isEmpty()) {
                Toast.makeText(this, "Please choose a unit", Toast.LENGTH_SHORT).show();
                unitSpinner.requestFocus();
                return;
            }

            PantryItem item = new PantryItem();
            item.setName(name);
            item.setQuantity(quantity);
            item.setUnit(unit);
            item.setExpiryDate(expiry);
            item.setId(itemId);

            boolean saved;
            if (itemId == -1) {
                saved = DBHelper.getInstance(getApplicationContext()).addPantryItem(item) != -1;
            } else {
                saved = DBHelper.getInstance(getApplicationContext()).updatePantryItem(item) > 0;
            }
            if (saved) {
                Toast.makeText(this, itemId == -1 ? "Ingredient saved" : "Ingredient updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Could not save ingredient", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(calendar.getTime());
                    expiryDateEditText.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
}
