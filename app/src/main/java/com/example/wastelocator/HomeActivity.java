package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.example.wastelocator.Utils.SharedPrefManager;


public class HomeActivity extends AppCompatActivity {

    //private Button btnGoToContactUs, btnGoToDashboard;
    private CardView nearbyBinsCard, notificationsCard, eventsCard;
    private CardView feedbackCard, contactUsCard, dashboardCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setViewIds();

        nearbyBinsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NearByBinActivity.class);
                startActivity(intent);
            }
        });

        notificationsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        eventsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EventListActivity.class);
                startActivity(intent);
            }
        });
        feedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        contactUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        dashboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        dashboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        if(SharedPrefManager.isAdmin()) {
            dashboardCard.setVisibility(View.VISIBLE);
        } else {
            dashboardCard.setVisibility(View.GONE);
        }
    }


    private void setViewIds() {
        nearbyBinsCard = findViewById(R.id.nearby_bins_card);
        notificationsCard = findViewById(R.id.notifications_card);
        eventsCard = findViewById(R.id.events_card);
        feedbackCard = findViewById(R.id.feedback_card);
        contactUsCard = findViewById(R.id.contact_us_card);
        dashboardCard = findViewById(R.id.dashboard_card);
    }
}