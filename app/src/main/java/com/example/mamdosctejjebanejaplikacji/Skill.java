package com.example.mamdosctejjebanejaplikacji;

public class Skill {
    public String name;
    public String description;
    public int unlockLevel;
    public boolean unlocked;

    public Skill(String name, String description, int unlockLevel, boolean unlocked) {
        this.name = name;
        this.description = description;
        this.unlockLevel = unlockLevel;
        this.unlocked = unlocked;
    }
}
