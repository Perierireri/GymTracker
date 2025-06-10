package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class, CharacterData.class, WorkoutEntryEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract CharacterDao characterDao();
    public abstract WorkoutEntryDao workoutEntryDao();
}
