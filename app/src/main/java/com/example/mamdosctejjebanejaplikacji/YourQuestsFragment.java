package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YourQuestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private QuestAdapter adapter;
    private List<Quest> questList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_quests, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewYourQuests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        questList = new ArrayList<>();
        questList.add(new Quest(UUID.randomUUID().toString(), "Wyciśnij 100kg na ławce płaskiej", "Wyciśnij 100kg na ławce płaskiej", null, null, 150));
        questList.add(new Quest(UUID.randomUUID().toString(), "Zrób 50 pompek", "Zrób 50 pompek", null, null, 80));
        questList.add(new Quest(UUID.randomUUID().toString(), "Przebiegnij 5 km", "Przebiegnij 5 km", null, null, 120));
        questList.add(new Quest(UUID.randomUUID().toString(), "Podciągnij się 10 razy", "Podciągnij się 10 razy", null, null, 100));
        questList.add(new Quest(UUID.randomUUID().toString(), "Zrób plank przez 3 minuty", "Zrób plank przez 3 minuty", null, null, 90));
        questList.add(new Quest(UUID.randomUUID().toString(), "Wykonaj 100 przysiadów", "Wykonaj 100 przysiadów", null, null, 110));
        adapter = new QuestAdapter(questList, quest -> {
            com.example.mamdosctejjebanejaplikacji.GameData.addXp(quest.xpReward);
            Toast.makeText(getContext(), "Zdobywasz XP: " + quest.xpReward + " za: " + quest.title, Toast.LENGTH_SHORT).show();
            questList.remove(quest);
            adapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}
