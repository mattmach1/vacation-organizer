package com.example.d308mobileapplication.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308mobileapplication.dao.ExcursionDAO;
import com.example.d308mobileapplication.dao.LogDAO;
import com.example.d308mobileapplication.dao.VacationDAO;
import com.example.d308mobileapplication.entities.Excursion;
import com.example.d308mobileapplication.entities.LogEntry;
import com.example.d308mobileapplication.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class, LogEntry.class}, version= 6, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    public abstract LogDAO logDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                    .fallbackToDestructiveMigration()
                            .build();
                }

            }
        }
        return INSTANCE;
    }
}
