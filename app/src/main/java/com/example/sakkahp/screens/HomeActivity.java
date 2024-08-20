package com.example.sakkahp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.sakkahp.R;
import com.example.sakkahp.fragment.AccountFragment;
import com.example.sakkahp.fragment.FavouriteFragment;
import com.example.sakkahp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);






        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.menu_home) {
            fragment = new HomeFragment();
        } else if (item.getItemId() == R.id.menu_favourite) {
            fragment = new FavouriteFragment();
        } else if (item.getItemId() == R.id.menu_account) {
            fragment = new AccountFragment();
        }
        return loadFragment(fragment);

    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}