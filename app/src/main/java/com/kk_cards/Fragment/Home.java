package com.kk_cards.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.kk_cards.Adapter.Card_adapter;
import com.kk_cards.Adapter.MyAdapter;
import com.kk_cards.Adapter.TopSellingadapter;
import com.kk_cards.Adapter.category_adapter;
import com.kk_cards.Adapter.super_save_adapter;
import com.kk_cards.BroadcastReceiver.NetworkStatusChangeReceiver;
import com.kk_cards.ClickableViewPager;
import com.kk_cards.Config;
import com.kk_cards.ExpandableHeightGridView;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by user on 1/23/2018.
 */

public class Home extends Fragment {


    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    private static ClickableViewPager mPager;
    @BindView(R.id.recyclerview)
    RecyclerView catgory_grid;
    @BindView(R.id.top_offer)
    RecyclerView top_offer;
    @BindView(R.id.top_seling)
    RecyclerView top_seling;
    @BindView(R.id.super_save)
    RecyclerView super_save;
    @BindView(R.id.gridview_popular)
    ExpandableHeightGridView popular_grid;
    @BindView(R.id.gridview_latest)
    ExpandableHeightGridView latest_grid;
    ArrayList<String> id_list,sub_cat_name_list,image_list,name_list,count_list,product_id_list,sub_cat_list,cat_list;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;

    @BindView(R.id.linear_layout)
    LinearLayout linear_layout;
    @BindView(R.id.scroll)
    ScrollView scroll;


    @BindView(R.id.latset_linesr)
    LinearLayout latset_linesr;
    @BindView(R.id.pop_linear)
    LinearLayout pop_linear;


    ArrayList<ItemData> os_versions;
    ArrayList<ItemData>super_list;
    ArrayList<ItemData>topselling_list;

    NetworkStatusChangeReceiver reciver;
TopSellingadapter sellingadapter;
    View v;
    private String MY_PREFS_NAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, v);



    /*    ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll);
        scrollView.setFocusableInTouchMode(true);
        scrollView.fullScroll(ScrollView.FOCUS_UP);*/

        p_bar.setProgress(0);
        linear_layout.setVisibility(View.GONE);
        mPager=(ClickableViewPager) v.findViewById(R.id.pager);
       // catgory_grid.setExpanded(true);
        popular_grid.setExpanded(true);
        latest_grid.setExpanded(true);


        if (true==NetworkStatusChangeReceiver.isConnected(getContext())) {
            getdata();
            get_super_save();
            get_top_offer();
            get_top_selling();

            get_popular_latest(Config.Base_Url + "/API/popularApi.php", "popular");
            get_popular_latest(Config.Base_Url + "/API/latestApi.php", "latest");
        }
        else {

            p_bar.setVisibility(View.GONE);
            latset_linesr.setVisibility(View.GONE);
            pop_linear.setVisibility(View.GONE);
            //scroll.setBackgroundResource(R.drawable.no_internett);
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }

        mPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                if("product".equals(name_list.get(position)))

                {
                    Intent i=new Intent(getContext(),product_details.class);
                    i.putExtra("id_value",product_id_list.get(position));
                    i.putExtra("image_path","no_images");
                    startActivity(i);

                }

                if("category".equals(name_list.get(position)))

                {
                    Intent i=new Intent(getContext(),product_list.class);
                    i.putExtra("id_value",cat_list.get(position));
                    i.putExtra("sub_cat_val", sub_cat_list.get(position));
                    i.putExtra("sub_cat_name","");
                    startActivity(i);

                }





            }
        });

        super_save.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i=new Intent(getContext(),product_details.class);
                        i.putExtra("id_value",super_list.get(position).getId());
                        Log.d("pppppp",super_list.get(position).getId());
                        i.putExtra("image_path","no_images");
                        startActivity(i);




                        // TODO Handle item click
                    }
                })
        );
        top_offer.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i=new Intent(getContext(),product_details.class);
                        i.putExtra("id_value",super_list.get(position).getId());
                        Log.d("iiiddd",super_list.get(position).getId());
                        i.putExtra("image_path","no_images");
                        startActivity(i);




                        // TODO Handle item click
                    }
                })
        );


        top_seling.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i=new Intent(getContext(),product_details.class);
                        i.putExtra("id_value",topselling_list.get(position).getId());
                        i.putExtra("image_path","no_images");
                        startActivity(i);




                        // TODO Handle item click
                    }
                })
        );
        return v;


    }


    public void get_super_save() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/superSaverApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        super_list = new ArrayList<ItemData>();
                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("superSaver");




                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                ItemData feed = new ItemData();
                                feed.setId(objj.getString("id"));
                                feed.setCat_name(objj.getString("name"));
                                feed.setCat_image(objj.getString("image"));

                                int discount= Integer.parseInt(objj.getString("discount"));
                                int price=Integer.parseInt(objj.getString("price"));


                                int price_cut=(100-discount)*price/100;
                                feed.setPrice(objj.getString("price"));
                                feed.setPrice_cut(String.valueOf(price_cut));

                                feed.setCheck("Super_Saver");

                                super_list.add(feed);


                            }






                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



                            super_save_adapter mAdapter1 = new super_save_adapter(getContext(),super_list);
                            super_save.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            super_save.setLayoutManager(gridLayoutManager);


                            super_save.setAdapter(mAdapter1);




                        } catch (JSONException e) {

                            linear_layout.setBackgroundResource(R.drawable.emptybag);
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

    public void get_top_offer() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/topOfferApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        super_list = new ArrayList<ItemData>();
                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("topOffer");



                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                ItemData feed = new ItemData();
                                feed.setId(objj.getString("id"));
                                feed.setCat_name(objj.getString("name"));
                                feed.setCat_image(objj.getString("image"));

                                feed.setCheck("Offer");
                                super_list.add(feed);


                            }






                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



                            super_save_adapter mAdapter1 = new super_save_adapter(getContext(),super_list);
                            top_offer.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            top_offer.setLayoutManager(gridLayoutManager);


                            top_offer.setAdapter(mAdapter1);




                        } catch (JSONException e) {

                            linear_layout.setBackgroundResource(R.drawable.emptybag);
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

    public void get_top_selling() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/topSellingApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        topselling_list = new ArrayList<ItemData>();
                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("topSelling");




                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                ItemData feed = new ItemData();
                                feed.setId(objj.getString("id"));
                                feed.setCat_name(objj.getString("name"));
                                feed.setCat_image(objj.getString("image"));
                                feed.setCheck("Offer");
                                topselling_list.add(feed);


                            }






                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



                            TopSellingadapter mAdapter1 = new TopSellingadapter(getContext(),topselling_list);
                            top_seling.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            top_seling.setLayoutManager(gridLayoutManager);


                            top_seling.setAdapter(mAdapter1);









                        } catch (JSONException e) {

                            linear_layout.setBackgroundResource(R.drawable.emptybag);
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


    public void getdata() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/categoryBannerApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        id_list=new ArrayList<String>();
                        image_list=new ArrayList<String>();
                        name_list=new ArrayList<String>();
                        count_list=new ArrayList<String>();
                        product_id_list=new ArrayList<String>();
                        cat_list=new ArrayList<String>();
                        sub_cat_name_list=new ArrayList<String>();
                        sub_cat_list=new ArrayList<String>();

                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray arry=obj.getJSONArray("category");



                            JSONArray banner_array=obj.getJSONArray("banner");

                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                id_list.add(objj.getString("id"));
                                image_list.add(objj.getString("image"));
                                name_list.add(objj.getString("name"));
                                count_list.add(objj.getString("count"));

                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                               //



                            }



                            Log.d("hcduci",id_list.get(0));
/*

                            blog_adapter adapter1 = new blog_adapter(getActivity(),id_list,name_list,image_list,count_list);
                            catgory_grid.setAdapter(adapter1);

                            adapter1.notifyDataSetChanged();
*/


                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



                            category_adapter mAdapter1 = new category_adapter(getContext(), id_list,name_list,image_list,count_list);
                            catgory_grid.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            catgory_grid.setLayoutManager(gridLayoutManager);


                            catgory_grid.setAdapter(mAdapter1);


                            id_list=new ArrayList<String>();
                            image_list=new ArrayList<String>();
                            name_list=new ArrayList<String>();


                            for(int i=0;i<banner_array.length();i++)
                            {

                                JSONObject objj=banner_array.getJSONObject(i);
                                id_list.add(objj.getString("id"));
                                image_list.add(objj.getString("image"));
                                name_list.add(objj.getString("btype"));
                                cat_list.add(objj.getString("catID"));
                                sub_cat_list.add(objj.getString("scatID"));
                                product_id_list.add(objj.getString("productID"));
                             //   sub_cat_name_list.add(objj.getString("productID"));




                            }


                            mPager.setAdapter(new MyAdapter(getActivity(),image_list));
                            CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
                            //  indicator.setBackgroundColor(Color.parseColor("#E43F3F"));
                            indicator.setViewPager(mPager);

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == image_list.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);




                        } catch (JSONException e) {

                            linear_layout.setBackgroundResource(R.drawable.emptybag);
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
            linear_layout.setVisibility(View.VISIBLE);

        }
    }

    public void get_popular_latest(String url, final String check) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        os_versions = new ArrayList<ItemData>();

                        try {
                            JSONObject obj=new JSONObject(response);

                            JSONArray arry = null;

                            if (obj.has("popular")) {
                                arry= obj.getJSONArray("popular");


                            }
                            else if(obj.has("latest"))
                            {
                                arry= obj.getJSONArray("latest");

                            }


                            for(int i=0;i<arry.length();i++)
                            {

                                JSONObject objj=arry.getJSONObject(i);

                                ItemData feed = new ItemData();
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





                            Card_adapter adapter = new Card_adapter(getActivity(),os_versions);


                            if("popular".equals(check))
                                popular_grid.setAdapter(adapter);
                            else
                                latest_grid.setAdapter(adapter);

                            adapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            linear_layout.setBackgroundResource(R.drawable.emptybag);

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

}