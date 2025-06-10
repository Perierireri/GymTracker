package com.example.mamdosctejjebanejaplikacji;

public class EquipmentSlots {
    public Equipment weapon;
    public Equipment gloves;
    public Equipment headband;

    public EquipmentSlots() {
        this.weapon = new Equipment("Iron Dumbbell", 2, 0, 0, 0);
        this.gloves = new Equipment("Epic Grip Gloves", 0, 1, 0, 0);
        this.headband = new Equipment("Legendary Sweatband", 0, 0, 1, 0);
    }
}
