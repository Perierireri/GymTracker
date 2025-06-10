package com.example.mamdosctejjebanejaplikacji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONObject;
import android.widget.Toast;
import android.util.Log;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentTransaction;

public class    MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            ((View)v).setPadding(0, 0, 0, 0);
            return insets;
        });

        Context context = this;
        String name = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_name", null);
        String password = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_password", null);
        
        if (name == null || password == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        try {
            loadProgress(name);
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            viewPager.setAdapter(new MainPagerAdapter(this));
            
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (position >= 0 && position < bottomNav.getMenu().size()) {
                        bottomNav.getMenu().getItem(position).setChecked(true);
                    }
                }
            });
            
            bottomNav.setOnItemSelectedListener(item -> {
                int pageIndex = 0;
                if (item.getItemId() == R.id.nav_character) {
                    pageIndex = 0;
                } else if (item.getItemId() == R.id.nav_battle) {
                    pageIndex = 1;
                } else if (item.getItemId() == R.id.nav_workout_logger) {
                    pageIndex = 2;
                } else if (item.getItemId() == R.id.navigation_your_quests) {
                    pageIndex = 3;
                }
                viewPager.setCurrentItem(pageIndex, true);
                return true;
            });
            
            viewPager.setUserInputEnabled(true);
            viewPager.setOffscreenPageLimit(4);
            viewPager.setSaveEnabled(false);
            viewPager.setPageTransformer(null);
            viewPager.setNestedScrollingEnabled(false);
            viewPager.setFocusable(true);
            viewPager.setFocusableInTouchMode(true);
            viewPager.setClickable(true);
            viewPager.setLongClickable(true);
            viewPager.setOnTouchListener((v, event) -> false);
            viewPager.setNestedScrollingEnabled(false);
            viewPager.setSaveEnabled(false);

            ImageButton settingsButton = findViewById(R.id.settingsButton);
            settingsButton.setOnClickListener(v -> {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
            });

        } catch (Exception e) {
            Log.e("MainActivity", "error: ", e);
            Toast.makeText(this, "MainActivity error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Animate stats bar on main screen entry
        View statsBar = findViewById(R.id.statsBar);
        if (statsBar != null) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            statsBar.startAnimation(anim);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProgress();
    }

    private void saveProgress() {
        Context context = this;
        String name = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_name", null);
        if (name == null) return;
        
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("name", GameData.user.getName());
            userJson.put("class", GameData.user.getCharacterClass().name());
            userJson.put("strength", GameData.user.getStrength());
            // ... continue with other properties
        } catch (Exception e) {
            Log.e("MainActivity", "Error saving progress: ", e);
        }
    }

    private void loadProgress(String name) {
        // Implementation needed
    }
}

class MainPagerAdapter extends FragmentStateAdapter {
    public MainPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CharacterFragment();
            case 1:
                return new BattleFragment();
            case 2:
                return new WorkoutLoggerFragment();
            case 3:
                return new YourQuestsFragment();
            default:
                return new CharacterFragment();
        }
    }
}
