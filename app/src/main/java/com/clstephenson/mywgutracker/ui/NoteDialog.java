package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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
                .setIcon(R.drawable.ic_note);
        return builder.create();
    }

    @Override
    public void onStart() {
        viewModel = ((NotesListActivity) getActivity()).getViewModel();
        note = viewModel.getNote();
        super.onStart();
        TextView cancelButton = getDialog().findViewById(R.id.note_cancel);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> getDialog().cancel());
        }

        TextView saveButton = getDialog().findViewById(R.id.note_save);
        if (saveButton != null) {
            saveButton.setOnClickListener(v -> saveNote());
        }

        TextView deleteButton = getDialog().findViewById(R.id.note_delete);
        if (deleteButton != null) {
            deleteButton.setOnClickListener(v -> deleteNote());
        }

        TextView shareButton = getDialog().findViewById(R.id.note_share);
        if (shareButton != null) {
            shareButton.setOnClickListener(v -> shareNote());
        }

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
                deleteNote();
            }
        }
        getDialog().dismiss();
    }

    private void deleteNote() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Note?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_note))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.delete(note);
                    dialog.dismiss();
                    getDialog().dismiss();
                })
                .create()
                .show();

    }

    private void shareNote() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.sharing_course_note);
        intent.putExtra(Intent.EXTRA_TEXT, note.getNote());
        startActivity(Intent.createChooser(intent, getString(R.string.share_a_note)));
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setViewModel(NoteListViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
