package com.example.mamdosctejjebanejaplikacji;

public class Quest {
    public String id;
    public String title;
    public String description;
    public QuestType type;
    public StatType statReward;
    public int xpReward;
    public boolean isCompleted;

    public Quest(String id, String title, String description, QuestType type, StatType statReward, int xpReward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.statReward = statReward;
        this.xpReward = xpReward;
        this.isCompleted = false;
    }
}
