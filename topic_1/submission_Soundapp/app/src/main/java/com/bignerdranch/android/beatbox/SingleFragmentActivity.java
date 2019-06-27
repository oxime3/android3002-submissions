package com.bignerdranch.android.beatbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        switchFragment(createFragment());
    }


    public void switchFragment(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = frag;
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        } else {
          manager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null);
        }
    }
}
