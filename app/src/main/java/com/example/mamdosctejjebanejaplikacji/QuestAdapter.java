package com.example.mamdosctejjebanejaplikacji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.mamdosctejjebanejaplikacji.Quest;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {
    public interface OnQuestClickListener {
        void onQuestClick(Quest quest);
    }
    private List<Quest> questList;
    private OnQuestClickListener listener;

    public QuestAdapter(List<Quest> questList, OnQuestClickListener listener) {
        this.questList = questList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest, parent, false);
        return new QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        Quest quest = questList.get(position);
        holder.titleView.setText(quest.title);
        holder.xpView.setText("XP: " + quest.xpReward);
        holder.itemView.setOnClickListener(v -> listener.onQuestClick(quest));
    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    static class QuestViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView xpView;
        QuestViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.questTitle);
            xpView = itemView.findViewById(R.id.questXp);
        }
    }
}
