package com.clstephenson.mywgutracker.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.DataTaskResult;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseViewModel;
import com.clstephenson.mywgutracker.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

@SuppressWarnings("WeakerAccess")
public class CourseActivity extends AppCompatActivity implements OnDataTaskResultListener {

    private final String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_COURSE_ID = CourseActivity.class.getSimpleName() + "extra_course_id";
    public static final String EXTRA_ASSESSMENT_ID = CourseActivity.class.getSimpleName() + "extra_assessment_id";
    public static final String EXTRA_MESSAGE_STRING_ID = MainActivity.class.getSimpleName() + "REQUESTED_MESSAGE";
    public static final String EXTRA_MESSAGE_LENGTH = MainActivity.class.getSimpleName() + "REQUESTED_SNACKBAR_LENGTH";
    private CourseViewModel viewModel;
    private Course currentCourse;
    private TextView termView;
    private List<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        viewModel.setDataTaskResultListener(this);
        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, 0);
        viewModel.getCourseById(courseId).observe(this, this::setupViews);
        if (savedInstanceState == null) {
            setupAssessmentListFragment(courseId);
        }

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

        termView = findViewById(R.id.course_term);

        viewModel.getAllTermsAsList();
    }

    private void setupMentorViews(Mentor mentor) {
        TextView mentorName = findViewById(R.id.course_text_mentor_name);
        mentorName.setText(String.format("%s %s", mentor.getFirstName(), mentor.getLastName()));

        TextView mentorEmail = findViewById(R.id.course_text_mentor_email);
        mentorEmail.setText(mentor.getEmail());

        TextView mentorPhone = findViewById(R.id.course_text_mentor_phone);
        mentorPhone.setText(mentor.getPhone());

        LinearLayout mentor2Layout = findViewById(R.id.mentor_2_layout);
        if (TextUtils.getTrimmedLength(mentor.getFirstName2()) > 0 ||
                TextUtils.getTrimmedLength(mentor.getLastName2()) > 0) {
            mentor2Layout.setVisibility(View.VISIBLE);
            TextView mentor2Name = findViewById(R.id.course_text_mentor_2_name);
            mentor2Name.setText(String.format("%s %s", mentor.getFirstName2(), mentor.getLastName2()));

            TextView mentor2Email = findViewById(R.id.course_text_mentor_2_email);
            if (TextUtils.getTrimmedLength(mentor.getEmail2()) > 0) {
                mentor2Email.setVisibility(View.VISIBLE);
                mentor2Email.setText(mentor.getEmail2());
            } else {
                mentor2Email.setVisibility(View.GONE);
            }

            TextView mentor2Phone = findViewById(R.id.course_text_mentor_2_phone);
            if (TextUtils.getTrimmedLength(mentor.getPhone2()) > 0) {
                mentor2Phone.setVisibility(View.VISIBLE);
                mentor2Phone.setText(mentor.getPhone2());
            } else {
                mentor2Phone.setVisibility(View.GONE);
            }
        } else {
            mentor2Layout.setVisibility(View.GONE);
        }

        LinearLayout mentor3Layout = findViewById(R.id.mentor_3_layout);
        if (TextUtils.getTrimmedLength(mentor.getFirstName3()) > 0 ||
                TextUtils.getTrimmedLength(mentor.getLastName3()) > 0) {
            mentor3Layout.setVisibility(View.VISIBLE);
            TextView mentor3Name = findViewById(R.id.course_text_mentor_3_name);
            mentor3Name.setText(String.format("%s %s", mentor.getFirstName3(), mentor.getLastName3()));

            TextView mentor3Email = findViewById(R.id.course_text_mentor_3_email);
            if (TextUtils.getTrimmedLength(mentor.getEmail3()) > 0) {
                mentor3Email.setVisibility(View.VISIBLE);
                mentor3Email.setText(mentor.getEmail3());
            } else {
                mentor3Email.setVisibility(View.GONE);
            }

            TextView mentor3Phone = findViewById(R.id.course_text_mentor_3_phone);
            if (TextUtils.getTrimmedLength(mentor.getPhone3()) > 0) {
                mentor3Phone.setVisibility(View.VISIBLE);
                mentor3Phone.setText(mentor.getPhone3());
            } else {
                mentor3Phone.setVisibility(View.GONE);
            }
        } else {
            mentor3Layout.setVisibility(View.GONE);
        }
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
            case android.R.id.home:
                finish();
                break;
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

    private void setTerms(List<Term> terms) {
        this.allTerms = terms;
    }

    @Nullable
    private Term getTermById(long id) {
        for (Term term : allTerms) {
            if (term.getId() == id) {
                return term;
            }
        }
        return null;
    }

    @Override
    public void onNotifyDataChanged(DataTaskResult result) {
        switch (result.getAction()) {
            case DELETE:
                if (result.isSuccessful()) {
                    cancelCourseNotifications(currentCourse);
                    Toast.makeText(this, getString(R.string.course_deleted), Toast.LENGTH_SHORT).show();
                    finish();
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
            case GET:
                // this will be the term list data
                if (result.isSuccessful()) {
                    setTerms((List<Term>) result.getData());
                    termView.setText(String.format("%s: %s", getString(R.string.term),
                            getTermById(currentCourse.getTermId()).getName()));
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

    public void handleMentorPhoneClick(View view) {
        String phone;
        switch (view.getId()) {
            case R.id.course_text_mentor_2_phone:
                phone = currentCourse.getMentor().getPhone2();
                break;
            case R.id.course_text_mentor_3_phone:
                phone = currentCourse.getMentor().getPhone3();
                break;
            default:
                phone = currentCourse.getMentor().getPhone();
                break;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", phone)));
        startActivity(intent);
    }

    public void handleMentorEmailClick(View view) {
        String email;
        switch (view.getId()) {
            case R.id.course_text_mentor_2_email:
                email = currentCourse.getMentor().getEmail2();
                break;
            case R.id.course_text_mentor_3_email:
                email = currentCourse.getMentor().getEmail3();
                break;
            default:
                email = currentCourse.getMentor().getEmail();
                break;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(String.format("mailto:%s", email)));
        startActivity(intent);
    }
}
