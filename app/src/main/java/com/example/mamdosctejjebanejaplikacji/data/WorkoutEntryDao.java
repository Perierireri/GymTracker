package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface WorkoutEntryDao {
    @Insert
    void insert(WorkoutEntryEntity entry);

    @Query("SELECT * FROM WorkoutEntryEntity ORDER BY date DESC")
    List<WorkoutEntryEntity> getAll();
}
