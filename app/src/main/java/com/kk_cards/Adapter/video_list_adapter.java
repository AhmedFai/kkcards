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
import com.kk_cards.Modal.Video;
import com.kk_cards.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class video_list_adapter extends RecyclerView.Adapter<video_list_adapter.MyViewHolder> {



    Context context;
    private List<Video> list;

    public video_list_adapter(Context context, List<Video> list) {
        this.context = context;
       this.list=list;
    }


    @Override
    public video_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(video_list_adapter.MyViewHolder holder, int position) {

        final Video video = list.get(position);
        holder.text.setText(video.getVname());

        Picasso.with(context).load(video.getThumbnail()).fit().into(holder.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ExoPlayer.class);
                i.putExtra("link",video.getVurl());
                context.startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView title;
        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (ImageView)itemView.findViewById(R.id.cover);
            text = (TextView)itemView.findViewById(R.id.titleText);
        }
    }
}
