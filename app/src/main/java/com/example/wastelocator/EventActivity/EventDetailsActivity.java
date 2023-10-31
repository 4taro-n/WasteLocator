package com.example.wastelocator.EventActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastelocator.DB.Event;
import com.example.wastelocator.DB.EventDao;
import com.example.wastelocator.R;
import com.example.wastelocator.Utils.MyApp;
import com.example.wastelocator.Utils.SharedPrefManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDetailsActivity extends AppCompatActivity {
    int eventID;
    private EventDao eventDao;
    private Event event;
    private ImageView imageView;
    private TextView eventName, eventDateTime, eventLocation, eventDescription;
    private Button volunteersBtn, editBtn, deleteBtn, registerBtn, feedbackBtn;
    private LinearLayout adminBtn;
    private RelativeLayout userBtn; //, userBtn2;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_events_details);

        bindingViews();
        daoInitialisation();
        manageRoleBasedFeature();
        populatingEventDetails();
    }

    private void bindingViews() {
        imageView = findViewById(R.id.event_details_image);
        eventName = findViewById(R.id.event_details_name);
        eventDateTime = findViewById(R.id.event_details_dateTime);
        eventLocation = findViewById(R.id.event_details_location);
        eventDescription = findViewById(R.id.event_details_description);
        volunteersBtn = findViewById(R.id.btn_volunteers);
        editBtn = findViewById(R.id.btn_edit);
        deleteBtn = findViewById(R.id.btn_delete);
        registerBtn = findViewById(R.id.btn_join);
        feedbackBtn = findViewById(R.id.btn_feedback);
        adminBtn = findViewById(R.id.event_details_admin_btn);
        userBtn = findViewById(R.id.event_details_user_btn);
        //userBtn2 = findViewById(R.id.event_details_user_btn_2);
    }

    private void daoInitialisation() {
        eventDao = MyApp.getAppDatabase().eventDAO();
    }

    private void manageRoleBasedFeature() {
        if (SharedPrefManager.isAdmin()) {
            adminBtn.setVisibility(View.VISIBLE);
            userBtn.setVisibility(View.GONE);
            //userBtn2.setVisibility(View.GONE);
        } else {
            adminBtn.setVisibility(View.GONE);
            userBtn.setVisibility(View.VISIBLE);
            //userBtn2.setVisibility(View.GONE);
        }
    }

    private void populatingEventDetails() {
        eventID = getIntent().getIntExtra("eventID", -1);
        if (eventID != -1) {
            retrievingAndSettingEvent(eventID);

            volunteersBtn.setOnClickListener(view -> {
                Intent intent = new Intent(this, VolunteerListActivity.class);
                intent.putExtra("EVENT_ID", eventID);
                startActivity(intent);
            });

            editBtn.setOnClickListener(view -> {
                Intent intent = new Intent(this, NewEventActivity.class);
                intent.putExtra("eventData", event);
                startActivity(intent);
            });

            deleteBtn.setOnClickListener(view -> {
                deleteTheEvent();
            });

            registerBtn.setOnClickListener(view -> {
                Intent intent = new Intent(this, EventRegistrationActivity.class);
                intent.putExtra("eventData", event);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            });
            feedbackBtn.setOnClickListener(view -> {
                Intent intent = new Intent(this, EventFeedbackActivity.class);
                intent.putExtra("eventData", event);
                startActivity(intent);
            });

        }
    }

    private void retrievingAndSettingEvent(int eventId) {
        eventDao.getEventByID(eventId).observe(this, dbEvent -> {
            if (dbEvent != null) {
                this.event = dbEvent;

                String imageUriStr = this.event.getImageUri();
                if (imageUriStr != null && !imageUriStr.isEmpty()) {
                    Uri imageUri = Uri.parse(imageUriStr);
                    imageView.setImageURI(imageUri);
                }
                LocalDate date = this.event.getEventDate();
                LocalTime time = this.event.getEventTime();
                String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

                eventName.setText(this.event.getEventName());
                eventDateTime.setText(formattedDate + "   " + formattedTime);
                eventLocation.setText(this.event.getEventLocation());
                eventDescription.setText(this.event.getEventDescription());

            }
        });
    }

    private void deleteTheEvent() {
        executorService.execute(() -> {
            eventDao.delete(event);
            finish();
        });
    }


}
