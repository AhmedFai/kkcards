package com.kk_cards;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kk_cards.Adapter.OnLoadMoreListener;
import com.kk_cards.Adapter.review_Adapter;
import com.kk_cards.Modal.ItemData;

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
 * Created by user on 4/24/2018.
 */

public class All_reviews extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> review_versions,rate_versions;
    protected Handler handler;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_review);
        ButterKnife.bind(this);

        session=new SessionManagement(getApplicationContext());

        review_versions = new ArrayList<ItemData>();
        rate_versions = new ArrayList<ItemData>();

        get_vote(new CallBack() {
            @Override
            public void onSuccess(String data) {

                try
                {
                    JSONObject obj=new JSONObject("data");

                    JSONArray array=obj.getJSONArray("review");

                    for(int i=0;i<array.length();i++)
                    {
                        ItemData feed=new ItemData();
                        JSONObject objj=array.getJSONObject(i);
                        feed.setId(objj.getString("id"));
                        feed.setUp_vote(objj.getString("dvote"));
                        feed.setDown_vote(objj.getString("downvote"));

                        rate_versions.add(feed);



                    }


                }
                catch(Exception e)
                {


                }





            }
            @Override
            public void onFail(String msg) {

                // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                Log.d("jhvfff", "failed");
                // Do Stuff
            }
        });
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }

        this.setTitle("All Reviews");


        get_review(new CallBack() {
            @Override
            public void onSuccess(String data) {

                Log.d("loginnnnninnnter", data);
                try {
                    JSONObject obj = new JSONObject(data);

                    final JSONArray review=obj.getJSONArray("review");

                    if(review.length()>10) {


                    for(int i=0;i<review.length();i++) {
                        ItemData feed = new ItemData();
                        JSONObject objj = review.getJSONObject(i);

                        feed.setId(objj.getString("id"));
                        feed.setMobile(objj.getString("mobile"));
                        feed.setCat_name(objj.getString("name"));
                        feed.setRate(objj.getString("rate"));
                        feed.setReview(objj.getString("review"));

                        feed.setDate(objj.getString("date"));

                        feed.setUp_vote(objj.getString("upvote"));
                        feed.setDown_vote(objj.getString("downvote"));

                        if ("1".equals(feed.getRate()))
                            feed.setShort_des_review("Bad Product");
                        if ("2".equals(feed.getRate()))
                            feed.setShort_des_review("Average");
                        if ("3".equals(feed.getRate()))
                            feed.setShort_des_review("Good Product");
                        if ("4".equals(feed.getRate()))
                            feed.setShort_des_review("Vey Good");
                        if ("5".equals(feed.getRate()))
                            feed.setShort_des_review("Brilliant");

                        //  average_rate=average_rate+Integer.parseInt(feed.getRate());

                        review_versions.add(feed);

                        Log.d("khh", feed.getRate());


                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                    final review_Adapter mAdapter1 = new review_Adapter(getApplicationContext(), review_versions,mAdapter,rate_versions);
                        mAdapter.setHasFixedSize(true);
                    // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                        mAdapter.setLayoutManager(gridLayoutManager);


                        mAdapter.setAdapter(mAdapter1);

                        if (review_versions.isEmpty()) {
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
                                review_versions.add(null);
                                mAdapter1.notifyItemInserted(review_versions.size() - 1);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //   remove progress item
                                        review_versions.remove(review_versions.size() - 1);
                                        mAdapter1.notifyItemRemoved(review_versions.size());
                                        //add items one by one
                                        int start = review_versions.size();
                                        int end = start + 10;

                                        for (int i = start; i <= end; i++) {

                                            try {
                                                ItemData feed=new ItemData();
                                                JSONObject objj=review.getJSONObject(i);

                                                feed.setId(objj.getString("id"));
                                                feed.setMobile(objj.getString("mobile"));
                                                feed.setCat_name(objj.getString("name"));
                                                feed.setRate(objj.getString("rate"));
                                                feed.setReview(objj.getString("review"));

                                                feed.setDate(objj.getString("date"));

                                                feed.setUp_vote(objj.getString("upvote"));
                                                feed.setDown_vote(objj.getString("downvote"));

                                                if("1".equals(feed.getRate()))
                                                    feed.setShort_des_review("Bad Product");
                                                if("2".equals(feed.getRate()))
                                                    feed.setShort_des_review("Average");
                                                if("3".equals(feed.getRate()))
                                                    feed.setShort_des_review("Good Product");
                                                if("4".equals(feed.getRate()))
                                                    feed.setShort_des_review("Vey Good");
                                                if("5".equals(feed.getRate()))
                                                    feed.setShort_des_review("Brilliant");

                                                //  average_rate=average_rate+Integer.parseInt(feed.getRate());

                                                review_versions.add(feed);
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
                    else {
                        for(int i=0;i<review.length();i++)
                        {
                            ItemData feed=new ItemData();
                            JSONObject objj=review.getJSONObject(i);

                            feed.setId(objj.getString("id"));
                            feed.setMobile(objj.getString("mobile"));
                            feed.setCat_name(objj.getString("name"));
                            feed.setRate(objj.getString("rate"));
                            feed.setReview(objj.getString("review"));

                            feed.setDate(objj.getString("date"));

                            feed.setUp_vote(objj.getString("upvote"));
                            feed.setDown_vote(objj.getString("downvote"));

                            if("1".equals(feed.getRate()))
                                feed.setShort_des_review("Bad Product");
                            if("2".equals(feed.getRate()))
                                feed.setShort_des_review("Average");
                            if("3".equals(feed.getRate()))
                                feed.setShort_des_review("Good Product");
                            if("4".equals(feed.getRate()))
                                feed.setShort_des_review("Vey Good");
                            if("5".equals(feed.getRate()))
                                feed.setShort_des_review("Brilliant");

                            //  average_rate=average_rate+Integer.parseInt(feed.getRate());

                            review_versions.add(feed);

                            Log.d("khh",feed.getRate());







                        }


                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                        final review_Adapter mAdapter1 = new review_Adapter(getApplicationContext(), review_versions,mAdapter,rate_versions);
                        mAdapter.setHasFixedSize(true);
                        // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                        mAdapter.setLayoutManager(gridLayoutManager);


                        mAdapter.setAdapter(mAdapter1);

                        if (review_versions.isEmpty()) {
                            mAdapter.setVisibility(View.GONE);
                            // tvEmptyView.setVisibility(View.VISIBLE);

                        } else {
                            mAdapter.setVisibility(View.VISIBLE);
                            //  tvEmptyView.setVisibility(View.GONE);
                        }


                    }
                    //  String success_val = obj.getString("success");

                       /* if ("true".equals(success_val)) {

                        }*/


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String msg) {

                // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                Log.d("jhvfff", "failed");
                // Do Stuff
            }
        });



    }

    protected void get_review(final CallBack mResultCallback) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/fetchReviewApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssss5555ssssss",response);


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
                params.put("productID", getIntent().getStringExtra("id_value"));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    protected void get_vote(final CallBack mResultCallback) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/fraLoginApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("jhfujfhg",response);


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
                params.put("productID", getIntent().getStringExtra("id_value"));
                params.put("mobile",session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}