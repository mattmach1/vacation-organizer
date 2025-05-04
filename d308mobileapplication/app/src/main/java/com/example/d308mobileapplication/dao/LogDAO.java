package com.example.d308mobileapplication.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import com.example.d308mobileapplication.entities.LogEntry;
import java.util.List;



@Dao
public interface LogDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LogEntry logEntry);

    @Query("SELECT * FROM logs ORDER BY logId ASC")
    LiveData<List<LogEntry>> getAllLogsLive();

}
