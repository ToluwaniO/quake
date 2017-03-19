package com.green_pastures.awesome_tbee.quake;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by tbee on 3/16/17.
 */

public class EarthQuakeLoader extends AsyncTaskLoader<List<Quakes>> {

    String mUrl;
    String tag = EarthQuakeLoader.class.getName();

    public EarthQuakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Quakes> loadInBackground() {

        List<Quakes> quakes = QueryUtils.getQuakes(mUrl);

        return quakes;
    }

}
