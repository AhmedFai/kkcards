package com.kk_cards.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.R;

public class video_list_adapter extends RecyclerView.Adapter<video_list_adapter.MyViewHolder> {

    Context context;

    public video_list_adapter(Context context) {
        this.context = context;
    }


    @Override
    public video_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(video_list_adapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
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
