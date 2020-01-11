package com.example.project3_test1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_ID = "userID";
    static final String PREF_SLIDE_VALUE = "isChecked";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserID(Context context, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.apply();
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    public static void clearUserName(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public static void setSlide(Context context, boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_SLIDE_VALUE, isChecked);
        editor.commit();
    }

    public static boolean getSlide(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_SLIDE_VALUE, false);
    }
}
