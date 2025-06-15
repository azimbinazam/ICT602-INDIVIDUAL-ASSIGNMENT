package com.example.billestimator;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtInfo = findViewById(R.id.txtInfo);
        TextView txtLink = findViewById(R.id.txtLink);

        // Your details
        txtInfo.setText("Name: Muhammad Azim Mohamad Azam\n" +
                "Student ID: 2023119119\n" +
                "Course: ICT602 - Web Technology And Application\n" +
                "Project: Monthly Electricity Bill Estimator");

        // Make GitHub link clickable
        txtLink.setText("https://github.com/azimbinazam/ICT602-INDIVIDUAL-ASSIGNMENT.git");
        txtLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
