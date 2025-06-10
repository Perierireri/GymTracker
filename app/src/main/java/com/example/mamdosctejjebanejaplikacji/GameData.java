package com.example.mamdosctejjebanejaplikacji;

public class GameData {
    public static UserCharacter user = new UserCharacter();

    public static void addXp(int amount) {
        user.xp += amount;
        while (user.xp >= xpToLevelUp(user.level)) {
            user.xp -= xpToLevelUp(user.level);
            user.level++;
        }
    }
    public static int xpToLevelUp(int level) {
        return (int)(100 * Math.pow(1.2, level - 1));
    }
    public static void updateSkills() {
        // Implement skill unlocking logic here if needed
    }
    public static void reset() {
        user = new UserCharacter();
    }
}
