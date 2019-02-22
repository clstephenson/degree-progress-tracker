package com.clstephenson.mywgutracker.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.ui.viewmodels.TermViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import static com.clstephenson.mywgutracker.ui.TermListFragment.TERM_ID;

public class TermActivity extends AppCompatActivity {

    private TermViewModel termViewModel;
    private Term currentTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        long termId = getIntent().getLongExtra(TERM_ID, 0);
        termViewModel.getTermById(termId).observe(this, this::setupTermViews);

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
                break;
            case R.id.action_edit_term:
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
