package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Account {
    @PrimaryKey
    @NonNull
    public String name;
    public String password;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
