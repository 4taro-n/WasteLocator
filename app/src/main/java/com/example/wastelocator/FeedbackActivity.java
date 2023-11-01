package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;

public class FeedbackActivity extends AppCompatActivity {
    private ImageView feedbackImage;
    private EditText locationEditText, descriptionEditText;
    private Button photoBtn, locationBtn, sendBtn;
    private Spinner titleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setViewIds();
        setTitleSpinner();
    }

    private void setTitleSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Bin Location Accuracy");
        categories.add("Bin Fill Level Accuracy");
        categories.add("Suggestion for New Features");
        categories.add("Reporting technical Issues");
        categories.add("General Feedback");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(dataAdapter);
    }

    private void setViewIds() {
        feedbackImage = findViewById(R.id.feedbackImage);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText  = findViewById(R.id.descriptionEditText);
        photoBtn = findViewById(R.id.photoBtn);
        locationBtn = findViewById(R.id.locationBtn);
        sendBtn = findViewById(R.id.sendBtn);
        titleSpinner = findViewById(R.id.titleSpinner);
    }
}