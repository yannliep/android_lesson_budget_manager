package com.cours.budgetmanagertd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity {

    public static final String TAG = "BudgetManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new HistoryFragment());
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.history:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_container, new HistoryFragment())
                                .addToBackStack("history")
                                .commit();
                        return true;
                    case R.id.categories:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_container, new CategoriesFragment())
                                .addToBackStack("categories")
                                .commit();
                        return true;
                    case R.id.settings:
                        Log.d(TAG, "onNavigationItemSelected: settings");
                        return true;
                }

                return false;
            }
        });


    }


}
