package com.kk_cards;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk_cards.Adapter.video_list_adapter;


public class videos_fragment extends Fragment {


    RecyclerView homeRecycler;
    video_list_adapter adapter;
    GridLayoutManager manager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view  = inflater.inflate(R.layout.fragment_videos_fragment,container,false);

        homeRecycler = (RecyclerView)view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new video_list_adapter(getContext());
        homeRecycler.setLayoutManager(manager);
        homeRecycler.setAdapter(adapter);

        return view;


    }




}
