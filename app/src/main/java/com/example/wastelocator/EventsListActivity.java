package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class EventsListActivity extends AppCompatActivity {
    ArrayList<Event> eventArrayList;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
    }

    // Display event list
    private void initialising() {

    }
}