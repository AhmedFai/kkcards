package com.kk_cards.Adapter;

/**
 * Created by user on 4/24/2018.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.CallBack;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class review_Adapter extends RecyclerView.Adapter{
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions,rate_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    SessionManagement session;

    public review_Adapter(Context context, ArrayList<ItemData> itemsData,RecyclerView recyclerView,ArrayList<ItemData> itemsData1) {

        this.os_versions = itemsData;
        this.mContext = context;
        this.rate_versions = itemsData1;

        session=new SessionManagement(mContext);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.review_layout_new, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof StudentViewHolder) {
            fp = os_versions.get(position);

            //    viewHolder.price_cut.setPaintFlags(viewHolder.price_cut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            ((StudentViewHolder) holder).star_val.setText(fp.getRate());
            ((StudentViewHolder) holder).brief_review.setText(fp.getShort_des_review());
            ((StudentViewHolder) holder).long_review.setText(fp.getReview());
            ((StudentViewHolder) holder).user_name.setText(fp.getCat_name() + ".., ");
            ((StudentViewHolder) holder).date.setText(fp.getDate());


            ((StudentViewHolder) holder).up_vote_val.setText(fp.getUp_vote());
            ((StudentViewHolder) holder).down_vote_value.setText(fp.getDown_vote());


       /*   if("true".equals(rate_versions.get(position).getUp_vote()))
            {
                ((StudentViewHolder) holder).up_vote_img.setImageResource(R.drawable.up_blue);


            }

            else  if("true".equals(rate_versions.get(position).getDown_vote()))
            {
                ((StudentViewHolder) holder).down_vote_img.setImageResource(R.drawable.down_blue);


            }


*/





            if("2".equals(fp.getRate()))
                ((StudentViewHolder) holder).linear.setBackgroundColor(R.color.yellow);
            else if("1".equals(fp.getRate()))
                ((StudentViewHolder) holder).linear.setBackgroundColor(R.color.red);


            ((StudentViewHolder) holder).up_vote_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (session.isLoggedIn() == false) {
                        Toast.makeText(mContext, "Please Login", Toast.LENGTH_SHORT).show();

                    } else {

                        up_down_vote(os_versions.get(position).getId(), "true", "false", new CallBack() {
                            @Override
                            public void onSuccess(String data) {

                                try {
                                    JSONObject obj = new JSONObject(data);

                                    if ("true".equals(obj.getString("success"))) {
                                        ((StudentViewHolder) holder).up_vote_img.setImageResource(R.drawable.up_blue);
                                        ((StudentViewHolder) holder).down_vote_img.setImageResource(R.drawable.down_vote);

                                        ((StudentViewHolder) holder).up_vote_val.setText(obj.getString("upvote"));
                                        ((StudentViewHolder) holder).down_vote_value.setText(obj.getString("downvote"));


                                    }


                                } catch (JSONException e) {


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
                }
                });


            ((StudentViewHolder) holder).down_vote_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (session.isLoggedIn() == false) {
                        Toast.makeText(mContext, "Please Login", Toast.LENGTH_SHORT).show();

                    } else {

                        up_down_vote(os_versions.get(position).getId(), "false", "true", new CallBack() {
                            @Override
                            public void onSuccess(String data) {


                                try {
                                    JSONObject obj = new JSONObject(data);

                                    if ("true".equals(obj.getString("success"))) {
                                        ((StudentViewHolder) holder).down_vote_img.setImageResource(R.drawable.down_blue);
                                        ((StudentViewHolder) holder).up_vote_img.setImageResource(R.drawable.up_vote);

                                        ((StudentViewHolder) holder).up_vote_val.setText(obj.getString("upvote"));
                                        ((StudentViewHolder) holder).down_vote_value.setText(obj.getString("downvote"));


                                    }


                                } catch (JSONException e) {


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
                }
            });

            //  viewHolder.brief_review.setText(fp.getShort_des_review());
            //   viewHolder.long_review.setText(fp.getReview());
            // viewHolder.star_val.setText(fp.getRate());

        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }



    }





    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return os_versions.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    @Override

    public int getItemCount() {
        return os_versions.size();
    }


    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.star_val)
        TextView star_val;
        @BindView(R.id.brief_review)
        TextView brief_review;
        @BindView(R.id.long_review)
        TextView long_review;

        @BindView(R.id.user_name)
        TextView user_name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.up_vote_val)
        TextView up_vote_val;
        @BindView(R.id.down_vote_value)
        TextView down_vote_value;
        @BindView(R.id.up_vote_img)
        ImageButton up_vote_img;

        @BindView(R.id.linear)
        LinearLayout linear;

        @BindView(R.id.down_vote_img)
        ImageButton down_vote_img;

        public StudentViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    protected void up_down_vote(final String id, final String up, final String down, final CallBack mResultCallback) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/voteApi.php",
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
                params.put("id",id);
                params.put("mobile",session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                params.put("upvote",up);
                params.put("downvote", down);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}

