package com.kk_cards;

import android.content.Context;
import android.net.Uri;
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
import com.kk_cards.Adapter.blog_adapter;
import com.kk_cards.Adapter.video_list_adapter;
import com.kk_cards.Modal.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class videos_fragment extends Fragment {


    RecyclerView homeRecycler;
    video_list_adapter adapter;
    GridLayoutManager manager;
    private List<Video> video;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view  = inflater.inflate(R.layout.fragment_videos_fragment,container,false);

        homeRecycler = (RecyclerView)view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        video = new ArrayList<>();
        adapter = new video_list_adapter(getContext(),video);
        homeRecycler.setLayoutManager(manager);
        homeRecycler.setAdapter(adapter);

        get_data();

        return view;


    }

    public void get_data(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/videoApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //video=new ArrayList<String>();




                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("video");




                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                Video vid = new Video();

                                vid.setVname(objj.getString("vname"));
                                vid.setThumbnail(objj.getString("thumbnail"));
                                vid.setVurl(objj.getString("vurl"));

                                Log.d("link",objj.getString("vurl"));

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
