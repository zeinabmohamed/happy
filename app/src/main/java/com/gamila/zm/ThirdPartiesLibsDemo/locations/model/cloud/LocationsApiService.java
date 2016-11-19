package com.gamila.zm.ThirdPartiesLibsDemo.locations.model.cloud;

import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.LocationsDAO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by zeinab on 4/16/2016.
 */
public interface LocationsApiService {

    /**
     * load  locations
     * @return list of locations
     */
    @GET("33jl2")
    Call<LocationsDAO> loadLocations();



}
