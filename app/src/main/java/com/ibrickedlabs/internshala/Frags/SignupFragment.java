package com.ibrickedlabs.internshala.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ibrickedlabs.internshala.UserData.UserContract.UserEntry;

import com.ibrickedlabs.internshala.R;
import com.ibrickedlabs.internshala.UserData.UserDbHelper;


public class SignupFragment extends Fragment {
    //TopLayout for snackbar
    private RelativeLayout topLayout;
    //layouts
    private TextInputLayout firstnameLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout lastnameLayout;
    //fields
    private ImageView profileView;
    private AppCompatEditText firstName;
    private AppCompatEditText lasstName;
    private AppCompatEditText email;
    private AppCompatEditText password;
    //buttons
    private Button signupButton;
    //Database
    private UserDbHelper mUserDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    //Context
    Context context;
    //String username duplicated usernmae
    String alreadyRegisteredUsername;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mUserDbHelper = new UserDbHelper(getContext());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //To overlap the toolbar
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_user, container, false);
        intializeTheViews(view);
        //validations for Fields
        validationForFirstName();
        validationForLastName();
        validationForPassword();
        //onClicking signup Button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushIntoTheDatabase();
            }
        });
        deattachOtherFragments();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //becz it will called soon we enter something & we will remove the error
                if (email.getText().toString().isEmpty() || !email.getText().toString().contains("@")) {
                    emailLayout.setEnabled(true);
                    emailLayout.setError(getString(R.string.enter_valid_email_id));
                } else {
                    emailLayout.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //return the entire view
        return view;
    }

    private void validationForPassword() {
       
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                attachErrorToLayout(password, passwordLayout, getString(R.string.password));
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                attachErrorToLayout(password, passwordLayout, getString(R.string.password));
                if(password.getText().toString().trim().length()<6){
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(getString(R.string.password_len));
                }
                else if ((password.getText().toString().trim().length())>=6){
                    passwordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validationForLastName() {
        lasstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                attachErrorToLayout(lasstName, lastnameLayout, getString(R.string.last_name));
            }
        });
        lasstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                attachErrorToLayout(lasstName, lastnameLayout, getString(R.string.last_name));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validationForFirstName() {

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                attachErrorToLayout(firstName, firstnameLayout, getString(R.string.first_name));
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                attachErrorToLayout(firstName, firstnameLayout, getString(R.string.first_name));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void attachErrorToLayout(AppCompatEditText appCompatEditText, TextInputLayout textInputLayout, String field) {
        if ((appCompatEditText.getText().toString().trim().length()) <= 0) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Enter your " + field);
        } else {
            textInputLayout.setErrorEnabled(false);
        }
    }

    private void deattachOtherFragments() {
        LayoutInflater.from(getContext()).inflate(R.layout.workshop_details_layout, null, false).setVisibility(View.GONE);
    }

    private void pushIntoTheDatabase() {
        //Extract from the fields
        String fnameExtracted = firstName.getText().toString().trim();
        String lnameExtracted = lasstName.getText().toString().trim();
        String emailExtracted = email.getText().toString().trim();
        String passwordExtracted = password.getText().toString().trim();
        if (!TextUtils.isEmpty(fnameExtracted) && !TextUtils.isEmpty(lnameExtracted) && !TextUtils.isEmpty(emailExtracted) && !TextUtils.isEmpty(passwordExtracted)) {
            //check if the email is valid
            if (isValidEmail(emailExtracted)) {
                //check if the email is previously registrered

                if (!isIdRegistered(emailExtracted)) {

                    if(passwordExtracted.length()<6){//password length constraint fails
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError(getString(R.string.password_len));

                    }
                    else{//password satisfies

                        //Drop into contentvalues
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserEntry.USER_EMAIL, emailExtracted);
                        contentValues.put(UserEntry.USER_PASSWORD, passwordExtracted);
                        contentValues.put(UserEntry.USER_FIRST_NAME, fnameExtracted);
                        contentValues.put(UserEntry.USER_LAST_NAME, lnameExtracted);
                        //getWritableDatabase
                        mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
                        // long id=mSqLiteDatabase.insert(UserEntry.TABLE_NAME,null,contentValues);
                        System.out.println(UserEntry.USER_CONTENT_URI + "");
                        Uri uri = getActivity().getApplicationContext().getContentResolver().insert(UserEntry.USER_CONTENT_URI, contentValues);
                        if (uri != null) {
                            Snackbar.make(topLayout, R.string.account_created_succ, Snackbar.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            fm.popBackStack("addSignUpFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } else {
                            Snackbar.make(topLayout, R.string.oope_something, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    //Sorry username already taken
                    alreadyRegisteredUsername = emailExtracted;
                    setErrorForDuplicateUserName(alreadyRegisteredUsername);

                }

            } else {
                //email id is invalid
                emailLayout.setError("Enter Valid email id");

            }

        } else {
            //Field is missing
            Snackbar.make(topLayout, R.string.please_fill_fields, Snackbar.LENGTH_SHORT).show();

        }


    }



    private void setErrorForDuplicateUserName(final String alreadyRegisteredUsername) {
        emailLayout.setErrorEnabled(true);
        emailLayout.setError(getString(R.string.username_taken));
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String reEnteredEmail = email.getText().toString().trim();
                if (TextUtils.isEmpty(reEnteredEmail)) {

                    emailLayout.setError(getString(R.string.please_enter_email));
                } else if (reEnteredEmail.equals(alreadyRegisteredUsername)) {
                    emailLayout.setError(getString(R.string.email_taken));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
        //checking if the username is registered
    private boolean isIdRegistered(String emailExtracted) {
        String projection[] = {
                UserEntry.USER_EMAIL,
                UserEntry.USER_FIRST_NAME,
                UserEntry.USER_LAST_NAME,
                UserEntry.USER_PASSWORD
        };
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(UserEntry.USER_CONTENT_URI, projection, UserEntry.USER_EMAIL + "=?", new String[]{emailExtracted}, null);

        if (cursor.getCount() > 0) {
            //user is existing
            return true;
        } else {
            //user is a non existing user
            return false;

        }


    }


    //checking the format of email
    private boolean isValidEmail(String emailExtracted) {

        return emailExtracted != null && android.util.Patterns.EMAIL_ADDRESS.matcher(emailExtracted).matches();

        }


    private void intializeTheViews(View view) {
        //layouts intz
        firstnameLayout = (TextInputLayout) view.findViewById(R.id.firstNameTextLayout);
        lastnameLayout = (TextInputLayout) view.findViewById(R.id.lastNameTextLayout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.emailTextLayout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.passwordTextLayout_email_login);

        //fields intz
        firstName = (AppCompatEditText) view.findViewById(R.id.firstnameField);
        lasstName = (AppCompatEditText) view.findViewById(R.id.lastnameField);
        email = (AppCompatEditText) view.findViewById(R.id.emailField);
        password = (AppCompatEditText) view.findViewById(R.id.passwordField_email_login);

        //Button intz
        profileView = (ImageView) view.findViewById(R.id.profileImage);
        signupButton = (Button) view.findViewById(R.id.signupBtn);

        topLayout = (RelativeLayout) view.findViewById(R.id.userTopLayout);
    }
}
