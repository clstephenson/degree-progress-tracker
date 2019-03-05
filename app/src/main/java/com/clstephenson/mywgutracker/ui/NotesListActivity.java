package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.adapters.NoteListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.NoteListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private NoteListViewModel viewModel;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        viewModel.setDataTaskResultListener(this);
        courseId = getIntent().getLongExtra(CourseActivity.EXTRA_COURSE_ID, 0);

        RecyclerView notesListView = findViewById(R.id.notes_recyclerview);
        NoteListAdapter adapter = new NoteListAdapter(this);
        notesListView.setAdapter(adapter);
        notesListView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getNotes(courseId).observe(this, adapter::setNotes);

        setTitle(R.string.course_notes);

        FloatingActionButton fab = findViewById(R.id.fab_add_note);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleDeleteNote() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Term?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_note))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    //viewModel.deleteNote(currentNote);
                    dialog.dismiss();
                })
                .create()
                .show();
    }


    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case DELETE:
                if (result.isSuccessful()) {
                    //openTermList(R.string.term_deleted, Snackbar.LENGTH_LONG);
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

}
