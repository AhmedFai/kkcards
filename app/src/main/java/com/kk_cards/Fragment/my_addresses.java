package com.kk_cards.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk_cards.Adapter.display_add_adapter;
import com.kk_cards.BroadcastReceiver.NetworkStatusChangeReceiver;
import com.kk_cards.Config;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2/7/2018.
 */

public class my_addresses extends Fragment
{

    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;

    @BindView(R.id.myCardView)
    CardView card;
    @BindView(R.id.count_address)
    TextView count_address;

    @BindView(R.id.add_address)
    TextView add_address;

    @BindView(R.id.progressBar)
    ProgressBar p_bar;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SessionManagement session;
    ArrayList<ItemData> os_versions;
    View v;
    String key_mobile;
    private String MY_PREFS_NAME ;
    int MODE_PRIVATE;
    NetworkStatusChangeReceiver reciver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.product_list_detail, container, false);
        ButterKnife.bind(this, v);

        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
        key_mobile=user.get(SessionManagement.KEY_MOBILE);



        card.setVisibility(View.VISIBLE);
        count_address.setVisibility(View.VISIBLE);
        p_bar.setVisibility(View.GONE);
        os_versions = new ArrayList<ItemData>();

        if (true==NetworkStatusChangeReceiver.isConnected(getContext()))
            display_address(Config.Base_Url+"/API/addressApi.php?mobile="+key_mobile);
        else
            count_address.setText("No Internet Found");


        return v;
    }





    @OnClick(R.id.add_address)
    protected void add_address(){

        add_new_address grid = new add_new_address();
        Bundle arg=new Bundle();
        arg.putString("check_value","add");
        arg.putString("address_id","");
        grid.setArguments(arg);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();

    }


    public void display_address(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj=new JSONObject(response);


                            if(obj.has("success"))

                                if("false".equals(obj.getString("success")))
                                {

                                    count_address.setText("No Address Found");
                                }




                                    JSONArray product_list = obj.getJSONArray("products");

                                    for (int i = 0; i < product_list.length(); i++) {
                                        ItemData feed = new ItemData();
                                        JSONObject objj = product_list.getJSONObject(i);

                                        feed.setId(objj.getString("add_id"));
                                        feed.setCat_name(objj.getString("name"));

                                        feed.setCity(objj.getString("city"));
                                        feed.setArea(objj.getString("locality"));
                                        feed.setLandmark(objj.getString("landmark"));

                                        feed.setPincode(objj.getString("pincode"));


                                        feed.setState(objj.getString("state"));
                                        feed.setFlat_no(objj.getString("flatno"));

                                        feed.setMobile(objj.getString("amobile"));


                                        feed.setAdd_type(objj.getString("addtype"));



                                        os_versions.add(feed);


                                        if (i == 0) {


                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                            editor.putString("name", os_versions.get(i).getCat_name());
                                            editor.putString("phone", os_versions.get(i).getMobile());
                                            editor.putString("address", os_versions.get(i).getFlat_no() + " , " + os_versions.get(i).getArea() + " , " + os_versions.get(i).getCity() + " , " + os_versions.get(i).getState() + " - " + os_versions.get(i).getPincode() + " , " + os_versions.get(i).getLandmark());

                                            editor.commit();

                                        }





                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                        //


                                    }


                                    count_address.setText(os_versions.size() + " " + "Saved Address");


                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
                                    display_add_adapter mAdapter1 = new display_add_adapter(getActivity(), os_versions, "", "");
                                    mAdapter.setHasFixedSize(true);
                                    // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                    mAdapter.setLayoutManager(gridLayoutManager);


                                    mAdapter.setAdapter(mAdapter1);

                          /*  if(mAdapter1.getItemCount()==0)
                                Toast.makeText(getContext(), "nnnnnnnnnooooo", Toast.LENGTH_SHORT).show();
*/



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
