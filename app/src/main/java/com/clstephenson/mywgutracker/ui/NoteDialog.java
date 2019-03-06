package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.ui.viewmodels.NoteListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NoteDialog extends DialogFragment {

    private NoteListViewModel viewModel;
    private long noteId;
    private long courseId;

    public static NoteDialog newInstance(String title, long noteId, long courseId) {
        NoteDialog dialog = new NoteDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putLong("noteId", noteId);
        args.putLong("courseId", courseId);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        noteId = getArguments().getLong("noteId");
        courseId = getArguments().getLong("courseId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"))
                .setView(R.layout.dialog_note)
                .setIcon(R.drawable.ic_note)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.save, (dialog, which) -> saveNote());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText noteText = getDialog().findViewById(R.id.note_edit_text);
        //noteText.setText("This is a test note, not from the DB.");
    }

    private void saveNote() {
        EditText noteView = getDialog().findViewById(R.id.note_edit_text);
        if (noteId == NotesListActivity.NEW_NOTE_DEFAULT_ID) {
            if (TextUtils.getTrimmedLength(noteView.getText()) > 0) {
                viewModel.insert(new Note(noteView.getText().toString(), courseId));
            }
            getDialog().dismiss();
        }
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public void setViewModel(NoteListViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
