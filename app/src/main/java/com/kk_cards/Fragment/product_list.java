package com.kk_cards.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk_cards.Adapter.OnLoadMoreListener;
import com.kk_cards.Adapter.product_adapter;
import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Listener.RecyclerItemClickListener;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;


import com.kk_cards.search_activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;




/**
 * Created by user on 1/23/2018.
 */

public class product_list extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;
    private String MY_PREFS_NAME;
    SessionManagement session;
    String cart_counter_real=null;
    protected Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_detail);
        ButterKnife.bind(this);

        session=new SessionManagement(getApplicationContext());




        if(getSupportActionBar()!=null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }
        Log.d("sesssssss",getIntent().getStringExtra("id_value"));

        this.setTitle(getIntent().getStringExtra("sub_cat_name"));





        mAdapter.setVisibility(View.GONE);

        os_versions = new ArrayList<ItemData>();

        if("search_api".equals(getIntent().getStringExtra("id_value")))
        {

            get_data(getIntent().getStringExtra("sub_cat_name"),Config.Base_Url+"/API/searchApi.php");

        }

        else {

            String id = getIntent().getStringExtra("id_value");
            String sub_cat_id = getIntent().getStringExtra("sub_cat_val");

            Log.d("idddddddddd", id);


            getdata(Config.Base_Url + "/API/productListApi.php?catid=" + id + "&scatid=" + sub_cat_id);
        }
        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {

                            Intent i=new Intent(getApplicationContext(),product_details.class);
                            i.putExtra("id_value",os_versions.get(position).getProductID());
                            i.putExtra("image_path","no_images");
                            startActivity(i);

                           /* product_details grid=new product_details();

                       Bundle args = new Bundle();
                        args.putString("id_value",os_versions.get(position).getId());
                        grid.setArguments(args);
                            mFragmentManager = getActivity().getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();


*/


                            // TODO Handle item click
                        }
                    })
            );

    }


    public void getdata(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        Log.d("Response_listing",response);

                        try {
                            JSONObject obj=new JSONObject(response);


                            final JSONArray product_list=obj.getJSONArray("products");

                            if(product_list.length()>10) {

                                for (int i = 0; i < product_list.length(); i++) {
                                    ItemData feed = new ItemData();
                                    JSONObject objj = product_list.getJSONObject(i);

                                    feed.setProductID(objj.getString("productID"));
                                    feed.setCategoryID(objj.getString("categoryID"));
                                    feed.setProductName(objj.getString("productName"));
                                    feed.setProductImage(objj.getString("productImage"));

                                   // feed.setDiscount(objj.getString("discount"));

                                   /* int discount = Integer.parseInt(objj.getString("discount"));
                                    int price = Integer.parseInt(objj.getString("price"));*/


                                    //int price_cut = (100 - discount) * price / 100;
                                    feed.setPrice(objj.getString("price"));
                                    feed.setMrp(objj.getString("mrp"));
                                   // feed.setPrice_cut(String.valueOf(price_cut));

                                    os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                    //


                                }


                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                                final product_adapter mAdapter1 = new product_adapter(getApplicationContext(), os_versions, mAdapter, "");
                                mAdapter.setHasFixedSize(true);
                                // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                mAdapter.setLayoutManager(gridLayoutManager);


                                mAdapter.setAdapter(mAdapter1);

                                if (os_versions.isEmpty()) {
                                    mAdapter.setVisibility(View.GONE);
                                    // tvEmptyView.setVisibility(View.VISIBLE);

                                } else {
                                    mAdapter.setVisibility(View.VISIBLE);
                                    //  tvEmptyView.setVisibility(View.GONE);
                                }

                                mAdapter1.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {


                                        //add null , so the adapter will check view_type and show progress bar at bottom
                                        os_versions.add(null);
                                        mAdapter1.notifyItemInserted(os_versions.size() - 1);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //   remove progress item
                                                os_versions.remove(os_versions.size() - 1);
                                                mAdapter1.notifyItemRemoved(os_versions.size());
                                                //add items one by one
                                                int start = os_versions.size();
                                                int end = start + 10;

                                                for (int i = start; i <= end; i++) {
                                                    ItemData feed = new ItemData();

                                                    JSONObject objj = null;
                                                    try {
                                                        objj = product_list.getJSONObject(i);
                                                        feed.setProductID(objj.getString("productID"));
                                                        feed.setProductName(objj.getString("productName"));
                                                        feed.setProductImage(objj.getString("productName"));
                                                        feed.setCategoryID(objj.getString("categoryID"));

                                                        feed.setMrp(objj.getString("mrp"));

                                                        /*int discount = Integer.parseInt(objj.getString("discount"));
                                                        int price = Integer.parseInt(objj.getString("price"));*/


                                                       // int price_cut = (100 - discount) * price / 100;
                                                        feed.setPrice(objj.getString("price"));
                                                        //feed.setPrice_cut(String.valueOf(price_cut));

                                                        os_versions.add(feed);
                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }


                                                mAdapter1.notifyDataSetChanged();
                                                mAdapter1.setLoaded();
                                                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                            }
                                        }, 2000);


                                    }
                                });


                            }
                            else
                            {


                                    for (int i = 0; i < product_list.length(); i++) {
                                        ItemData feed = new ItemData();
                                        JSONObject objj = product_list.getJSONObject(i);

                                        feed.setProductID(objj.getString("productID"));
                                        feed.setProductImage(objj.getString("productName"));
                                        feed.setProductImage(objj.getString("productImage"));
                                        feed.setCategoryID(objj.getString("categoryID"));

                                        feed.setMrp(objj.getString("mrp"));

                                       /* int discount = Integer.parseInt(objj.getString("discount"));
                                        int price = Integer.parseInt(objj.getString("price"));*/


                                       // int price_cut = (100 - discount) * price / 100;
                                        feed.setPrice(objj.getString("price"));
                                       // feed.setPrice_cut(String.valueOf(price_cut));

                                        os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                        //


                                    }


                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                                    product_adapter mAdapter1 = new product_adapter(getApplicationContext(), os_versions, mAdapter, "");
                                    mAdapter.setHasFixedSize(true);
                                    // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                    mAdapter.setLayoutManager(gridLayoutManager);


                                    mAdapter.setAdapter(mAdapter1);

                                    if (os_versions.isEmpty()) {
                                        mAdapter.setVisibility(View.GONE);
                                        // tvEmptyView.setVisibility(View.VISIBLE);

                                    } else {
                                        mAdapter.setVisibility(View.VISIBLE);
                                        //  tvEmptyView.setVisibility(View.GONE);
                                    }



                            }

                        } catch (JSONException e) {

                            mAdapter.setVisibility(View.GONE);

                            ImageView iv = new ImageView(getApplicationContext());
                            LinearLayout layot = (LinearLayout) findViewById(R.id.layout);

                            // Set an image for ImageView
                            iv.setImageResource(R.drawable.emptybag);

                            // Create layout parameters for ImageView
                            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            // Add rule to layout parameters
                            // Add the ImageView below to Button

                            // Add layout parameters to ImageView
                            iv.setLayoutParams(lp);

                            layot.addView(iv);

                            // Finally, add the ImageView to layout

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
                hidePDialog();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }
    public void get_data(final String search, String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hidePDialog();

                        try {
                            JSONObject obj=new JSONObject(response);


                            final JSONArray product_list=obj.getJSONArray("products");

                            if(product_list.length()>10) {

                                for (int i = 0; i < product_list.length(); i++) {
                                    ItemData feed = new ItemData();
                                    JSONObject objj = product_list.getJSONObject(i);

                                    feed.setId(objj.getString("id"));
                                    feed.setCat_name(objj.getString("name"));
                                    feed.setCat_image(objj.getString("image"));

                                    feed.setDiscount(objj.getString("discount"));

                                    int discount = Integer.parseInt(objj.getString("discount"));
                                    int price = Integer.parseInt(objj.getString("price"));


                                    int price_cut = (100 - discount) * price / 100;
                                    feed.setPrice(objj.getString("price"));
                                    feed.setPrice_cut(String.valueOf(price_cut));

                                    os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                    //


                                }


                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                                final product_adapter mAdapter1 = new product_adapter(getApplicationContext(), os_versions, mAdapter, "");
                                mAdapter.setHasFixedSize(true);
                                // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                mAdapter.setLayoutManager(gridLayoutManager);


                                mAdapter.setAdapter(mAdapter1);

                                if (os_versions.isEmpty()) {
                                    mAdapter.setVisibility(View.GONE);
                                    // tvEmptyView.setVisibility(View.VISIBLE);

                                } else {
                                    mAdapter.setVisibility(View.VISIBLE);
                                    //  tvEmptyView.setVisibility(View.GONE);
                                }

                                mAdapter1.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {


                                        //add null , so the adapter will check view_type and show progress bar at bottom
                                        os_versions.add(null);
                                        mAdapter1.notifyItemInserted(os_versions.size() - 1);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //   remove progress item
                                                os_versions.remove(os_versions.size() - 1);
                                                mAdapter1.notifyItemRemoved(os_versions.size());
                                                //add items one by one
                                                int start = os_versions.size();
                                                int end = start + 10;

                                                for (int i = start; i <= end; i++) {
                                                    ItemData feed = new ItemData();

                                                    JSONObject objj = null;
                                                    try {
                                                        objj = product_list.getJSONObject(i);
                                                        feed.setId(objj.getString("id"));
                                                        feed.setCat_name(objj.getString("name"));
                                                        feed.setCat_image(objj.getString("image"));

                                                        feed.setDiscount(objj.getString("discount"));

                                                        int discount = Integer.parseInt(objj.getString("discount"));
                                                        int price = Integer.parseInt(objj.getString("price"));


                                                        int price_cut = (100 - discount) * price / 100;
                                                        feed.setPrice(objj.getString("price"));
                                                        feed.setPrice_cut(String.valueOf(price_cut));

                                                        os_versions.add(feed);
                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }


                                                mAdapter1.notifyDataSetChanged();
                                                mAdapter1.setLoaded();
                                                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                            }
                                        }, 2000);


                                    }
                                });


                            }
                            else
                            {


                                for (int i = 0; i < product_list.length(); i++) {
                                    ItemData feed = new ItemData();
                                    JSONObject objj = product_list.getJSONObject(i);

                                    feed.setId(objj.getString("id"));
                                    feed.setCat_name(objj.getString("name"));
                                    feed.setCat_image(objj.getString("image"));

                                    feed.setDiscount(objj.getString("discount"));

                                    int discount = Integer.parseInt(objj.getString("discount"));
                                    int price = Integer.parseInt(objj.getString("price"));


                                    int price_cut = (100 - discount) * price / 100;
                                    feed.setPrice(objj.getString("price"));
                                    feed.setPrice_cut(String.valueOf(price_cut));

                                    os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                    //


                                }


                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                                product_adapter mAdapter1 = new product_adapter(getApplicationContext(), os_versions, mAdapter, "");
                                mAdapter.setHasFixedSize(true);
                                // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                mAdapter.setLayoutManager(gridLayoutManager);


                                mAdapter.setAdapter(mAdapter1);

                                if (os_versions.isEmpty()) {
                                    mAdapter.setVisibility(View.GONE);
                                    // tvEmptyView.setVisibility(View.VISIBLE);

                                } else {
                                    mAdapter.setVisibility(View.VISIBLE);
                                    //  tvEmptyView.setVisibility(View.GONE);
                                }



                            }

                        } catch (JSONException e) {

                            mAdapter.setVisibility(View.GONE);

                            ImageView iv = new ImageView(getApplicationContext());
                            LinearLayout layot = (LinearLayout) findViewById(R.id.layout);

                            // Set an image for ImageView
                            iv.setImageResource(R.drawable.emptybag);

                            // Create layout parameters for ImageView
                            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            // Add rule to layout parameters
                            // Add the ImageView below to Button

                            // Add layout parameters to ImageView
                            iv.setLayoutParams(lp);

                            layot.addView(iv);

                            // Finally, add the ImageView to layout

                            e.printStackTrace();
                        }


                        // Result handling


                        // Result handling
                        Log.d("Response",response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();


            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("search",search);

                return params;
            }

        };

// Add the request to the queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }


    private void hidePDialog() {
        if (p_bar != null) {
            p_bar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
     final TextView cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);



        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_to_cart.class);
                i.putExtra("test","");
                startActivity(i);

            }
        });

        if(session.isLoggedIn()==true) {
            cart_counter(new CallBack() {
                @Override
                public void onSuccess(String data) {


                    try {
                        JSONObject obj=new JSONObject(data);
                        cart_counter_real=obj.getString("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    if(session.isLoggedIn()==true) {
                        if (!"0".equals(cart_counter_real)) {
                            cartcounterTV.setVisibility(View.VISIBLE);
                            cartcounterTV.setText(cart_counter_real);
                        }
                        else
                            cartcounterTV.setVisibility(View.GONE);

                    }


                }

                @Override
                public void onFail(String msg) {
                    //  Log.d("jhvfff", "failed");

                }
            });

        }



        else
        {
            DatabaseHandler db=new DatabaseHandler(getApplicationContext());

            String cart_value= String.valueOf(db.count_rows());



            if (!"0".equals(cart_value)) {
                cartcounterTV.setVisibility(View.VISIBLE);
                cartcounterTV.setText(cart_value);
            }
            else
                cartcounterTV.setVisibility(View.GONE);



        }

        return true;
    }

    protected void cart_counter(final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/cartCounterApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssssssssss",response);


                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();

        }
        else if(item.getItemId()==R.id.action_search){
            Intent i = new Intent(this,search_activity.class);
            startActivity(i);
            return true;
        }

        else if (id == R.id.cart) {
            Intent i=new Intent(getApplicationContext(),add_to_cart.class);
            i.putExtra("test","");
            startActivity(i);


        }

        else if(item.getItemId()==R.id.action_search){
            Intent i = new Intent(this,search_activity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    static class VersionHelper
    {
        static void refreshActionBarMenu(Activity activity)
        {
            activity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        VersionHelper.refreshActionBarMenu(this);
        // put your code here...

    }
}