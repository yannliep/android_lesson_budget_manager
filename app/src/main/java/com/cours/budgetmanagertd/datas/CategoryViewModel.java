package com.cours.budgetmanagertd.datas;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryDAO categoryDAO;


    public CategoryViewModel(@NonNull Application application) {
        super(application);

        categoryDAO = Database.getInstance(application.getApplicationContext())
                .categoryDAO();
    }

    public LiveData<List<Category>> getAll() {
        return categoryDAO.getAll();
    }

    public LiveData<Category> getById(int id) {
        return categoryDAO.getById(id);
    }

    public void insertOrUpdateCategory(Category category) {
        new InsertOrUpdateTask(categoryDAO).execute(category);
    }


    private static class InsertOrUpdateTask extends
            AsyncTask<Category, Void, Void> {
        private CategoryDAO categoryDAO;

        public InsertOrUpdateTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            Category category = categories[0];
            if (category.getId() > 0) {
                categoryDAO.update(category);
            } else {
                categoryDAO.insert(category);
            }
            return null;
        }
    }
}
