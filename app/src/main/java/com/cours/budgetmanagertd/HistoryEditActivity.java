package com.cours.budgetmanagertd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.cours.budgetmanagertd.adapters.CategoryAdapter;
import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;
import com.cours.budgetmanagertd.datas.HistoryViewModel;

import java.util.List;

public class HistoryEditActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private HistoryViewModel historyViewModel;

    private History history;

    private Spinner categorySpinner;
    private TextView nameTextView;
    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        history = new History();

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        categorySpinner = findViewById(R.id.spinner);
        nameTextView = findViewById(R.id.name);
        valueTextView = findViewById(R.id.value);

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
                    for (int i=0;i<adapter.getCount();i++){
                        Category category = (Category) adapter.getItem(i);
                        if (category.getId() == history.getCategoryId()){
                            categorySpinner.setSelection(i);
                        }
                    }
                }
            }
        });
    }

    /**
     * Sauvegarde de l'entrée/sortie dans la BDD
     */
    private void save() {
        Category category = (Category) categorySpinner.getSelectedItem();
        history.setCategoryId(category.getId());
        history.setName(nameTextView.getText().toString());
        //On convertit la valeur
        history.setValue(Float.parseFloat(valueTextView.getText().toString()));
        historyViewModel.insertOrUpdate(history);
        finish();
    }
}
