package com.ibrickedlabs.internshala.UserData;

import android.net.Uri;
import android.provider.BaseColumns;

import java.security.Security;

public class UserContract {
    //Contract class to define all the constants of the table

    //ContentUri requirements
    public static final String CONTENT_AUTHORITY = "com.ibrickedlabs.internshala.UserData";
    //Adding the segment to the ContentAuthority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Table name
    public static final String PATH_USERS = "users";

    public UserContract() {
    }

    //BaseColumns for the _id attribute we used
    public static final class UserEntry implements BaseColumns {
        //content uri
        public static final Uri USER_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        //Table name
        public static final String TABLE_NAME = "users";
        //column names
        public static final String id = BaseColumns._ID;
        public static final String USER_FIRST_NAME = "firstName";
        public static final String USER_LAST_NAME = "lastName";
        public static final String USER_EMAIL = "email";
        public static final String USER_PASSWORD = "password";
        public static final String WORKSHOP_ANDROID = "android";
        public static final String WORKSHOP_Artificial_Intelligence = "ArtificialIntelligence";
        public static final String WORKSHOP_BigData_Bootcamp = "BigDataBootcamp";
        public static final String WORKSHOP_BlockChain_Technology = "BlockChainTechnology";
        public static final String WORKSHOP_Cyber_Security = "CyberSecurity";
        public static final String WORKSHOP_CloudComputing = "CloudComputing";
        public static final String WORKSHOP_Datascience_Bootcamp = "DatascienceBootcamp";
        public static final String WORKSHOP_IoT = "IoT";
        public static final String WORKSHOP_Machine_Learning = "MachineLearning";
        public static final String WORKSHOP_SAP_Bootcamp = "SAPBootcamp";
        public static final String WORKSHOP_Web_Development = "WebDevelopment";


    }
}
