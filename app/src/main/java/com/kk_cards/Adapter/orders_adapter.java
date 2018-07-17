package com.kk_cards.Adapter;

/**
 * Created by user on 3/21/2018.
 */


/**
 * Created by user on 1/23/2018.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kk_cards.Fragment.order_details;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.write_review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class orders_adapter extends RecyclerView.Adapter<orders_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    String check_value;


    public orders_adapter(Context context, ArrayList<ItemData> itemsData) {

        this.os_versions = itemsData;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_new_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        fp = os_versions.get(position);




        viewHolder.cat_name.setText(fp.getCat_name());
        viewHolder.delivery_date.setText(fp.getDate());



        if("Delivered".equals(os_versions.get(position).getStatus()))
        {
            viewHolder.ratingBar1.setVisibility(View.VISIBLE);
            viewHolder.order_status.setVisibility(View.GONE);



            if(fp.getRate().equals("0"))
            {

                viewHolder.order_status.setVisibility(View.VISIBLE);
                viewHolder.order_status.setText(fp.getStatus());
                viewHolder.ratingBar1.setVisibility(View.GONE);
                viewHolder.write_review.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.ratingBar1.setClickable(false);
                viewHolder.ratingBar1.setIsIndicator(true);
                viewHolder.ratingBar1.setRating(Float.parseFloat(fp.getRate()));
                viewHolder.write_review.setVisibility(View.GONE);

            }





        }
        else
        viewHolder.order_status.setText(fp.getStatus());

        Picasso.with(mContext).load(fp.getCat_image()).fit().into(viewHolder.image);



        viewHolder.write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(mContext,write_review.class);
                i.putExtra("image",os_versions.get(position).getCat_image());
                i.putExtra("name",os_versions.get(position).getCat_name());
                i.putExtra("rate_value",viewHolder.ratingBar1.getRating());
                i.putExtra("product_id",os_versions.get(position).getId());

                mContext.startActivity(i);





            }
        });


        viewHolder.image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order_details grid=new order_details();

                Bundle args = new Bundle();
                args.putString("id",os_versions.get(position).getTxnid());
                args.putString("product_id",os_versions.get(position).getId());
                args.putString("order_status",os_versions.get(position).getStatus());

                grid.setArguments(args);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();





                // TODO Handle item click
            }
        });
        viewHolder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order_details grid=new order_details();

                Bundle args = new Bundle();
                args.putString("id",os_versions.get(position).getTxnid());
                args.putString("product_id",os_versions.get(position).getId());
                args.putString("order_status",os_versions.get(position).getStatus());

                grid.setArguments(args);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();





                // TODO Handle item click
            }
        });

        viewHolder.delivery_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order_details grid=new order_details();

                Bundle args = new Bundle();
                args.putString("id",os_versions.get(position).getTxnid());
                args.putString("product_id",os_versions.get(position).getId());
                args.putString("order_status",os_versions.get(position).getStatus());

                grid.setArguments(args);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();





                // TODO Handle item click
            }
        });






        //  Log.d("bhs",fp.getCat_name());
        // viewHolder.cat_image.setImageResource(fp.getCat_image());


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text1) TextView cat_name;
        @BindView(R.id.delivery_date) TextView delivery_date;
        @BindView(R.id.order_status) Button order_status;

        @BindView(R.id.image1)
        ImageView image;
        @BindView(R.id.ratingBar1)
        RatingBar ratingBar1;
        @BindView(R.id.write_review)
        Button write_review;


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
