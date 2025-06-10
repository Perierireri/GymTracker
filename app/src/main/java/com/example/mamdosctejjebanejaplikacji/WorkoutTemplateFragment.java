package com.example.mamdosctejjebanejaplikacji;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mamdosctejjebanejaplikacji.data.AppDatabase;
import com.example.mamdosctejjebanejaplikacji.data.DatabaseProvider;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryDao;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryEntity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutTemplateFragment extends Fragment {
    private final String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private final Map<String, List<String>> template = new HashMap<>(); // day -> exercises
    private LinearLayout dayTabs;
    private LinearLayout exerciseList;
    private MaterialButton addExerciseBtn;
    private MaterialButton saveBtn;
    private String selectedDay = "";
    private WorkoutEntryDao workoutEntryDao;
    private Map<String, List<String>> loggedWorkoutsByDay = new HashMap<>();
    private int totalWeightLifted = 0;
    private int totalDistanceRun = 0;
    private RecyclerView workoutLogRecyclerView;
    private WorkoutLogAdapter workoutLogAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_template, container, false);
        MaterialButton prevDayBtn = view.findViewById(R.id.prevDayBtn);
        MaterialButton nextDayBtn = view.findViewById(R.id.nextDayBtn);
        TextView currentDayLabel = view.findViewById(R.id.currentDayLabel);
        dayTabs = view.findViewById(R.id.daySelectorRow);
        exerciseList = view.findViewById(R.id.exerciseList);
        addExerciseBtn = view.findViewById(R.id.addExerciseBtn);
        saveBtn = view.findViewById(R.id.saveTemplateBtn);
        workoutLogRecyclerView = view.findViewById(R.id.workoutLogRecyclerView);
        workoutLogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutLogAdapter = new WorkoutLogAdapter();
        workoutLogRecyclerView.setAdapter(workoutLogAdapter);
        loadWorkoutLog();
        for (String day : daysOfWeek) template.put(day, new ArrayList<>());
        int todayIdx = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
        selectedDay = (todayIdx >= 0 && todayIdx < daysOfWeek.length) ? daysOfWeek[todayIdx] : "Mon";
        // TODO: Initialize workoutEntryDao
        // TODO: Implement timers, logger, DB logic
        updateDayLabel(currentDayLabel);
        prevDayBtn.setOnClickListener(v -> {
            goToPrevDay(currentDayLabel);
            loadLoggedWorkoutsForDay(selectedDay);
        });
        nextDayBtn.setOnClickListener(v -> {
            goToNextDay(currentDayLabel);
            loadLoggedWorkoutsForDay(selectedDay);
        });
        prevDayBtn.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_left_gray));
        nextDayBtn.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_right_gray));
        updateExerciseList();
        addExerciseBtn.setOnClickListener(v -> showAddExerciseDialog());
        saveBtn.setOnClickListener(v -> {
            saveTemplate();
            saveTemplateToDb();
        });
        return view;
    }

    private void updateDayLabel(TextView currentDayLabel) {
        switch (selectedDay) {
            case "Mon": currentDayLabel.setText("Monday"); break;
            case "Tue": currentDayLabel.setText("Tuesday"); break;
            case "Wed": currentDayLabel.setText("Wednesday"); break;
            case "Thu": currentDayLabel.setText("Thursday"); break;
            case "Fri": currentDayLabel.setText("Friday"); break;
            case "Sat": currentDayLabel.setText("Saturday"); break;
            case "Sun": currentDayLabel.setText("Sunday"); break;
            default: currentDayLabel.setText(selectedDay);
        }
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
        boolean isToday = daysOfWeek[today >= 0 && today < daysOfWeek.length ? today : 0].equals(selectedDay);
        int color = isToday ? ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
                : ContextCompat.getColor(requireContext(), R.color.electric_blue);
        currentDayLabel.setTextColor(color);
    }

    private void goToPrevDay(TextView currentDayLabel) {
        int idx = java.util.Arrays.asList(daysOfWeek).indexOf(selectedDay);
        selectedDay = daysOfWeek[(idx - 1 + daysOfWeek.length) % daysOfWeek.length];
        updateDayLabel(currentDayLabel);
        updateExerciseList();
    }

    private void goToNextDay(TextView currentDayLabel) {
        int idx = java.util.Arrays.asList(daysOfWeek).indexOf(selectedDay);
        selectedDay = daysOfWeek[(idx + 1) % daysOfWeek.length];
        updateDayLabel(currentDayLabel);
        updateExerciseList();
    }

    private void updateExerciseList() {
        exerciseList.removeAllViews();
        List<String> exercises = template.get(selectedDay);
        if (exercises == null) return;
        for (int idx = 0; idx < exercises.size(); idx++) {
            String ex = exercises.get(idx);
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            TextView textView = new TextView(requireContext());
            textView.setText(ex);
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_on_dark));
            textView.setTextSize(16f);
            MaterialButton delBtn = new MaterialButton(requireContext());
            delBtn.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_x_gray));
            delBtn.setIconTint(ContextCompat.getColorStateList(requireContext(), R.color.text_secondary));
            delBtn.setText("");
            delBtn.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            final int removeIdx = idx;
            delBtn.setOnClickListener(v -> {
                exercises.remove(removeIdx);
                updateExerciseList();
            });
            row.addView(textView);
            row.addView(delBtn);
            exerciseList.addView(row);
        }
        // TODO: Add logged workouts and totals
    }

    private void showAddExerciseDialog() {
        EditText input = new EditText(requireContext());
        new AlertDialog.Builder(requireContext())
                .setTitle("Add Exercise")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String ex = input.getText().toString().trim();
                    if (!ex.isEmpty()) {
                        List<String> dayList = template.get(selectedDay);
                        if (dayList != null) {
                            dayList.add(ex);
                            updateExerciseList();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveTemplate() {
        Context context = requireContext();
        String templateName = "WeeklyPlan_" + System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (String day : daysOfWeek) {
            List<String> dayList = template.get(day);
            sb.append(day).append(":");
            if (dayList != null) {
                sb.append(android.text.TextUtils.join(",", dayList));
            }
            sb.append(";");
        }
        context.getSharedPreferences("workout_templates", 0)
                .edit().putString(templateName, sb.toString()).apply();
        Toast.makeText(context, "Template saved!", Toast.LENGTH_SHORT).show();
    }

    private void saveTemplateToDb() {
        // TODO: Implement DB save logic
    }

    private void loadLoggedWorkoutsForDay(String day) {
        // TODO: Implement loading logged workouts from DB
    }

    private void loadWorkoutLog() {
        new Thread(() -> {
            AppDatabase db = DatabaseProvider.getDatabase(requireContext());
            WorkoutEntryDao dao = db.workoutEntryDao();
            List<WorkoutEntryEntity> entries = dao.getAll();
            requireActivity().runOnUiThread(() -> workoutLogAdapter.setEntries(entries));
        }).start();
    }

    // Adapter for workout log
    private static class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.ViewHolder> {
        private List<WorkoutEntryEntity> entries;

        public void setEntries(List<WorkoutEntryEntity> entries) {
            this.entries = entries;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_log_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WorkoutEntryEntity entry = entries.get(position);
            holder.date.setText(entry.date);
            holder.type.setText(entry.type);
            holder.sets.setText(entry.setsJson);
        }

        @Override
        public int getItemCount() {
            return entries == null ? 0 : entries.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView date, type, sets;
            ViewHolder(View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.logDate);
                type = itemView.findViewById(R.id.logType);
                sets = itemView.findViewById(R.id.logSets);
            }
        }
    }
}
