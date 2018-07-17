package com.kk_cards.Adapter;

/**
 * Created by user on 1/23/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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



public class no_product_adapter extends RecyclerView.Adapter<no_product_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<String> path_list,fname_list;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    ArrayList<String> image_list;
    String video_path,id_val;


    public no_product_adapter(Context context, ArrayList<String> path,ArrayList<String> path1,String id_val) {

        this.path_list = path;
        this.fname_list = path1;
        this.mContext = context;
        this.id_val = id_val;
        image_list=new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_product_layout1, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {




          if("Image".equals(fname_list.get(position)))
        {
            image_list.add(path_list.get(position));
            Picasso.with(mContext).load(path_list.get(position)).placeholder(R.drawable.load).fit().into(viewHolder.cat_image);


        }

        else
          {

              video_path=path_list.get(position);
              Picasso.with(mContext)
                      .load("http://img.youtube.com/vi/"+path_list.get(position)+"/mqdefault.jpg")
                      .into(viewHolder.cat_image);


          }

          Log.d("image_size", String.valueOf(image_list.size())) ;


          viewHolder.cat_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                 /* if("Image".equals(fname_list.get(position)))
                  {

                      Intent i=new Intent(mContext,product_details.class);
                      i.putExtra("image_path",path_list.get(position));
                      i.putExtra("id_value",id_val);
                    //  i.putExtra("position",position);
                      mContext.startActivity(i);
                      ((AppCompatActivity)mContext).finish();



                     *//* Intent i=new Intent(mContext,no_product_details.class);
                      i.putStringArrayListExtra("image_path",image_list);
                      i.putExtra("position",position);
                      mContext.startActivity(i);*//*

                  }

                  else
                  {


                      Intent i=new Intent(mContext,youtube_video.class);
                      i.putExtra("video_path",path_list.get(position));
                      mContext.startActivity(i);

                      Log.d("video_path",path_list.get(position));
                  }
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
