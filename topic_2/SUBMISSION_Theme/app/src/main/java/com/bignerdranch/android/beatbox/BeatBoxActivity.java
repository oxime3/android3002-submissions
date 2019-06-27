package com.bignerdranch.android.beatbox;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class BeatBoxActivity extends SingleFragmentActivity {
    public final String TAG = "BeatBoxActivity";

    @Override
    protected Fragment createFragment() { return BeatBoxFragment.newInstance(); }

//    @Override
//    public void changeTheme(int theme) {
//        setTheme(theme);
//        recreate();
//       super.changeTheme(theme);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        String intent_string = intent.getStringExtra("theme_change");

//        int style = intent.getIntExtra("style", 0);
//        System.out.println(style);
//        changeTheme(style);
        changeTheme();
//        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }
}
