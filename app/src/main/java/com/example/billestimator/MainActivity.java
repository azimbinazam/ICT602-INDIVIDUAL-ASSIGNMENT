package com.example.billestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editUnits;
    RadioGroup radioGroupRebate;
    TextView txtTotalCharges, txtFinalCost;
    Button btnCalculate;

    DatabaseHelper db; // Declare DB helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnits = findViewById(R.id.editUnits);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        txtTotalCharges = findViewById(R.id.txtTotalCharges);
        txtFinalCost = findViewById(R.id.txtFinalCost);
        btnCalculate = findViewById(R.id.btnCalculate);

        db = new DatabaseHelper(this); // Initialize DB helper

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBill();
            }
        });

        // View History button
        Button btnViewHistory = findViewById(R.id.btnViewHistory);
        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }


    private void calculateBill() {
        String unitsStr = editUnits.getText().toString();

        if (unitsStr.isEmpty()) {
            editUnits.setError("Please enter units used");
            return;
        }

        int units = Integer.parseInt(unitsStr);
        double totalCharges = calculateCharges(units);

        // Get selected rebate
        int selectedId = radioGroupRebate.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select rebate", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRebate = findViewById(selectedId);
        String rebateText = selectedRebate.getText().toString().replace("%", "");
        double rebatePercent = Double.parseDouble(rebateText) / 100.0;

        double finalCost = totalCharges - (totalCharges * rebatePercent);

        // Display results
        txtTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        txtFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));

        // Save to database
        String selectedMonth = spinnerMonth.getSelectedItem().toString();
        boolean inserted = db.insertBill(selectedMonth, units, totalCharges, rebatePercent, finalCost);

        if (inserted) {
            Toast.makeText(this, "Bill saved to database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving to database.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateCharges(int units) {
        double total = 0;

        if (units <= 200) {
            total = units * 0.218;
        } else if (units <= 300) {
            total = 200 * 0.218 + (units - 200) * 0.334;
        } else if (units <= 600) {
            total = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        } else {
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        }

        return total;
    }
}
