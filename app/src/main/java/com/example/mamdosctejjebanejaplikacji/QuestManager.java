package com.example.mamdosctejjebanejaplikacji;

import java.util.ArrayList;
import java.util.List;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.widget.Toast;
import android.media.MediaPlayer;

public class QuestManager {
    private static final QuestManager instance = new QuestManager();
    private List<Quest> dailyQuests = new ArrayList<>();
    private List<Quest> weeklyQuests = new ArrayList<>();
    private List<Quest> bossQuests = new ArrayList<>();

    private QuestManager() {}

    public static QuestManager getInstance() {
        return instance;
    }

    public void generateQuests(UserCharacter character) {
        generateDailyQuests(character);
        generateWeeklyQuests(character);
        generateBossQuests(character);
    }

    private void generateDailyQuests(UserCharacter character) {
        dailyQuests.clear();
        
        // Strength-based quest
        dailyQuests.add(new Quest(
            "daily_strength_" + character.getLevel(),
            "Daily Strength Challenge",
            "Complete 3 sets of bench press with at least " + (character.getStrength() * 10) + "kg",
            QuestType.DAILY,
            StatType.STRENGTH,
            50
        ));

        // Endurance-based quest
        dailyQuests.add(new Quest(
            "daily_endurance_" + character.getLevel(),
            "Daily Endurance Test",
            "Complete a 30-minute run",
            QuestType.DAILY,
            StatType.ENDURANCE,
            40
        ));

        // Agility-based quest
        dailyQuests.add(new Quest(
            "daily_agility_" + character.getLevel(),
            "Daily Agility Workout",
            "Complete 50 push-ups",
            QuestType.DAILY,
            StatType.AGILITY,
            30
        ));
    }

    private void generateWeeklyQuests(UserCharacter character) {
        weeklyQuests.clear();
        
        // Weekly volume quest
        weeklyQuests.add(new Quest(
            "weekly_volume_" + character.getLevel(),
            "Weekly Training Volume",
            "Lift a total of " + (character.getLevel() * 500) + "kg this week",
            QuestType.WEEKLY,
            StatType.ENERGY,
            100
        ));

        // Weekly consistency quest
        weeklyQuests.add(new Quest(
            "weekly_consistency_" + character.getLevel(),
            "Weekly Workout Streak",
            "Complete 4 workouts this week",
            QuestType.WEEKLY,
            StatType.ENDURANCE,
            80
        ));
    }

    private void generateBossQuests(UserCharacter character) {
        bossQuests.clear();
        
        // Boss quest based on character level
        Boss boss = Bosses.getBossForLevel(character.getLevel());
        bossQuests.add(new Quest(
            "boss_" + character.getLevel(),
            "Defeat " + boss.getName(),
            "Challenge " + boss.getName() + " to a battle!",
            QuestType.BOSS,
            StatType.ENERGY,
            200
        ));
    }

    public List<Quest> getDailyQuests() {
        return new ArrayList<>(dailyQuests);
    }

    public List<Quest> getWeeklyQuests() {
        return new ArrayList<>(weeklyQuests);
    }

    public List<Quest> getBossQuests() {
        return new ArrayList<>(bossQuests);
    }

    public List<Quest> checkQuestCompletion(UserCharacter character) {
        List<Quest> completed = new ArrayList<>();
        for (Quest q : getActiveQuests()) {
            if (!q.isCompleted && /* TODO: add real completion logic */ false) {
                q.isCompleted = true;
                completed.add(q);
            }
        }
        return completed;
    }

    public List<Quest> getActiveQuests() {
        List<Quest> all = new ArrayList<>();
        all.addAll(dailyQuests);
        all.addAll(weeklyQuests);
        all.addAll(bossQuests);
        return all;
    }

    public List<Quest> getCompletedQuests() {
        List<Quest> completed = new ArrayList<>();
        for (Quest q : getActiveQuests()) {
            if (q.isCompleted) completed.add(q);
        }
        return completed;
    }

    public void showQuestCompleted(View questView) {
        // Animate quest completion (e.g. badge pop)
        Animation anim = AnimationUtils.loadAnimation(questView.getContext(), R.anim.scale_in);
        questView.startAnimation(anim);
    }

    // RPG Quest Function: Claim Reward
    public void claimQuestReward(View questView) {
        Animation anim = AnimationUtils.loadAnimation(questView.getContext(), R.anim.scale_in);
        questView.startAnimation(anim);
        Toast.makeText(questView.getContext(), "Quest reward claimed!", Toast.LENGTH_SHORT).show();
        // Play reward sound effect
        MediaPlayer mp = MediaPlayer.create(questView.getContext(), R.raw.sfx_potion);
        if (mp != null) mp.start();
        // Add RPG logic here: add XP, loot, etc.
    }
}
