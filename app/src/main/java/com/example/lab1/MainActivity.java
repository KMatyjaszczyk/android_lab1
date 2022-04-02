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

public class MainActivity extends AppCompatActivity {
    private static final int MIN_NUMBER_OF_GRADES = 5;
    private static final int MAX_NUMBER_OF_GRADES = 15;

    public static final String EDIT_TEXT_NAME_KEY = "editTextName";
    public static final String EDIT_TEXT_SURNAME_KEY = "editTextSurname";
    public static final String EDIT_TEXT_NUMBER_OF_GRADES_KEY = "editTextNumberOfGrades";
    public static final String IS_BUTTON_VISIBLE_KEY = "isButtonVisible";

    EditText editTextName;
    EditText editTextSurname;
    EditText editTextNumberOfGrades;
    Button buttonGoFurther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);

        connectLayoutElementsWithFields();
        hideButtonAtTheBeginning();

        addListenersToElements();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        TextView editTextName = findViewById(R.id.editTextName); // TODO does findViewById() have to be here ? those variables are in the class fields
        TextView editTextSurname = findViewById(R.id.editTextSurname);
        TextView editTextNumberGrades = findViewById(R.id.editTextNumberOfGrades);
        boolean isButtonVisible = findViewById(R.id.buttonGoFurther).getVisibility() == View.VISIBLE;

        outState.putString(EDIT_TEXT_NAME_KEY, editTextName.getText().toString());
        outState.putString(EDIT_TEXT_SURNAME_KEY, editTextSurname.getText().toString());
        outState.putString(EDIT_TEXT_NUMBER_OF_GRADES_KEY, editTextNumberGrades.getText().toString());
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

    private void connectLayoutElementsWithFields() {
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextNumberOfGrades = findViewById(R.id.editTextNumberOfGrades);
        buttonGoFurther = findViewById(R.id.buttonGoFurther);
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
}