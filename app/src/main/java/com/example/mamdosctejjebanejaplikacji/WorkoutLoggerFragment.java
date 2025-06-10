package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mamdosctejjebanejaplikacji.data.AppDatabase;
import com.example.mamdosctejjebanejaplikacji.data.DatabaseProvider;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryDao;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryEntity;
import com.example.mamdosctejjebanejaplikacji.WorkoutLogAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class WorkoutLoggerFragment extends Fragment {
    private WorkoutLogAdapter workoutLogAdapter;
    private RecyclerView workoutLogRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_logger, container, false);
        workoutLogRecyclerView = view.findViewById(R.id.workoutLogRecyclerView);
        workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutLogAdapter = new WorkoutLogAdapter();
        workoutLogRecyclerView.setAdapter(workoutLogAdapter);
        loadWorkoutLog();

        MaterialButton logBtn = view.findViewById(R.id.btnLogWorkout);
        com.google.android.material.textfield.MaterialAutoCompleteTextView nameInput = view.findViewById(R.id.inputWorkoutName);
        TextInputEditText setsInput = view.findViewById(R.id.inputSets);
        TextInputEditText repsInput = view.findViewById(R.id.inputReps);
        TextInputEditText weightInput = view.findViewById(R.id.inputWeight);

        // Provide workout name suggestions
        String[] workoutNames = new String[] {"Bench Press", "Squats", "Deadlift", "Push-ups", "Sit-ups", "Pull-ups", "Plank", "Burpees", "Lunges", "Overhead Press", "Running", "Cycling", "Rowing", "Jump Rope", "Dips", "Crunches"};
        ArrayAdapter<String> workoutAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, workoutNames);
        nameInput.setAdapter(workoutAdapter);
        nameInput.setThreshold(1);

        logBtn.setOnClickListener(v -> {
            String type = nameInput.getText() != null ? nameInput.getText().toString() : "";
            String setsJson = setsInput.getText() != null ? setsInput.getText().toString() : "";
            String date = String.valueOf(System.currentTimeMillis());
            if (type.isEmpty() || setsJson.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            WorkoutEntryEntity entry = new WorkoutEntryEntity(0, date, type, setsJson);
            new Thread(() -> {
                AppDatabase db = DatabaseProvider.getDatabase(requireContext());
                db.workoutEntryDao().insert(entry);
                GameData.addXp(100); // Add XP and level up after logging workout
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Workout logged!", Toast.LENGTH_SHORT).show();
                    nameInput.setText("");
                    setsInput.setText("");
                    repsInput.setText("");
                    weightInput.setText("");
                    loadWorkoutLog();
                });
            }).start();
        });
        return view;
    }

    private void loadWorkoutLog() {
        new Thread(() -> {
            AppDatabase db = DatabaseProvider.getDatabase(requireContext());
            List<WorkoutEntryEntity> entries = db.workoutEntryDao().getAll();
            requireActivity().runOnUiThread(() -> workoutLogAdapter.setEntries(entries));
        }).start();
    }
}
