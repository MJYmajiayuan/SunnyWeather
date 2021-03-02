package com.sunnyweather.android.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyweather.android.R;
import com.sunnyweather.android.logic.model.Place;
import com.sunnyweather.android.ui.weather.WeatherActivity;

import java.util.List;

public class PlaceFragment extends Fragment {
    PlaceViewModel viewModel;
    private PlaceAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载前面编写的fragment_place布局
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (viewModel.isPlaceSaved()) {
            Place place = viewModel.getSavedPlace();
            Intent intent = new Intent(getContext(), WeatherActivity.class);
            intent.putExtra("location_lng", place.getLocation().getLng());
            intent.putExtra("location_lat", place.getLocation().getLat());
            intent.putExtra("place_name", place.getName());
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        // 初始化组件
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        EditText searchPlaceEdit = (EditText) getActivity().findViewById(R.id.searchPlaceEdit);
        ImageView bgImageView = (ImageView) getActivity().findViewById(R.id.bgImageView);

        // 给RecyclerView设置layoutManager和适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlaceAdapter(this, viewModel.placeList);
        recyclerView.setAdapter(adapter);

        // 给搜索框设置监听器
        searchPlaceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() != null) {
                    viewModel.searchPlaces(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                    bgImageView.setVisibility(View.VISIBLE);
                    viewModel.placeList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    viewModel.searchPlaces(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                    bgImageView.setVisibility(View.VISIBLE);
                    viewModel.placeList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // 对PlaceViewModel中的placeLiveData对象进行观察
        viewModel.placeLiveData.observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> placeList) {
                if (placeList != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    bgImageView.setVisibility(View.GONE);
                    viewModel.placeList.clear();
                    viewModel.placeList.addAll(placeList);
                } else {
                    Toast.makeText(getActivity(), "未能查询到任何地点", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
