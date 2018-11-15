package com.ibrickedlabs.internshala.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibrickedlabs.internshala.R;
import com.ibrickedlabs.internshala.UserData.UserContract;
import com.ibrickedlabs.internshala.UserData.UserDbHelper;

import java.util.ArrayList;

public class DashboardFrag extends Fragment {
    private static final String TAG = "DashboardFrag";
    //Textview
    private TextView userNameView;
    private TextView countView;

    //TopLayout
    private FrameLayout dashTopLayout;

    //sting const
    String email;

    //Listview
    ListView listView;
    //EmptyView
    RelativeLayout emptyView;
//ImageView
ImageView noGifView;

    ArrayList<String> appliedworksops;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_layout, container, false);
        userNameView = (TextView) view.findViewById(R.id.name_of_the_user);
        countView = (TextView) view.findViewById(R.id.countView);
        dashTopLayout = (FrameLayout) view.findViewById(R.id.dashboardTopLayout);
        listView = (ListView) view.findViewById(R.id.dashboardListView);
        emptyView = (RelativeLayout) view.findViewById(R.id.emptyView);
        noGifView=(ImageView)view.findViewById(R.id.noGif);
        appliedworksops = new ArrayList<>();
        email = getUserName();
        setUpTheDashboard(email);
        if (appliedworksops.size() > 0) {
            emptyView.setVisibility(View.INVISIBLE);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dashboard_list_iten, R.id.applied_wsname, appliedworksops);
            listView.setAdapter(arrayAdapter);
        } else {
            //Empty
            emptyView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.waiter).into(noGifView);

        }


        return view;
    }


    private void setUpTheDashboard(String email) {
        if (email != null || !TextUtils.isEmpty(email)) {
            Log.d(TAG, email + " here you go");
            UserDbHelper mUserDbHelper = new UserDbHelper(getContext());
            SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
            String projection[] = {
                    UserContract.UserEntry.USER_FIRST_NAME,
                    UserContract.UserEntry.USER_LAST_NAME,
                    UserContract.UserEntry.WORKSHOP_ANDROID,
                    UserContract.UserEntry.WORKSHOP_Artificial_Intelligence,
                    UserContract.UserEntry.WORKSHOP_BigData_Bootcamp,
                    UserContract.UserEntry.WORKSHOP_BlockChain_Technology,
                    UserContract.UserEntry.WORKSHOP_Cyber_Security,
                    UserContract.UserEntry.WORKSHOP_CloudComputing,
                    UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp,
                    UserContract.UserEntry.WORKSHOP_IoT,
                    UserContract.UserEntry.WORKSHOP_Machine_Learning,
                    UserContract.UserEntry.WORKSHOP_SAP_Bootcamp,
                    UserContract.UserEntry.WORKSHOP_Web_Development
            };
            Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME, projection,
                    UserContract.UserEntry.USER_EMAIL + "=?",
                    new String[]{email}, null, null, null);

            if (cursor.moveToFirst()) {
                int w1colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_ANDROID);
                int w2colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Artificial_Intelligence);
                int w3colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_BigData_Bootcamp);
                int w4colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_BlockChain_Technology);
                int w5colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Cyber_Security);
                int w6colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_CloudComputing);
                int w7colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp);
                int w8colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_IoT);
                int w9colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Machine_Learning);
                int w10colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_SAP_Bootcamp);
                int w11colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Web_Development);
                int colarray[] = {w1colIndex, w2colIndex, w3colIndex, w4colIndex, w5colIndex, w6colIndex, w7colIndex, w8colIndex, w9colIndex, w10colIndex, w11colIndex};

                int sum = 0;
                for (int i : colarray) {
                    int val = cursor.getInt(i);
                    if (val == 1) {
                        String str = cursor.getColumnName(i);
                        appliedworksops.add(str);
                    }
                    sum = sum + val;
                }
                countView.setText(String.valueOf(sum));
                int fnameIndex = cursor.getColumnIndex(UserContract.UserEntry.USER_FIRST_NAME);
                int lnameIndex = cursor.getColumnIndex(UserContract.UserEntry.USER_LAST_NAME);
                String name = cursor.getString(fnameIndex) + " " + cursor.getString(lnameIndex);
                userNameView.setText(name);
            }

        } else {
            Snackbar.make(dashTopLayout, R.string.you_are_not_signed_in, Snackbar.LENGTH_SHORT).show();
        }
    }

    private String getUserName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString("USERNAME", null);
    }
}
