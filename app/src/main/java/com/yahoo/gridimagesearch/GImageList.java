package com.yahoo.gridimagesearch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinze on 8/4/15.
 */
public class GImageList {
    public String apiUrl = "https://ajax.googleapis.com/ajax/services/search/images?";
    List<GImageItem> list;
    int start = 0;
    final int count = 8;
    final int limit = 64;
    String queryString;
    String color;
    String size;
    String type;
    String site;

    public GImageList () {
        list = new ArrayList<GImageItem>();
    }
    public String getApiUrl(){
        if (queryString == null) {
            return null;
        } else if (queryString.equals("")) {
            return null;
        } else if (start >= limit) {
            return null;
        }

        String res = apiUrl + "q=" + queryString + "&v=1.0&start=" + start + "&rsz=" + count + getColorArg() + getImgSizeArg() + getTypeArg() + getSiteArg();
        Log.d("my", "apiUrl:  " + res);
        return res;
    }

    public void clear() {
        start = 0;
        list.clear();
    }

    public void updateStart() {
        if (start >= limit) {
            return;
        } else {
            start += count;
        }
    }

    public void setQueryString(String query) {
        queryString = query;
        try {
            queryString = URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            Log.d("my", "Url param encoding fail");
        }
    }

    public void setUpList(JSONObject data) {
        try {
            JSONArray rawItemJsonArray = data.getJSONObject("responseData").getJSONArray("results");
            for (int i = 0; i < rawItemJsonArray.length(); i++) {
                GImageItem item = new GImageItem(rawItemJsonArray.getJSONObject(i));
                list.add(item);
            }
        } catch (Exception e) {
            Log.d("my", "Fail when parsing JSON in GImageList");
        }
    }
    public String getImgSizeArg() {
        String argBase = "&imgsz=";
        String arg;
        if (size == null) {
            return "";
        }

        switch (size) {
            case "none":
                return "";
            case "extra large":
                arg = "xlarge";
                break;
            default:
                arg = size;
                break;
        }
        return argBase + arg;
    }

    public String getColorArg() {
        String argBase = "&imgcolor=";
        String arg;
        if (color == null) {
            return "";
        }

        switch (color) {
            case "none":
                return "";
            default:
                arg = color;
                break;
        }
        return argBase + arg;
    }
    public String getTypeArg() {
        String argBase = "&imgtype=";
        String arg;
        if (type == null) {
            return "";
        }

        switch (type) {
            case "none":
                return "";
            default:
                arg = type;
                break;
        }
        return argBase + arg;
    }

    public String getSiteArg() {
        if (site == null) {
            return "";
        } else if (site.length() == 0) {
            return "";
        } else {
            return "&as_sitesearch=" + site;
        }
    }

    public void setImgSize(String input) {
        size = input;
    }

    public void setColor(String input) {
        color = input;
    }

    public void setType(String input) {
        type = input;
    }

    public void setSite(String input) {
        site = input;
    }

    public List<GImageItem> getList() {
        return list;
    }
}
