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
    private Note note;

    public static NoteDialog newInstance(String title) {
        NoteDialog dialog = new NoteDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"))
                .setView(R.layout.dialog_note)
                .setIcon(R.drawable.ic_note)
                .setNeutralButton(R.string.delete, (dialog, which) -> handleDeleteNote())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.save, (dialog, which) -> saveNote());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText noteText = getDialog().findViewById(R.id.note_edit_text);
        noteText.setText(note.getNote());
    }

    private void saveNote() {
        EditText noteView = getDialog().findViewById(R.id.note_edit_text);
        if (note.getId() == NotesListActivity.NEW_NOTE_DEFAULT_ID) {
            if (TextUtils.getTrimmedLength(noteView.getText()) > 0) {
                note.setNote(noteView.getText().toString());
                viewModel.insert(note);
            }
        } else {
            if (TextUtils.getTrimmedLength(noteView.getText()) > 0) {
                note.setNote(noteView.getText().toString());
                viewModel.update(note);
            } else {
                handleDeleteNote();
            }
        }
        getDialog().dismiss();
    }

    private void handleDeleteNote() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Note?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_note))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.delete(note);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setViewModel(NoteListViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
