package com.example.travelcompanionapp21p;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert, buttonReset;
    TextView textViewResult;

    String[] categories = {"Currency", "Fuel / Distance", "Temperature"};

    String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuelUnits = {"MPG", "KM/L", "Gallon", "Liter", "Nautical Mile", "Kilometer"};
    String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextValue = findViewById(R.id.editTextValue);
        buttonConvert = findViewById(R.id.buttonConvert);
        buttonReset = findViewById(R.id.buttonReset);
        textViewResult = findViewById(R.id.textViewResult);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );

        spinnerCategory.setAdapter(categoryAdapter);

        updateUnitSpinners(currencyUnits);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedCategory = spinnerCategory.getSelectedItem().toString();

                if (selectedCategory.equals("Currency")) {
                    updateUnitSpinners(currencyUnits);

                } else if (selectedCategory.equals("Fuel / Distance")) {
                    updateUnitSpinners(fuelUnits);

                } else {
                    updateUnitSpinners(temperatureUnits);
                }

                textViewResult.setText("Result will appear here");
                editTextValue.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonConvert.setOnClickListener(v -> convertValue());

        buttonReset.setOnClickListener(v -> {
            editTextValue.setText("");
            spinnerCategory.setSelection(0);
            spinnerFrom.setSelection(0);
            spinnerTo.setSelection(0);
            textViewResult.setText("Result will appear here");

            Toast.makeText(this, "Reset completed", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUnitSpinners(String[] units) {

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                units
        );

        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);
    }

    private void convertValue() {

        String input = editTextValue.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;

        try {
            value = Double.parseDouble(input);

        } catch (NumberFormatException e) {

            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        if (from.equals(to)) {

            textViewResult.setText(String.format("%.2f %s = %.2f %s",
                    value, from, value, to));

            Toast.makeText(this,
                    "Same unit selected",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (category.equals("Fuel / Distance") && value < 0) {

            Toast.makeText(this,
                    "Fuel and distance values cannot be negative",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        double result;

        if (category.equals("Currency")) {

            result = convertCurrency(value, from, to);

        } else if (category.equals("Fuel / Distance")) {

            result = convertFuelDistance(value, from, to);

        } else {

            result = convertTemperature(value, from, to);
        }

        if (result == -1) {

            Toast.makeText(this,
                    "Conversion not available",
                    Toast.LENGTH_SHORT).show();

        } else {

            textViewResult.setText(String.format("%.2f %s = %.2f %s",
                    value, from, result, to));
        }
    }

    private double convertCurrency(double value, String from, String to) {

        double usdValue;

        switch (from) {

            case "USD":
                usdValue = value;
                break;

            case "AUD":
                usdValue = value / 1.55;
                break;

            case "EUR":
                usdValue = value / 0.92;
                break;

            case "JPY":
                usdValue = value / 148.50;
                break;

            case "GBP":
                usdValue = value / 0.78;
                break;

            default:
                return -1;
        }

        switch (to) {

            case "USD":
                return usdValue;

            case "AUD":
                return usdValue * 1.55;

            case "EUR":
                return usdValue * 0.92;

            case "JPY":
                return usdValue * 148.50;

            case "GBP":
                return usdValue * 0.78;

            default:
                return -1;
        }
    }

    private double convertFuelDistance(double value, String from, String to) {

        if (from.equals("MPG") && to.equals("KM/L")) {

            return value * 0.425;

        } else if (from.equals("KM/L") && to.equals("MPG")) {

            return value / 0.425;

        } else if (from.equals("Gallon") && to.equals("Liter")) {

            return value * 3.785;

        } else if (from.equals("Liter") && to.equals("Gallon")) {

            return value / 3.785;

        } else if (from.equals("Nautical Mile") && to.equals("Kilometer")) {

            return value * 1.852;

        } else if (from.equals("Kilometer") && to.equals("Nautical Mile")) {

            return value / 1.852;

        } else {

            return -1;
        }
    }

    private double convertTemperature(double value, String from, String to) {

        if (from.equals("Celsius") && to.equals("Fahrenheit")) {

            return (value * 1.8) + 32;

        } else if (from.equals("Fahrenheit") && to.equals("Celsius")) {

            return (value - 32) / 1.8;

        } else if (from.equals("Celsius") && to.equals("Kelvin")) {

            return value + 273.15;

        } else if (from.equals("Kelvin") && to.equals("Celsius")) {

            return value - 273.15;

        } else {

            return -1;
        }
    }
}