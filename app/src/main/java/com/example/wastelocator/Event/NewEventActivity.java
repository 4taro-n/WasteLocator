package com.example.wastelocator.Event;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastelocator.DB.Event;
import com.example.wastelocator.DB.EventDao;
import com.example.wastelocator.R;
import com.example.wastelocator.Utils.MyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewEventActivity extends AppCompatActivity {
    private ImageView eventImage;
    private Button addImageBtn, selectedDateBtn, selectedTimeBtn, submitBtn;
    private EditText eventName, eventLocation, eventDescription;
    private TextView eventDate, eventTime;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Uri selectedImageUri;
    private boolean isEditMode = false;
    private Event eventToEdit;
    private EventDao eventDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        bindViews();
        daoInitialisation();
        checkEditMode();

        addImageBtn.setOnClickListener(view -> pickImage());
        selectedDateBtn.setOnClickListener(view -> pickDate());
        selectedTimeBtn.setOnClickListener(view -> pickTime());
        submitBtn.setOnClickListener(view -> submitEvent());
    }

    private void bindViews() {
        eventImage = findViewById(R.id.add_event_image);
        addImageBtn = findViewById(R.id.add_event_image_btn);
        selectedDateBtn = findViewById(R.id.add_event_date_btn);
        selectedTimeBtn = findViewById(R.id.add_event_time_btn);
        submitBtn = findViewById(R.id.submit_event_btn);
        eventName = findViewById(R.id.add_event_name);
        eventLocation = findViewById(R.id.add_event_location);
        eventDescription = findViewById(R.id.add_event_description);
        eventDate = findViewById(R.id.add_event_date_textView);
        eventTime = findViewById(R.id.add_event_time_textView);

    }
    private void daoInitialisation() {
        eventDao = MyApp.getAppDatabase().eventDAO();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mGetContent.launch(intent);
    }

    private void pickDate() {
        LocalDate currentDate = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // display the selected date in the TextView
                    eventDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                },
                currentDate.getYear(),
                currentDate.getMonthValue() - 1,
                currentDate.getDayOfMonth()
        );
        datePickerDialog.show();
    }

    private void pickTime() {
        LocalTime currentTime = LocalTime.now();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    // display the selected time in the TextView
                    eventTime.setText(String.format("%02d:%02d",hourOfDay, minute));
                },
                currentTime.getHour(),
                currentTime.getMinute(),
                true // 24hr view
        );
        timePickerDialog.show();
    }

    private void submitEvent() {
        LocalDate date;
        LocalTime time;
        // Extract date from the TextViews
        String dateString = eventDate.getText().toString();
        String timeString = eventTime.getText().toString();
        String name = eventName.getText().toString();
        String location = eventLocation.getText().toString();
        String description = eventDescription.getText().toString();
        String imageUri = selectedImageUri != null ? selectedImageUri.toString() : null;

        // Validation
        if (name.isEmpty() || location.isEmpty() || description.isEmpty() ||
                dateString.equals("date")|| timeString.equals("time")) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        date = LocalDate.parse(dateString);
        time = LocalTime.parse(timeString);

        if (isEditMode) {
            // Update the eventToEdit properties
            eventToEdit.setEventName(name);
            eventToEdit.setEventDate(date);
            eventToEdit.setEventTime(time);
            eventToEdit.setEventLocation(location);
            eventToEdit.setEventDescription(description);
            eventToEdit.setImageUri(imageUri);

            executorService.execute(() -> {
                eventDao.update(eventToEdit);
                runOnUiThread(() -> Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show());
                finish();
            });

        } else {
            // Create an Event object
            Event newEvent = new Event(name, date, time, location, description, imageUri);

            executorService.execute(() -> {
                eventDao.insert(newEvent);
                runOnUiThread(() -> Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show());
                finish();
            });
        }
    }

    // Image Uri ------------------------------------------
    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();

                        // Immediately save the selected image to internal storage
                        Uri saveUri = saveImageToInternalStorage(selectedImageUri);
                        if (saveUri != null) {
                            selectedImageUri = saveUri;
                            eventImage.setImageURI(saveUri);
                        } else {
                            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private Uri saveImageToInternalStorage(Uri selectedImageUri) {
        try {
            // Open the stream from the original URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Create an output file in the app's cache directory
            File outputFile = new File(getCacheDir(), "img_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            // Copy the stream
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            fileOutputStream.close();
            inputStream.close();

            return Uri.fromFile(outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Receiving data to update the event and Populating data -----------------------
    private void checkEditMode() {
        if (getIntent().hasExtra("eventData")) {
            isEditMode = true;
            eventToEdit = (Event) getIntent().getSerializableExtra("eventData");
            assert eventToEdit != null;
            populateData(eventToEdit);
        }
    }
    private void populateData(Event editEvent) {

        if (editEvent.getImageUri() != null) {
            selectedImageUri = Uri.parse((editEvent.getImageUri()));
            eventImage.setImageURI(selectedImageUri);
        }
        eventName.setText(editEvent.getEventName());
        eventLocation.setText(editEvent.getEventLocation());
        eventDescription.setText(editEvent.getEventDescription());
        eventDate.setText(editEvent.getEventDate().toString());
        eventTime.setText(editEvent.getEventTime().toString());
    }
}
