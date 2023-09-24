package com.example.wastelocator.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FeedbackDao {
    @Insert
    void insert(Feedback feedback);

    @Update
    void update(Feedback feedback);

    @Delete
    void delete(Feedback feedback);

    @Query("SELECT * FROM feedback WHERE feedbackID = :feedbackID")
    LiveData<Feedback> getFeedbackByID(int feedbackID);
}
