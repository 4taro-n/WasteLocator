package com.example.wastelocator.DB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class EventWithVolunteers {
    @Embedded
    public Event event;

    @Relation(
            parentColumn = "eventID",
            entityColumn = "eventID"
    )
    public List<Volunteer> volunteers;
}
