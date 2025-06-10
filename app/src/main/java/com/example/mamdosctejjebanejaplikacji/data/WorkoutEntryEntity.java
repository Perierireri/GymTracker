package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkoutEntryEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String type;
    public String setsJson;

    public WorkoutEntryEntity(int id, String date, String type, String setsJson) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.setsJson = setsJson;
    }
}
