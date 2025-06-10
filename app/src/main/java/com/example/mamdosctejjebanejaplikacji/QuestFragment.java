package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView questProgress;
    private QuestAdapter questAdapter;
    private QuestManager questManager = QuestManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests, container, false);
        recyclerView = view.findViewById(R.id.questsRecyclerView);
        questProgress = view.findViewById(R.id.questProgress);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        questAdapter = new QuestAdapter();
        recyclerView.setAdapter(questAdapter);
        // Generate quests when fragment is created
        questManager.generateQuests(GameData.user);
        questAdapter.notifyDataSetChanged();
        updateQuestProgress();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check for completed quests and update UI
        questAdapter.notifyDataSetChanged();
        updateQuestProgress();
    }

    private void updateQuestProgress() {
        List<Quest> activeQuests = questManager.getActiveQuests();
        List<Quest> completedQuests = questManager.getCompletedQuests();
        questProgress.setText("Progress: " + completedQuests.size() + "/" + activeQuests.size());
    }

    class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {
        @NonNull
        @Override
        public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_item, parent, false);
            return new QuestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
            Quest quest = questManager.getActiveQuests().get(position);
            holder.bind(quest);
            // Dodaj kliknięcie na quest - po kliknięciu dodaje exp i oznacza quest jako ukończony
            holder.itemView.setOnClickListener(v -> {
                if (!quest.isCompleted) {
                    quest.isCompleted = true;
                    GameData.addXp(quest.xpReward);
                    // Możesz dodać tu animację, toast lub dźwięk
                    notifyItemChanged(position);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).runOnUiThread(() -> {
                            if (questProgress != null) updateQuestProgress();
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return questManager.getActiveQuests().size();
        }

        class QuestViewHolder extends RecyclerView.ViewHolder {
            private TextView title, description, xpReward, progress;

            QuestViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.questTitle);
                description = itemView.findViewById(R.id.questDescription);
                xpReward = itemView.findViewById(R.id.xpReward);
                progress = itemView.findViewById(R.id.questProgress);
            }

            void bind(Quest quest) {
                title.setText(quest.title);
                description.setText(quest.description);
                xpReward.setText("+" + quest.xpReward + " XP");
                // Update progress based on quest type
                switch (quest.type) {
                    case DAILY:
                        progress.setText(quest.isCompleted ? "Completed" : "In progress");
                        break;
                    case WEEKLY:
                        progress.setText(quest.isCompleted ? "Completed" : "Weekly");
                        break;
                    case BOSS:
                        progress.setText(quest.isCompleted ? "Victory!" : "Boss Battle");
                        break;
                }
            }
        }
    }
}
