package com.kk_cards.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.kk_cards.Adapter.blog_adapter;
import com.kk_cards.Config;
import com.kk_cards.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 5/15/2018.
 */

public class category_fragment extends Fragment {

    @BindView(R.id.gridview)
    GridView gridview;
    ArrayList<String> id_list,image_list,name_list,count_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.category_grid, null);
        ButterKnife.bind(this, x);


        getdata();

        return x;
    }

    public void getdata() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/categoryBannerApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        id_list=new ArrayList<String>();
                        image_list=new ArrayList<String>();
                        name_list=new ArrayList<String>();
                        count_list=new ArrayList<String>();



                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("category");




                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                id_list.add(objj.getString("categoryID"));
                                image_list.add(objj.getString("categoryImage"));
                                name_list.add(objj.getString("categoryName"));
                                count_list.add(objj.getString("count"));


                            }






                            blog_adapter adapter1 = new blog_adapter(getActivity(),id_list,name_list,image_list,count_list);
                            gridview.setAdapter(adapter1);

                            adapter1.notifyDataSetChanged();







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

// Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }
}