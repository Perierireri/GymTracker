package com.example.mamdosctejjebanejaplikacji;

import java.util.Arrays;
import java.util.List;

public class Bosses {
    public static final List<Boss> all = Arrays.asList(
        new Boss("Bronze Boss", 500, 10),
        new Boss("Silver Boss", 1000, 15, 1.0f, 1.1f),
        new Boss("Golden Boss", 2000, 25, 1.0f, 1.15f),
        new Boss("Platinum Boss", 2000, 35, 1.0f, 1.18f),
        new Boss("Diamond Boss", 3000, 50, 1.0f, 1.22f)
    );

    public static Boss getBossForLevel(int level) {
        if (level < 5) return all.get(0); // Bronze
        else if (level < 10) return all.get(1); // Silver
        else if (level < 20) return all.get(2); // Gold
        else if (level < 50) return all.get(3); // Platinum
        else return all.get(4); // Diamond
    }

    public static int[] getScaledStats(Boss boss, int userLevel) {
        int bossIndex = all.indexOf(boss);
        int hp;
        int attack;
        if (boss.name.equals("Silver Boss") && userLevel == 5) {
            hp = 1000;
            attack = boss.baseAttack;
        } else if (boss.name.equals("Golden Boss") && userLevel == 10) {
            hp = 2000;
            attack = boss.baseAttack;
        } else {
            int baseHp = boss.baseHp + bossIndex * 500;
            hp = (int)(baseHp * Math.pow(boss.scalingHp, userLevel - 1));
            attack = (int)(boss.baseAttack * Math.pow(boss.scalingAttack, userLevel - 1));
        }
        return new int[]{hp, attack};
    }

    public static int getXpReward(Boss boss) {
        if (boss.name.equals("Silver Boss")) return 300;
        if (boss.name.equals("Golden Boss")) return 500;
        int bossIndex = all.indexOf(boss);
        return 100 + bossIndex * 200;
    }
}
