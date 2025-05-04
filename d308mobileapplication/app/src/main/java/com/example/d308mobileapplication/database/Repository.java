package com.example.d308mobileapplication.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.d308mobileapplication.dao.ExcursionDAO;
import com.example.d308mobileapplication.dao.LogDAO;
import com.example.d308mobileapplication.dao.VacationDAO;
import com.example.d308mobileapplication.entities.Excursion;
import com.example.d308mobileapplication.entities.LogEntry;
import com.example.d308mobileapplication.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Repository {
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO;
    private LogDAO mLogDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private Vacation mVacation;
    private List<LogEntry> mAllLogs;

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db=VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO=db.excursionDAO();
        mVacationDAO= db.vacationDAO();
        mLogDAO = db.logDAO();
    }
    public List<Excursion>getAllExcursions(){
        databaseExecutor.execute(()->{
            mAllExcursions=mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }
    public List<Excursion> getAssociatedExcursions(int vacationID) {
        databaseExecutor.execute(()-> {
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }
    public void insert(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion){
        databaseExecutor.execute(()->{
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion){
        databaseExecutor.execute(()->{
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Vacation>getmAllVacations(){
        databaseExecutor.execute(()->{
            mAllVacations=mVacationDAO.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(()-> {
            mVacationDAO.insert(vacation);

            String ts = now();
            LogEntry log = new LogEntry(0, "Created vacation: \"" + vacation.getTitle() + "\"", ts);
            mLogDAO.insert(log);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.update(vacation);

            String ts = now();
            LogEntry log = new LogEntry(0, "Updated vacation: \"" + vacation.getTitle() + "\"", ts);
            mLogDAO.insert(log);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.delete(vacation);

            String ts = now();
            LogEntry log = new LogEntry(0, "Deleted vacation: \"" + vacation.getTitle() + "\"", ts);
            mLogDAO.insert(log);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Vacation> getVacationByIDLive(int vacationID) {
        return mVacationDAO.getVacationByIDLive(vacationID);
    }

    public LiveData<List<LogEntry>> getAllLogsLive() {
        return mLogDAO.getAllLogsLive();
    }

    public void insert(LogEntry logEntry) {
        databaseExecutor.execute(() -> mLogDAO.insert(logEntry));
    }

}

