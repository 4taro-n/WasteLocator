package com.example.wastelocator.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VolunteerDao {
    @Insert
    void insert(Volunteer volunteer);

    @Update
    void update(Volunteer volunteer);

    @Delete
    void delete(Volunteer volunteer);

    @Query("SELECT * FROM volunteers WHERE volunteerID = :volunteerID")
    LiveData<Volunteer> getVolunteerByID(int volunteerID);

}
