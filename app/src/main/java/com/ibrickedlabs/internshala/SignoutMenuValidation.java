package com.ibrickedlabs.internshala;

//A static class to tack the user login & logout states
public class SignoutMenuValidation {
    static  boolean mState;

    public SignoutMenuValidation() {
    }

    public static boolean ismState() {
        return mState;
    }

    public static void setmState(boolean mState) {
        SignoutMenuValidation.mState = mState;
    }
}
