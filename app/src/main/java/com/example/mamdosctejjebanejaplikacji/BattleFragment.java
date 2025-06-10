package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.media.MediaPlayer;
import androidx.room.Room;
import com.example.mamdosctejjebanejaplikacji.data.AppDatabase;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryEntity;
import com.example.mamdosctejjebanejaplikacji.WorkoutSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleFragment extends Fragment {
    // === DODAJEMY POLA KLASY ===
    private java.util.Set<String> defeatedBosses = new java.util.HashSet<>();
    private int[] unlockLevels;
    private Boss[] selectedBoss = new Boss[1];
    private int[] selectedBossLevel = new int[1];
    private int[] bossHp = new int[1];
    private TextView bossNameView;
    private TextView bossHpLabel;
    private com.google.android.material.progressindicator.LinearProgressIndicator bossHpBar;
    private TextView battleInfo;
    private ImageView bossAvatar;
    private Button workoutButton;
    private LinearLayout bossListContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle, container, false);
        // Animate boss card entry
        View bossCard = view.findViewById(R.id.bossCard);
        if (bossCard != null) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
            bossCard.startAnimation(anim);
        }
        // Animate HP bar
        bossHpBar = view.findViewById(R.id.bossHpBar);
        if (bossHpBar != null) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            bossHpBar.startAnimation(anim);
        }
        bossNameView = view.findViewById(R.id.bossName);
        bossHpLabel = view.findViewById(R.id.bossHpLabel);
        battleInfo = view.findViewById(R.id.battleInfo);
        TextView bossLevelInfo = view.findViewById(R.id.bossLevelInfo);
        workoutButton = view.findViewById(R.id.workoutButton);
        bossAvatar = view.findViewById(R.id.bossAvatar);

        // Inicjalizacja unlockLevels
        unlockLevels = new int[]{1, 5, 10, 20, 50};

        // --- Boss List as INFO ONLY ---
        bossListContainer = view.findViewById(R.id.bossListContainer);
        if (bossListContainer != null) {
            bossListContainer.removeAllViews();
            for (int i = 0; i < Bosses.all.size(); i++) {
                Boss b = Bosses.all.get(i);
                int unlockLevel = unlockLevels[i];
                int hp = b.baseHp;
                int xp = Bosses.getXpReward(b);
                TextView bossView = new TextView(requireContext());
                String bossLabel = b.name + "  |  Unlock: lvl " + unlockLevel + "  |  HP: " + hp + "  |  XP: " + xp;
                bossView.setText(bossLabel);
                bossView.setTextColor(getResources().getColor(R.color.text_on_dark));
                bossView.setTextSize(15f);
                bossView.setPadding(8, 8, 8, 8);
                bossView.setEnabled(false);
                bossView.setAlpha(0.7f);
                bossView.setClickable(false);
                bossView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                bossListContainer.addView(bossView);
            }
        }

        // --- AUTO PROGRESSION: after defeating boss, show next in list regardless of level ---
        // Find first undefeated boss (by order)
        final Boss[] nextBoss = new Boss[1];
        final int[] nextBossIdx = new int[1];
        nextBoss[0] = null;
        nextBossIdx[0] = -1;
        for (int i = 0; i < Bosses.all.size(); i++) {
            Boss b = Bosses.all.get(i);
            if (!defeatedBosses.contains(b.name)) {
                nextBoss[0] = b;
                nextBossIdx[0] = i;
                break;
            }
        }
        if (nextBoss[0] != null) {
            selectedBoss[0] = nextBoss[0];
            selectedBossLevel[0] = unlockLevels[nextBossIdx[0]];
            int[] stats = Bosses.getScaledStats(nextBoss[0], selectedBossLevel[0]);
            bossHp[0] = stats[0];
            bossNameView.setText(nextBoss[0].name);
            bossHpLabel.setText("HP: " + bossHp[0] + "/" + stats[0]);
            bossHpBar.setMax(stats[0]);
            bossHpBar.setProgress(bossHp[0]);
            battleInfo.setText("Boss: " + nextBoss[0].name + "\nHP: " + bossHp[0] + "\nDefeat the boss by working out!\nBoss Damage: " + stats[1]);
            bossAvatar.setEnabled(true);
            workoutButton.setEnabled(true);
        } else {
            bossNameView.setText("No more bosses!");
            bossHpLabel.setText("");
            bossHpBar.setProgress(0);
            battleInfo.setText("You have defeated all available bosses!");
            bossAvatar.setEnabled(false);
            workoutButton.setEnabled(false);
        }

        View.OnClickListener attackListener = v -> {
            if (!bossAvatar.isEnabled() || !workoutButton.isEnabled()) return;
            AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "app-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            java.util.List<WorkoutEntryEntity> workouts = db.workoutEntryDao().getAll();
            if (workouts.isEmpty()) {
                Toast.makeText(requireContext(), "Log a workout first!", Toast.LENGTH_SHORT).show();
                return;
            }
            WorkoutEntryEntity latestValidWorkout = null;
            boolean setsIsNumber = false;
            int setsNumberValue = 0;
            for (WorkoutEntryEntity workout : workouts) {
                if (workout.setsJson != null && !workout.setsJson.trim().isEmpty()) {
                    try {
                        Object parsed = new org.json.JSONTokener(workout.setsJson).nextValue();
                        if (parsed instanceof org.json.JSONArray) {
                            latestValidWorkout = workout;
                            break;
                        } else if (parsed instanceof Number) {
                            setsIsNumber = true;
                            setsNumberValue = ((Number) parsed).intValue();
                            latestValidWorkout = workout;
                            break;
                        }
                    } catch (Exception ignored) {}
                }
            }
            if (latestValidWorkout == null) {
                Toast.makeText(requireContext(), "No valid workout found!", Toast.LENGTH_SHORT).show();
                return;
            }
            int damage = 0;
            try {
                if (setsIsNumber) {
                    damage = setsNumberValue;
                } else {
                    JSONArray setsArray = new JSONArray(latestValidWorkout.setsJson);
                    for (int i = 0; i < setsArray.length(); i++) {
                        JSONObject setObj = setsArray.getJSONObject(i);
                        int reps = setObj.optInt("reps", 0);
                        damage += reps;
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(requireContext(), "Error parsing sets!", Toast.LENGTH_SHORT).show();
                return;
            }
            int[] stats = Bosses.getScaledStats(selectedBoss[0], selectedBossLevel[0]);
            bossHp[0] = Math.max(0, bossHp[0] - damage);
            bossHpBar.setProgress(bossHp[0]);
            bossHpLabel.setText("HP: " + bossHp[0] + "/" + stats[0]);
            battleInfo.setText("Boss: " + selectedBoss[0].name + "\nHP: " + bossHp[0] + "\nDefeat the boss by working out!\nBoss Damage: " + stats[1]);
            if (bossHp[0] <= 0) {
                int xp = Bosses.getXpReward(selectedBoss[0]);
                GameData.addXp(xp);
                Toast.makeText(requireContext(), "You defeated " + selectedBoss[0].name + "! +" + xp + " XP", Toast.LENGTH_LONG).show();
                bossHp[0] = 0;
                bossHpBar.setProgress(bossHp[0]);
                bossHpLabel.setText("HP: 0/" + stats[0]);
                bossAvatar.setEnabled(false);
                workoutButton.setEnabled(false);
                defeatedBosses.add(selectedBoss[0].name);
                // Find next undefeated boss (reuse variables)
                nextBoss[0] = null;
                nextBossIdx[0] = -1;
                for (int i = 0; i < Bosses.all.size(); i++) {
                    Boss b = Bosses.all.get(i);
                    if (!defeatedBosses.contains(b.name)) {
                        nextBoss[0] = b;
                        nextBossIdx[0] = i;
                        break;
                    }
                }
                if (nextBoss[0] != null) {
                    selectedBoss[0] = nextBoss[0];
                    selectedBossLevel[0] = unlockLevels[nextBossIdx[0]];
                    int[] stats2 = Bosses.getScaledStats(nextBoss[0], selectedBossLevel[0]);
                    bossHp[0] = stats2[0];
                    bossNameView.setText(nextBoss[0].name);
                    bossHpLabel.setText("HP: " + bossHp[0] + "/" + stats2[0]);
                    bossHpBar.setMax(stats2[0]);
                    bossHpBar.setProgress(bossHp[0]);
                    battleInfo.setText("Boss: " + nextBoss[0].name + "\nHP: " + bossHp[0] + "\nDefeat the boss by working out!\nBoss Damage: " + stats2[1]);
                    bossAvatar.setEnabled(true);
                    workoutButton.setEnabled(true);
                } else {
                    bossNameView.setText("No more bosses!");
                    bossHpLabel.setText("");
                    bossHpBar.setProgress(0);
                    battleInfo.setText("You have defeated all available bosses!");
                    bossAvatar.setEnabled(false);
                    workoutButton.setEnabled(false);
                }
            }
        };
        bossAvatar.setOnClickListener(attackListener);
        workoutButton.setOnClickListener(attackListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Find first undefeated boss (by order)
        final Boss[] nextBoss = new Boss[1];
        final int[] nextBossIdx = new int[1];
        nextBoss[0] = null;
        nextBossIdx[0] = -1;
        for (int i = 0; i < Bosses.all.size(); i++) {
            Boss b = Bosses.all.get(i);
            if (!defeatedBosses.contains(b.name)) {
                nextBoss[0] = b;
                nextBossIdx[0] = i;
                break;
            }
        }
        if (nextBoss[0] != null) {
            selectedBoss[0] = nextBoss[0];
            selectedBossLevel[0] = unlockLevels[nextBossIdx[0]];
            int[] stats = Bosses.getScaledStats(nextBoss[0], selectedBossLevel[0]);
            bossHp[0] = stats[0];
            bossNameView.setText(nextBoss[0].name);
            bossHpLabel.setText("HP: " + bossHp[0] + "/" + stats[0]);
            bossHpBar.setMax(stats[0]);
            bossHpBar.setProgress(bossHp[0]);
            battleInfo.setText("Boss: " + nextBoss[0].name + "\nHP: " + bossHp[0] + "\nDefeat the boss by working out!\nBoss Damage: " + stats[1]);
            bossAvatar.setEnabled(true);
            workoutButton.setEnabled(true);
        } else {
            bossNameView.setText("No more bosses!");
            bossHpLabel.setText("");
            bossHpBar.setProgress(0);
            battleInfo.setText("You have defeated all available bosses!");
            bossAvatar.setEnabled(false);
            workoutButton.setEnabled(false);
        }
    }
}
