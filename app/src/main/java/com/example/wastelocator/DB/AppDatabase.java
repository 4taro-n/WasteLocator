package com.example.wastelocator.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Event.class, Volunteer.class, EventFeedback.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDAO();
    public abstract VolunteerDao volunteerDao();
    public abstract EventFeedbackDao feedbackDao();
}
