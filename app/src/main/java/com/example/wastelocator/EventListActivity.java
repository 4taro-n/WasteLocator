package com.example.wastelocator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wastelocator.DB.Event;
import com.example.wastelocator.DB.EventDao;
import com.example.wastelocator.Utils.MyApp;
import com.example.wastelocator.Utils.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    ArrayList<Event> eventArrayList;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private Button addEvent;
    private LinearLayout emptyStateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

    viewBinding();
    initialising();
    getDataFromDatabase();
    handleClickOnEventItem();
    manageRoleBasedFeature();

    }

    // add new event -------------------------------------------
    private void handleClickOnEventItem() {
        eventAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            Event selectedEvent = eventArrayList.get(position);
            intent.putExtra("eventID", selectedEvent.eventID);
            startActivity(intent);
        });
    }

    private void manageRoleBasedFeature() {
        //Toast.makeText(this, "Admin is: " + SharedPrefManager.isAdmin(), Toast.LENGTH_SHORT).show();
        if (SharedPrefManager.isAdmin()) {
            addEvent.setVisibility(View.VISIBLE);
            addEvent.setOnClickListener(view -> {
                manageNewEventFunctionality();
            });
        } else {
            addEvent.setVisibility(View.GONE);
        }
    }

    private void manageNewEventFunctionality() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    // Database ------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    private void getDataFromDatabase() {
        EventDao eventDao = MyApp.getAppDatabase().eventDAO();
        LiveData<List<Event>> eventLiveData = eventDao.getAllEvents();

        eventLiveData.observe(this, events -> {
            eventArrayList.clear();

            if (events != null && !events.isEmpty()) {
                emptyStateImage.setVisibility(View.GONE);
                eventArrayList.addAll(events);
                eventAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                emptyStateImage.setVisibility(View.VISIBLE);
                eventAdapter.notifyDataSetChanged();
            }
        });
    }

    // Views and recyclerView ----------------------------
    private void initialising() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventArrayList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventArrayList);
        recyclerView.setAdapter(eventAdapter);
    }

    private void viewBinding() {
        recyclerView = findViewById(R.id.recyclerView_eventList);
        addEvent = findViewById(R.id.add_event_btn);
        emptyStateImage = findViewById(R.id.empty_event_state);
    }

}