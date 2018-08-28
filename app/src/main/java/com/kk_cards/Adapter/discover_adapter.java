package com.kk_cards.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.ExoPlayer;
import com.kk_cards.Fragment.product_details;
import com.kk_cards.Modal.Video;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class discover_adapter extends RecyclerView.Adapter<discover_adapter.MyViewHolder> {


    Context context;
    private List<Video> list;

    public discover_adapter(Context context, List<Video> video) {

        this.context = context;
        this.list = video;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discover_item, parent, false);
        return new discover_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Video video = list.get(position);


        if ("Image".equals(video.getFtype())){
            Picasso.with(context).load(video.getDisUrl()).fit().into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context,product_details.class);
                    i.putExtra("id_value",list.get(position).getProductId());
                    i.putExtra("discover", "discover");
                    i.putExtra("image_path","no_images");
                    context.startActivity(i);
                }
            });

        }else {
            Picasso.with(context).load(video.getDisThumbnail()).fit().into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ExoPlayer.class);
                    i.putExtra("link", video.getDisUrl());
                    context.startActivity(i);
                }
            });

        }

        holder.title.setText(video.getTitle());
        holder.text.setText(video.getContent());
        holder.date.setText(video.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, text, date;

        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.back);
            title = (TextView)itemView.findViewById(R.id.title);
            text  = (TextView)itemView.findViewById(R.id.secondary);
            date  = (TextView)itemView.findViewById(R.id.date);

        }
    }
}
