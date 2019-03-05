package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.AssessmentEditViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.clstephenson.mywgutracker.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class AssessmentEditActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private MODE entryMode;
    private Assessment currentAssessment;
    private Assessment dirtyAssessment;
    private TextInputEditText titleInput;
    private Spinner typeInput;
    private TextInputEditText goalDateInput;

    private AssessmentEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(AssessmentEditViewModel.class);
        viewModel.setDataTaskResultListener(this);
        long assessmentId = getIntent().getLongExtra(CourseActivity.EXTRA_ASSESSMENT_ID, 0);
        if (assessmentId == 0) {
            entryMode = MODE.CREATE;
            this.setTitle(R.string.new_assessment);
            setupAssessmentViews(null);
        } else {
            entryMode = MODE.UPDATE;
            this.setTitle(R.string.edit_assessment);
            viewModel.getAssessmentById(assessmentId).observe(this, this::setupAssessmentViews);
        }
    }

    private void setupAssessmentViews(@Nullable Assessment assessment) {
        titleInput = findViewById(R.id.assessment_input_title);
        typeInput = findViewById(R.id.assessment_input_type);
        goalDateInput = findViewById(R.id.assessment_input_goal);

        if (entryMode == MODE.UPDATE) {
            currentAssessment = assessment;
            titleInput.setText(currentAssessment.getName());
            goalDateInput.setText(DateUtils.getFormattedDate(currentAssessment.getGoalDate()));
            //typeInput.setText(DateUtils.getFormattedDate(currentAssessment.getEndDate()));
        } else {
            currentAssessment = viewModel.getNewAssessment();
        }
        dirtyAssessment = new Assessment(currentAssessment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (entryMode == MODE.UPDATE) {
            getMenuInflater().inflate(R.menu.menu_assessment_edit, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void handleSaveAssessment(View view) {
        if (isFormValid()) {
            updateDirtyAssessment();

            if (!dirtyAssessment.equals(currentAssessment)) {
                currentAssessment.setName(titleInput.getText().toString());
                //currentAssessment.setStartDate(dirtyAssessment.getStartDate());
                currentAssessment.setGoalDate(dirtyAssessment.getGoalDate());

                if (entryMode == MODE.UPDATE) {
                    viewModel.updateAssessment(currentAssessment);
                    Intent intent = new Intent(this, CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_ASSESSMENT_ID, currentAssessment.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    viewModel.insertAssessment(currentAssessment);
                }
            }
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;

        clearInputErrors(R.id.assessment_input_title_layout, R.id.assessment_input_goal_layout);

        if (ValidationUtils.isEmpty(titleInput.getText().toString())) {
            showValidationError(R.id.assessment_input_title_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(goalDateInput.getText().toString())) {
            showValidationError(R.id.assessment_input_goal_layout, R.string.validation_required);
            isValid = false;
        }

        return isValid;
    }

    private void clearInputErrors(int... layoutResourceIds) {
        for (int layoutId : layoutResourceIds) {
            TextInputLayout layout = findViewById(layoutId);
            if (layout != null) {
                layout.setErrorEnabled(false);
            }
        }
    }

    private void showValidationError(int layoutResourceId, int stringResourceId) {
        TextInputLayout layout = findViewById(layoutResourceId);
        layout.setErrorEnabled(true);
        layout.setError(getString(stringResourceId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete_assessment_edit:
                handleDeleteAssessment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case DELETE:
                if (result.isSuccessful()) {
                    openCourseActivity(R.string.assessment_deleted, Snackbar.LENGTH_LONG);
                } else {
                    int messageResourceId;
                    if (result.getConstraintException() != null) {
                        messageResourceId = R.string.assessment_delete_failed;
                    } else {
                        messageResourceId = R.string.unexpected_error;
                    }
                    showDataChangedSnackbarMessage(messageResourceId);
                }
                break;
            case UPDATE:
                if (result.isSuccessful()) {
                    finish();
                } else {
                    showDataChangedSnackbarMessage(R.string.unexpected_error);
                }
                break;
            case INSERT:
                if (result.isSuccessful()) {
                    openCourseActivity(R.string.assessment_added, Snackbar.LENGTH_LONG);
                } else {
                    int messageResourceId;
                    if (result.getConstraintException() != null) {
                        messageResourceId = R.string.assessment_add_failed;
                    } else {
                        messageResourceId = R.string.unexpected_error;
                    }
                    showDataChangedSnackbarMessage(messageResourceId);
                }
                break;
        }
    }

    private void handleDeleteAssessment() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Assessment?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_assessment))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.deleteAssessment(currentAssessment);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void openCourseActivity(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra(CourseActivity.EXTRA_COURSE_ID, currentAssessment.getCourseId());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }

    public void handleGoalDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        if (entryMode == MODE.UPDATE) {
            calendarView.setDate(DateUtils.getMillisFromDate(currentAssessment.getGoalDate()));
        } else {
            calendarView.setDate(DateUtils.getMillisFromDate(new Date()));
        }
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            goalDateInput.setText(DateUtils.getFormattedDate(newDate));
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    private Dialog getCalendarDialog(View view) {
        Dialog calendarDialog = new Dialog(this);
        calendarDialog.setContentView(R.layout.calendar_dialog_content);
        return calendarDialog;
    }

    private void showDataChangedSnackbarMessage(int messageResourceId) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.assessment_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
        snackbar.show();
    }

    private void updateDirtyAssessment() {
        dirtyAssessment.setName(titleInput.getText().toString());
        //dirtyAssessment.setStartDate(DateUtils.getDateFromFormattedString(goalDateInput.getText().toString()));
        dirtyAssessment.setGoalDate(DateUtils.getDateFromFormattedString(goalDateInput.getText().toString()));
    }

    private enum MODE {
        CREATE, UPDATE
    }
}