package com.kk_cards.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kk_cards.Adapter.discover_adapter;
import com.kk_cards.Adapter.video_list_adapter;
import com.kk_cards.Config;
import com.kk_cards.Modal.Video;
import com.kk_cards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class discovery extends Fragment {

    RecyclerView homeRecycler;
    discover_adapter adapter;
    GridLayoutManager manager;
    private List<Video> video;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        homeRecycler = (RecyclerView) view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        video = new ArrayList<>();
        adapter = new discover_adapter(getContext(), video);
        homeRecycler.setLayoutManager(manager);
        homeRecycler.setAdapter(adapter);

        get_data();

        return view;
    }


    public void get_data() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url + "/API/discoverApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //video=new ArrayList<String>();


                        try {
                            JSONObject obj = new JSONObject(response);


                            JSONArray arry = obj.getJSONArray("discover");


                            for (int i = 0; i < arry.length(); i++) {

                                JSONObject objj = arry.getJSONObject(i);

                                Video vid = new Video();

                                vid.setFtype(objj.getString("ftype"));
                                vid.setDisUrl(objj.getString("disUrl"));
                                vid.setDisThumbnail(objj.getString("disThumbnail"));
                                vid.setTitle(objj.getString("title"));
                                vid.setContent(objj.getString("content"));
                                vid.setDate(objj.getString("Date"));

                                Log.d("link", objj.getString("disUrl"));

                                video.add(vid);



                              /*  id_list.add(objj.getString("id"));
                                name_list.add(objj.getString("name"));
                                image_list.add(objj.getString("thumbnail"));
                                count_list.add(objj.getString("vurl"));
                                date_list.add(objj.getString("dat"));
*/

                            }




/*


                             adapter = new video_list_adapter(getActivity(),id_list,name_list,image_list,count_list,date_list);
                            homeRecycler.setAdapter(adapter);
*/

                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {

                            //   linear_layout.setBackgroundResource(R.drawable.emptybag);
                            e.printStackTrace();
                        }


                        // Result handling


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();


            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }


}
