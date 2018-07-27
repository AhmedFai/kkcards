package com.kk_cards.Adapter;

/**
 * Created by user on 5/15/2018.
 */

import android.content.Context;
import android.graphics.Paint;
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


public class super_save_adapter extends RecyclerView.Adapter<super_save_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;

    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    ArrayList<ItemData> super_list;
    String video_path,id_val;


    public super_save_adapter(Context context,ArrayList<ItemData> itemsData) {
        // TODO Auto-generated constructor stub

        this.super_list=itemsData;

        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_card_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        if("Super_Saver".equals(super_list.get(position).getCheck())) {
            viewHolder.cat_name.setText(super_list.get(position).getProductName());
            viewHolder.cat_price_sale.setText("\u20B9" + super_list.get(position).getPrice());
            viewHolder.cat_price.setText("\u20B9" + super_list.get(position).getMrp());
            Picasso.with(mContext).load(super_list.get(position).getProductImage()).into(viewHolder.cat_image);
            viewHolder.cat_price.setPaintFlags(viewHolder.cat_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        else{

            viewHolder.cat_name.setText(super_list.get(position).getProductName());
            viewHolder.cat_price_sale.setVisibility(View.GONE);
            viewHolder.cat_price.setVisibility(View.GONE);
            Picasso.with(mContext).load(super_list.get(position).getProductImage()).into(viewHolder.cat_image);


        }





    }


    public static class ViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.cat_img)
        ImageView cat_image;
        @BindView(R.id.cat_price_sale)
        TextView cat_price_sale;
        @BindView(R.id.cat_price)
        TextView cat_price;
        @BindView(R.id.cat_name)
        TextView cat_name;



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



        return super_list.size();

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
