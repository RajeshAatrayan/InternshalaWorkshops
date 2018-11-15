package com.ibrickedlabs.internshala;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ibrickedlabs.internshala.Frags.DashboardFrag;
import com.ibrickedlabs.internshala.Frags.WorkshopsFragment;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopContract;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopDbHelper;

public class MainActivity extends AppCompatActivity {
    //TopLayout of the mainactivity class
    private FrameLayout frameLayout;
    //FirstTime
    boolean firstTimeEntering = true;
    //Workshop items
    String workshopNames[] = {"Android", "Artificial Intelligence", "BigData Bootcamp", "BlockChain Technology", "Cyber Security", "CloudComputing", "Datascience Bootcamp", "Iot", "Machine Learning", "SAP Bootcamp", "Web Development"};
    //Workshop details
    String workshopDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //To overlap the toolbar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //Check if the user is opening the app for the first time
        getThestatus();
        //Get the string res
        workshopDetails = getResources().getString(R.string.lorem);
        //feed the database
        LoadtheWorkshops();
        //Initiate the workshops fragment
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new WorkshopsFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.mainActivityFragmentLayout, fragment);
        ft.commit();
        //mainActivity top layout
        frameLayout = (FrameLayout) findViewById(R.id.mainActivityFragmentLayout);


    }

    //hide the overflow menu if the user is in signed out state to avoid redundancy
    private void hideOptionsMenuIfSignedOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean statusLoggedIn = sharedPreferences.getBoolean("LOGGED_IN", false);
        if (statusLoggedIn) {
            SignoutMenuValidation.mState = true;

        } else {
            SignoutMenuValidation.mState = false;
            invalidateOptionsMenu();//It will onCreateOptions menu again
        }


    }

    //returns if the user entering for the first time
    private void getThestatus() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstTimeEntering = sharedPreferences.getBoolean("FIRSTENTRY", true);
    }

    //Loads the workshop
    private void LoadtheWorkshops() {
        if (firstTimeEntering) {
            //If the user is opeing the app for the first time feed the database with the workshops
            WorkshopDbHelper mWorkshopDbHelper = new WorkshopDbHelper(this);
            SQLiteDatabase mSqLiteDatabase = mWorkshopDbHelper.getWritableDatabase();
            ContentValues cv;
            for (String item : workshopNames) {
                cv = new ContentValues();
                cv.put(WorkshopContract.WorkshopEntry.WORKSHOP_NAME, item);
                cv.put(WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS, workshopDetails);
                mSqLiteDatabase.insert(WorkshopContract.WorkshopEntry.TABLE_NAME, null, cv);
            }
            //setting it to false again
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRSTENTRY", false);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        hideOptionsMenuIfSignedOut();

        getMenuInflater().inflate(R.menu.main_menu, menu);
        //If the user is signout dont show the overflow menu
        if (!SignoutMenuValidation.ismState()) {
            MenuItem menuItem1 = menu.findItem(R.id.signout);
            MenuItem menuItem2 = menu.findItem(R.id.dashboard);
            menuItem1.setVisible(false);
            menuItem2.setVisible(false);
        } else {
            MenuItem menuItem1 = menu.findItem(R.id.signout);
            MenuItem menuItem2 = menu.findItem(R.id.dashboard);
            menuItem1.setVisible(true);
            menuItem2.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                callDashBoard();
                return true;
            case R.id.signout:
                signOutTheUser();
                return true;
            case R.id.homesc:
                takeTohomescreen();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    //Home menu button working
    private void takeTohomescreen() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    private void signOutTheUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("LOGGED_IN", false)) {
            SignoutMenuValidation.mState = false;
            invalidateOptionsMenu();
            Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USERNAME", null);
            editor.putBoolean("LOGGED_IN", false);
            editor.commit();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();




        }

    }
    //Taking the user to dash board
    private void callDashBoard() {
        Fragment fragment = new DashboardFrag();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainActivityFragmentLayout, fragment);
        ft.addToBackStack(getString(R.string.addDash));
        ft.commit();

    }

}
