package com.sunnyweather.android.ui.place;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyweather.android.R;
import com.sunnyweather.android.logic.model.Place;
import com.sunnyweather.android.ui.weather.WeatherActivity;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Fragment fragment;
    private List<Place> placeList;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeAddress;
        public ViewHolder(View view) {
            super(view);
            placeName = view.findViewById(R.id.placeName);
            placeAddress = view.findViewById(R.id.placeAddress);
        }
    }

    public PlaceAdapter(Fragment fragment, List<Place> placeList) {
        this.fragment = fragment;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Place place = placeList.get(position);
                Intent intent = new Intent(parent.getContext(), WeatherActivity.class);
                intent.putExtra("location_lng", place.getLocation().getLng());
                intent.putExtra("location_lat", place.getLocation().getLat());
                intent.putExtra("place_name", place.getName());
                fragment.startActivity(intent);
                Log.d("Debug", "onClick running...");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
