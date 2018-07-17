package com.kk_cards.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * Created by user on 1/30/2018.
 */

public class Search_Fragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.product_list_detail, container, false);
        ButterKnife.bind(this, v);

        mAdapter.setVisibility(View.GONE);

        os_versions = new ArrayList<ItemData>();


          String query=getArguments().getString("query");

        getdata(Config.Base_Url+"/search.php?search="+query);

        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i=new Intent(getContext(),product_details.class);
                        i.putExtra("id_value",os_versions.get(position).getId());
                        startActivity(i);



                        // TODO Handle item click
                    }
                })
        );






        return v;


    }


    public void getdata(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();


                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray product_list=obj.getJSONArray("products");

                            for(int i=0;i<product_list.length();i++)
                            {
                                ItemData feed = new ItemData();
                                JSONObject objj=product_list.getJSONObject(i);

                                feed.setId(objj.getString("id"));
                                feed.setCat_name(objj.getString("name"));
                                feed.setCat_image(objj.getString("image"));

                                int discount= Integer.parseInt(objj.getString("discount"));
                                int price=Integer.parseInt(objj.getString("price"));


                                int price_cut=(100-discount)*price/100;
                                feed.setPrice(objj.getString("price"));
                                feed.setPrice_cut(String.valueOf(price_cut));

                                os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                //



                            }


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
                            product_adapter mAdapter1 = new product_adapter(getActivity(), os_versions,mAdapter,"");
                            mAdapter.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);




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
                hidePDialog();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

    private void hidePDialog() {
        if (p_bar != null) {
            p_bar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);

        }
    }
}