package com.example.mamdosctejjebanejaplikacji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView settingsInfo = view.findViewById(R.id.settingsInfo);
        settingsInfo.setText("Wearable sync coming soon! (MVP demo)");
        Button resetButton = new Button(requireContext());
        resetButton.setText("Reset Progress");
        resetButton.setOnClickListener(v -> {
            GameData.reset();
            Toast.makeText(requireContext(), "Progress reset!", Toast.LENGTH_SHORT).show();
        });
        ((ViewGroup)settingsInfo.getParent()).addView(resetButton);
        return view;
    }
}
