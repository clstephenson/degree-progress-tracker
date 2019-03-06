package com.clstephenson.mywgutracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.adapters.NoteListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.NoteListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListActivity extends AppCompatActivity implements OnDataTaskResultListener {

    public static final String EXTRA_NOTE_ID = CourseActivity.class.getSimpleName() + "extra_note_id";
    private NoteListViewModel viewModel;
    private long courseId;
    public static final long NEW_NOTE_DEFAULT_ID = 0;

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
        adapter.setOnItemInteractionListener(((view, position) -> {
            showNoteDialog(getString(R.string.edit_note), viewModel.getNote(position));
        }));

        viewModel.getNotes(courseId).observe(this, adapter::setNotes);

        setTitle(R.string.course_notes);

        FloatingActionButton fab = findViewById(R.id.fab_add_note);
        fab.setOnClickListener(view ->
                showNoteDialog(getString(R.string.create_new_note), viewModel.getNewNote(courseId))
        );
    }

    private void showNoteDialog(String title, Note note) {
        FragmentManager manager = getSupportFragmentManager();
        NoteDialog dialog = NoteDialog.newInstance(title);
        dialog.setNote(note);
        dialog.setViewModel(viewModel);
        dialog.showNow(getSupportFragmentManager(), this.getClass().getSimpleName());
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

    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case INSERT:
                if (result.isSuccessful()) {
                    showDataChangedSnackbarMessage(R.string.note_added);
                } else {
                    showDataChangedSnackbarMessage(R.string.note_insert_failed);
                }
                break;
            case DELETE:
                if (result.isSuccessful()) {
                    showDataChangedSnackbarMessage(R.string.note_deleted);
                } else {
                    showDataChangedSnackbarMessage(R.string.note_delete_failed);
                }
                break;
            case UPDATE:
                if (result.isSuccessful()) {
                    showDataChangedSnackbarMessage(R.string.note_updated);
                } else {
                    showDataChangedSnackbarMessage(R.string.note_update_failed);
                }
                break;
        }
    }

    private void showDataChangedSnackbarMessage(int messageResourceId) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.notes_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
        snackbar.show();
    }
}
