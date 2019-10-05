package com.learnforward.downloader;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DownloadItemHelper {

    public static ArrayList<DownloadableItem> loadDownloadItems(SharedPreferences mPrefs) {
        Gson gson = new Gson();
        String json = mPrefs.getString("downloadItems", "");

        Type listType = new TypeToken<ArrayList<DownloadableItem>>() {}.getType();
        ArrayList<DownloadableItem> downloadItems = gson.fromJson(json, listType);
        if(downloadItems==null){
            downloadItems = new ArrayList<DownloadableItem>();
        }
        return downloadItems;
    }

    public static void saveDownloadItems(SharedPreferences mPrefs, ArrayList<DownloadableItem> downloadItems){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(downloadItems); // myObject - instance of MyObject
        prefsEditor.putString("downloadItems", json);
        prefsEditor.commit();
    }
}
