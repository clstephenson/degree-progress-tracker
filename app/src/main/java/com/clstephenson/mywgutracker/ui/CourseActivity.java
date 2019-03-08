package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class CourseActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private final String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_COURSE_ID = CourseActivity.class.getSimpleName() + "extra_course_id";
    public static final String EXTRA_ASSESSMENT_ID = CourseActivity.class.getSimpleName() + "extra_assessment_id";
    public static final String EXTRA_MESSAGE_STRING_ID = MainActivity.class.getSimpleName() + "REQUESTED_MESSAGE";
    public static final String EXTRA_MESSAGE_LENGTH = MainActivity.class.getSimpleName() + "REQUESTED_SNACKBAR_LENGTH";
    private CourseViewModel viewModel;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        viewModel.setDataTaskResultListener(this);
        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, 0);
        viewModel.getCourseById(courseId).observe(this, this::setupViews);
        setupAssessmentListFragment(courseId);

        FloatingActionButton fab = findViewById(R.id.fab_add_assessment);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(AssessmentEditActivity.EXTRA_COURSE_ID, courseId);
            startActivity(intent);
        });
        fab.show();

        setTitle(R.string.title_activity_course);
        processIntentExtraData(getIntent());
    }

    private void processIntentExtraData(Intent intent) {
        if (intent.hasExtra(EXTRA_MESSAGE_STRING_ID) && intent.hasExtra(EXTRA_MESSAGE_LENGTH)) {
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.course_coordinator_layout),
                    intent.getIntExtra(EXTRA_MESSAGE_STRING_ID, 0),
                    intent.getIntExtra(EXTRA_MESSAGE_LENGTH, Snackbar.LENGTH_LONG));
            snackbar.setAction(getString(R.string.dismiss).toUpperCase(), v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void setupViews(Course course) {
        if (course != null) {
            currentCourse = course;
            setupCourseViews(course);
            setupMentorViews(course.getMentor());
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

        TextView termView = findViewById(R.id.course_term);
        termView.setText(String.format("%s: %s", getString(R.string.term), viewModel.getTerm(course)));
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
            case R.id.action_view_notes:
                openNotesList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openNotesList() {
        Intent intent = new Intent(this, NotesListActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, currentCourse.getId());
        startActivity(intent);
    }

    private void handleEditCourse() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, currentCourse.getId());
        startActivity(intent);
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
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case DELETE:
                if (result.isSuccessful()) {
                    cancelCourseNotifications(currentCourse);
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
                break;
        }
    }

    private void cancelCourseNotifications(Course course) {
        CourseEditActivity.submitCourseNotificationRequest(this, course,
                CourseEditActivity.NOTIFICATION_TYPE.START, true);
        CourseEditActivity.submitCourseNotificationRequest(this, course,
                CourseEditActivity.NOTIFICATION_TYPE.END, true);
    }

    private void openCourseList(int messageId, int snackbarLength) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, CourseListFragment.class.getSimpleName());
        intent.putExtra(MainActivity.EXTRA_MESSAGE_STRING_ID, messageId);
        intent.putExtra(MainActivity.EXTRA_MESSAGE_LENGTH, snackbarLength);
        startActivity(intent);
        finish();
    }

    public void handleMentorPhoneClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", currentCourse.getMentor().getPhone())));
        startActivity(intent);
    }

    public void handleMentorEmailClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(String.format("mailto:%s", currentCourse.getMentor().getEmail())));
        startActivity(intent);
    }
}
