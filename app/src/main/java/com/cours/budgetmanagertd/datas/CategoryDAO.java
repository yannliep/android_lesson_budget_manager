package com.cours.budgetmanagertd.datas;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM category WHERE income=1")
    LiveData<List<Category>> getAllIncomesCategories();

    @Query("SELECT * FROM category WHERE id=:id")
    LiveData<Category> getById(int id);

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
