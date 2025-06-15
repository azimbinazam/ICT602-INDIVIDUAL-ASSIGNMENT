package com.example.billestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView detailText;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailText = findViewById(R.id.detailText);
        db = new DatabaseHelper(this);

        int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(this, "Invalid record ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor c = db.getBillById(id);
        if (c.moveToFirst()) {
            String month = c.getString(1);
            int units = c.getInt(2);
            double charges = c.getDouble(3);
            double rebate = c.getDouble(4);
            double finalCost = c.getDouble(5);

            String details = "Month: " + month +
                    "\nUnits Used: " + units +
                    "\nTotal Charges: RM " + String.format("%.2f", charges) +
                    "\nRebate: " + (int)(rebate * 100) + "%" +
                    "\nFinal Cost: RM " + String.format("%.2f", finalCost);

            detailText.setText(details);
        }
    }
}
