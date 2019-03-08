package com.clstephenson.mywgutracker.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.TermEditViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.clstephenson.mywgutracker.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class TermEditActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private MODE entryMode;
    private Term currentTerm;
    private Term dirtyTerm;
    private TextInputEditText titleInput;
    private TextInputEditText endDateInput;
    private TextInputEditText startDateInput;

    TermEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(TermEditViewModel.class);
        viewModel.setBackgroundTaskResultListener(this);
        long termId = getIntent().getLongExtra(TermActivity.EXTRA_TERM_ID, 0);
        if (termId == 0) {
            entryMode = MODE.CREATE;
            this.setTitle(R.string.new_term);
            setupTermViews(null);
        } else {
            entryMode = MODE.UPDATE;
            this.setTitle(R.string.edit_term);
            viewModel.getTermById(termId).observe(this, this::setupTermViews);
        }

    }

    private void setupTermViews(@Nullable Term term) {
        titleInput = findViewById(R.id.term_input_title);
        endDateInput = findViewById(R.id.term_input_end);
        startDateInput = findViewById(R.id.term_input_start);

        if (entryMode == MODE.UPDATE) {
            currentTerm = term;
            titleInput.setText(currentTerm.getName());
            startDateInput.setText(DateUtils.getFormattedDate(currentTerm.getStartDate()));
            endDateInput.setText(DateUtils.getFormattedDate(currentTerm.getEndDate()));
        } else {
            currentTerm = viewModel.getNewTerm();
        }
        dirtyTerm = new Term(currentTerm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (entryMode == MODE.UPDATE) {
            getMenuInflater().inflate(R.menu.menu_term_edit, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void handleSaveTerm() {
        if (isFormValid()) {
            updateDirtyTerm();

            if (!dirtyTerm.equals(currentTerm)) {
                currentTerm.setName(titleInput.getText().toString());
                currentTerm.setStartDate(dirtyTerm.getStartDate());
                currentTerm.setEndDate(dirtyTerm.getEndDate());

                if (entryMode == MODE.UPDATE) {
                    viewModel.updateTerm(currentTerm);
                } else {
                    viewModel.insertTerm(currentTerm);
                }
            }
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;

        clearInputErrors(R.id.term_input_title_layout, R.id.term_input_start_layout, R.id.term_input_end_layout);

        if (ValidationUtils.isEmpty(titleInput.getText().toString())) {
            showValidationError(R.id.term_input_title_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(startDateInput.getText().toString())) {
            showValidationError(R.id.term_input_start_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEmpty(endDateInput.getText().toString())) {
            showValidationError(R.id.term_input_end_layout, R.string.validation_required);
            isValid = false;
        } else if (ValidationUtils.isEndBeforeStart(startDateInput.getText().toString(),
                endDateInput.getText().toString())) {
            showValidationError(R.id.term_input_end_layout, R.string.validation_end_before_start);
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
            case R.id.action_save_term_edit:
                handleSaveTerm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case DELETE:
                if (result.isSuccessful()) {
                    openTermList(R.string.term_deleted, Snackbar.LENGTH_LONG);
                } else {
                    int messageResourceId;
                    if (result.getConstraintException() != null) {
                        messageResourceId = R.string.term_delete_failed;
                    } else {
                        messageResourceId = R.string.unexpected_error;
                    }
                    showDataChangedSnackbarMessage(messageResourceId);
                }
                break;
            case UPDATE:
                if (result.isSuccessful()) {
                    openTermList(R.string.term_updated, Snackbar.LENGTH_LONG);
                } else {
                    showDataChangedSnackbarMessage(R.string.unexpected_error);
                }
                break;
            case INSERT:
                if (result.isSuccessful()) {
                    openTermList(R.string.term_added, Snackbar.LENGTH_LONG);
                } else {
                    int messageResourceId;
                    if (result.getConstraintException() != null) {
                        messageResourceId = R.string.term_add_failed;
                    } else {
                        messageResourceId = R.string.unexpected_error;
                    }
                    showDataChangedSnackbarMessage(messageResourceId);
                }
                break;
        }
    }

    private enum MODE {
        CREATE, UPDATE
    }

    private void openTermList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, TermListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }

    public void handleStartDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        if (entryMode == MODE.UPDATE) {
            calendarView.setDate(DateUtils.getMillisFromDate(dirtyTerm.getStartDate()));
        } else {
            calendarView.setDate(DateUtils.getMillisFromDate(new Date()));
        }
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            startDateInput.setText(DateUtils.getFormattedDate(newDate));
            updateDirtyTerm();
            calendarDialog.dismiss();
        });
        calendarDialog.show();
    }

    public void handleEndDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        if (entryMode == MODE.UPDATE) {
            calendarView.setDate(DateUtils.getMillisFromDate(dirtyTerm.getEndDate()));
        } else {
            calendarView.setDate(DateUtils.getMillisFromDate(new Date()));
        }
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Date newDate = DateUtils.getDate(year, month, dayOfMonth);
            endDateInput.setText(DateUtils.getFormattedDate(newDate));
            updateDirtyTerm();
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
                findViewById(R.id.term_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
        snackbar.show();
    }

    private void updateDirtyTerm() {
        dirtyTerm.setName(titleInput.getText().toString());
        dirtyTerm.setStartDate(DateUtils.getDateFromFormattedString(startDateInput.getText().toString()));
        dirtyTerm.setEndDate(DateUtils.getDateFromFormattedString(endDateInput.getText().toString()));
    }
}
