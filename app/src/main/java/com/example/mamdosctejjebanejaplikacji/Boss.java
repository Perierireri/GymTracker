package com.example.mamdosctejjebanejaplikacji;

public class Boss {
    public String name;
    public int baseHp;
    public int baseAttack;
    public float scalingHp;
    public float scalingAttack;

    public Boss(String name, int baseHp, int baseAttack) {
        this(name, baseHp, baseAttack, 1.0f, 1.0f);
    }
    public Boss(String name, int baseHp, int baseAttack, float scalingHp, float scalingAttack) {
        this.name = name;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.scalingHp = scalingHp;
        this.scalingAttack = scalingAttack;
    }

    public String getName() { return name; }
}
