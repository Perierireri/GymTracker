package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.Nullable;

@Entity(foreignKeys = @ForeignKey(
        entity = Account.class,
        parentColumns = {"name"},
        childColumns = {"accountName"},
        onDelete = ForeignKey.CASCADE
))
public class CharacterData {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String accountName;
    public String statsJson; // Store stats, equipment, etc. as JSON for flexibility
    @Nullable
    public String avatarUri;

    public CharacterData(int id, String accountName, String statsJson, @Nullable String avatarUri) {
        this.id = id;
        this.accountName = accountName;
        this.statsJson = statsJson;
        this.avatarUri = avatarUri;
    }
}
