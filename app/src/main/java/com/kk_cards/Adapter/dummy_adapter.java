package com.kk_cards.Adapter;

/**
 * Created by user on 1/23/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class dummy_adapter extends RecyclerView.Adapter<dummy_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    String check_value;


    public dummy_adapter(Context context, ArrayList<ItemData> itemsData) {

        this.os_versions = itemsData;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        fp = os_versions.get(position);

    //    viewHolder.price_cut.setPaintFlags(viewHolder.price_cut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        viewHolder.cat_name.setText(fp.getCat_name());
        Picasso.with(mContext).load(fp.getCat_image()).placeholder(R.drawable.dummy_cat_image).fit().into(viewHolder.image);



    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cat_name) TextView cat_name;

        @BindView(R.id.cat_img)
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onClick(View v) {


    }


    @Override

    public int getItemCount() {
        return os_versions.size();
    }
}
