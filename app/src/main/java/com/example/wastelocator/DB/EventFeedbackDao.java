package com.example.wastelocator.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EventFeedbackDao {
    @Insert
    void insert(EventFeedback eventFeedback);

    @Update
    void update(EventFeedback eventFeedback);

    @Delete
    void delete(EventFeedback eventFeedback);

    @Query("SELECT * FROM event_feedback WHERE feedbackID = :feedbackID")
    LiveData<EventFeedback> getFeedbackByID(int feedbackID);
}
