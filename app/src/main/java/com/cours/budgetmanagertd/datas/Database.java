package com.cours.budgetmanagertd.datas;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Category.class, History.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract CategoryDAO categoryDAO();
    public abstract HistoryDAO historyDAO();

    static public Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    Database.class, "database").build();
        }
        return instance;
    }
}
