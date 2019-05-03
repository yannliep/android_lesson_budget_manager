package com.cours.budgetmanagertd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.cours.budgetmanagertd.adapters.CategoryAdapter;
import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;
import com.cours.budgetmanagertd.datas.HistoryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryEditActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private HistoryViewModel historyViewModel;

    private History history;

    private Spinner categorySpinner;
    private TextView nameTextView;
    private TextView valueTextView;
    private TextView dateTextView;

    private SharedPreferences sharedPreferences;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_edit);

        //On affiche le bouton de retour
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        history = new History();
        calendar = Calendar.getInstance();

        //On récupère les viewModels nécessaires
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        //On récupère les différents éléments visuels
        categorySpinner = findViewById(R.id.spinner);
        nameTextView = findViewById(R.id.name);
        valueTextView = findViewById(R.id.value);
        dateTextView = findViewById(R.id.date);

        dateTextView.setOnClickListener(onDateClickListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();

        //On récupère l'élément sélectionné si besoin
        if (intent.hasExtra(Intent.EXTRA_UID)) {
            int id = intent.getIntExtra(Intent.EXTRA_UID, 0);
            if (id > 0) {
                historyViewModel.getById(id).observe(this, new Observer<History>() {
                    @Override
                    public void onChanged(History historyFound) {
                        history = historyFound;
                        nameTextView.setText(history.getName());
                        //On convertit la valeur
                        valueTextView.setText(String.valueOf(history.getValue()));
                        String pattern = sharedPreferences.getString("date_format", "dd/MM/yyyy");
                        SimpleDateFormat format = new SimpleDateFormat(pattern);
                        Date date;
                        if (history.getDate() != null) {
                            date = history.getDate();
                        } else {
                            date = new Date();
                        }
                        calendar.setTime(date);
                        dateTextView.setText(format.format(date));
                        //On met à jour la liste des catégories maintenant afin de pouvoir sélectionner la bonne
                        updateCategories();
                    }
                });
            } else {
                updateCategories();
            }
        } else {
            updateCategories();
        }
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
                save();
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
                //Si il s'agit d'une réouverture on préselectionne la catégorie dans le spinner
                if (history.getCategoryId() > 0) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Category category = (Category) adapter.getItem(i);
                        if (category.getId() == history.getCategoryId()) {
                            categorySpinner.setSelection(i);
                        }
                    }
                }
            }
        });
    }

    public View.OnClickListener onDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(HistoryEditActivity.this,
                    onDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    public DatePickerDialog.OnDateSetListener onDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            String pattern = sharedPreferences.getString("date_format", "dd/MM/yyyy");
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateTextView.setText(format.format(calendar.getTime()));
        }
    };

    /**
     * Sauvegarde de l'entrée/sortie dans la BDD
     */
    private void save() {
        Category category = (Category) categorySpinner.getSelectedItem();
        history.setCategoryId(category.getId());
        history.setName(nameTextView.getText().toString());
        history.setDate(calendar.getTime());
        //On convertit la valeur
        history.setValue(Float.parseFloat(valueTextView.getText().toString()));
        historyViewModel.insertOrUpdate(history);
        finish();
    }
}
