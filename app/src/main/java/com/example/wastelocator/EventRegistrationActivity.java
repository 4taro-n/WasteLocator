package com.example.wastelocator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastelocator.DB.Event;
import com.example.wastelocator.DB.Volunteer;
import com.example.wastelocator.DB.VolunteerDao;
import com.example.wastelocator.Utils.MyApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRegistrationActivity extends AppCompatActivity {
    private TextView eventName;
    private EditText userName, userEmail, userMobilNum;
    private Button registerBtn;
    private Event eventToDisplay;
    private VolunteerDao volunteerDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    //private RelativeLayout userBtn, userBtn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);

        bindingViews();
        doaInitialising();
        displayEventName();
        joiningEvent();
    }

    private void doaInitialising() {
        volunteerDao = MyApp.getAppDatabase().volunteerDao();
    }

    private void bindingViews() {
        eventName = findViewById(R.id.event_registration_eventName);
        userName = findViewById(R.id.event_registration_userName);
        userEmail = findViewById(R.id.event_registration_userEmail);
        userMobilNum = findViewById(R.id.event_registration_userPhoneNum);
        registerBtn = findViewById(R.id.register_event_btn);
        //userBtn = findViewById(R.id.event_details_user_btn);
        //userBtn2 = findViewById(R.id.event_details_user_btn_2);
    }

    private void displayEventName() {
        // Receiving event data to display its name
        if (getIntent().hasExtra("eventData")) {
            eventToDisplay = (Event) getIntent().getSerializableExtra("eventData");
            assert eventToDisplay != null;
            eventName.setText(eventToDisplay.getEventName());
        }
    }
    private void joiningEvent() {
        registerBtn.setOnClickListener(view -> {
            // Extract date from the EditText
            String name = userName.getText().toString();
            String email = userEmail.getText().toString();
            String mobile = userMobilNum.getText().toString();

            // Validation
            if (name.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Create a volunteer object
                Volunteer volunteer = new Volunteer(name, email, mobile);

                // Linking the volunteer to the event
                if (eventToDisplay != null) {
                    volunteer.setEventID(eventToDisplay.eventID);
                }

                executorService.execute(() -> {
                    volunteerDao.insert(volunteer);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "You have joined the event successfully", Toast.LENGTH_SHORT).show();

                        // Update visibility of buttons after successfully registration
                        //userBtn.setVisibility(View.GONE);
                        //userBtn2.setVisibility(View.VISIBLE);
                    });
                    finish();
                });
            }
        });
    }
}
