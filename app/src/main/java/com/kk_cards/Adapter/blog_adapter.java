package com.kk_cards.Adapter;

/**
 * Created by nvp on 9/8/2016.
 */


/**
 * Created by nvp on 9/8/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.Fragment.product_list;
import com.kk_cards.Fragment.sub_cat_list;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class blog_adapter extends BaseAdapter{

  ArrayList<String> name_list,id_list,image_list,count_list;
    Context context;
    LayoutInflater inflater;
    int sub_size;
    public blog_adapter(FragmentActivity mainActivity, ArrayList<String>id,ArrayList<String>name,ArrayList<String>image,ArrayList<String> count_lis) {
        // TODO Auto-generated constructor stub
        this.image_list=image;
        this.context=mainActivity;
        this.id_list =id;
        this.name_list =name;
        this.count_list=count_lis;

        //   id_data1=public_id;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        return id_list.size();


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

        rowView = inflater.inflate(R.layout.home_recycler, null);
        holder.tv=(TextView) rowView.findViewById(R.id.cat_name);
        holder.price=(TextView) rowView.findViewById(R.id.cat_price);
        holder.sale_price=(TextView) rowView.findViewById(R.id.cat_price_sale);
        holder.img=(ImageView) rowView.findViewById(R.id.cat_img);

   //   arrayList=os_versions.get(position).getImage_src();

       Picasso.with(context).load(image_list.get(position)).fit().into(holder.img);


      //  Log.d("gdghdhdhd",os_versions.get(1).getid());



        Typeface tfaerial=Typeface.createFromAsset(context.getAssets(),"arial.ttf");
        holder.tv.setTypeface(tfaerial);
        holder.tv.setText(name_list.get(position));


        // id=os_versions.get(position).getid();


     //   Log.d("id",os_versions.get(position).getid());

/*

        holder.price.setText("\u20B9"+os_versions.get(position).getPrice());
        holder.sale_price.setText("\u20B9"+os_versions.get(position).getSale_price());
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
*/

  //   Picasso.with(context).setLoggingEnabled(true);


       rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {




                if("0".equals(count_list.get(position))) {

                    // Toast.makeText(context,"Hey.... Categories",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(context,product_list.class);
                    i.putExtra("id_value", id_list.get(position));
                    i.putExtra("image_path", "no_images");
                    i.putExtra("sub_cat_val", "0");
                    i.putExtra("sub_cat_name",name_list.get(position));
                    context.startActivity(i);
                        }

                else
                {

                    sub_cat_list grid = new sub_cat_list();
                    Bundle bg = new Bundle();
                    bg.putString("id_value", id_list.get(position));
                    grid.setArguments(bg);

                  /* Bundle args = new Bundle();
                args.putString("YourKey",os_versions.get(position).getid());
                        grid.setArguments(args);*/
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();


                }

            }
        });

        return rowView;
    }

    public class ViewHolder {
    }
}
