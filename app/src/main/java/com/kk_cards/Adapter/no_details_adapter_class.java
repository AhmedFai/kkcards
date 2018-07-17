package com.kk_cards.Adapter;

/**
 * Created by user on 5/15/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class no_details_adapter_class extends RecyclerView.Adapter<no_details_adapter_class.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<String> path_list;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;

    String video_path,id_val;


    public no_details_adapter_class(Context context, ArrayList<String> path) {

        this.path_list = path;

        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_product_layout1, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {




           Picasso.with(mContext).load(path_list.get(position)).into(viewHolder.cat_image);



        viewHolder.cat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* Drawable highlight = mContext.getResources().getDrawable(R.drawable.highlight);
                viewHolder.cat_image.setBackground(highlight);
*/



            }
        });


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cat_img)
        ImageView cat_image;

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
        return  path_list.size();

    }
}
