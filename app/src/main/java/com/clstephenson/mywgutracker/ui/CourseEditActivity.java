package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Switch;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.repositories.AsyncTaskResult;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseEditViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.clstephenson.mywgutracker.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class CourseEditActivity extends AppCompatActivity implements OnAsyncTaskResultListener {

    CourseEditViewModel viewModel;
    private MODE entryMode;
    private Course currentCourse;
    private Course dirtyCourse;
    private TextInputEditText titleInput;
    private TextInputEditText endDateInput;
    private TextInputEditText startDateInput;
    private Spinner statusInput;
    private TextInputEditText mentorFirstNameInput;
    private TextInputEditText mentorLastNameInput;
    private TextInputEditText mentorPhoneInput;
    private TextInputEditText mentorEmailInput;
    private Switch enableAlertStartSwitch;
    private Switch enableAlertEndSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel.class);
        viewModel.setBackgroundTaskResultListener(this);
        long courseId = getIntent().getLongExtra(CourseActivity.EXTRA_COURSE_ID, 0);
        if (courseId == 0) {
            entryMode = MODE.CREATE;
            this.setTitle(R.string.new_course);
            setupCourseViews(null);
        } else {
            entryMode = MODE.UPDATE;
            this.setTitle(R.string.edit_course);
            viewModel.getCourseById(courseId).observe(this, this::setupCourseViews);
        }
    }

    private void setupCourseViews(@Nullable Course course) {
        titleInput = findViewById(R.id.course_input_title);
        endDateInput = findViewById(R.id.course_input_end);
        startDateInput = findViewById(R.id.course_input_start);
        statusInput = findViewById(R.id.course_input_status);
        enableAlertStartSwitch = findViewById(R.id.course_switch_alert_start);
        enableAlertEndSwitch = findViewById(R.id.course_switch_alert_end);

        ArrayAdapter<CourseStatus> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CourseStatus.values());
        statusInput.setAdapter(adapter);

        if (entryMode == MODE.UPDATE) {
            currentCourse = course;
            titleInput.setText(currentCourse.getName());
            startDateInput.setText(DateUtils.getFormattedDate(currentCourse.getStartDate()));
            endDateInput.setText(DateUtils.getFormattedDate(currentCourse.getEndDate()));
            statusInput.setSelection(adapter.getPosition(currentCourse.getStatus()));
            enableAlertStartSwitch.setChecked(currentCourse.isStartAlertOn());
            enableAlertEndSwitch.setChecked(currentCourse.isEndAlertOn());
            viewModel.getMentor(currentCourse.getMentorId()).observe(this, this::setupMentorList);
        } else {
            currentCourse = viewModel.getNewCourse();
        }
        dirtyCourse = new Course(currentCourse);
    }

    private void setupMentorList(Mentor mentor) {
        mentorFirstNameInput = findViewById(R.id.mentor_input_first_name);
        mentorLastNameInput = findViewById(R.id.mentor_input_last_name);
        mentorPhoneInput = findViewById(R.id.mentor_input_phone);
        mentorEmailInput = findViewById(R.id.mentor_input_email);

        mentorFirstNameInput.setText(mentor.getFirstName());
        mentorLastNameInput.setText(mentor.getLastName());
        mentorEmailInput.setText(mentor.getEmail());
        mentorPhoneInput.setText(mentor.getPhone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (entryMode == MODE.UPDATE) {
            getMenuInflater().inflate(R.menu.menu_course_edit, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void handleSaveCourse(View view) {
        if (isFormValid()) {
            updateDirtyCourse();

            if (!dirtyCourse.equals(currentCourse)) {
                currentCourse.setName(titleInput.getText().toString());
                currentCourse.setStartDate(dirtyCourse.getStartDate());
                currentCourse.setEndDate(dirtyCourse.getEndDate());

                if (entryMode == MODE.UPDATE) {
                    viewModel.updateCourse(currentCourse);
                    Intent intent = new Intent(this, CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, currentCourse.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    viewModel.insertCourse(currentCourse);
                }
            }
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;

        clearInputErrors(R.id.course_input_title_layout, R.id.course_input_start_layout, R.id.course_input_end_layout);

        if (ValidationUtils.isEmpty(titleInput.getText().toString())) {
            showValidationError(R.id.course_input_title_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(startDateInput.getText().toString())) {
            showValidationError(R.id.course_input_start_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(endDateInput.getText().toString())) {
            showValidationError(R.id.course_input_end_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEndBeforeStart(startDateInput.getText().toString(),
                endDateInput.getText().toString())) {
            showValidationError(R.id.course_input_end_layout, R.string.validation_end_before_start);
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
            case R.id.action_delete_course_edit:
                handleDeleteCourse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncInsertDataCompleted(AsyncTaskResult result) {
        if (result.isSuccessful()) {
            openCourseList(R.string.course_added, Snackbar.LENGTH_LONG);
        } else {
            int messageResourceId;
            if (result.getConstraintException() != null) {
                messageResourceId = R.string.course_add_failed;
            } else {
                messageResourceId = R.string.unexpected_error;
            }
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.course_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void handleDeleteCourse() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Course?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_course))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.deleteCourse(currentCourse);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @Override
    public void onAsyncDeleteDataCompleted(AsyncTaskResult result) {
        if (result.isSuccessful()) {
            openCourseList(R.string.course_deleted, Snackbar.LENGTH_LONG);
        } else {
            int messageResourceId;
            if (result.getConstraintException() != null) {
                messageResourceId = R.string.course_delete_failed;
            } else {
                messageResourceId = R.string.unexpected_error;
            }
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.course_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    @Override
    public void onAsyncUpdateDataCompleted(AsyncTaskResult result) {
        if (result.isSuccessful()) {
            finish();
        } else {
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.course_edit_coordinator_layout), R.string.unexpected_error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void openCourseList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, CourseListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }

    public void handleStartDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        if (entryMode == MODE.UPDATE) {
            calendarView.setDate(DateUtils.getMillisFromDate(currentCourse.getStartDate()));
        } else {
            calendarView.setDate(DateUtils.getMillisFromDate(new Date()));
        }
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            startDateInput.setText(DateUtils.getFormattedDate(newDate));
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    public void handleEndDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        if (entryMode == MODE.UPDATE) {
            calendarView.setDate(DateUtils.getMillisFromDate(currentCourse.getEndDate()));
        } else {
            calendarView.setDate(DateUtils.getMillisFromDate(new Date()));
        }
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            endDateInput.setText(DateUtils.getFormattedDate(newDate));
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    private Dialog getCalendarDialog(View view) {
        Dialog calendarDialog = new Dialog(this);
        calendarDialog.setContentView(R.layout.calendar_dialog_content);
        return calendarDialog;
    }

    private void updateDirtyCourse() {
        dirtyCourse.setName(titleInput.getText().toString());
        dirtyCourse.setStartDate(DateUtils.getDateFromFormattedString(startDateInput.getText().toString()));
        dirtyCourse.setEndDate(DateUtils.getDateFromFormattedString(endDateInput.getText().toString()));
    }

    private enum MODE {
        CREATE, UPDATE
    }
}
