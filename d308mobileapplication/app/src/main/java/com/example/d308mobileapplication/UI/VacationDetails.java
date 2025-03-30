package com.example.d308mobileapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.Excursion;
import com.example.d308mobileapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    String startDate;
    String endDate;
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
        startDate = getIntent().getStringExtra("start date");
        endDate = getIntent().getStringExtra("end date");

        editName.setText(name);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
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
        }
        return true;
    }
}