package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveTask {
    private static final String SAVE_KEY = "696969";

    public static void saveTaskArray(Context context, ArrayList<String> arrayTask) {
        SharedPreferences save = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = save.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(arrayTask);
        editor.putString(SAVE_KEY, jsonString);
        editor.apply();
    }
    public static ArrayList<String> getTaskArray(Context context) {
        SharedPreferences save = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = save.getString(SAVE_KEY, "");
        if (jsonString.length() == 0) {
            return new ArrayList<>();
        }
        else {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> arrayTask = gson.fromJson(jsonString, type);
            return arrayTask;
        }
    }
}
