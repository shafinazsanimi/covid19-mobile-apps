package com.example.covid19apps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19apps.guest.DataSource;
import com.example.covid19apps.guest.TimelineAdapter;
import com.example.covid19apps.guest.TimelineItem;

import java.util.List;

public class GuestHomeFragment extends Fragment {

    private RecyclerView timelineRecyclerView;
    private TimelineAdapter adapter;
    private List<TimelineItem> mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_home_guest, container, false);
        timelineRecyclerView = fragmentView.findViewById(R.id.guestPostRV);
        timelineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getListData();
        setupAdapter();
        return fragmentView;
    }

    private void setupAdapter() {
        adapter = new TimelineAdapter(getActivity(), mData);
        timelineRecyclerView.setAdapter(adapter);
    }

    private void getListData() {
        mData = DataSource.getTimelineData();
    }

}
