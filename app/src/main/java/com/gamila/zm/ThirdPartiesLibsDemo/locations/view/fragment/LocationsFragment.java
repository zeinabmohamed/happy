package com.gamila.zm.ThirdPartiesLibsDemo.locations.view.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.Location;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.presenter.UpadateLocationsListner;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.view.adapter.LocationsRecyclerViewAdapter;
import com.gamila.zm.ThirdPartiesLibsDemo.util.NetworkConnectionUtil;
import com.gamila.zm.ThirdPartiesLibsDemo.util.ShowMessageUtil;
import com.gamila.zm.ThirdPartiesLibsDemo.R;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.presenter.LocationsPresenter;
import com.gamila.zm.ThirdPartiesLibsDemo.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeinab on 4/30/2016.
 */
public class LocationsFragment extends Fragment implements UpadateLocationsListner ,LocationUtil.LocationListenerUtil {

    private static final List<Location> ITEMS = new ArrayList<Location>();

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManger;
    private LocationsRecyclerViewAdapter adapter;
    private LocationsPresenter presenter;
    private ProgressDialog mProgressDialog;

    public static LocationsFragment newInstance() {

        Bundle args = new Bundle();

        LocationsFragment fragment = new LocationsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LocationsPresenter(this);
        LocationUtil.getInstance().connect();
        LocationUtil.getInstance().addLocationListener(this);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        if (view instanceof RecyclerView) {

            recyclerView = (RecyclerView) view;
            mLayoutManger = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManger);
            adapter = new LocationsRecyclerViewAdapter(getActivity(), ITEMS);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // strat load locations

        if(ITEMS.size() == 0)
        loadLocations();

    }

    private void loadLocations() {
        if (NetworkConnectionUtil.isNetworkAvailable(getContext())) {
            presenter.loadLocations();
        } else {
            ShowMessageUtil.showSnackBarWithAction(getView(), getString(R.string.no_internt_connection)
                    , Color.YELLOW, getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadLocations();
                        }
                    }, Color.RED);
        }
    }


    @Override
    public void showLoading() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();

    }

    @Override
    public void onSuccess(List<Location> locations) {

        ITEMS.addAll(locations);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onError(String errorMessage) {
        ShowMessageUtil.showSnackBar(getView(), errorMessage, Color.YELLOW);

    }

    @Override
    public void hideLoading() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtil.getInstance().disconnect();
        LocationUtil.getInstance().removeLocationListener(this);
    }

    @Override
    public void onConnect() {

        LocationUtil.getInstance().getCurrentLocation(getActivity());

        adapter.notifyDataSetChanged();
    }
}
