package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ProgressionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progression, container, false);
        TextView progressionInfo = view.findViewById(R.id.progressionInfo);
        TextView levelLabel = view.findViewById(R.id.levelLabel);
        TextView xpLabel = view.findViewById(R.id.xpLabel);
        LinearProgressIndicator xpProgressBar = view.findViewById(R.id.xpProgressBar);
        ChipGroup skillsChipGroup = view.findViewById(R.id.skillsChipGroup);
        UserCharacter user = GameData.user;
        StringBuilder sb = new StringBuilder();
        sb.append("Level: ").append(user.level).append("\nXP: ").append(user.xp).append("/").append(GameData.xpToLevelUp(user.level)).append("\n\nSkills:\n");
        if (user.skills.isEmpty()) GameData.updateSkills();
        for (Skill skill : user.skills) {
            sb.append("- ").append(skill.name).append(": ").append(skill.description).append(" ");
            sb.append(skill.unlocked ? "(Unlocked)" : "(Locked at lvl " + skill.unlockLevel + ")");
            sb.append("\n");
        }
        progressionInfo.setText(sb.toString());
        // Update card graphics below
        levelLabel.setText("Level: " + user.level);
        xpLabel.setText("XP: " + user.xp + "/" + GameData.xpToLevelUp(user.level));
        xpProgressBar.setMax(GameData.xpToLevelUp(user.level));
        xpProgressBar.setProgress(user.xp);
        // Update skills chip group
        skillsChipGroup.removeAllViews();
        for (Skill skill : user.skills) {
            Chip chip = new Chip(requireContext());
            chip.setText(skill.name);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setChipBackgroundColorResource(skill.unlocked ? R.color.mint : R.color.blue_dim);
            chip.setTextColor(getResources().getColor(R.color.text_on_dark));
            skillsChipGroup.addView(chip);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            TextView progressionInfo = view.findViewById(R.id.progressionInfo);
            TextView levelLabel = view.findViewById(R.id.levelLabel);
            TextView xpLabel = view.findViewById(R.id.xpLabel);
            LinearProgressIndicator xpProgressBar = view.findViewById(R.id.xpProgressBar);
            ChipGroup skillsChipGroup = view.findViewById(R.id.skillsChipGroup);
            UserCharacter user = GameData.user;
            StringBuilder sb = new StringBuilder();
            sb.append("Level: ").append(user.level).append("\nXP: ").append(user.xp).append("/").append(GameData.xpToLevelUp(user.level)).append("\n\nSkills:\n");
            for (Skill skill : user.skills) {
                sb.append("- ").append(skill.name).append(": ").append(skill.description).append(" ");
                sb.append(skill.unlocked ? "(Unlocked)" : "(Locked at lvl " + skill.unlockLevel + ")");
                sb.append("\n");
            }
            progressionInfo.setText(sb.toString());
            // Update card graphics below
            levelLabel.setText("Level: " + user.level);
            xpLabel.setText("XP: " + user.xp + "/" + GameData.xpToLevelUp(user.level));
            xpProgressBar.setMax(GameData.xpToLevelUp(user.level));
            xpProgressBar.setProgress(user.xp);
            // Update skills chip group
            skillsChipGroup.removeAllViews();
            for (Skill skill : user.skills) {
                Chip chip = new Chip(requireContext());
                chip.setText(skill.name);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setChipBackgroundColorResource(skill.unlocked ? R.color.mint : R.color.blue_dim);
                chip.setTextColor(getResources().getColor(R.color.text_on_dark));
                skillsChipGroup.addView(chip);
            }
        }
    }
}
