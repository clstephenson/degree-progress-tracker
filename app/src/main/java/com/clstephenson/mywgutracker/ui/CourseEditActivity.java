package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.notifications.AlertNotification;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseEditViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.clstephenson.mywgutracker.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class CourseEditActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private static final String TAG = CourseEditActivity.class.getSimpleName();
    private CourseEditViewModel viewModel;
    private MODE entryMode;
    private Course currentCourse;
    private Course dirtyCourse;
    private TextInputEditText titleInput;
    private TextInputEditText endDateInput;
    private TextInputEditText startDateInput;
    private Spinner statusInput;
    private Spinner termInput;
    private TextInputEditText mentorFirstNameInput;
    private TextInputEditText mentorLastNameInput;
    private TextInputEditText mentorPhoneInput;
    private TextInputEditText mentorEmailInput;
    private TextInputEditText mentor2FirstNameInput;
    private TextInputEditText mentor2LastNameInput;
    private TextInputEditText mentor2PhoneInput;
    private TextInputEditText mentor2EmailInput;
    private TextInputEditText mentor3FirstNameInput;
    private TextInputEditText mentor3LastNameInput;
    private TextInputEditText mentor3PhoneInput;
    private TextInputEditText mentor3EmailInput;
    private Switch enableAlertStartSwitch;
    private Switch enableAlertEndSwitch;
    private List<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel.class);
        viewModel.setDataTaskResultListener(this);
        long courseId = getIntent().getLongExtra(CourseActivity.EXTRA_COURSE_ID, 0);

        boolean isConfigurationChange = savedInstanceState != null;
        if (courseId == 0) {
            entryMode = MODE.CREATE;
            this.setTitle(R.string.new_course);
            long termId = getIntent().getLongExtra(TermActivity.EXTRA_TERM_ID, 0);
            setupCourseViews(viewModel.getNewCourse(termId), isConfigurationChange);
        } else {
            entryMode = MODE.UPDATE;
            this.setTitle(R.string.edit_course);
            viewModel.getCourseById(courseId).observe(this, course -> setupCourseViews(course, isConfigurationChange));
        }
    }

    private void setupCourseViews(Course course, boolean isConfigurationChange) {
        currentCourse = course;
        titleInput = findViewById(R.id.course_input_title);
        endDateInput = findViewById(R.id.course_input_end);
        startDateInput = findViewById(R.id.course_input_start);
        statusInput = findViewById(R.id.course_input_status);
        termInput = findViewById(R.id.course_input_term);
        enableAlertStartSwitch = findViewById(R.id.course_switch_alert_start);
        enableAlertEndSwitch = findViewById(R.id.course_switch_alert_end);
        mentorFirstNameInput = findViewById(R.id.mentor_input_first_name);
        mentorLastNameInput = findViewById(R.id.mentor_input_last_name);
        mentorPhoneInput = findViewById(R.id.mentor_input_phone);
        mentorEmailInput = findViewById(R.id.mentor_input_email);
        mentor2FirstNameInput = findViewById(R.id.mentor_2_input_first_name);
        mentor2LastNameInput = findViewById(R.id.mentor_2_input_last_name);
        mentor2PhoneInput = findViewById(R.id.mentor_2_input_phone);
        mentor2EmailInput = findViewById(R.id.mentor_2_input_email);
        mentor3FirstNameInput = findViewById(R.id.mentor_3_input_first_name);
        mentor3LastNameInput = findViewById(R.id.mentor_3_input_last_name);
        mentor3PhoneInput = findViewById(R.id.mentor_3_input_phone);
        mentor3EmailInput = findViewById(R.id.mentor_3_input_email);

        viewModel.getAllTermsAsList();

        ArrayAdapter<CourseStatus> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CourseStatus.values());
        statusInput.setAdapter(statusAdapter);

        if (!isConfigurationChange) {
            titleInput.setText(currentCourse.getName());
            startDateInput.setText(DateUtils.getFormattedDate(currentCourse.getStartDate()));
            endDateInput.setText(DateUtils.getFormattedDate(currentCourse.getEndDate()));
            statusInput.setSelection(statusAdapter.getPosition(currentCourse.getStatus()));
            enableAlertStartSwitch.setChecked(currentCourse.isStartAlertOn());
            enableAlertEndSwitch.setChecked(currentCourse.isEndAlertOn());

            mentorFirstNameInput.setText(currentCourse.getMentor().getFirstName());
            mentorLastNameInput.setText(currentCourse.getMentor().getLastName());
            mentorEmailInput.setText(currentCourse.getMentor().getEmail());
            mentorPhoneInput.setText(currentCourse.getMentor().getPhone());

            mentor2FirstNameInput.setText(currentCourse.getMentor().getFirstName2());
            mentor2LastNameInput.setText(currentCourse.getMentor().getLastName2());
            mentor2EmailInput.setText(currentCourse.getMentor().getEmail2());
            mentor2PhoneInput.setText(currentCourse.getMentor().getPhone2());

            mentor3FirstNameInput.setText(currentCourse.getMentor().getFirstName3());
            mentor3LastNameInput.setText(currentCourse.getMentor().getLastName3());
            mentor3EmailInput.setText(currentCourse.getMentor().getEmail3());
            mentor3PhoneInput.setText(currentCourse.getMentor().getPhone3());

        }
        dirtyCourse = new Course(currentCourse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save_course_edit:
                handleSaveCourse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to submit a request to create or cancel a course notification.  Made it a static
     * method so it can be called from CourseActivity when a course is deleted.
     *
     * @param context
     * @param course
     * @param type
     * @param cancelRequest
     */
    @SuppressWarnings("JavaDoc")
    public static void submitCourseNotificationRequest(Context context, Course course,
                                                       NOTIFICATION_TYPE type, boolean cancelRequest) {
        // notification id is composed of the course id and hashcode of the type parameter.
        // this will allow start and end notifications to exist at the same time.
        int notificationId = (int) course.getId() + type.hashCode();

        Intent intent = new Intent(context, CourseActivity.class);
        intent.putExtra(CourseActivity.EXTRA_COURSE_ID, course.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // setup the dates and resources based on whether it's a start or end date.
        Date courseDate;
        int textResource;
        int titleResource;
        if (type == NOTIFICATION_TYPE.START) {
            courseDate = course.getStartDate();
            textResource = R.string.course_start_notification_text;
            titleResource = R.string.course_start_notification_title;
        } else {
            courseDate = course.getEndDate();
            textResource = R.string.course_end_notification_text;
            titleResource = R.string.course_end_notification_title;
        }

        Date reminderDate = DateUtils.createReminderDate(courseDate, 0,
                AlertNotification.REMINDER_DEFAULT_HOUR_OF_DAY);
        long delay = reminderDate.getTime() - new Date().getTime();
        String dateString = DateUtils.getFormattedDate(courseDate);
        AlertNotification.scheduleAlert(context, context.getString(titleResource),
                context.getString(textResource, course.getName(), dateString),
                delay, notificationId, notificationPendingIntent, cancelRequest);
    }

    private boolean isFormValid() {
        boolean isValid = true;

        clearInputErrors(R.id.course_input_title_layout, R.id.course_input_start_layout,
                R.id.course_input_end_layout, R.id.mentor_input_first_name_layout,
                R.id.mentor_input_last_name_layout, R.id.mentor_input_email_layout,
                R.id.mentor_input_phone_layout, R.id.mentor_2_input_first_name_layout,
                R.id.mentor_2_input_last_name_layout, R.id.mentor_2_input_email_layout,
                R.id.mentor_2_input_phone_layout, R.id.mentor_3_input_first_name_layout,
                R.id.mentor_3_input_last_name_layout, R.id.mentor_3_input_email_layout,
                R.id.mentor_3_input_phone_layout);

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
        } else if (ValidationUtils.isEmpty(mentorFirstNameInput.getText().toString())) {
            showValidationError(R.id.mentor_input_first_name_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(mentorLastNameInput.getText().toString())) {
            showValidationError(R.id.mentor_input_last_name_layout, R.string.validation_required);
            isValid = false;
        } else if (!ValidationUtils.isTelephone(mentorPhoneInput.getText().toString())) {
            showValidationError(R.id.mentor_input_phone_layout, R.string.validation_phone);
            isValid = false;
        } else if (!ValidationUtils.isEmail(mentorEmailInput.getText().toString())) {
            showValidationError(R.id.mentor_input_email_layout, R.string.validation_email);
            isValid = false;
        } else if (TextUtils.getTrimmedLength(mentor2PhoneInput.getText().toString()) > 0 &&
                !ValidationUtils.isTelephone(mentor2PhoneInput.getText().toString())) {
            showValidationError(R.id.mentor_2_input_phone_layout, R.string.validation_phone);
            isValid = false;
        } else if (TextUtils.getTrimmedLength(mentor2EmailInput.getText().toString()) > 0 &&
                !ValidationUtils.isEmail(mentor2EmailInput.getText().toString())) {
            showValidationError(R.id.mentor_2_input_email_layout, R.string.validation_email);
            isValid = false;
        } else if (TextUtils.getTrimmedLength(mentor3PhoneInput.getText().toString()) > 0 &&
                !ValidationUtils.isTelephone(mentor3PhoneInput.getText().toString())) {
            showValidationError(R.id.mentor_3_input_phone_layout, R.string.validation_phone);
            isValid = false;
        } else if (TextUtils.getTrimmedLength(mentor3EmailInput.getText().toString()) > 0 &&
                !ValidationUtils.isEmail(mentor3EmailInput.getText().toString())) {
            showValidationError(R.id.mentor_3_input_email_layout, R.string.validation_email);
            isValid = false;
        }
        //todo add validation for mentor 2 and 3 if entered

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

    private void handleSaveCourse() {
        if (isFormValid()) {
            updateDirtyCourse();

            if (!dirtyCourse.equals(currentCourse)) {
                currentCourse.setName(titleInput.getText().toString());
                currentCourse.setStartDate(dirtyCourse.getStartDate());
                currentCourse.setEndDate(dirtyCourse.getEndDate());
                currentCourse.setStatus((CourseStatus) statusInput.getSelectedItem());
                currentCourse.setTermId(((Term) termInput.getSelectedItem()).getId());
                currentCourse.setStartAlertOn(enableAlertStartSwitch.isChecked());
                currentCourse.setEndAlertOn(enableAlertEndSwitch.isChecked());

                if (!dirtyCourse.getMentor().equals(currentCourse.getMentor())) {
                    currentCourse.getMentor().setFirstName(dirtyCourse.getMentor().getFirstName());
                    currentCourse.getMentor().setLastName(dirtyCourse.getMentor().getLastName());
                    currentCourse.getMentor().setPhone(dirtyCourse.getMentor().getPhone());
                    currentCourse.getMentor().setEmail(dirtyCourse.getMentor().getEmail());
                    currentCourse.getMentor().setFirstName2(dirtyCourse.getMentor().getFirstName2());
                    currentCourse.getMentor().setLastName2(dirtyCourse.getMentor().getLastName2());
                    currentCourse.getMentor().setPhone2(dirtyCourse.getMentor().getPhone2());
                    currentCourse.getMentor().setEmail2(dirtyCourse.getMentor().getEmail2());
                    currentCourse.getMentor().setFirstName3(dirtyCourse.getMentor().getFirstName3());
                    currentCourse.getMentor().setLastName3(dirtyCourse.getMentor().getLastName3());
                    currentCourse.getMentor().setPhone3(dirtyCourse.getMentor().getPhone3());
                    currentCourse.getMentor().setEmail3(dirtyCourse.getMentor().getEmail3());
                }

                if (entryMode == MODE.UPDATE) {
                    viewModel.updateCourse(currentCourse);
                } else {
                    viewModel.insertCourse(currentCourse);
                }
            }
        }
    }

    public void handleStartDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog();
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        calendarView.setDate(DateUtils.getMillisFromDate(dirtyCourse.getStartDate()));
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            startDateInput.setText(DateUtils.getFormattedDate(newDate));
            updateDirtyCourse();
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    public void handleEndDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog();
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        calendarView.setDate(DateUtils.getMillisFromDate(dirtyCourse.getEndDate()));
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            endDateInput.setText(DateUtils.getFormattedDate(newDate));
            updateDirtyCourse();
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    private Dialog getCalendarDialog() {
        Dialog calendarDialog = new Dialog(this);
        calendarDialog.setContentView(R.layout.calendar_dialog_content);
        return calendarDialog;
    }

    private void showDataChangedSnackbarMessage(int messageResourceId) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.course_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
        snackbar.show();
    }

    private void updateDirtyCourse() {
        dirtyCourse.setName(titleInput.getText().toString());
        if (!TextUtils.isEmpty(startDateInput.getText().toString())) {
            dirtyCourse.setStartDate(DateUtils.getDateFromFormattedString(startDateInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(endDateInput.getText().toString())) {
            dirtyCourse.setEndDate(DateUtils.getDateFromFormattedString(endDateInput.getText().toString()));
        }
        dirtyCourse.setStatus((CourseStatus) statusInput.getSelectedItem());
        dirtyCourse.setTermId(((Term) termInput.getSelectedItem()).getId());
        dirtyCourse.setEndAlertOn(enableAlertEndSwitch.isChecked());
        dirtyCourse.setStartAlertOn(enableAlertStartSwitch.isChecked());
        dirtyCourse.getMentor().setFirstName(mentorFirstNameInput.getText().toString());
        dirtyCourse.getMentor().setLastName(mentorLastNameInput.getText().toString());
        dirtyCourse.getMentor().setPhone(mentorPhoneInput.getText().toString());
        dirtyCourse.getMentor().setEmail(mentorEmailInput.getText().toString());
        dirtyCourse.getMentor().setFirstName2(mentor2FirstNameInput.getText().toString());
        dirtyCourse.getMentor().setLastName2(mentor2LastNameInput.getText().toString());
        dirtyCourse.getMentor().setPhone2(mentor2PhoneInput.getText().toString());
        dirtyCourse.getMentor().setEmail2(mentor2EmailInput.getText().toString());
        dirtyCourse.getMentor().setFirstName3(mentor3FirstNameInput.getText().toString());
        dirtyCourse.getMentor().setLastName3(mentor3LastNameInput.getText().toString());
        dirtyCourse.getMentor().setPhone3(mentor3PhoneInput.getText().toString());
        dirtyCourse.getMentor().setEmail3(mentor3EmailInput.getText().toString());
    }

    private void setTerms(List<Term> terms) {
        this.allTerms = terms;
    }

    @Nullable
    private Term getTermById(long id) {
        for (Term term : allTerms) {
            if (term.getId() == id) {
                return term;
            }
        }
        return null;
    }

    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case UPDATE:
                if (result.isSuccessful()) {
                    handleAlertsIfSelectedAndFinish();
                } else {
                    showDataChangedSnackbarMessage(R.string.unexpected_error);
                }
                break;
            case INSERT:
                if (result.isSuccessful()) {
                    handleAlertsIfSelectedAndFinish();
                } else {
                    int messageResourceId;
                    if (result.getConstraintException() != null) {
                        messageResourceId = R.string.course_add_failed;
                    } else {
                        messageResourceId = R.string.unexpected_error;
                    }
                    showDataChangedSnackbarMessage(messageResourceId);
                }
                break;
            case GET:
                // this will be the term list data
                if (result.isSuccessful()) {
                    setTerms((List<Term>) result.getData());
                    if (allTerms == null || allTerms.size() == 0) {
                        new AlertDialog.Builder(this)
                                .setTitle("No Terms Present")
                                .setIcon(R.drawable.ic_notifications)
                                .setMessage(getString(R.string.please_add_term))
                                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                                    finish();
                                    dialog.cancel();
                                })
                                .create()
                                .show();
                    } else {
                        ArrayAdapter<Term> termAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line, allTerms);
                        termInput.setAdapter(termAdapter);
                        switch (entryMode) {
                            case UPDATE:
                                termInput.setSelection(termAdapter.getPosition(getTermById(currentCourse.getTermId())));
                                break;
                            case CREATE:
                                if (currentCourse.getTermId() > 0) {
                                    termInput.setSelection(termAdapter.getPosition(getTermById(currentCourse.getTermId())));
                                }
                                break;
                        }
                    }
                }
                break;
        }
    }

    private void handleAlertsIfSelectedAndFinish() {
        submitCourseNotificationRequest(this, currentCourse, NOTIFICATION_TYPE.START, !currentCourse.isStartAlertOn());
        submitCourseNotificationRequest(this, currentCourse, NOTIFICATION_TYPE.END, !currentCourse.isEndAlertOn());
        if (currentCourse.isEndAlertOn() || currentCourse.isStartAlertOn()) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert Notification Added")
                    .setIcon(R.drawable.ic_notifications)
                    .setMessage(getString(R.string.course_notification_added))
                    .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                        finish();
                        dialog.cancel();
                    })
                    .create()
                    .show();
        } else {
            finish();
        }
    }

    private enum MODE {
        CREATE, UPDATE
    }

    public enum NOTIFICATION_TYPE {
        START, END
    }
}
