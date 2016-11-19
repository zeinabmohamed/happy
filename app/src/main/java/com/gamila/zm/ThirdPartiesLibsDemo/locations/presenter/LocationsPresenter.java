package com.gamila.zm.ThirdPartiesLibsDemo.locations.presenter;

import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.cloud.LocationsCloud;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.LocationsDAO;
import com.gamila.zm.ThirdPartiesLibsDemo.util.ResourcesUitl;
import com.gamila.zm.ThirdPartiesLibsDemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeinab on 4/30/2016.
 */
public class LocationsPresenter {

    private final UpadateLocationsListner view;

    public LocationsPresenter(UpadateLocationsListner view) {
        this.view = view;

    }

    public void loadLocations() {

        view.showLoading();
        new LocationsCloud().loadLocations(new Callback<LocationsDAO>() {
            @Override
            public void onResponse(Call<LocationsDAO> call, Response<LocationsDAO> response) {
                view.hideLoading();
                if (response.body() != null) {
                    view.onSuccess(response.body().getTravel_offices());
                } else {
                    view.onError(ResourcesUitl.
                            getInstance().getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onFailure(Call<LocationsDAO> call, Throwable t) {
                view.hideLoading();
                view.onError(ResourcesUitl.
                        getInstance().getString(R.string.server_connection_error));
            }
        });
    }
}
