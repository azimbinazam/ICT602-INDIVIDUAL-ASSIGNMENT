package com.example.billestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> displayList;
    ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);
        displayList = new ArrayList<>();
        idList = new ArrayList<>();

        Cursor cursor = db.getAllBills();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            double finalCost = cursor.getDouble(5);

            displayList.add(month + " - RM " + String.format("%.2f", finalCost));
            idList.add(id);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            int selectedId = idList.get(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", selectedId);
            startActivity(intent);
        });
    }
}
