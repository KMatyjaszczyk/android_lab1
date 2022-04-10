package com.example.lab1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String EDIT_TEXT_NAME_KEY = "editTextName";
    public static final String EDIT_TEXT_SURNAME_KEY = "editTextSurname";
    public static final String EDIT_TEXT_NUMBER_OF_GRADES_KEY = "editTextNumberOfGrades";
    public static final String IS_BUTTON_VISIBLE_KEY = "isButtonVisible";
    public static final String ARE_AVERAGE_ELEMENTS_VISIBLE_KEY = "areAverageElementsVisible";
    public static final String CALCULATED_AVERAGE_KEY = "calculatedAverageKey";

    private static final int MIN_NUMBER_OF_GRADES = 5;
    private static final int MAX_NUMBER_OF_GRADES = 15;
    private static final double POSITIVE_AVERAGE_THRESHOLD = 3.0;

    private EditText mEditTextName;
    private EditText mEditTextSurname;
    private EditText mEditTextNumberOfGrades;
    private Button mButtonGoFurther;
    private TextView mTextViewYourAverage;
    private Button mButtonConfirmAverage;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    private boolean mAreAverageElementsVisible;
    private double mCalculatedAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectLayoutElementsWithFields();
        hideButtonAtTheBeginning();

        addListenersToElements();

        setUpForProcessingCalculatedAverage();
    }

    private void connectLayoutElementsWithFields() {
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextSurname = findViewById(R.id.editTextSurname);
        mEditTextNumberOfGrades = findViewById(R.id.editTextNumberOfGrades);
        mButtonGoFurther = findViewById(R.id.buttonGoFurther);
        mTextViewYourAverage = findViewById(R.id.textViewYourAverage);
        mButtonConfirmAverage = findViewById(R.id.buttonConfirmAverage);
    }

    private void hideButtonAtTheBeginning() {
        mButtonGoFurther.setVisibility(View.INVISIBLE);
    }

    private void addListenersToElements() {
        addNotNullListenersToTextField(mEditTextName);
        addNotNullListenersToTextField(mEditTextSurname);
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
        mEditTextNumberOfGrades.addTextChangedListener(new TextWatcher() {
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

        mEditTextNumberOfGrades.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isNumberOfGradesIsInProperInterval()) {
                String errorText = getResources().getString(R.string.wrongNumberOfGradesAnnouncement);
                mEditTextNumberOfGrades.setError(errorText);
                Toast.makeText(MainActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGoToNextActivityListener() {
        mButtonGoFurther.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GradesActivity.class);
            intent.putExtra(EDIT_TEXT_NUMBER_OF_GRADES_KEY, mEditTextNumberOfGrades.getText().toString());
            mActivityResultLauncher.launch(intent);
        });
    }

    private void switchButtonVisibilityIfNecessary() {
        if (canButtonBeVisible()) {
            mButtonGoFurther.setVisibility(View.VISIBLE);
        } else {
            mButtonGoFurther.setVisibility(View.INVISIBLE);
        }
    }

    private boolean canButtonBeVisible() {
        boolean nameIsNotEmpty = isTextFieldNotEmpty(mEditTextName);
        boolean surnameIsNotEmpty = isTextFieldNotEmpty(mEditTextSurname);
        boolean numberOfGradesIsInProperInterval = isNumberOfGradesIsInProperInterval();

        return nameIsNotEmpty && surnameIsNotEmpty && numberOfGradesIsInProperInterval;
    }

    private boolean isTextFieldNotEmpty(EditText textField) {
        return !TextUtils.isEmpty(textField.getText().toString());
    }

    private boolean isNumberOfGradesIsInProperInterval() {
        int numberOfGrades;
        try {
            numberOfGrades = Integer.parseInt(mEditTextNumberOfGrades.getText().toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return numberOfGrades >= MIN_NUMBER_OF_GRADES && numberOfGrades <= MAX_NUMBER_OF_GRADES;
    }

    private void setUpForProcessingCalculatedAverage() {
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::processCalculatedAverage);
    }

    private void processCalculatedAverage(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultData = result.getData();
            mAreAverageElementsVisible = true;
            mCalculatedAverage = Objects.requireNonNull(resultData)
                    .getDoubleExtra(GradesActivity.AVERAGE_KEY, 0.0);

            displayAverageElementsIfNecessary();
        }
    }

    private void displayAverageElementsIfNecessary() {
        if (mAreAverageElementsVisible) {
            boolean isGradePositive = mCalculatedAverage >= POSITIVE_AVERAGE_THRESHOLD;

            displayAverageViewElements();
            setAverageInTextView(mCalculatedAverage);
            setProperTextInFinalButton(isGradePositive);
            displayFinalMessageAndCloseApplication(isGradePositive);
        }
    }

    private void displayAverageViewElements() {
        mTextViewYourAverage.setVisibility(View.VISIBLE);
        mButtonConfirmAverage.setVisibility(View.VISIBLE);
    }

    private void setAverageInTextView(double average) {
        String averageMessage = String.format(Locale.getDefault(), "%s: %.2f",
                getResources().getString(R.string.textViewYourAverage), average);
        mTextViewYourAverage.setText(averageMessage);
    }

    private void setProperTextInFinalButton(boolean isGradePositive) {
        mButtonConfirmAverage.setText(isGradePositive ?
                R.string.buttonConfirmAveragePositive : R.string.buttonConfirmAverageNegative);
    }

    private void displayFinalMessageAndCloseApplication(boolean isGradePositive) {
        mButtonConfirmAverage.setOnClickListener(view -> {
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

        outState.putString(EDIT_TEXT_NAME_KEY, mEditTextName.getText().toString());
        outState.putString(EDIT_TEXT_SURNAME_KEY, mEditTextSurname.getText().toString());
        outState.putString(EDIT_TEXT_NUMBER_OF_GRADES_KEY, mEditTextNumberOfGrades.getText().toString());
        outState.putBoolean(IS_BUTTON_VISIBLE_KEY, isButtonVisible);

        outState.putBoolean(ARE_AVERAGE_ELEMENTS_VISIBLE_KEY, mAreAverageElementsVisible);
        outState.putDouble(CALCULATED_AVERAGE_KEY, mCalculatedAverage);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mEditTextName.setText(savedInstanceState.getString(EDIT_TEXT_NAME_KEY));
        mEditTextSurname.setText(savedInstanceState.getString(EDIT_TEXT_SURNAME_KEY));
        mEditTextNumberOfGrades.setText(savedInstanceState.getString(EDIT_TEXT_NUMBER_OF_GRADES_KEY));
        mButtonGoFurther.setVisibility(
                savedInstanceState.getBoolean(IS_BUTTON_VISIBLE_KEY) ? View.VISIBLE : View.INVISIBLE);

        mAreAverageElementsVisible = savedInstanceState.getBoolean(ARE_AVERAGE_ELEMENTS_VISIBLE_KEY);
        mCalculatedAverage = savedInstanceState.getDouble(CALCULATED_AVERAGE_KEY);
        displayAverageElementsIfNecessary();
    }
}