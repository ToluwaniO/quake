package com.green_pastures.awesome_tbee.quake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Quakes>> {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<Quakes> quakeArrayList;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    QuakeAdapter quakeAdapter;
    private final int loaderId = 1;
    String tag = MainActivity.class.getName();
    TextView unAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "OnCreate method called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unAvailable = (TextView)findViewById(R.id.no_data_txtView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(loaderId,null,this);
        }
        else {
            unAvailable.setText("No Internet connection");
            progressBar.setVisibility(View.GONE);
            unAvailable.setVisibility(View.VISIBLE);
        }

        listView = (ListView)findViewById(R.id.listView);
        //QueryUtils queryUtils = new QueryUtils();

        quakeArrayList = new ArrayList<>();

        quakeAdapter = new QuakeAdapter(MainActivity.this, quakeArrayList);

        listView.setAdapter(quakeAdapter);

        quakeArrayList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quakes q = quakeAdapter.getItem(position);

                Uri webUri = Uri.parse(q.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, webUri);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUi(List<Quakes> quakesList){

        quakeAdapter.clear();

        if(quakesList != null && !quakesList.isEmpty()){
            quakeAdapter.addAll(quakesList);
        }

        Log.d("UPDATE UI", "in");


    }

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public Loader<List<Quakes>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthQuakeLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Quakes>> loader, List<Quakes> data) {

        quakeAdapter.clear();

        progressBar.setVisibility(View.GONE);

        if (data != null && data.size() > 0){
            Log.d(tag, data.get(0).getLocation());
            quakeAdapter.addAll(data);
            unAvailable.setVisibility(View.GONE);
        }
        else{
            unAvailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Quakes>> loader) {
        quakeAdapter.clear();
    }


    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, List<Quakes>>{


        @Override
        protected List<Quakes> doInBackground(String... params) {
            if(params.length < 1 || params[0] == null){
                return null;
            }

            return QueryUtils.getQuakes(params[0]);
        }

        @Override
        protected void onPostExecute(List<Quakes> quakesList) {
            updateUi(quakesList);
        }
    }




}
