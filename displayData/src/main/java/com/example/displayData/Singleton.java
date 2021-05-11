package com.example.displayData;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Singleton extends Application {
    public static Singleton INSTANCE;
    public SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public SharedPreferences.Editor editorShowData;
    private SharedPreferences prefsShowData;


    public static Singleton getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }

        return INSTANCE;
    }

    public void setString(String key, String value, Activity activity) {
        editor = getAppPrefs(activity).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, Activity activity) {
        return getAppPrefs(activity).getString(key, "");
    }

    public SharedPreferences getAppPrefs(Activity activity) {

        Context con = null;//first app package name is "com.sharedpref1"
        try {
            con = activity.createPackageContext("com.example.showData", CONTEXT_IGNORE_SECURITY);
            if (prefs == null) {
                if (activity != null)
                    prefs = con.getSharedPreferences("sharedPreferences", activity.MODE_PRIVATE);
                else
                    prefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        SharedPreferences pref = con.getSharedPreferences(
//                "demopref", Context.MODE_PRIVATE);


        return prefs;
    }

    public static <T> String getJsonStringFromObject(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setStringShowData(String key, String value, Activity activity) {
        editorShowData = getAppPrefsShowdata(activity).edit();
        editorShowData.putString(key, value);
        editorShowData.apply();
    }

    public String getStringShowData(String key, Activity activity) {
        return getAppPrefsShowdata(activity).getString(key, "");
    }

    public SharedPreferences getAppPrefsShowdata(Activity activity) {

        Context con = null;//first app package name is "com.sharedpref1"
        try {
            con = activity.createPackageContext("com.example.storeData", CONTEXT_IGNORE_SECURITY);
            if (prefsShowData == null) {
                if (activity != null)
                    prefsShowData = con.getSharedPreferences("sharedPreferencesShowData", activity.MODE_PRIVATE);
                else
                    prefsShowData = getSharedPreferences("sharedPreferencesShowData", Context.MODE_PRIVATE);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return prefsShowData;
    }


}
