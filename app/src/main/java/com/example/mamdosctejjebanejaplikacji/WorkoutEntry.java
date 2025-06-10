package com.example.mamdosctejjebanejaplikacji;

import java.util.List;

public class WorkoutEntry {
    public String date;
    public String type;
    public List<WorkoutSet> sets;

    public WorkoutEntry(String date, String type, List<WorkoutSet> sets) {
        this.date = date;
        this.type = type;
        this.sets = sets;
    }
}
