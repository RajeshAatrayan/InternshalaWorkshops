package com.ibrickedlabs.internshala.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibrickedlabs.internshala.R;
import com.ibrickedlabs.internshala.SignoutMenuValidation;
import com.ibrickedlabs.internshala.UserData.UserContract.UserEntry;
import com.ibrickedlabs.internshala.UserData.UserDbHelper;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    //layouts
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    //TopLayout
    private LinearLayout mLinearLayout;

    //fields
    private AppCompatEditText email;
    private AppCompatEditText password;

    //buttons
    private Button signupButton;
    private ImageButton goButton;

    //Database
    private UserDbHelper mUserDbHelper;
    private SQLiteDatabase mSqLiteDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUserDbHelper = new UserDbHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflating the view
        View view = inflater.inflate(R.layout.email_login, container, false);
        intialiseViews(view);
        //Adding textwatcher for validation
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailExtracted = email.getText().toString().trim();
                if (TextUtils.isEmpty(emailExtracted)) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getString(R.string.please_enter_email_add));
                } else if (!emailExtracted.contains("@")) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getString(R.string.please_enter_valid));
                } else {
                    emailLayout.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //sign in button working
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameExtracted = email.getText().toString().trim();
                String passwordExtracted = password.getText().toString().trim();
                boolean logedIn = loginTheUser(usernameExtracted, passwordExtracted);
                if (logedIn) {
                    //Show the signout menu
                    SignoutMenuValidation.setmState(true);
                    getActivity().invalidateOptionsMenu();
                    //savethe username for further reference
                    setSharedPrefConstant(usernameExtracted);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack("addLoginFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    startDashBoard();

                }


            }
        });


        //signupButton
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignUpScreen();
            }
        });
        return view;
    }

    private void startDashBoard() {
        Log.d(TAG, "IN DashBoard");
        Fragment fragment = new DashboardFrag();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainActivityFragmentLayout, fragment);
        ft.addToBackStack("addDashFrag");
        ft.commit();
    }

    private void setSignUpScreen() {
        Fragment fragment = new SignupFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainActivityFragmentLayout, fragment);
        ft.addToBackStack("addSignUpFrag");
        ft.commit();
    }

    private boolean loginTheUser(String usernameExtracted, String passwordExtracted) {
        if (isValidEmail(usernameExtracted)) {
            //entered email id is in proper format
            if (!TextUtils.isEmpty(usernameExtracted) && !TextUtils.isEmpty(passwordExtracted)) {
                mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
                Cursor cursor = mSqLiteDatabase.query(UserEntry.TABLE_NAME,
                        new String[]{UserEntry.USER_EMAIL, UserEntry.USER_PASSWORD},
                        UserEntry.USER_EMAIL + "=?",
                        new String[]{usernameExtracted}, null, null, null);
                if(cursor.getCount()<=0){
                    Snackbar.make(mLinearLayout, "Please enter registered email id", Snackbar.LENGTH_SHORT).show();
                }
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int passwordColIndex = cursor.getColumnIndex(UserEntry.USER_PASSWORD);
                        String password = cursor.getString(passwordColIndex);
                        if (password.equals(passwordExtracted)) {
                            Snackbar.make(mLinearLayout, "Login Successful!", Snackbar.LENGTH_SHORT).show();
                            return true;
                        } else {
                            Snackbar.make(mLinearLayout, "Incorrect password!", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                    }

                } else {
                    Snackbar.make(mLinearLayout, "Email entered is invalid!", Snackbar.LENGTH_SHORT).show();
                    return false;

                }
            } else {
                Snackbar.make(mLinearLayout, "Please fill all the fields", Snackbar.LENGTH_SHORT).show();
            }

        } else {//not in prpoper format
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Please enter valid email id");
        }


        return false;


    }

    //Checking the format of the email
    private boolean isValidEmail(String usernameExtracted) {
        return usernameExtracted != null && android.util.Patterns.EMAIL_ADDRESS.matcher(usernameExtracted).matches();
    }

    private void setSharedPrefConstant(String usernameExtracted) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", usernameExtracted);
        editor.putBoolean("LOGGED_IN", true);
        editor.commit();

    }

    private void intialiseViews(View view) {
        //Layouts Intz
        emailLayout = (TextInputLayout) view.findViewById(R.id.usernameTextLayout_email_login);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.passwordTextLayout_email_login);
        //Top
        mLinearLayout = (LinearLayout) view.findViewById(R.id.emailLogin_toplayout);

        //AppCompatEditText Intz
        email = (AppCompatEditText) view.findViewById(R.id.usernameField_email_login);
        password = (AppCompatEditText) view.findViewById(R.id.passwordField_email_login);


        //Button Intz
        goButton = (ImageButton) view.findViewById(R.id.goButton);
        signupButton = (Button) view.findViewById(R.id.signupButton_email_login);
    }
}
