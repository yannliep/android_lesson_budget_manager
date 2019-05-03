package com.cours.budgetmanagertd.datas;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Category.class, History.class}, version = 3, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract CategoryDAO categoryDAO();
    public abstract HistoryDAO historyDAO();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE history ADD COLUMN date INTEGER");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE history_temp(`id` INTEGER " +
                    "PRIMARY KEY AUTOINCREMENT NOT NULL, `categoryId` " +
                    "INTEGER NOT NULL, `name` TEXT, `value` REAL " +
                    "NOT NULL, `date` INTEGER, FOREIGN KEY(`categoryId`) " +
                    "REFERENCES `Category`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            database.execSQL("INSERT INTO history_temp SELECT * FROM history");
            database.execSQL("DROP TABLE history");
            database.execSQL("ALTER TABLE history_temp RENAME TO history");
        }
    };

    static public Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    Database.class, "database").addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build();
        }
        return instance;
    }
}
