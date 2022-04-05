package com.example.lab1;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesAdapterViewHolder> {
    public static final int GRADE_TWO = 2;
    public static final int GRADE_THREE = 3;
    public static final int GRADE_FOUR = 4;
    public static final int GRADE_FIVE = 5;

    private final List<Grade> mGrades;
    private final Activity mActivity;

    public GradesAdapter(Activity mActivity, List<Grade> mGrades) {
        this.mActivity = mActivity;
        this.mGrades = mGrades;
    }

    @NonNull
    @Override
    public GradesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = mActivity.getLayoutInflater().inflate(R.layout.grade_row, parent, false);

        return new GradesAdapterViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesAdapterViewHolder holder, int position) {
        Grade grade = mGrades.get(position);

        holder.mTextViewGrade.setText(grade.getSubject());
        holder.mRadioGroupGrades.setTag(position);
        setProperRadioButtonInGroup(holder, grade);
    }

    private void setProperRadioButtonInGroup(@NonNull GradesAdapterViewHolder holder, Grade grade) {
        int radioButtonIdToCheck;
        switch (grade.getGrade()) {
            case GRADE_THREE:
                radioButtonIdToCheck = R.id.radioButtonThree;
                break;
            case GRADE_FOUR:
                radioButtonIdToCheck = R.id.radioButtonFour;
                break;
            case GRADE_FIVE:
                radioButtonIdToCheck = R.id.radioButtonFive;
                break;
            default:
                radioButtonIdToCheck = R.id.radioButtonTwo;
                break;
        }
        holder.mRadioGroupGrades.check(radioButtonIdToCheck);
    }

    @Override
    public int getItemCount() {
        return mGrades.size();
    }

    class GradesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewGrade;
        RadioGroup mRadioGroupGrades;
        RadioButton mRadioButtonTwo;
        RadioButton mRadioButtonThree;
        RadioButton mRadioButtonFour;
        RadioButton mRadioButtonFive;

        public GradesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            connectLayoutElementsToFields(itemView);
            setOnChangeListenerOnRadioGroup();
        }

        private void connectLayoutElementsToFields(@NonNull View itemView) {
            mRadioGroupGrades = itemView.findViewById(R.id.radioGroupGrades);
            mTextViewGrade = itemView.findViewById(R.id.textViewGrade);
            mRadioButtonTwo = itemView.findViewById(R.id.radioButtonTwo);
            mRadioButtonThree = itemView.findViewById(R.id.radioButtonThree);
            mRadioButtonFour = itemView.findViewById(R.id.radioButtonFour);
            mRadioButtonFive = itemView.findViewById(R.id.radioButtonFive);
        }

        private void setOnChangeListenerOnRadioGroup() {
            mRadioGroupGrades.setOnCheckedChangeListener((radioGroup, i) -> {
                int index = (int) mRadioGroupGrades.getTag();
                switch (i) {
                    case R.id.radioButtonThree:
                        mGrades.get(index).setGrade(GRADE_THREE);
                        break;
                    case R.id.radioButtonFour:
                        mGrades.get(index).setGrade(GRADE_FOUR);
                        break;
                    case R.id.radioButtonFive:
                        mGrades.get(index).setGrade(GRADE_FIVE);
                        break;
                    default:
                        mGrades.get(index).setGrade(GRADE_TWO);
                        break;
                }
            });
        }
    }
}
