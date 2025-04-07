package com.example.d308mobileapplication.UI;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.Excursion;
import com.example.d308mobileapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    String name;
    String date;
    int vacationID;
    int excursionID;
    EditText editName;
    EditText editDate;
    Repository repository;
    TextView editNotifyDate;
    DatePickerDialog.OnDateSetListener startNotifyDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    String vacationStartDate;
    String vacationEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        repository = new Repository((getApplication()));
        editName = findViewById(R.id.titletext);
        editDate = findViewById(R.id.datetext);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        name = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");
        excursionID = getIntent().getIntExtra("excursionID", -1);
        editName.setText(name);
        editDate.setText(date);

        repository.getVacationByIDLive(vacationID).observe(this, new Observer<Vacation>() {
            @Override
            public void onChanged(Vacation vacation) {
                if (vacation != null) {
                    vacationStartDate = vacation.getStartDate();
                    vacationEndDate = vacation.getEndDate();

                }
            }
        });

    }
    private boolean validateExcursionDateFormat(String date) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setLenient(false);
        try {
            Date excursionDate = sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    private boolean validateExcursionDate(String date, String vacationStartDate, String vacationEndDate) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setLenient(false);
        try {
            Date excursionDate = sdf.parse(date);
            Date vacationStartDateFormatted = sdf.parse(vacationStartDate);
            Date vacationEndDateFormatted = sdf.parse(vacationEndDate);

            assert excursionDate != null;
            if (excursionDate.before(vacationStartDateFormatted) || excursionDate.after(vacationEndDateFormatted)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


//    private void updateLabelStart() {
//        String myFormat = "MM/dd/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        editNotifyDate.setText(sdf.format(myCalendarStart.getTime()));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.excursionsave) {


            String excursionDateStr = editDate.getText().toString();
            if (!validateExcursionDateFormat(excursionDateStr)) {
                Toast.makeText(this, "Excursion date must be in MM/dd/yy format", Toast.LENGTH_LONG).show();
                return true;
            }


            if(vacationStartDate != null && vacationEndDate != null) {
                if (!validateExcursionDate(excursionDateStr,vacationStartDate,vacationEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation start and end dates", Toast.LENGTH_LONG).show();
                    return true;
                }
            } else {
                Toast.makeText(this, "Vacation dates are not provided for validation. Please first enter vacation start and end dates.", Toast.LENGTH_SHORT).show();
                return true;
            }

            Excursion excursion;
            if (excursionID == -1) {
                if (repository.getAllExcursions().size() == 0) excursionID = 1;
                else
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                repository.insert(excursion);
                this.finish();
            } else {
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                repository.update(excursion);
                this.finish();
            }
            return true;
        }
        if (item.getItemId() == R.id.excursiondelete) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Excursion")
                    .setMessage("Are you sure you want to delete this excursion?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        Excursion excursion = new Excursion(
                                excursionID,
                                editName.getText().toString(),
                                editDate.getText().toString(),
                                vacationID
                        );
                        repository.delete(excursion);
                        Toast.makeText(ExcursionDetails.this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }

        if (item.getItemId() == R.id.excursionnotification) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", "Your " + name + " excursion is today!");
            PendingIntent sender=PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
