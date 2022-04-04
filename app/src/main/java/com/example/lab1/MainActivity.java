package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int MIN_NUMBER_OF_GRADES = 5;
    private static final int MAX_NUMBER_OF_GRADES = 15;
    private static final double POSITIVE_AVERAGE_THRESHOLD = 3.0;

    public static final String EDIT_TEXT_NAME_KEY = "editTextName";
    public static final String EDIT_TEXT_SURNAME_KEY = "editTextSurname";
    public static final String EDIT_TEXT_NUMBER_OF_GRADES_KEY = "editTextNumberOfGrades";
    public static final String IS_BUTTON_VISIBLE_KEY = "isButtonVisible";

    EditText editTextName;
    EditText editTextSurname;
    EditText editTextNumberOfGrades;
    Button buttonGoFurther;
    TextView textViewYourAverage;
    Button buttonConfirmAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectLayoutElementsWithFields();
        hideButtonAtTheBeginning();

        addListenersToElements();

        processCountedAverage();
    }

    private void connectLayoutElementsWithFields() {
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextNumberOfGrades = findViewById(R.id.editTextNumberOfGrades);
        buttonGoFurther = findViewById(R.id.buttonGoFurther);
        textViewYourAverage = findViewById(R.id.textViewYourAverage);
        buttonConfirmAverage = findViewById(R.id.buttonConfirmAverage);
    }

    private void hideButtonAtTheBeginning() {
        buttonGoFurther.setVisibility(View.INVISIBLE);
    }

    private void addListenersToElements() {
        addNotNullListenersToTextField(editTextName);
        addNotNullListenersToTextField(editTextSurname);
        addProperNumberOfGradesListeners();
        addGoToNextActivityListener();
    }

    private void addNotNullListenersToTextField(EditText textField) {
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // empty method
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switchButtonVisibilityIfNecessary();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // empty method
            }
        });

        textField.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isTextFieldNotEmpty(textField)) {
                String errorText = getResources().getString(R.string.textIsEmptyAnnouncement);
                textField.setError(errorText);
                Toast.makeText(MainActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProperNumberOfGradesListeners() {
        editTextNumberOfGrades.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // empty method
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switchButtonVisibilityIfNecessary();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // empty method
            }
        });
        editTextNumberOfGrades.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isNumberOfGradesIsInProperInterval()) {
                String errorText = getResources().getString(R.string.wrongNumberOfGradesAnnouncement);
                editTextNumberOfGrades.setError(errorText);
                Toast.makeText(MainActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGoToNextActivityListener() {
        buttonGoFurther.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GradesActivity.class);
            intent.putExtra(EDIT_TEXT_NUMBER_OF_GRADES_KEY, editTextNumberOfGrades.getText().toString());
            startActivity(intent);
        });
    }

    private void switchButtonVisibilityIfNecessary() {
        if (canButtonBeVisible()) {
            buttonGoFurther.setVisibility(View.VISIBLE);
        } else {
            buttonGoFurther.setVisibility(View.INVISIBLE);
        }
    }

    private boolean canButtonBeVisible() {
        boolean nameIsNotEmpty = isTextFieldNotEmpty(editTextName);
        boolean surnameIsNotEmpty = isTextFieldNotEmpty(editTextSurname);
        boolean numberOfGradesIsInProperInterval = isNumberOfGradesIsInProperInterval();
        return nameIsNotEmpty && surnameIsNotEmpty && numberOfGradesIsInProperInterval;
    }

    private boolean isTextFieldNotEmpty(EditText textField) {
        return !TextUtils.isEmpty(textField.getText().toString());
    }

    private boolean isNumberOfGradesIsInProperInterval() {
        int numberOfGrades;
        try {
            numberOfGrades = Integer.parseInt(editTextNumberOfGrades.getText().toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return numberOfGrades >= MIN_NUMBER_OF_GRADES && numberOfGrades <= MAX_NUMBER_OF_GRADES;
    }

    private void processCountedAverage() {
        Bundle bundleFromGradesActivity = getIntent().getExtras();
        if (bundleFromGradesActivity != null) {
            double average = bundleFromGradesActivity.getDouble(GradesActivity.AVERAGE_KEY);
            boolean isGradePositive = average >= POSITIVE_AVERAGE_THRESHOLD;

            displayAverageViewElements();
            setAverageInTextView(average);
            setProperTextInFinalButton(isGradePositive);
            displayFinalMessageAndCloseApplication(isGradePositive);
        }
    }

    private void displayAverageViewElements() {
        textViewYourAverage.setVisibility(View.VISIBLE);
        buttonConfirmAverage.setVisibility(View.VISIBLE);
    }

    private void setAverageInTextView(double average) {
        String averageMessage = String.format(Locale.getDefault(), "%s: %.2f",
                getResources().getString(R.string.textViewYourAverage), average);
        textViewYourAverage.setText(averageMessage);
    }

    private void setProperTextInFinalButton(boolean isGradePositive) {
        buttonConfirmAverage.setText(isGradePositive ?
                R.string.buttonConfirmAveragePositive : R.string.buttonConfirmAverageNegative);
    }

    private void displayFinalMessageAndCloseApplication(boolean isGradePositive) {
        buttonConfirmAverage.setOnClickListener(view -> {
            Toast.makeText(
                    MainActivity.this,
                    isGradePositive ? R.string.textOnExitApplicationPositive : R.string.textOnExitApplicationNegative,
                    Toast.LENGTH_LONG)
                    .show();
            MainActivity.this.finishAffinity();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        boolean isButtonVisible = findViewById(R.id.buttonGoFurther).getVisibility() == View.VISIBLE;

        outState.putString(EDIT_TEXT_NAME_KEY, editTextName.getText().toString());
        outState.putString(EDIT_TEXT_SURNAME_KEY, editTextSurname.getText().toString());
        outState.putString(EDIT_TEXT_NUMBER_OF_GRADES_KEY, editTextNumberOfGrades.getText().toString());
        outState.putBoolean(IS_BUTTON_VISIBLE_KEY, isButtonVisible);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editTextName.setText(savedInstanceState.getString(EDIT_TEXT_NAME_KEY));
        editTextSurname.setText(savedInstanceState.getString(EDIT_TEXT_SURNAME_KEY));
        editTextNumberOfGrades.setText(savedInstanceState.getString(EDIT_TEXT_NUMBER_OF_GRADES_KEY));
        buttonGoFurther.setVisibility(
                savedInstanceState.getBoolean(IS_BUTTON_VISIBLE_KEY) ? View.VISIBLE : View.INVISIBLE);
    }
}