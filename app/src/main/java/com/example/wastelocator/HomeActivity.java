package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.example.wastelocator.Utils.SharedPrefManager;


public class HomeActivity extends AppCompatActivity {

    private Button btnGoToNearlyBin, btnGoToNotification, btnGoToEvents,btnGoToFeedback,btnGoToContactUs, btnGoToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setViewIds();

        btnGoToNearlyBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NearlyBinActivity.class);
                startActivity(intent);
            }
        });

        btnGoToNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        btnGoToEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });
        btnGoToFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        btnGoToContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        if(SharedPrefManager.isAdmin()) {
            btnGoToDashboard.setVisibility(View.VISIBLE);
        } else {
            btnGoToDashboard.setVisibility(View.GONE);
        }
    }

    private void setViewIds() {
        btnGoToNearlyBin = findViewById(R.id.btnNearbyBin);
        btnGoToNotification = findViewById(R.id.btnNotifications);
        btnGoToEvents = findViewById(R.id.btnGoToEvents);
        btnGoToFeedback = findViewById(R.id.btnGoToFeedback);
        btnGoToContactUs = findViewById(R.id.btnGoToContactUs);
        btnGoToDashboard = findViewById(R.id.btnGoToDashboard);
    }
}