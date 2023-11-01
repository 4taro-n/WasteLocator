package com.example.wastelocator.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "event_feedback",
        foreignKeys = @ForeignKey(
                entity = Event.class,
                parentColumns = "eventID",
                childColumns = "eventID",
                onDelete = ForeignKey.CASCADE
        ), indices = {@Index("eventID")})
public class EventFeedback implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int feedbackID;
    private float rating;
    private String comments;
    private int eventID; // Links a feedback to an event

    public EventFeedback(float rating, String comments) {
        this.rating = rating;
        this.comments = comments;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
