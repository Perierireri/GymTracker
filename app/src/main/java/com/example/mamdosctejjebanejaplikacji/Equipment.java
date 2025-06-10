package com.example.mamdosctejjebanejaplikacji;

public class Equipment {
    public String name;
    public int bonusStrength;
    public int bonusEndurance;
    public int bonusAgility;
    public int bonusEnergy;

    public Equipment(String name, int bonusStrength, int bonusEndurance, int bonusAgility, int bonusEnergy) {
        this.name = name;
        this.bonusStrength = bonusStrength;
        this.bonusEndurance = bonusEndurance;
        this.bonusAgility = bonusAgility;
        this.bonusEnergy = bonusEnergy;
    }
    public Equipment(String name) {
        this(name, 0, 0, 0, 0);
    }
}
