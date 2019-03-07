package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.TermViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class TermActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private final String TAG = this.getClass().getSimpleName();
    private TermViewModel termViewModel;
    private Term currentTerm;

    public static final String EXTRA_TERM_ID = TermActivity.class.getSimpleName() + "extra_term_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.setDataTaskResultListener(this);
        long termId = getIntent().getLongExtra(EXTRA_TERM_ID, 0);
        termViewModel.getTermById(termId).observe(this, this::setupTermViews);
        setupCourseListFragment(termId);
        setTitle(R.string.title_activity_term);

        FloatingActionButton fab = findViewById(R.id.fab_add_course);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(EXTRA_TERM_ID, termId);
            startActivity(intent);
        });
        fab.show();
    }

    private void setupCourseListFragment(long termId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_TERM_ID, termId);
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
                handleDeleteTerm();
                break;
            case R.id.action_edit_term:
                handleEditTerm();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleEditTerm() {
        Intent intent = new Intent(this, TermEditActivity.class);
        intent.putExtra(EXTRA_TERM_ID, currentTerm.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                long termId = data.getLongExtra(EXTRA_TERM_ID, 0);
            }
        }
    }

    private void handleDeleteTerm() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Term?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_term))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    termViewModel.deleteTerm(currentTerm);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void setupTermViews(Term term) {
        //todo: this is getting called when deleting a term, but not sure why. Checking for null is a work-around.  Need to fix.
        if (term != null) {
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
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.term_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
                    snackbar.show();
                }
                break;
        }
    }

    private void openTermList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, TermListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }
}
