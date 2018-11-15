package com.ibrickedlabs.internshala.Frags;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibrickedlabs.internshala.R;
import com.ibrickedlabs.internshala.UserData.UserContract.UserEntry;
import com.ibrickedlabs.internshala.UserData.UserDbHelper;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopContract;

public class WorkshopDetails extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 123;
    //Position of the clicked item
    String position;
    String selectedWorkshop;
    //Textview
    TextView detailsView;
    //Button
    Button applyButton;
    //Top Layout
    FrameLayout mFrameLayout;

    private static final String TAG = "WorkshopDetails";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public WorkshopDetails() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //Getting the position from previous activity
            position = bundle.getString("POSITION");
            selectedWorkshop = bundle.getString("SELECTEDWORKSHOPNAME");
        }

        View view = inflater.inflate(R.layout.workshop_details_layout, container, false);
        detailsView = (TextView) view.findViewById(R.id.workshopDetailsTv);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.detailsTopLayout);
        applyButton = (Button) view.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userSignedIn = checkTheStatusOfTheUser();
                if (userSignedIn) {
                    Log.d(TAG, "userSigned In!");

                    //Signed In state
                    if (hasAlreadyApplied()) {

                        showAlreadyAppliedDialog();
                        //Snackbar.make(mFrameLayout, R.string.sorry_already_registered, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Not Applied!");
                        applyForTheWorkshop();

                    }
                } else {
                    //Signed out state
                    Log.d(TAG, "Signed out!");
                    Fragment fragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.mainActivityFragmentLayout, fragment);
                    ft.addToBackStack("addLoginFrag");
                    ft.commit();
                }

            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }

    private void showAlreadyAppliedDialog() {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
        View v=LayoutInflater.from(getContext()).inflate(R.layout.oops_dialog,null,false);
        ImageView gifView=(ImageView)v.findViewById(R.id.sorrygifView);
        Glide.with(getContext()).load(R.drawable.astronaut).into(gifView);
        mBuilder.setView(v);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.bltxt_clr));
    }

    private void applyForTheWorkshop() {
        UserDbHelper mUserDbHelper = new UserDbHelper(getContext());
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
        String workshopname = getWorkShopName();
        String emailOfTheUser = getEmail();
        ContentValues contentValues = new ContentValues();
        contentValues.put(workshopname, 1);
        int rowsUpdated = mSqLiteDatabase.update(UserEntry.TABLE_NAME, contentValues, UserEntry.USER_EMAIL + "=?", new String[]{emailOfTheUser});
        if (rowsUpdated == 0) {
            Snackbar.make(mFrameLayout, R.string.req_failed, Snackbar.LENGTH_SHORT).show();
        } else {
            showDialog();
            //Snackbar.make(mFrameLayout, R.string.req_sent, Snackbar.LENGTH_SHORT).show();
        }


    }

    private void showDialog() {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
        View v=LayoutInflater.from(getContext()).inflate(R.layout.request_dialog,null,false);
        ImageView gifView=(ImageView)v.findViewById(R.id.applygif);
        Glide.with(getContext()).load(R.drawable.reqstsent).into(gifView);
        mBuilder.setView(v);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getContext().getColor(R.color.bltxt_clr));

    }

    private boolean hasAlreadyApplied() {
        UserDbHelper mUserDbHelper = new UserDbHelper(getContext());
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        String workshopname = getWorkShopName();
        String emailOfTheUser = getEmail();
        String projection[] = {UserEntry.USER_EMAIL, workshopname};
        Cursor cursor = mSqLiteDatabase.query(UserEntry.TABLE_NAME, projection,
                UserEntry.USER_EMAIL + "=?",
                new String[]{emailOfTheUser},
                null,
                null, null
        );
        int value = 0;
        if (cursor.moveToFirst()) {
            int selectoedWorkshopColIndex = cursor.getColumnIndex(workshopname);
            value = cursor.getInt(selectoedWorkshopColIndex);

        }
        boolean b = false;
        if (value == 1)
            b = true;
        return b;
    }

    private String getEmail() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString("USERNAME", null);
    }


    private String getWorkShopName() {
        String str = null;
        switch (selectedWorkshop) {
            case "Android":
                str = UserEntry.WORKSHOP_ANDROID;
                break;
            case "Artificial Intelligence":
                str = UserEntry.WORKSHOP_Artificial_Intelligence;
                break;

            case "BigData Bootcamp":
                str = UserEntry.WORKSHOP_BigData_Bootcamp;
                break;
            case "BlockChain Technology":
                str = UserEntry.WORKSHOP_BlockChain_Technology;
                break;
            case "Cyber Security":
                str = UserEntry.WORKSHOP_Cyber_Security;
                break;
            case "CloudComputing":
                str = UserEntry.WORKSHOP_CloudComputing;
                break;
            case "Datascience Bootcamp":
                str = UserEntry.WORKSHOP_Datascience_Bootcamp;
                break;
            case "Iot":
                str = UserEntry.WORKSHOP_IoT;
                break;
            case "Machine Learning":
                str = UserEntry.WORKSHOP_Machine_Learning;
                break;
            case "SAP Bootcamp":
                str = UserEntry.WORKSHOP_SAP_Bootcamp;
                break;
            case "Web Development":
                str = UserEntry.WORKSHOP_Web_Development;
                break;
        }
        return str;

    }

    private boolean checkTheStatusOfTheUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean("LOGGED_IN", false);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS};
        Uri currentUri = ContentUris.withAppendedId(WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI, Long.parseLong(position));
        return new CursorLoader(getContext(),
                currentUri,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int detailsColIndex = data.getColumnIndex(WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS);
            String details = data.getString(detailsColIndex);
            detailsView.setText(details);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
