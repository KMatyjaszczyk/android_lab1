package com.example.lab1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class GradesActivity extends AppCompatActivity {
    List<Grade> mGrades;
    Button mButtonCountAverage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        mButtonCountAverage = findViewById(R.id.buttonCountAverage);

        Bundle bundle = getIntent().getExtras();
        int numberOfGrades = Integer.parseInt(bundle.getString(MainActivity.EDIT_TEXT_NUMBER_OF_GRADES_KEY));

        createGrades(numberOfGrades);
        createRecyclerViewWithAdapter();
        setOnClickListenerToCountAverageButton();
    }

    // TODO zrealizuj odpakowywanie i pakowanie danych o ocenach (odpakowywanie mozesz zrobic w onCreate())
    // TODO przetestowac obracanie ekranu
    // TODO przy przewijaniu pozaznaczaj elementy 2, 3, 4, 5 - potem obroc ekran i zobacz czy dobrze sie przyciski zaznaczaja

    private void createGrades(int numberOfGrades) {
        mGrades = new ArrayList<>();
        for (int i = 0; i < numberOfGrades; i++) {
            mGrades.add(new Grade(String.format("Przedmiot %d", i + 1), GradesAdapter.GRADE_TWO));
        }
    }

    private void createRecyclerViewWithAdapter() {
        RecyclerView gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
        GradesAdapter gradesAdapter = new GradesAdapter(this, mGrades);
        gradesRecyclerView.setAdapter(gradesAdapter);
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setOnClickListenerToCountAverageButton() {
        mButtonCountAverage.setOnClickListener(view -> {
            double average = mGrades.stream().mapToDouble(Grade::getGrade).average().orElse(0);
            String outputText = String.format("Average: %.2f", average);
            Toast.makeText(GradesActivity.this, outputText, Toast.LENGTH_SHORT).show();
        });
    }
}