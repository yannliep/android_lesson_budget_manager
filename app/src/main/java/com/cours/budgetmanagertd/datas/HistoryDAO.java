package com.cours.budgetmanagertd.datas;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HistoryDAO {
    @Query("SELECT * FROM history")
    public LiveData<List<History>> getAll();

    @Query("SELECT * FROM history WHERE id=:id")
    public LiveData<History> getById(int id);

    @Insert
    public void insert(History history);

    @Update
    public void update(History history);

    @Delete
    public void delete(History history);
}
