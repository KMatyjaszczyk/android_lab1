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
    public static int GRADE_TWO = 2;
    public static int GRADE_THREE = 3;
    public static int GRADE_FOUR = 4;
    public static int GRADE_FIVE = 5;

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

        holder.mRadioButtonTwo.setChecked(grade.getGrade() == GRADE_TWO);
        holder.mRadioButtonThree.setChecked(grade.getGrade() == GRADE_THREE);
        holder.mRadioButtonFour.setChecked(grade.getGrade() == GRADE_FOUR);
        holder.mRadioButtonFive.setChecked(grade.getGrade() == GRADE_FIVE);
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
                switch (i) {
                    case R.id.radioButtonThree:
                        mGrades.get((int) mRadioGroupGrades.getTag()).setGrade(GRADE_THREE);
                        break;
                    case R.id.radioButtonFour:
                        mGrades.get((int) mRadioGroupGrades.getTag()).setGrade(GRADE_FOUR);
                        break;
                    case R.id.radioButtonFive:
                        mGrades.get((int) mRadioGroupGrades.getTag()).setGrade(GRADE_FIVE);
                        break;
                    default:
                        mGrades.get((int) mRadioGroupGrades.getTag()).setGrade(GRADE_TWO);
                        break;
                }
            });
        }
    }
}
