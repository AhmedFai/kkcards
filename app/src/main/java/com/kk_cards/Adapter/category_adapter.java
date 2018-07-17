package com.kk_cards.Adapter;

/**
 * Created by user on 5/15/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Fragment.product_list;
import com.kk_cards.Fragment.sub_cat_list;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;

    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    ArrayList<String> name_list,id_list,image_list,count_list;
    String video_path,id_val;


    public category_adapter(Context context,ArrayList<String>id,ArrayList<String>name,ArrayList<String>image,ArrayList<String> count_lis) {
        // TODO Auto-generated constructor stub
        this.image_list=image;
        this.id_list =id;
        this.name_list =name;
        this.count_list=count_lis;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_product_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {



             viewHolder.cat_name.setVisibility(View.VISIBLE);

        viewHolder.cat_image.setVisibility(View.GONE);
        viewHolder.cat_name.setText(name_list.get(position));

        Picasso.with(mContext).load(image_list.get(position)).into(viewHolder.imageView1);

        viewHolder.cat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if("0".equals(count_list.get(position))) {

                     Toast.makeText(mContext,"Hey.... Categories",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(mContext,product_list.class);
                    i.putExtra("id_value", id_list.get(position));
                    i.putExtra("image_path", "no_images");
                    i.putExtra("sub_cat_val", "0");
                    i.putExtra("sub_cat_name",name_list.get(position));
                    mContext.startActivity(i);
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
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();


                }



            }
        });

        viewHolder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if("0".equals(count_list.get(position))) {

                    // Toast.makeText(context,"Hey.... Categories",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(mContext,product_list.class);
                    i.putExtra("id_value", id_list.get(position));
                    i.putExtra("image_path", "no_images");
                    i.putExtra("sub_cat_val", "0");
                    i.putExtra("sub_cat_name",name_list.get(position));
                    mContext.startActivity(i);
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
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();


                }



            }
        });
        viewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if("0".equals(count_list.get(position))) {
                  //  Toast.makeText(mContext,"Hey.... Categories",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(mContext,product_list.class);
                    i.putExtra("id_value", id_list.get(position));
                    i.putExtra("image_path", "no_images");
                    i.putExtra("sub_cat_val", "0");
                    i.putExtra("sub_cat_name",name_list.get(position));
                    mContext.startActivity(i);
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
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();


                }



            }
        });

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.cat_img)
        ImageView cat_image;
        @BindView(R.id.imageView1)
        ImageView imageView1;
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



        return id_list.size();

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
