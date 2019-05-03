package com.cours.budgetmanagertd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "BudgetManager";


    private int yearSelected;
    private int monthSelected;

    private HistoryFragment historyFragment;

    private Calendar calendar;

    public MainActivity() {
        super();

        calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (historyFragment != null) {
            getMenuInflater().inflate(R.menu.main_action_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {

            MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                    .getInstance(monthSelected, yearSelected);

            dialogFragment.show(getSupportFragmentManager(), null);
            dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int year, int month) {
                    yearSelected = year;
                    monthSelected = month;
                    if (historyFragment != null) {
                        historyFragment.updateList(yearSelected, monthSelected);
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        historyFragment = new HistoryFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, historyFragment);
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                invalidateOptionsMenu();
                historyFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.history:
                        historyFragment = new HistoryFragment();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_container, historyFragment)
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
                        fragmentManager.
                                beginTransaction().
                                replace(R.id.fragment_container, new SettingsFragment()).
                                addToBackStack("settings").
                                commit();
                        return true;
                }

                return false;
            }
        });


    }


}
