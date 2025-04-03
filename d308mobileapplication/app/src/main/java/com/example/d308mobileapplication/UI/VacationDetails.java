package com.example.d308mobileapplication.UI;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.Excursion;
import com.example.d308mobileapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    String vacationStartDate;
    String vacationEndDate;
    int vacationID;
    EditText editName;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;
    ExcursionAdapter excursionAdapter;

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editName = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.starttext);
        editEndDate = findViewById(R.id.endtext);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        name = getIntent().getStringExtra("name");
        hotel = getIntent().getStringExtra("hotel");
        vacationStartDate = getIntent().getStringExtra("vacationStartDate");
        vacationEndDate = getIntent().getStringExtra("vacationEndDate");

        editName.setText(name);
        editHotel.setText(hotel);
        editStartDate.setText(vacationStartDate);
        editEndDate.setText(vacationEndDate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("vacationStartDate", editStartDate.getText().toString());
                intent.putExtra("vacationEndDate", editEndDate.getText().toString());
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        repository = new Repository(getApplication());
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadExcursions();
    }
    private void loadExcursions() {
            List<Excursion> filteredExcursions = new ArrayList<>();
            for (Excursion e : repository.getAllExcursions()) {
                if (e.getVacationID() == vacationID) {
                    filteredExcursions.add(e);
            }
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    private boolean validateVacationDateFormat(String vacationStartDate, String vacationEndDate) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setLenient(false);
        try {
            Date startDate = sdf.parse(vacationStartDate);
            Date endDate = sdf.parse(vacationEndDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean validateEndDateAfterStartDate(String vacationStartDate, String vacationEndDate) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setLenient(false);
        try {
            Date startDateFormatted = sdf.parse(vacationStartDate);
            Date endDateFormatted = sdf.parse(vacationEndDate);

            assert endDateFormatted != null;
            if (endDateFormatted.before(startDateFormatted)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {

            String vacationStartDateStr = editStartDate.getText().toString();
            String vacationEndDateStr = editEndDate.getText().toString();
            if (!validateVacationDateFormat(vacationStartDateStr, vacationEndDateStr)) {
                Toast.makeText(this, "Start and end dates must be in MM/dd/yy format", Toast.LENGTH_LONG).show();
                return true;
            }

            if(!validateEndDateAfterStartDate(vacationStartDateStr, vacationEndDateStr)) {
                Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_LONG).show();
                return true;
            }

            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else
                    vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
            } else {
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacation);
                this.finish();
            }
            return true;
        }
        if (item.getItemId() == R.id.vacationdelete) {
            List<Excursion> associatedExcursions = repository.getAssociatedExcursions(vacationID);
            if (associatedExcursions != null && !associatedExcursions.isEmpty()) {
                Toast.makeText(this, "Cannot delete vacation because there are excursions associated with it.", Toast.LENGTH_LONG).show();
            } else {
                Vacation vacation = new Vacation(vacationID,
                        editName.getText().toString(),
                        editHotel.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString());
                repository.delete(vacation);
                Toast.makeText(this, "Vacation deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
             return true;
        }
        if (item.getItemId() == R.id.vacationnotification) {
            String startDateFromScreen = editStartDate.getText().toString();
            String endDateFromScreen = editEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(startDateFromScreen);
                endDate = sdf.parse(endDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long startTrigger = startDate.getTime();
            Long endTrigger = endDate.getTime();
            Intent startIntent = new Intent(VacationDetails.this, MyReceiver.class);
            startIntent.putExtra("key", "Your " + name + " vacation starts today!");
            PendingIntent startSender=PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, startIntent, FLAG_IMMUTABLE);
            AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManagerStart.set(AlarmManager.RTC_WAKEUP, startTrigger, startSender);

            Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
            endIntent.putExtra("key", "Your " + name + " vacation ends today!");
            PendingIntent endSender=PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, endIntent, FLAG_IMMUTABLE);
            AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManagerEnd.set(AlarmManager.RTC_WAKEUP, endTrigger, endSender);

            return true;
        }

        if (item.getItemId() == R.id.vacationshare) {

            StringBuilder shareVacation = new StringBuilder();
            shareVacation.append("Vacation name: ").append(editName.getText().toString()).append("\n")
                    .append("Start Date: ").append(editStartDate.getText().toString()).append("\n")
                    .append("End date: ").append(editEndDate.getText().toString()).append("\n")
                    .append("Hotel: ").append(editHotel.getText().toString()).append("\n\n")
                    .append("Planned Excursions: ").append("\n");

            List<Excursion> associatedExcursions = repository.getAssociatedExcursions(vacationID);
            if (associatedExcursions != null && !associatedExcursions.isEmpty()) {
                for (Excursion e : associatedExcursions) {
                    shareVacation.append("- ").append(e.getExcursionTitle())
                            .append(" on ").append(e.getExcursionDate()).append("\n");
                }
            } else {
                shareVacation.append("None");
            }

            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, shareVacation.toString());
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Information");
            sentIntent.setType("text/plain");
            Intent shareIntent=Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
    }

        return super.onOptionsItemSelected(item);
    }

}