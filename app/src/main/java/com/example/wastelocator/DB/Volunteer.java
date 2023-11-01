package com.example.wastelocator.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "volunteers",
    foreignKeys = @ForeignKey(
            entity = Event.class,
            parentColumns = "eventID",
            childColumns = "eventID",
            onDelete = ForeignKey.CASCADE
    ), indices = {@Index("eventID")})
public class Volunteer implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int volunteerID;
    private String name;
    private String email;
    private String mobileNum;
    private int eventID; // links a volunteer to an event

    public Volunteer(String name, String email, String mobileNum) {
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        this.volunteerID = volunteerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
