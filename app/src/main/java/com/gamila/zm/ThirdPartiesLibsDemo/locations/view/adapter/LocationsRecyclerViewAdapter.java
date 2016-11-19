package com.gamila.zm.ThirdPartiesLibsDemo.locations.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.Location;
import com.gamila.zm.ThirdPartiesLibsDemo.R;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.view.activity.LocationMapActivity;
import com.gamila.zm.ThirdPartiesLibsDemo.util.LocationUtil;
import com.gamila.zm.ThirdPartiesLibsDemo.util.ResourcesUitl;

import java.util.List;

/**
 * Created by zeinab on 4/30/2016.
 */
public class LocationsRecyclerViewAdapter extends
        RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder> {
    private final List<Location> data;
    private final Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mLocationTitle;
        private final TextView mLocationDistance;
        public final View mView;
        public Location item;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLocationTitle = (TextView) itemView.findViewById(R.id.row_locationItem_title);
            mLocationDistance = (TextView) itemView.findViewById(R.id.row_locationItem_distance);
        }
    }


    public LocationsRecyclerViewAdapter(Context context, List<Location> data) {
        this.context = context;
        this.data = data;

        if (LocationUtil.getInstance().getCurrentLocation((Activity) context) == null) {
            LocationUtil.getInstance().activateLocationAccess(context);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_location, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = data.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startMapActivity(holder.item);
            }
        });
        holder.mLocationTitle.setText(holder.item.getCity());


        holder.mLocationDistance.setText(ResourcesUitl.getInstance()
                .getString(R.string.distance, holder.item.getDistance((Activity) context)));
    }

    private void startMapActivity(Location item) {
        Intent mapIntent = new Intent(context, LocationMapActivity.class);
        mapIntent.putExtra(LocationMapActivity.LOCATION_ITEM_KEY, item);
        context.startActivity(mapIntent);

    }


}
