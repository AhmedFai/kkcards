package com.kk_cards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk_cards.Adapter.dummy_adapter;
import com.kk_cards.Modal.ItemData;
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
 * Created by user on 4/4/2018.
 */

public class Home_New extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;
    ArrayList<String> id_list,image_list,name_list,count_list;

    ArrayList<ItemData> os_versions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View  v = inflater.inflate(R.layout.home_new, container, false);
        ButterKnife.bind(this, v);

        os_versions=new ArrayList<>();

        getdata();



        return v;
    }
    public void getdata() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/first.php",
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



                            JSONArray banner_array=obj.getJSONArray("banner");

                            for(int i=0;i<arry.length();i++)
                            {

                                ItemData feed=new ItemData();

                                JSONObject objj=arry.getJSONObject(i);

                                id_list.add(objj.getString("id"));
                                image_list.add(objj.getString("image"));
                                name_list.add(objj.getString("name"));
                                count_list.add(objj.getString("count"));

                                feed.setCat_image(objj.getString("image"));
                                feed.setCat_name(objj.getString("name"));

                                os_versions.add(feed);

                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                //



                            }



                            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

                         dummy_adapter adapter=new dummy_adapter(getContext(),os_versions);


                            recyclerview.setLayoutManager(layoutManager);





                            id_list=new ArrayList<String>();
                            image_list=new ArrayList<String>();
                            name_list=new ArrayList<String>();


                            for(int i=0;i<banner_array.length();i++)
                            {

                                JSONObject objj=banner_array.getJSONObject(i);
                                id_list.add(objj.getString("id"));
                                image_list.add(objj.getString("image"));
                                name_list.add(objj.getString("name"));


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("Response",response);


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