package com.gamila.zm.ThirdPartiesLibsDemo.locations.presenter;

import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.Location;

import java.util.List;

/**
 * Created by zeinab on 4/30/2016.
 */
public interface UpadateLocationsListner {


   public void  showLoading();
    public void onSuccess(List<Location> locations);

    public void onError(String errorMessage);

    public void  hideLoading();

}
