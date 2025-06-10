package com.example.mamdosctejjebanejaplikacji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import com.example.mamdosctejjebanejaplikacji.data.AppDatabase;
import com.example.mamdosctejjebanejaplikacji.data.DatabaseProvider;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryEntity;
import java.util.List;
import org.json.JSONArray;

public class CharacterFragment extends Fragment {
    private TextView title, equipment, statTotalWorkouts, statTotalWeight, statTotalDistance, statBestLift, statWorkoutStreak, workoutToday, workoutWeek, levelLabel;
    private LinearLayout badgesContainer;
    private ImageView profilePic;
    private Button changePicBtn;
    private ImageButton settingsButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_READ_IMAGE_PERMISSION = 100;
    private Uri profilePicUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);
        title = view.findViewById(R.id.characterTitle);
        equipment = view.findViewById(R.id.characterEquipment);
        badgesContainer = view.findViewById(R.id.badgesContainer);
        statTotalWorkouts = view.findViewById(R.id.statTotalWorkouts);
        statTotalWeight = view.findViewById(R.id.statTotalWeight);
        statTotalDistance = view.findViewById(R.id.statTotalDistance);
        statBestLift = view.findViewById(R.id.statBestLift);
        statWorkoutStreak = view.findViewById(R.id.statWorkoutStreak);
        workoutToday = view.findViewById(R.id.workoutToday);
        workoutWeek = view.findViewById(R.id.workoutWeek);
        levelLabel = view.findViewById(R.id.levelLabel);
        profilePic = view.findViewById(R.id.profilePic);
        changePicBtn = view.findViewById(R.id.changePicBtn);
        settingsButton = view.findViewById(R.id.settingsButton);

        // Ustawianie nicku i levelu
        if (GameData.user != null) {
            title.setText(GameData.user.getName());
            title.setTextColor(getResources().getColor(R.color.text_highlight)); // jaśniejszy kolor
        }

        settingsButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(android.R.id.content, new SettingsFragment())
                .addToBackStack(null)
                .commit();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String uriString = prefs.getString("profile_pic_uri", null);
        if (uriString != null) {
            profilePicUri = Uri.parse(uriString);
            if (hasReadImagePermission()) {
                profilePic.setImageURI(profilePicUri);
            } else {
                requestReadImagePermission();
            }
        }
        profilePic.setOnClickListener(v -> pickImage());
        changePicBtn.setOnClickListener(v -> pickImage());
        // Set other fields using GameData.user etc.
        // ... (populate UI fields as in Kotlin)
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            if (GameData.user != null && levelLabel != null) {
                levelLabel.setText("Level: " + GameData.user.getLevel());
            }
            if (GameData.user != null && title != null) {
                String name = GameData.user.getName();
                if (name != null && !name.isEmpty()) {
                    title.setText(name);
                } else {
                    title.setText("Set your name!");
                }
            }
            updateStatistics();
        }
    }

    private void updateStatistics() {
        Context context = requireContext();
        new Thread(() -> {
            AppDatabase db = DatabaseProvider.getDatabase(context);
            List<WorkoutEntryEntity> workouts = db.workoutEntryDao().getAll();
            int totalWorkouts = 0;
            int totalSets = 0;
            int totalReps = 0;
            double totalWeight = 0;
            double bestLift = 0;
            java.util.TreeSet<String> workoutDays = new java.util.TreeSet<>();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            for (WorkoutEntryEntity entry : workouts) {
                if (entry.setsJson == null) continue;
                String setsJsonStr = entry.setsJson.trim();
                if (setsJsonStr.isEmpty() || setsJsonStr.equals("[]")) continue;
                boolean counted = false;
                try {
                    JSONArray arr = new JSONArray(setsJsonStr);
                    if (arr.length() > 0) {
                        totalWorkouts++;
                        counted = true;
                    }
                    for (int i = 0; i < arr.length(); i++) {
                        JSONArray set = arr.getJSONArray(i);
                        int sets = 1;
                        int reps = 1;
                        double weight = 0;
                        if (set.length() == 3) {
                            sets = set.getInt(0);
                            reps = set.getInt(1);
                            weight = set.getDouble(2);
                        } else if (set.length() == 2) {
                            reps = set.getInt(0);
                            weight = set.getDouble(1);
                        } else if (set.length() == 1) {
                            weight = set.getDouble(0);
                        }
                        totalSets += sets;
                        totalReps += sets * reps;
                        totalWeight += sets * reps * weight;
                        if (weight > bestLift) bestLift = weight;
                    }
                } catch (Exception ignored) {}
                if (counted) {
                    try {
                        long timestamp = Long.parseLong(entry.date);
                        String day = sdf.format(new java.util.Date(timestamp));
                        workoutDays.add(day);
                    } catch (Exception ignored) {}
                }
            }
            int streak = 0;
            if (!workoutDays.isEmpty()) {
                java.util.List<String> days = new java.util.ArrayList<>(workoutDays);
                java.util.Collections.reverse(days);
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int idx = 0;
                while (idx < days.size()) {
                    String day = days.get(idx);
                    String today = sdf.format(cal.getTime());
                    if (day.equals(today)) {
                        streak++;
                        cal.add(java.util.Calendar.DATE, -1);
                        idx++;
                    } else {
                        break;
                    }
                }
            }
            final int fTotalWorkouts = totalWorkouts;
            final int fTotalSets = totalSets;
            final int fTotalReps = totalReps;
            final double fTotalWeight = totalWeight;
            final double fBestLift = bestLift;
            final int fStreak = streak;
            requireActivity().runOnUiThread(() -> {
                statTotalWorkouts.setText("Total Workouts: " + fTotalWorkouts);
                TextView statTotalSets = getView().findViewById(R.id.statTotalSets);
                TextView statTotalReps = getView().findViewById(R.id.statTotalReps);
                TextView statTotalWeight = getView().findViewById(R.id.statTotalWeight);
                TextView statBestLift = getView().findViewById(R.id.statBestLift);
                TextView statWorkoutStreak = getView().findViewById(R.id.statWorkoutStreak);
                statTotalSets.setText("Total Sets: " + fTotalSets);
                statTotalReps.setText("Total Reps: " + fTotalReps);
                statTotalWeight.setText("Total Weight: " + String.format("%.1f kg", fTotalWeight));
                statBestLift.setText("Best Lift: " + String.format("%.1f kg", fBestLift));
                statWorkoutStreak.setText("Workout Streak: " + fStreak + (fStreak == 1 ? " day" : " days"));
            });
        }).start();
    }

    private boolean hasReadImagePermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestReadImagePermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_READ_IMAGE_PERMISSION);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_IMAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_IMAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (profilePicUri != null) {
                    profilePic.setImageURI(profilePicUri);
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied to read images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            if (hasReadImagePermission()) {
                profilePic.setImageURI(profilePicUri);
            } else {
                requestReadImagePermission();
            }
            Context context = requireContext();
            SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit().putString("profile_pic_uri", profilePicUri.toString()).apply();
        }
    }
}
