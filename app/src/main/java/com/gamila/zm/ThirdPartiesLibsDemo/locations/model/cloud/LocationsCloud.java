package com.gamila.zm.ThirdPartiesLibsDemo.locations.model.cloud;


import com.gamila.zm.ThirdPartiesLibsDemo.AppConstants;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.LocationsDAO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zeinab on 4/16/2016.
 */
public class LocationsCloud {


    private static final String TAG = LocationsCloud.class.getName();
    private Retrofit retrofit;

    /**
     * @return retrofit object used to create the requests
     */
    public Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public void loadLocations(Callback<LocationsDAO> callback) {
        Call<LocationsDAO> loadLocationsCall = retrofit().create(LocationsApiService.class).loadLocations();
        loadLocationsCall.enqueue(callback);
    }


}
