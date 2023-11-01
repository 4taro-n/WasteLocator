package com.example.wastelocator.Event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastelocator.DB.Event;
import com.example.wastelocator.DB.EventFeedback;
import com.example.wastelocator.DB.EventFeedbackDao;
import com.example.wastelocator.R;
import com.example.wastelocator.Utils.MyApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventFeedbackActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText comments;
    private Button submitBtn;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private EventFeedbackDao eventFeedbackDao;
    private Event eventToLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feedback);

        bindingViews();
        doaInitialising();
        initialisingEventToLink();
        submitFeedback();
    }

    private void bindingViews() {
        ratingBar = findViewById(R.id.ratingBar);
        comments = findViewById(R.id.event_comments);
        submitBtn = findViewById(R.id.submit_event_feedback_btn);
    }

    private void doaInitialising() {
        eventFeedbackDao = MyApp.getAppDatabase().feedbackDao();
    }

    private void initialisingEventToLink() {
        if (getIntent().hasExtra("eventData")) {
            eventToLink = (Event) getIntent().getSerializableExtra("eventData");
        }
    }
    private void submitFeedback() {
        submitBtn.setOnClickListener(view -> {
            // Extract date from the EditText
            float rating = ratingBar.getRating();
            String feedbackComment = comments.getText().toString();

            // Validation
            if (rating == 0 || feedbackComment.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create a feedback object
                EventFeedback eventFeedback = new EventFeedback(rating, feedbackComment);

                // Linking the feedback to the event
                if (eventToLink != null) {
                    eventFeedback.setEventID(eventToLink.eventID);
                }

                executorService.execute(() -> {
                    eventFeedbackDao.insert(eventFeedback);
                    runOnUiThread(() -> Toast.makeText(this, "Thank you for your feedback", Toast.LENGTH_SHORT).show());
                });
                finish();
            }

        });

    }


}
