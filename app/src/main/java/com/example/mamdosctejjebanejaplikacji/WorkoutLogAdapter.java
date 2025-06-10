package com.example.mamdosctejjebanejaplikacji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mamdosctejjebanejaplikacji.data.WorkoutEntryEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.ViewHolder> {
    private List<WorkoutEntryEntity> entries = new ArrayList<>();

    public void setEntries(List<WorkoutEntryEntity> entries) {
        this.entries = entries != null ? entries : new ArrayList<>();
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
        // Format the date string
        String formattedDate = entry.date;
        try {
            long timestamp = Long.parseLong(entry.date);
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            formattedDate = sdf.format(date);
        } catch (Exception ignored) {}
        holder.date.setText(formattedDate);
        holder.type.setText(entry.type);
        holder.sets.setText(entry.setsJson);
        holder.weight.setText(""); // Not used in schema, leave blank
    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, sets, type, weight;
        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.logDate);
            sets = itemView.findViewById(R.id.logSets);
            type = itemView.findViewById(R.id.logType);
            weight = itemView.findViewById(R.id.logWeight);
        }
    }
}
