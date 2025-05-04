package com.example.d308mobileapplication.UI;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.LogEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogReportActivity extends AppCompatActivity {
    private RecyclerView rv;
    private LogAdapter adapter;
    private Repository repo;
    private TextView tvTitle, tvTimestamp;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_log_report);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repo = new Repository(getApplication());
        tvTitle = findViewById(R.id.tvReportTitle);
        tvTimestamp = findViewById(R.id.tvTimeStamp);
        searchView = findViewById(R.id.searchView);
        rv = findViewById(R.id.rvLogs);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogAdapter();
        rv.setAdapter(adapter);

         // create a test log entry
       /* String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        LogEntry test = new LogEntry(0, "Opened LogReportActivity", now);

        // insert on a background thread
        new Thread(() -> repo.insert(test)).start(); */


        LiveData<List<LogEntry>> allLogs = repo.getAllLogsLive();
        allLogs.observe(this, logs -> {
            adapter.setLogs(logs);
    });


        tvTitle.setText("Application Log Report");
        tvTimestamp.setText("Generated: " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                adapter.getFilter().filter(q);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
