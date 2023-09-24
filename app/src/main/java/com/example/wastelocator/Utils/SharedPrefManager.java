package com.example.wastelocator.Utils;

import android.content.SharedPreferences;
public class SharedPrefManager {
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences getSharedPreference(){
        if (sharedPreferences == null){
            sharedPreferences = MyApp.getPreferences();
        }
        return sharedPreferences;
    }
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ROLE = "userRole";
    public static void setLoginState(boolean isLoggedIn){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving login state
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn); // false if not logged in
        editor.apply();
    }

    public static boolean isLoggedIn(){
        return getSharedPreference().getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setAdmin(boolean isAdmin){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving login state
        editor.putBoolean(KEY_USER_ROLE, isAdmin); // false if not logged in
        editor.apply();
    }

    public static boolean isAdmin(){
        return getSharedPreference().getBoolean(KEY_USER_ROLE, false);
    }
}

