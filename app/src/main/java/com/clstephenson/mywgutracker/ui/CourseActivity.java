package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.repositories.AsyncTaskResult;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class CourseActivity extends AppCompatActivity implements OnAsyncTaskResultListener {

    public static final String EXTRA_COURSE_ID = CourseActivity.class.getSimpleName() + "extra_course_id";
    private CourseViewModel viewModel;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        setupFloatingActionButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        viewModel.setBackgroundTaskResultListener(this);
        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, 0);
        viewModel.getCourseById(courseId).observe(this, this::setupViews);
        setupAssessmentListFragment(courseId);
        setTitle(R.string.title_activity_course);
    }

    private void setupFloatingActionButton() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view ->
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show());
    }

    private void setupViews(Course course) {
        //todo: this is getting called when deleting a term, but not sure why. Checking for null is a work-around.  Need to fix.
        if (course != null) {
            currentCourse = course;
            setupCourseViews(course);

            viewModel.getMentorId(course.getMentorId()).observe(this, this::setupMentorViews);
        }
    }

    private void setupCourseViews(Course course) {
        TextView nameView = findViewById(R.id.course_text_name);
        nameView.setText(course.getName());

        TextView statusView = findViewById(R.id.course_text_status);
        StringBuilder statusText = new StringBuilder()
                .append(this.getString(R.string.status))
                .append(":  ")
                .append(course.getStatus().getFriendlyName());
        statusView.setText(statusText);

        TextView startView = findViewById(R.id.course_text_start);
        startView.setText(
                String.format("%s: %s", getString(R.string.start), DateUtils.getFormattedDate(course.getStartDate())));

        TextView endView = findViewById(R.id.course_text_end);
        endView.setText(
                String.format("%s: %s", getString(R.string.end), DateUtils.getFormattedDate(course.getEndDate())));
    }

    private void setupMentorViews(Mentor mentor) {
        TextView mentorName = findViewById(R.id.course_text_mentor_name);
        mentorName.setText(String.format("%s %s", mentor.getFirstName(), mentor.getLastName()));

        TextView mentorEmail = findViewById(R.id.course_text_mentor_email);
        mentorEmail.setText(mentor.getEmail());

        TextView mentorPhone = findViewById(R.id.course_text_mentor_phone);
        mentorPhone.setText(mentor.getPhone());
    }

    private void setupAssessmentListFragment(long courseId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_COURSE_ID, courseId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment assessmentListFragment = new AssessmentListFragment();
        assessmentListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.course_content_fragment, assessmentListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_delete_course:
                handleDeleteCourse();
                break;
            case R.id.action_edit_course:
                handleEditCourse();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleEditCourse() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, currentCourse.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                long courseId = data.getLongExtra(EXTRA_COURSE_ID, 0);
            }
        }
    }

    private void handleDeleteCourse() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_course))
                .setIcon(R.drawable.ic_warning)
                .setMessage(getString(R.string.confirm_delete_course))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    viewModel.deleteCourse(currentCourse);
                    dialog.dismiss();
                })
                .create()
                .show();
    }


    @Override
    public void onAsyncDeleteDataCompleted(AsyncTaskResult result) {
        if (result.isSuccessful()) {
            openCourseList(R.string.course_deleted, Snackbar.LENGTH_LONG);
        } else {
            int messageResourceId;
            if (result.getConstraintException() != null) {
                messageResourceId = R.string.course_delete_failed;
            } else {
                messageResourceId = R.string.unexpected_error;
            }
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.course_coordinator_layout), messageResourceId, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.dismiss), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    @Override
    public void onAsyncUpdateDataCompleted(AsyncTaskResult result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onAsyncInsertDataCompleted(AsyncTaskResult result) {
        throw new UnsupportedOperationException();
    }

    private void openCourseList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, CourseListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }
}
