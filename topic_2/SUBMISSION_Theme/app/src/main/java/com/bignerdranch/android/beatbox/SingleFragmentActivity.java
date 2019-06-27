package com.bignerdranch.android.beatbox;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = userPref.getString("theme_select", "Default theme");

        switch (theme) {
            case "Light theme":
                setTheme(R.style.CustomLightAppTheme_1_AppCompat_NoActionBar);
                Log.i("Theme: ", "Light theme 1");
                break;
            case "Dark theme":
                setTheme(R.style.CustomDarkAppTheme_1_AppCompat_NoActionBar);
                Log.i("Theme: ", "Light theme 2");
                break;
            default:
                setTheme(R.style.AppTheme);
                Log.i("Theme: ", "Default theme selected");
                break;
        }
        setContentView(getLayoutResId());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    public void changeTheme(int theme) {
        setTheme(theme);
        recreate();
    }

    public void changeTheme(){
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beatbox_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public void launchSettingsFragment(MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("settings_class", getClass());
        settingsFragment.setArguments(bundle);

        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.fragment_container, settingsFragment)
                    .addToBackStack("SettingsFragment")
                    .commit();
        }
    }
}
