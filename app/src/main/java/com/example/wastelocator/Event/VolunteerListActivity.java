package com.example.wastelocator.Event;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastelocator.DB.EventDao;
import com.example.wastelocator.DB.EventWithVolunteers;
import com.example.wastelocator.DB.Volunteer;
import com.example.wastelocator.R;
import com.example.wastelocator.Utils.MyApp;

import java.util.ArrayList;

public class VolunteerListActivity extends AppCompatActivity {
    private ArrayList<Volunteer> volunteerArrayList;
    private RecyclerView recyclerView;
    private VolunteerAdapter volunteerAdapter;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteers_list);

        viewBinding();
        initialising();
        getDataFromDatabase();


    }

    // Database -------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    private void getDataFromDatabase() {
        EventDao eventDao = MyApp.getAppDatabase().eventDAO();
        int eventID = getIntent().getIntExtra("EVENT_ID", -1);

        if (eventID != -1) {
            emptyState.setVisibility(View.GONE);

            LiveData<EventWithVolunteers> eventWithVolunteersLiveData = eventDao.getEventWithVolunteers(eventID);
            eventWithVolunteersLiveData.observe(this, eventWithVolunteers -> {
                volunteerArrayList.clear();
                if (eventWithVolunteers != null && eventWithVolunteers.volunteers != null) {
                    if (eventWithVolunteers.volunteers.isEmpty()) {
                        emptyState.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Volunteer data not found for this event", Toast.LENGTH_SHORT).show();
                    } else {
                        volunteerArrayList.addAll(eventWithVolunteers.volunteers);
                        volunteerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(this, "Error retrieving volunteers.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Views and recyclerView ----------------------------
    private void initialising() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteerArrayList = new ArrayList<>();
        volunteerAdapter = new VolunteerAdapter(volunteerArrayList);
        recyclerView.setAdapter(volunteerAdapter);
    }
    private void viewBinding() {
        recyclerView = findViewById(R.id.recyclerView_volunteerList);
        emptyState = findViewById(R.id.empty_volunteer_state);
    }
}
