package com.kk_cards.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kk_cards.Adapter.product_adapter;
import com.kk_cards.Config;
import com.kk_cards.Listener.RecyclerItemClickListener;
import com.kk_cards.Modal.ItemData;
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
 * Created by user on 1/27/2018.
 */

public class sub_cat_list extends Fragment {
    private static int currentPage = 0;
    private static ViewPager mPager;
    View v;
    ArrayList<String> id_list,image_list,name_list;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;


    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    private static final Integer[] XMEN = {R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy};


    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sub_cat_list, container, false);
        ButterKnife.bind(this, v);
        os_versions = new ArrayList<ItemData>();
        mPager=(ViewPager)v.findViewById(R.id.pager);
        p_bar.setProgress(0);



        id=getArguments().getString("id_value");

        Log.d("ddddddddd",id);

        get_sub_category(Config.Base_Url+"/API/categoryApi.php?catid="+id);






/*

        for (int i = 0; i < 4; i++) {
            ItemData feed = new ItemData();
            feed.setCat_name("Spy Sacret Bag Camera");
            //  feed.setCat_image(image_cat_list.get(i));
            // feed.setId(id_value.get(i));
            //   feed.setCat_image(image.get(i));

            os_versions.add(feed);


            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
            product_adapter mAdapter1 = new product_adapter(getActivity(), os_versions);
            mAdapter.setHasFixedSize(true);
            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

            mAdapter.setLayoutManager(gridLayoutManager);


            mAdapter.setAdapter(mAdapter1);
*/


            mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {

                            Intent i=new Intent(getActivity(),product_list.class);
                            i.putExtra("id_value", id);
                            i.putExtra("sub_cat_val", os_versions.get(position).getId());
                            i.putExtra("sub_cat_name", os_versions.get(position).getCat_name());
                            getActivity().startActivity(i);






                            // TODO Handle item click
                        }
                    })
            );





        return v;

    }


    private void hidePDialog() {
        if (p_bar != null) {
            p_bar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);

        }
    }

    public void get_sub_category(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();


                        try {
                            JSONObject obj=new JSONObject(response);

                            JSONArray banner_array=obj.getJSONArray("subCategory");



                            for(int i=0;i<banner_array.length();i++)
                            {
                                JSONObject objj=banner_array.getJSONObject(i);
                                ItemData feed = new ItemData();
                                feed.setCat_name(objj.getString("subcat"));
                                feed.setId(objj.getString("id"));
                                //  feed.setCat_image(image_cat_list.get(i));
                                // feed.setId(id_value.get(i));
                                //   feed.setCat_image(image.get(i));

                                os_versions.add(feed);





                             /*   id_list.add(objj.getString("id"));
                                name_list.add(objj.getString("name"));*/


                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
                            product_adapter mAdapter1 = new product_adapter(getActivity(), os_versions,mAdapter,"sub_category");
                            mAdapter.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("sub_category",response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
                hidePDialog();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

}