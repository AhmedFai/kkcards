package com.kk_cards.Fragment;


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
import android.widget.RelativeLayout;

import com.kk_cards.Adapter.orders_adapter;
import com.kk_cards.Config;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by user on 2/12/2018.
 */

public class my_orders extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @BindView(R.id.progressBar)
    ProgressBar pbar;
    @BindView(R.id.relative)
    RelativeLayout relativeLayout;
    SessionManagement session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.order, null);
        ButterKnife.bind(this, x);

        mAdapter.setVisibility(View.GONE);

        session = new SessionManagement(getContext());


        os_versions = new ArrayList<ItemData>();

        getdata(Config.Base_Url + "/API/fetchOrderApi.php");



/*
        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                     order_details grid=new order_details();

                        Bundle args = new Bundle();
                        args.putString("id",os_versions.get(position).getTxnid());
                        args.putString("product_id",os_versions.get(position).getId());
                        args.putString("order_status",os_versions.get(position).getStatus());

                        grid.setArguments(args);
                        mFragmentManager = getActivity().getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();





                        // TODO Handle item click
                    }
                })
        );*/


        return x;
    }

    public void getdata(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();


                        try {
                            JSONObject obj = new JSONObject(response);
                            //   record_count.setText(obj.getString("count")+" Records found");


                            JSONArray product_list = obj.getJSONArray("order");

                            for (int i = 0; i < product_list.length(); i++) {
                                ItemData feed = new ItemData();
                                JSONObject objj = product_list.getJSONObject(i);

                                feed.setId(objj.getString("productID"));
                                feed.setCat_name(objj.getString("pname"));

                                feed.setTxnid(objj.getString("txnid"));
                                feed.setRate(objj.getString("rate"));

                                feed.setCat_image(objj.getString("image"));
                                feed.setDate(objj.getString("date"));

                                //  String kept = objj.getString("date").substring( 0, objj.getString("date").indexOf(" "));
                                // feed.setDate(kept);
                                //  String only_date=feed.getDate().replace()

                                feed.setStatus(objj.getString("orderStatus"));

                                if ("0".equals(objj.getString("orderStatus")) || "1".equals(objj.getString("orderStatus"))) {

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    try {
                                        Date myDate = dateFormat.parse(objj.getString("date"));
                                        Date newDate = new Date(myDate.getTime() + 604800000L);
                                        String date = dateFormat.format(newDate);
                                        Log.d("date_changed", date);
                                        feed.setDate("Expected Delivery on " + date);


                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                } else if ("2".equals(objj.getString("orderStatus"))) {

                                    feed.setDate("Delivered  " + objj.getString("deliveryDate"));


                                } else if ("5".equals(objj.getString("orderStatus"))) {

                                    feed.setDate("Cancelled  " + objj.getString("deliveryDate"));


                                }


                                if ("0".equals(feed.getStatus()))
                                    feed.setStatus("Pending/ordered");
                                else if ("1".equals(feed.getStatus()))
                                    feed.setStatus("Dispatched");
                                else if ("2".equals(feed.getStatus()))
                                    feed.setStatus("Delivered");
                                else if ("3".equals(feed.getStatus()))
                                    feed.setStatus("Replacement Applied");
                                else if ("4".equals(feed.getStatus()))
                                    feed.setStatus("Delivered to merchant");
                                else if ("5".equals(feed.getStatus()))
                                    feed.setStatus("Order Cancelled");

                                os_versions.add(feed);


                            }


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
                            orders_adapter mAdapter1 = new orders_adapter(getActivity(), os_versions);

                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);

                            mAdapter1.notifyDataSetChanged();


                        } catch (JSONException e) {
                            relativeLayout.setBackgroundResource(R.drawable.emptybag);
                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("Response", response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void hidePDialog() {
        if (pbar != null) {
            pbar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);

        }
    }
}