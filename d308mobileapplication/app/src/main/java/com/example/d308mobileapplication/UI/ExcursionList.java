package com.example.d308mobileapplication.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.database.Repository;
import com.example.d308mobileapplication.entities.Excursion;

import java.util.ArrayList;
import java.util.List;

public class ExcursionList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        repository = new Repository((getApplication()));

        int vacationID = getIntent().getIntExtra("vacationID", -1);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> associatedExcursions;
        if (vacationID != -1) {
            associatedExcursions = repository.getAssociatedExcursions(vacationID);
        } else {
            associatedExcursions = repository.getAllExcursions();
        }
        excursionAdapter.setExcursions(associatedExcursions);
    }

}
