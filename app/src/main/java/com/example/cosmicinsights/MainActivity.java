package com.example.cosmicinsights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
BottomNavigationView bnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnView = findViewById(R.id.bnView);
        ColorStateList iconTintList = getResources().getColorStateList(R.color.bottom_nav_icon_tint);
        bnView.setItemIconTintList(iconTintList);

        bnView.setOnNavigationItemSelectedListener(item -> {
            // Handle item selection here
            return true;
        });

        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home) {
                    loadFrag(new HomeFragment(), false);

                } else if (id == R.id.nav_charts) {
                    loadFrag(new ChartsFragment(), false);

                } else if (id == R.id.nav_premium) {
                    loadFrag(new Add_OnsFragment(), false);

                } else if (id == R.id.nav_events) {
                    loadFrag(new ResourcesFragment(), false);

                } else { //Profile
                    loadFrag(new MoreFragment(), true);
                }
                return true;
            }
        });

        bnView.setSelectedItemId(R.id.nav_home);
    }
    public void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag)
            ft.add(R.id.frameLayout, fragment);
        else
            ft.add(R.id.frameLayout, fragment);
        ft.commit();
    }
}