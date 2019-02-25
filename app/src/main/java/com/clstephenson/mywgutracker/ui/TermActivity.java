package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.AsyncTaskResult;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.TermViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class TermActivity extends AppCompatActivity implements OnAsyncTaskResultListener {

    private TermViewModel termViewModel;
    private Term currentTerm;
    public static final String TERM_EXTRA_NAME = "TERM_EXTRA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        setupFloatingActionButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.setBackgroundTaskResultListener(this);
        long termId = getIntent().getLongExtra(TermListFragment.TERM_LIST_EXTRA_NAME, 0);
        termViewModel.getTermById(termId).observe(this, this::setupTermViews);
        setupCourseListFragment(termId);
        setTitle(R.string.title_activity_term);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                //todo need to implement add course to term using fab
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    private void setupCourseListFragment(long termId) {
        Bundle bundle = new Bundle();
        bundle.putLong(TERM_EXTRA_NAME, termId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment courseListFragment = new CourseListFragment();
        courseListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.term_content_fragment, courseListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_delete_term:
                //todo need to implement delete term
                handleDeleteTerm();
                break;
            case R.id.action_edit_term:
                //todo need to implement edit term
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleDeleteTerm() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Term?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_term))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    termViewModel.deleteTermById(currentTerm.getId());
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void setupTermViews(Term term) {
        currentTerm = term;
        TextView nameView = findViewById(R.id.term_text_name);
        nameView.setText(term.getName());

        TextView statusView = findViewById(R.id.term_text_status);
        StringBuilder statusText = new StringBuilder()
                .append(this.getString(R.string.status))
                .append(":  ")
                .append(term.getStatus().getFriendlyName());
        statusView.setText(statusText);

        TextView startView = findViewById(R.id.term_text_start);
        startView.setText(
                String.format("%s: %s", getString(R.string.start), DateUtils.getFormattedDate(term.getStartDate())));

        TextView endView = findViewById(R.id.term_text_end);
        endView.setText(
                String.format("%s: %s", getString(R.string.end), DateUtils.getFormattedDate(term.getEndDate())));
    }


    @Override
    public void onAsyncTaskResult(AsyncTaskResult result) {
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
                    findViewById(R.id.term_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }

    }

    private void openTermList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, TermListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_SNACKBAR_LENGTH, snackbarLength);
        startActivity(intent);
    }
}
