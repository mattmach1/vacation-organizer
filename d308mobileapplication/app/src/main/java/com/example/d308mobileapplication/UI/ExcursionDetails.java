package com.example.d308mobileapplication.UI;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class ExcursionDetails extends AppCompatActivity {
    String name;
    String date;
    int vacationID;
    int excursionID;
    EditText editName;
    EditText editDate;
    Repository repository;

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




    }

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

        }
        return true;
    }

}
