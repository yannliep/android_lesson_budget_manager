package com.cours.budgetmanagertd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;

public class CategoryEditActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private Category category;
    private TextView nameTextView;
    private Switch incomeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //On récupère notre ViewModel
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        //On crée un objet catégorie
        category = new Category();

        //On récupère nos éléments visuels
        nameTextView = findViewById(R.id.name);
        incomeSwitch = findViewById(R.id.income);
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
                saveCategory();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void saveCategory() {
        //On récupère le nom et le type de catégorie et on les injecte dans le modèle
        category.setName(nameTextView.getText().toString());
        category.setIncome(incomeSwitch.isChecked());
        //On injecte le modèle dans la base de données
        categoryViewModel.insertOrUpdateCategory(category);
        //On ferme l'activité
        finish();
    }
}
