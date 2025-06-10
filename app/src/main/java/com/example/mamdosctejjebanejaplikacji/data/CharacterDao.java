package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CharacterDao {
    @Insert
    void insert(CharacterData character);

    @Update
    void update(CharacterData character);

    @Query("SELECT * FROM CharacterData WHERE accountName = :accountName LIMIT 1")
    CharacterData getCharacter(String accountName);
}
