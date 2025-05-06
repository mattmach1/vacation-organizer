package com.example.d308mobileapplication.UI;

import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    private Repository repo;
    private TableLayout table;
    private TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        repo         = new Repository(getApplication());
        table        = findViewById(R.id.tableLayout);
        tvTimestamp  = findViewById(R.id.tvReportTimestamp);

        // 1) Header row
        String[] headers = { "ID", "Title", "Hotel", "Start Date", "End Date" };
        TableRow headerRow = new TableRow(this);
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(8, 8, 8, 8);
            headerRow.addView(tv);
        }
        table.addView(headerRow);

        // 2) Data rows
        List<Vacation> vacations = repo.getmAllVacations();
        for (Vacation v : vacations) {
            TableRow row = new TableRow(this);

            TextView c1 = makeCell(String.valueOf(v.getVacationID()));
            TextView c2 = makeCell(v.getTitle());
            TextView c3 = makeCell(v.getHotel());
            TextView c4 = makeCell(v.getStartDate());
            TextView c5 = makeCell(v.getEndDate());

            row.addView(c1);
            row.addView(c2);
            row.addView(c3);
            row.addView(c4);
            row.addView(c5);

            table.addView(row);
        }

        // 3) Timestamp
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        tvTimestamp.setText("Generated: " + now);
    }

    private TextView makeCell(String txt) {
        TextView tv = new TextView(this);
        tv.setText(txt);
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }
}
