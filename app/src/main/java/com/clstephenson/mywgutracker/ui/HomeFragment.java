package com.clstephenson.mywgutracker.ui;


import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.TermStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.ui.viewmodels.HomeViewModel;
import com.clstephenson.mywgutracker.utils.DimensionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String title;
    private HomeViewModel viewModel;
    //private ProgressBar termsProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        LinearLayout termsLayout = getActivity().findViewById(R.id.home_card_terms_layout);
        ProgressBar termsProgressBar = buildProgressBar();
        TextView termsProgressMessage = new TextView(getActivity());
        termsProgressMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        termsProgressMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        termsLayout.addView(termsProgressBar);
        termsLayout.addView(termsProgressMessage);

        LinearLayout coursesLayout = getActivity().findViewById(R.id.home_card_courses_layout);
        ProgressBar coursesProgressBar = buildProgressBar();
        TextView coursesProgressMessage = new TextView(getActivity());
        coursesProgressMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        coursesProgressMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        coursesLayout.addView(coursesProgressBar);
        coursesLayout.addView(coursesProgressMessage);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_main);
        fab.hide();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.TITLE_RESOURCE_ID)) {
                title = getString(getArguments().getInt(MainActivity.TITLE_RESOURCE_ID));
            }
        }

        viewModel.getAllTerms().observe(getActivity(), terms -> setTermsProgress(terms, termsProgressBar, termsProgressMessage));
        viewModel.getAllCourses().observe(getActivity(), courses -> setCoursesProgress(courses, coursesProgressBar, coursesProgressMessage));
    }

    private ProgressBar buildProgressBar() {
        LinearLayout.LayoutParams progressLayoutParams = new LinearLayout.LayoutParams(DimensionUtils.dpToPx(200), LinearLayout.LayoutParams.WRAP_CONTENT);
        ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.setLayoutParams(progressLayoutParams);
        return progressBar;
    }

    private void setTermsProgress(List<Term> terms, ProgressBar progressBar, TextView textView) {
        float totalTerms = terms.size();
        float completedTerms = 0;
        for (Term term : terms) {
            if (term.getStatus() == TermStatus.COMPLETED) {
                completedTerms++;
            }
        }
        if (totalTerms > 0) {
            int progress = Math.round(completedTerms / totalTerms * 100);
            progressBar.setProgress(progress);
        } else {
            progressBar.setProgress(0);

        }
        textView.setText(getString(R.string.termProgressMessage, (int) completedTerms, (int) totalTerms));
    }

    private void setCoursesProgress(List<Course> courses, ProgressBar progressBar, TextView textView) {
        float totalCourses = courses.size();
        float completedCourses = 0;
        for (Course course : courses) {
            if (course.getStatus() == CourseStatus.COMPLETED) {
                completedCourses++;
            }
        }
        if (totalCourses > 0) {
            int progress = Math.round(completedCourses / totalCourses * 100);
            progressBar.setProgress(progress);
        } else {
            progressBar.setProgress(0);
        }
        textView.setText(getString(R.string.courseProgressMessage, (int) completedCourses, (int) totalCourses));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(title);
    }
}
