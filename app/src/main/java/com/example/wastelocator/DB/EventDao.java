package com.example.wastelocator.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM events WHERE eventID = :eventID")
    LiveData<Event> getEventByID(int eventID);

    @Transaction
    @Query("SELECT * FROM events WHERE eventID = :eventId")
    LiveData<EventWithVolunteers> getEventWithVolunteers(int eventId);
}
