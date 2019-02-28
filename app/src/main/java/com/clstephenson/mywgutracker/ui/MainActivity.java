package com.clstephenson.mywgutracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TITLE_RESOURCE_ID = MainActivity.class.getSimpleName() + "TITLE_RESOURCE_ID";
    public static final String EXTRA_FRAGMENT_NAME = MainActivity.class.getSimpleName() + "REQUESTED_FRAGMENT";
    public static final String EXTRA_MESSAGE_STRING_ID = MainActivity.class.getSimpleName() + "REQUESTED_MESSAGE";
    public static final String EXTRA_MESSAGE_LENGTH = MainActivity.class.getSimpleName() + "REQUESTED_SNACKBAR_LENGTH";

    final String HOME_FRAGMENT = HomeFragment.class.getSimpleName();
    final String TERM_LIST_FRAGMENT = TermListFragment.class.getSimpleName();
    final String COURSE_LIST_FRAGMENT = CourseListFragment.class.getSimpleName();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        // configure navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();

        processIntentExtraData(getIntent());
    }

    private void processIntentExtraData(Intent intent) {
        if (intent.hasExtra(EXTRA_FRAGMENT_NAME)) {
            openFragment(intent.getStringExtra(EXTRA_FRAGMENT_NAME));
            if (intent.hasExtra(EXTRA_MESSAGE_STRING_ID) && intent.hasExtra(EXTRA_MESSAGE_LENGTH)) {
                Snackbar snackbar = Snackbar.make(
                        findViewById(R.id.main_coordinator_layout),
                        intent.getIntExtra(EXTRA_MESSAGE_STRING_ID, 0),
                        intent.getIntExtra(EXTRA_MESSAGE_LENGTH, Snackbar.LENGTH_LONG));
                snackbar.setAction(getString(R.string.dismiss).toUpperCase(), v -> snackbar.dismiss());
                snackbar.show();
            }
        } else {
            openHomeFragment();
        }
    }

    private void openFragment(String fragmentClassSimpleName) {
        if (fragmentClassSimpleName.equals(COURSE_LIST_FRAGMENT)) {
            openCourseListFragment();
        } else if (fragmentClassSimpleName.equals(TERM_LIST_FRAGMENT)) {
            openTermListFragment();
        } else {
            openHomeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            openHomeFragment();
        } else if (id == R.id.nav_terms) {
            openTermListFragment();
        } else if (id == R.id.nav_courses) {
            openCourseListFragment();
        } else if (id == R.id.nav_clear_db) {
            AppDatabase.clearData();
            openHomeFragment();
            Toast.makeText(this, "Database cleared.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_seed_db) {
            AppDatabase.seedDatabase();
            openHomeFragment();
            Toast.makeText(this, "Added some data to the database.", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void handleTermsCardClick(View view) {
        openTermListFragment();
    }

    public void handleCoursesCardClick(View view) {
        openCourseListFragment();
    }

    private void openCourseListFragment() {
        navigationView.setCheckedItem(R.id.nav_courses);
        CourseListFragment fragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TITLE_RESOURCE_ID, R.string.title_activity_course_list);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openTermListFragment() {
        navigationView.setCheckedItem(R.id.nav_terms);
        TermListFragment fragment = new TermListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TITLE_RESOURCE_ID, R.string.title_activity_term_list);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TITLE_RESOURCE_ID, R.string.app_name);
        homeFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content_fragment, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
