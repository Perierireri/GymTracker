package com.example.mamdosctejjebanejaplikacji.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AccountDao {
    @Insert
    void insert(Account account);

    @Query("SELECT * FROM Account WHERE name = :name AND password = :password LIMIT 1")
    Account login(String name, String password);

    @Query("SELECT * FROM Account WHERE name = :name LIMIT 1")
    Account getAccount(String name);
}
