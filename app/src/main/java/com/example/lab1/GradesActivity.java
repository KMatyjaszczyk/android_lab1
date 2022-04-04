package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradesActivity extends AppCompatActivity {
    public static final String AVERAGE_KEY = "average";
    private static final String GRADE_PREFIX_KEY = "grade_";

    List<Grade> mGrades;
    Button mButtonCountAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        mButtonCountAverage = findViewById(R.id.buttonCountAverage);

        createGrades();
        createRecyclerViewWithAdapter();
        setOnClickListenerToCountAverageButton();
    }

    private void createGrades() {
        Bundle bundleFromMainActivity = getIntent().getExtras();
        int numberOfGrades = Integer.parseInt(bundleFromMainActivity.getString(MainActivity.EDIT_TEXT_NUMBER_OF_GRADES_KEY));

        createGradesWithInitialValues(numberOfGrades);
    }

    private void createGradesWithInitialValues(int numberOfGrades) {
        mGrades = new ArrayList<>();
        String[] subjectNames = getResources().getStringArray(R.array.subjectNames);
        for (int i = 0; i < numberOfGrades; i++) {
            mGrades.add(new Grade(subjectNames[i], GradesAdapter.GRADE_TWO));
        }
    }

    private void createRecyclerViewWithAdapter() {
        RecyclerView gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
        GradesAdapter gradesAdapter = new GradesAdapter(this, mGrades);
        gradesRecyclerView.setAdapter(gradesAdapter);
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListenerToCountAverageButton() {
        mButtonCountAverage.setOnClickListener(view -> {
            double average = calculateAverage();
            comeBackToMainActivityWithAverage(view, average);
        });
    }

    private double calculateAverage() {
        int sum = 0;
        for (Grade grade : mGrades) {
            sum += grade.getGrade();
        }

        return (double) sum / (double) mGrades.size();
    }

    private void comeBackToMainActivityWithAverage(View actualView, double average) {
        Intent intent = new Intent(GradesActivity.this, MainActivity.class);
        intent.putExtra(AVERAGE_KEY, average);
        actualView.getContext().startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        for (int i = 0; i < mGrades.size(); i++) {
            String key = receiveGradeKeyForIndex(i);
            outState.putInt(key, mGrades.get(i).getGrade());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        for (int i = 0; i < mGrades.size(); i++) {
            String key = receiveGradeKeyForIndex(i);
            int grade = savedInstanceState.getInt(key);
            mGrades.get(i).setGrade(grade);
        }
    }

    @NonNull
    private String receiveGradeKeyForIndex(int index) {
        return String.format(Locale.getDefault(), "%s%d", GRADE_PREFIX_KEY, index);
    }
}