package com.ibrickedlabs.internshala.WorkshopsData;

import android.net.Uri;
import android.provider.BaseColumns;

public final class WorkshopContract {
    //ContentUri requirements
    public static final String CONTENT_AUTHORITY = "com.ibrickedlabs.internshala.WorkshopsData";
    //Adding the segment to the ContentAuthority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Table name
    public static final String PATH_WORKSHOPS = "workshops";

    public WorkshopContract() {
    }

    //BaseColumns for the _id attribute we used
    public static class WorkshopEntry implements BaseColumns {
        //content uri
        public static final Uri WORKSHOP_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WORKSHOPS);
        //Table name
        public static final String TABLE_NAME = "workshops";
        //Columns
        public static final String _ID = BaseColumns._ID;
        public static final String WORKSHOP_NAME = "wname";
        public static final String WORKSHOP_DETAILS = "wdetails";


    }
}
