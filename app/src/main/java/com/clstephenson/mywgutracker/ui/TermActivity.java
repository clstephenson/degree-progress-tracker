package com.clstephenson.mywgutracker.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.ui.viewmodels.TermViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class TermActivity extends AppCompatActivity {

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
                break;
            case R.id.action_edit_term:
                //todo need to implement edit term
                break;
        }

        return super.onOptionsItemSelected(item);
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
}
