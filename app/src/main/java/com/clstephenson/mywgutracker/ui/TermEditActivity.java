package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.AsyncTaskResult;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.TermEditViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class TermEditActivity extends AppCompatActivity implements OnAsyncTaskResultListener {

    TermEditViewModel viewModel;
    private Term currentTerm;
    private Term dirtyTerm;
    private TextInputEditText titleInput;
    private TextInputEditText endDateInput;
    private TextInputEditText startDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(TermEditViewModel.class);
        viewModel.setBackgroundTaskResultListener(this);
        long termId = getIntent().getLongExtra(TermActivity.TERM_EXTRA_NAME, 0);
        viewModel.getTermById(termId).observe(this, this::setupTermViews);
    }

    private void setupTermViews(Term term) {
        currentTerm = term;
        dirtyTerm = new Term(term);

        titleInput = findViewById(R.id.term_input_title);
        endDateInput = findViewById(R.id.term_input_end);
        startDateInput = findViewById(R.id.term_input_start);

        titleInput.setText(term.getName());
        startDateInput.setText(DateUtils.getFormattedDate(term.getStartDate()));
        endDateInput.setText(DateUtils.getFormattedDate(term.getEndDate()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete_term_edit:
                handleDeleteTerm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleUpdateTerm(View view) {
        updateDirtyTerm();

        if (!dirtyTerm.equals(currentTerm)) {
            currentTerm.setName(titleInput.getText().toString());
            currentTerm.setStartDate(dirtyTerm.getStartDate());
            currentTerm.setEndDate(dirtyTerm.getEndDate());

            viewModel.updateTerm(currentTerm);

            Intent intent = new Intent(this, TermActivity.class);
            intent.putExtra(TermActivity.TERM_EXTRA_NAME, currentTerm.getId());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void handleDeleteTerm() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Term?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_term))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.deleteTerm(currentTerm);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @Override
    public void onAsyncDeleteDataCompleted(AsyncTaskResult result) {
        if (result.isSuccessful()) {
            openTermList(R.string.term_deleted, Snackbar.LENGTH_LONG);
        } else {
            int messageResourceId;
            if (result.getConstraintException() != null) {
                messageResourceId = R.string.term_delete_failed;
            } else {
                messageResourceId = R.string.unexpected_error;
            }
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.term_edit_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
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
                    findViewById(R.id.term_edit_coordinator_layout), R.string.unexpected_error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    @Override
    public void onAsyncInsertDataCompleted(AsyncTaskResult result) {
        throw new UnsupportedOperationException();
    }

    private void openTermList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, TermListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_SNACKBAR_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }

    public void handleStartDateInputClick(View view) {
        Dialog calendarDialog = getCalendarDialog(view);
        CalendarView calendarView = calendarDialog.findViewById(R.id.calendar_date_picker);
        calendarView.setDate(DateUtils.getMillisFromDate(currentTerm.getStartDate()));
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
        calendarView.setDate(DateUtils.getMillisFromDate(currentTerm.getEndDate()));
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

    private void updateDirtyTerm() {
        dirtyTerm.setName(titleInput.getText().toString());
        dirtyTerm.setStartDate(DateUtils.getDateFromFormattedString(startDateInput.getText().toString()));
        dirtyTerm.setEndDate(DateUtils.getDateFromFormattedString(endDateInput.getText().toString()));
        // todo: finish this - need to get Date from date string

    }
}
