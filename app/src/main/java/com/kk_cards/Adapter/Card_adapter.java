package com.kk_cards.Adapter;

/**
 * Created by nvp on 9/8/2016.
 */


/**
 * Created by nvp on 9/8/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.Fragment.product_details;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class Card_adapter extends BaseAdapter{

    Integer[] images;
    String[] cat_data;
    Context context;
    LayoutInflater inflater;
    public ArrayList<ItemData> os_versions;
    public Card_adapter(FragmentActivity mainActivity,ArrayList<ItemData> itemsData) {
        // TODO Auto-generated constructor stub

        this.context=mainActivity;
        this.os_versions =itemsData;





        //   id_data1=public_id;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        return 4;


    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,price,sale_price;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.popular_card_layout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.cat_name);
        holder.img=(ImageView) rowView.findViewById(R.id.cat_img);
        holder.sale_price=(TextView) rowView.findViewById(R.id.cat_price_sale);
        holder.price=(TextView) rowView.findViewById(R.id.cat_price);

        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.tv.setText(os_versions.get(position).getProductName());
        holder.sale_price.setText("\u20B9"+os_versions.get(position).getPrice());
        holder.price.setText("\u20B9"+os_versions.get(position).getMrp());
        Picasso.with(context).load(os_versions.get(position).getProductImage()).fit().into(holder.img);

      rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(context,product_details.class);
                i.putExtra("image_path","no_images");

                i.putExtra("id_value",os_versions.get(position).getProductID());
                context.startActivity(i);

            }
        });

        return rowView;
    }


}
