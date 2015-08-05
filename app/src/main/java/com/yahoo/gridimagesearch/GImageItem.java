package com.yahoo.gridimagesearch;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by sinze on 8/4/15.
 */
public class GImageItem {
    public String img;
    public String hdImg;
    public String title;

    public GImageItem(JSONObject rawJson) {

        try {
            img = rawJson.getString("tbUrl");
            title = rawJson.getString("titleNoFormatting");
            hdImg = rawJson.getString("url");
        } catch (Exception e) {
            Log.d("my", "Fail to parse JSON data in GImageITem");
        }
    }
}
