package com.kk_cards.Adapter;

/**
 * Created by user on 1/23/2018.
 */

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class product_adapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<ItemData> os_versions;
    ItemData fp;
    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    Context mContext;
    private OnLoadMoreListener onLoadMoreListener;
    String check_value;


    public product_adapter(Context c,ArrayList<ItemData> students, RecyclerView recyclerView,String che) {
        os_versions = students;
        mContext=c;
        check_value=che;

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
    public int getItemViewType(int position) {
        return os_versions.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.product_list_layout, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof StudentViewHolder) {


            if("sub_category".equals(check_value))
            {
                ((StudentViewHolder)viewHolder).cat_name.setText(os_versions.get(position).getProductName());

                ((StudentViewHolder)viewHolder).cat_name.setTextSize(20);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(20,20,20,20);
                ((StudentViewHolder)viewHolder).cat_name.setLayoutParams(params);

                ((StudentViewHolder)viewHolder).price_cut.setVisibility(View.GONE);
               // ((StudentViewHolder)viewHolder).discount.setVisibility(View.GONE);
                ((StudentViewHolder)viewHolder).image.setVisibility(View.GONE);
                ((StudentViewHolder)viewHolder).code.setVisibility(View.GONE);
                ((StudentViewHolder)viewHolder).price.setVisibility(View.GONE);
            }
            else {

                fp = os_versions.get(position);

                ((StudentViewHolder)viewHolder).price_cut.setPaintFlags(((StudentViewHolder) viewHolder).price_cut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                ((StudentViewHolder)viewHolder).cat_name.setText(fp.getProductName());
                ((StudentViewHolder)viewHolder).price_cut.setText("\u20B9"+fp.getMrp());
                ((StudentViewHolder)viewHolder).price.setText("\u20B9"+fp.getPrice());
                ((StudentViewHolder)viewHolder).code.setText("Code: "+fp.getProductID());
                //Log.d("product ka name bhai", fp.getProductName());
                //((StudentViewHolder)viewHolder).discount.setText(fp.getDiscount()+"% off");

                Picasso.with(mContext).load(fp.getProductImage()).into(((StudentViewHolder)viewHolder).image);



            }
        } else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return os_versions.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text1) TextView cat_name;
        @BindView(R.id.price2) TextView price_cut;
        @BindView(R.id.price1) TextView price;
        @BindView(R.id.code) TextView code;
       /* @BindView(R.id.discount) TextView discount;*/
        @BindView(R.id.image1)
        ImageView image;
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
}
