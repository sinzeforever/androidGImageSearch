package com.yahoo.gridimagesearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    GImageList gImageList;
    GImageAdapter gListAdapter;
    StaggeredGridView lvGList;
    TextView tvMsg;
    boolean isAdapterSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.mainMsg);
        lvGList = (StaggeredGridView) findViewById(R.id.GImgList);
        lvGList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
            // Triggered only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView
            getGImgByAPINext();
            // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        // set up G image
        gImageList = new GImageList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Log.d("my", "on query change: " + query);
                gImageList.setQueryString(query);
                searchView.setQuery(query, false);
                getGImgByAPINew();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    public void clearList() {
        isAdapterSet = false;
        gImageList.clear();
        if (gListAdapter != null) {
            gListAdapter.clear();
        }
    }


    public void setUpAdapter() {
        if (!isAdapterSet) {
            hideMsg();
            gListAdapter = new GImageAdapter(this, gImageList.getList());
            lvGList.setAdapter(gListAdapter);
            isAdapterSet = true;
        } else {
            gListAdapter.notifyDataSetChanged();
        }
    }

    public void showImageDialog(GImageItem item) {
        ImageDialog imageDialog = ImageDialog.newInstance(this, item);
        imageDialog.show(getFragmentManager(), "imageDialog");
    }


    public void hideMsg() {
        tvMsg.setVisibility(View.GONE);
    }

    public void showMsg() {
        showMsg("");
    }

    public void showMsg(String input) {
        clearList();
        tvMsg.setText(input);
        tvMsg.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void getGImgByAPINext() {
        gImageList.updateStart();
        getGImgByAPI();
    }

    public void getGImgByAPINew() {
        gImageList.clear();
        clearList();
        getGImgByAPI();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnline() == false) {
            showMsg("No Network Connection");
            Log.d("my", "no network");
            return;
        } else {
            showMsg("No Result");
        }
    }

    public void sendMail(String receiver, String subject, String link) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{receiver});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , "Check out this interesting photo!\n"+ link);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void getGImgByAPI() {

        if (isOnline() == false) {
            showMsg("No Network Connection");
            Log.d("my", "no network");
            return;
        }

        String url = gImageList.getApiUrl();
        if (url == null) {
           showMsg("No Search Results");
        } else {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, null, new JsonHttpResponseHandler() {
                // on success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    gImageList.setUpList(response);
                    setUpAdapter();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("my", "Api call Fail");
                    showMsg("Fail to get data");
                    super.onFailure(statusCode, headers, errorResponse, e);
                }
            });
        }
    }

    public void onClickSetting(MenuItem mi) {
        // handle click here
        // Toast.makeText(this, "open setting", Toast.LENGTH_SHORT).show();
        FilterDialog filterDialog = FilterDialog.newInstance(this, gImageList);
        filterDialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
