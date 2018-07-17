package com.kk_cards.Adapter;

/**
 * Created by user on 1/25/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Fragment.product_details;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.kk_cards.check_out_buy_now;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class buy_now_adapter extends RecyclerView.Adapter<buy_now_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    String check_value,buy_type;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    DatabaseHandler db;
    SessionManagement session;


    public buy_now_adapter(Context context, ArrayList<ItemData> itemsData) {

        this.os_versions = itemsData;
        this.mContext = context;
        session=new SessionManagement(mContext);
        db = new DatabaseHandler(mContext);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_adapter_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        fp = os_versions.get(position);


        viewHolder.price_cut.setPaintFlags(viewHolder.price_cut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        viewHolder.cat_name.setText(fp.getCat_name());
        viewHolder.price_cut.setText("\u20B9" + fp.getPrice());
        viewHolder.price.setText("\u20B9" + fp.getPrice_cut());
        viewHolder.code.setText("Code: " + fp.getProduct_id());
        viewHolder.discount.setText(fp.getDiscount() + "% off");
        Picasso.with(mContext).load(fp.getCat_image()).into(viewHolder.image);


//        Log.d("coden",fp.getProduct_id());


        viewHolder.count_txt.setText(os_versions.get(position).getQuantity());

            viewHolder.remove.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.GONE);
            viewHolder.view2.setVisibility(View.GONE);



        //  final int update_id = Integer.parseInt(os_versions.get(position).getId());



         final int[] counter = new int[1];
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                int grand_total = 0;



                if(Integer.parseInt(viewHolder.count_txt.getText().toString()) <= 9) {

                    int counter = Integer.parseInt(viewHolder.count_txt.getText().toString());

                    counter = counter + 1;

                    viewHolder.count_txt.setText(Integer.toString(counter));

                    Boolean result = db.update_buy_now(viewHolder.count_txt.getText().toString());


                    //   Log.d("brghoih",String.valueOf(result));
                    Cursor c = db.get_buy_now();


                    int grand_total_adpter = 0, total_without_del = 0;

                    if (c != null)
                        if (c.moveToFirst()) {
                            do {


                                c.getString(c.getColumnIndex("quantity"));
                                c.getString(c.getColumnIndex("sale_price"));
                                total_without_del = total_without_del + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("sale_price")));


                                grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("del_charge"))) + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("sale_price")));


                            }
                            while (c.moveToNext());


                        }

                    grand_total_adpter = Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice_cut()) + Integer.parseInt(os_versions.get(position).getDel_charge());
                    check_out_buy_now.total_amt.setText("\u20B9" + grand_total_adpter);
                    check_out_buy_now.tot_price_items.setText("\u20B9" + total_without_del);

                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("total_price", grand_total_adpter);
                    editor.putInt("total_price_with_del", total_without_del);
                    editor.putString("quantity_value", viewHolder.count_txt.getText().toString());

                    editor.putString("check_value", "price_update");
                    editor.commit();

                }

                else

                {

                 /*   AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Only "+os_versions.get(position).getTot_quantity()+ " left in Stock");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();*/


                    Toast.makeText(mContext, "You can only select maximum 10 items", Toast.LENGTH_SHORT).show();

                }


                //


                //   notifyDataSetChanged();

            }
        });
        viewHolder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int grand_total = 0;

                int counter = Integer.parseInt(viewHolder.count_txt.getText().toString());
                if (counter==1){
                    counter=1;
                }else {
                    counter = counter - 1;
                }
                viewHolder.count_txt.setText(Integer.toString(counter));

                db.update_buy_now(viewHolder.count_txt.getText().toString());

                Cursor c = db.get_buy_now();


                int grand_total_adpter = 0, total_without_del = 0;

                if (c != null)
                    if (c.moveToFirst()) {
                        do {


                            c.getString(c.getColumnIndex("quantity"));
                            c.getString(c.getColumnIndex("sale_price"));
                            total_without_del = total_without_del + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("sale_price")));


                            grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("del_charge"))) + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("sale_price")));


                        }
                        while (c.moveToNext());


                    }

                grand_total_adpter = Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice_cut()) + Integer.parseInt(os_versions.get(position).getDel_charge());
                check_out_buy_now.total_amt.setText("\u20B9" + grand_total_adpter);
                check_out_buy_now.tot_price_items.setText("\u20B9" + total_without_del);

                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt("total_price", grand_total_adpter);
                editor.putInt("total_price_with_del", total_without_del);
                editor.putString("quantity_value", viewHolder.count_txt.getText().toString());

                editor.putString("check_value", "price_update");
                editor.commit();




                //   notifyDataSetChanged();

                }
        });



        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, product_details.class);
                i.putExtra("id_value", os_versions.get(position).getProduct_id());

                i.putExtra("image_path", "no_images");
                mContext.startActivity(i);
            }
        });
        viewHolder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, product_details.class);
                i.putExtra("image_path", "no_images");
                i.putExtra("id_value", os_versions.get(position).getProduct_id());
                mContext.startActivity(i);

            }
        });


        //  Log.d("bhs",fp.getCat_name());
        // viewHolder.cat_image.setImageResource(fp.getCat_image());



    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text1)
        TextView cat_name;
        @BindView(R.id.price2)
        TextView price_cut;
        @BindView(R.id.price1)
        TextView price;
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.discount)
        TextView discount;
        @BindView(R.id.image1)
        ImageView image;
        @BindView(R.id.subtract)
        TextView subtract;
        @BindView(R.id.count)
        TextView count_txt;
        @BindView(R.id.add)
        TextView add;
        @BindView(R.id.remove)
        TextView remove;


        @BindView(R.id.view1)
        View view1;
        @BindView(R.id.view2)
        View view2;

        @BindView(R.id.quan)
        TextView quan;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public int getItemViewType(int position) {


        return viewtype;

    }
    @Override

    public int getItemCount() {
        return os_versions.size();
    }



}




