package com.cours.budgetmanagertd.datas;

import java.util.Date;
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

    @Query("SELECT * FROM history WHERE date >= :startDate AND date <= :endDate")
    public LiveData<List<History>> getAllBetweenDate(Date startDate, Date endDate);

    @Query("SELECT SUM(value) FROM history, category WHERE " +
            "history.categoryId = category.id AND date >= :startDate AND date <= :endDate " +
            "AND income=1")
    public LiveData<Float> getSumIncomeBetweenDate(Date startDate, Date endDate);

    @Query("SELECT SUM(value) FROM history, category WHERE " +
            "history.categoryId = category.id AND date >= :startDate AND date <= :endDate " +
            "AND income=0")
    public LiveData<Float> getSumOutcomeBetweenDate(Date startDate, Date endDate);

    @Query("SELECT count(*) FROM history WHERE categoryId=:cat_id")
    public LiveData<Float> getCountByCategory(int cat_id);

    @Query("SELECT * FROM history WHERE id=:id")
    public LiveData<History> getById(int id);

    @Insert
    public void insert(History history);

    @Update
    public void update(History history);

    @Delete
    public void delete(History history);
}
