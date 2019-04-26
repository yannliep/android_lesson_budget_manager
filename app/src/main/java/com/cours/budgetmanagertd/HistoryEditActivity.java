package com.cours.budgetmanagertd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.cours.budgetmanagertd.adapters.CategoryAdapter;
import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;

import java.util.List;

public class HistoryEditActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;

    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        categorySpinner = findViewById(R.id.spinner);

        updateCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void updateCategories() {
        categoryViewModel.getAll().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                CategoryAdapter adapter = new CategoryAdapter(getApplicationContext(), categories);
                categorySpinner.setAdapter(adapter);
            }
        });
    }
}
