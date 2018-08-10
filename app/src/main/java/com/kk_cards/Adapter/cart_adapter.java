package com.kk_cards.Adapter;

/**
 * Created by user on 1/25/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Fragment.add_to_cart;
import com.kk_cards.Fragment.product_details;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.kk_cards.check_out_activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;



public class cart_adapter extends RecyclerView.Adapter<cart_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    String counter2;
    int viewtype;
    String check_value,buy_type;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    DatabaseHandler db;
    SessionManagement session;


    public cart_adapter(Context context, ArrayList<ItemData> itemsData, String check_val,String buy_type) {

        this.os_versions = itemsData;
        this.mContext = context;
        this.check_value = check_val;
        this.buy_type=buy_type;
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


       // viewHolder.price_cut.setPaintFlags(viewHolder.price_cut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        viewHolder.cat_name.setText(fp.getProductName());
       // viewHolder.price_cut.setText(" \u20B9" + fp.getMrp());
       // viewHolder.tot.setText(fp.getCardID());
        viewHolder.price.setText(" \u20B9" + fp.getPrice());
        viewHolder.code.setText("Code: " + fp.getProductID());
       // viewHolder.discount.setText(fp.getDiscount() + "% off");
        Picasso.with(mContext).load(fp.getProductImage()).into(viewHolder.image);


        Log.d("quaaaaaanttttt", fp.getQuantity());

        Log.d("caaaaaard", fp.getCardID());

       /* if (fp.getCardID().equals(String.valueOf(50))){
            viewHolder.tot.setText("Quantity : " + fp.getQuantity());
        }*/

        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        switch (fp.getCardID()){
            case "0":
                viewHolder.tot.setText("Lense > 1 piece");
                break;
            case "1":
                viewHolder.tot.setText("Lense > 6 piece");
                break;
            case "2":
                viewHolder.tot.setText("lense > 1 pack");
                break;
            case "3":
                viewHolder.tot.setText("Device > simple > 1 piece");
                break;
            case "4":
                viewHolder.tot.setText("Device > simple > 6 piece");
                break;
            case "5":
                viewHolder.tot.setText("Device > simple > 1 pack");
                break;
            case "6":
                viewHolder.tot.setText("Device > 4G > 1 piece");
                break;
            case "7":
                viewHolder.tot.setText("Device > 4G > 6 piece");
                break;
            case "8":
                viewHolder.tot.setText("Device > 4G > 1 pack");
                break;
            case "9":
                viewHolder.tot.setText("Device > 10G > 1 piece");
                break;
            case "10":
                viewHolder.tot.setText("Device > 10G > 6 piece");
                break;
            case "11":
                viewHolder.tot.setText("Device > 10G > 1 pack");
                break;
            case "12":
                viewHolder.tot.setText("For Both > 1 Box");
                break;
            case "50":
                viewHolder.tot.setText("Quantity : " + fp.getQuantity());
                break;
        }


      /*  if (!session.isLoggedIn()){
            viewHolder.tot.setText("Quantity : " + prefs.getString("qua", ""));
        }*/


//        Log.d("coden",fp.getProduct_id());

     //   viewHolder.count_txt.setText(os_versions.get(position).getQuantity());

        if("cart".equals(check_value)) {



            Boolean result = db.insert_quantity(os_versions.get(position).getQuantity(), os_versions.get(position).getCartID());

            Log.d("result_dbb", String.valueOf(result));





            int count=db.count_rows();
            Log.d("quant_val",os_versions.get(position).getQuantity());

        }

        else if("check_out".equals(check_value))
        {

            viewHolder.remove.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.VISIBLE);
            viewHolder.view2.setVisibility(View.GONE);
        }


      //  final int update_id = Integer.parseInt(os_versions.get(position).getId());
//        final int[] counter = new int[1];
/*
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Add", "adddddddd");

             //   Log.d("hkjdfoifghgih", os_versions.get(position).getId());
                int grand_total=0;
                //counter[0] = Integer.parseInt(viewHolder.count_txt.getText().toString());
//                counter[0] = counter[0] + 1;

//                if(counter[0]<=Integer.parseInt(os_versions.get(position).getTot_quantity()))
                if (Integer.parseInt(viewHolder.count_txt.getText().toString()) <= 9)
                {
                    int counter = Integer.parseInt(viewHolder.count_txt.getText().toString());
                   // int numquantity=Integer.parseInt(os_versions.get(position).getTot_quantity());
                     // Log.d("quantitty",String.valueOf(numquantity));
                      //Toast.makeText(mContext," "+String.valueOf(numquantity),Toast.LENGTH_SHORT).show();
                    //if (counter=
                    counter=counter+1;
                    viewHolder.count_txt.setText(Integer.toString(counter));
                    grand_total = grand_total + Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice());
                    // viewHolder.count_txt.setText(Integer.toString(counter[0]));
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("total_price", grand_total);
                    editor.putString("check_value", "price_update");
                    editor.commit();


                    if (session.isLoggedIn() == false) {

//                        SharedPreferences prefs = mContext.getSharedPreferences("AUTHENTICATION_FILE_NAME", MODE_PRIVATE);
//                        counter2 = prefs.getString("Name"," ");
//                        Toast.makeText(mContext,String.valueOf(counter2),Toast.LENGTH_SHORT).show();
                            Boolean result = db.update_quantity(viewHolder.count_txt.getText().toString(), os_versions.get(position).getCartID());
                            Log.d("counttttt", viewHolder.count_txt.getText().toString());
                            Cursor c = db.getall_data1();
                            int grand_total_adpter = 0;
                            if (c != null)
                                if (c.moveToFirst()) {
                                    do {
                                        c.getString(c.getColumnIndex("quantity"));
                                        c.getString(c.getColumnIndex("regular_price"));
                                        //    Log.d("quuuuuuuuuuu", c.getString(c.getColumnIndex("quantity")));
                                        //  Log.d("salllll", c.getString(c.getColumnIndex("sale_price")));
                                        grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));
                                    }
                                    while (c.moveToNext());
                                    add_to_cart.total_amt.setText(" \u20B9" + grand_total_adpter);
                                }
                            if ("cart".equals(check_value)) {
                                add_to_cart.total_amt.setText(" \u20B9" + grand_total_adpter);
                                add_to_cart.tot_price_items.setText("\u20B9" + grand_total_adpter);
                            } else if ("check_out".equals(check_value)) {
                                check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                check_out_activity.tot_price_items.setText("\u20B9" + grand_total_adpter);
                            }
                            SharedPreferences.Editor editor1 = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor1.putInt("total_price", grand_total_adpter);
                            editor1.putString("check_value", "price_update");
                            editor1.commit();

                        }
                      //session isnot login
                        else {
                            if ("check_out".equals(check_value) && "buy_now".equals(buy_type)) {
                                int grand_total_adpter = 0, total_without_del = 0;
                                total_without_del = Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice());
                                grand_total_adpter = Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice()) + Integer.parseInt(os_versions.get(position).getDeliveryCharge());
                                check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                check_out_activity.tot_price_items.setText("\u20B9" + total_without_del);
                            } else {
                                //  Boolean result = db.update_quantity_server(viewHolder.count_txt.getText().toString(),os_versions.get(position).getId());
                                Boolean result1 = db.update_server_table(viewHolder.count_txt.getText().toString(), os_versions.get(position).getId());
                                Cursor c = db.getall_data_server();
                                int grand_total_adpter = 0, total_without_del = 0;
                                if (c != null)
                                    if (c.moveToFirst()) {
                                        do {
                                            c.getString(c.getColumnIndex("quantity"));
                                            c.getString(c.getColumnIndex("regular_price"));
                                            total_without_del = total_without_del + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));
                                            grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("del_charge"))) + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));
                                        }
                                        while (c.moveToNext());
                                    }
                                Log.d("dddddddd", String.valueOf(grand_total_adpter));
                                if ("cart".equals(check_value)) {
                                    add_to_cart.total_amt.setText(" \u20B9" + grand_total_adpter);
                                    add_to_cart.tot_price_items.setText("\u20B9" + total_without_del);
                                } else if ("check_out".equals(check_value)) {
                                    check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                    check_out_activity.tot_price_items.setText("\u20B9" + total_without_del);
                                }
                                SharedPreferences.Editor editor1 = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor1.putInt("total_price", grand_total_adpter);
                                editor1.putInt("total_price_with_del", total_without_del);
                                editor1.putString("check_value", "price_update");
                                editor1.commit();
                            }}
                        }
                    else
                    {  */
/*AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
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
                        alert11.show();*//*


                        Toast.makeText(mContext, "You can only select maximum 10 items", Toast.LENGTH_SHORT).show();
                    }

             //   notifyDataSetChanged();
            }
        });
*/
/*
        viewHolder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("substract", "dsfdfd");

                int grand_total=0;
                    if (Integer.parseInt(viewHolder.count_txt.getText().toString()) >= 1) {


                        //testing

                       */
/* int numquantity=Integer.parseInt(os_versions.get(position).getTot_quantity());
                        Log.d("quantitty",String.valueOf(numquantity));
                        Toast.makeText(mContext," "+String.valueOf(numquantity),Toast.LENGTH_SHORT).show();
*//*


                        //testing

                        int counter = Integer.parseInt(viewHolder.count_txt.getText().toString());
                        if (counter==1){
                            counter=1;
                        }else {
                            counter = counter - 1;
                        }
                        viewHolder.count_txt.setText(Integer.toString(counter));
                        grand_total = grand_total + Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice());

                        //     Toast.makeText(mContext, ""+grand_total, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("total_price", grand_total);
                        editor.putString("check_value", "price_update");
                        editor.commit();

                        if (session.isLoggedIn() == false) {
                            Boolean result = db.update_quantity(viewHolder.count_txt.getText().toString(), os_versions.get(position).getCartID());
                            //Log.d("resilt", "wwwwwww");
                            Cursor c = db.getall_data1();


                            int grand_total_adpter = 0;

                            if (c != null)
                                if (c.moveToFirst()) {
                                    do {


                                        c.getString(c.getColumnIndex("quantity"));
                                        c.getString(c.getColumnIndex("regular_price"));

                                        grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));

                                    }
                                    while (c.moveToNext());
                                  //  Log.d("amounttttt", String.valueOf(grand_total_adpter));

                                    add_to_cart.total_amt.setText(" \u20B9" + grand_total_adpter);

                                }

                            if ("cart".equals(check_value)) {

                                add_to_cart.total_amt.setText("\u20B9" + grand_total_adpter);
                                add_to_cart.tot_price_items.setText("\u20B9" + grand_total_adpter);
                            } else if ("check_out".equals(check_value)) {
                                check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                check_out_activity.tot_price_items.setText("\u20B9" + grand_total_adpter);


                            }
                            SharedPreferences.Editor editor1 = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor1.putInt("total_price", grand_total_adpter);
                            editor1.putString("check_value", "price_update");
                            editor1.commit();


                        } else {
                            if ("check_out".equals(check_value) && "buy_now".equals(buy_type)) {
                                int grand_total_adpter = 0, total_without_del = 0;
                                total_without_del =Integer.parseInt(viewHolder.count_txt.getText().toString()) * Integer.parseInt(os_versions.get(position).getPrice());

                                grand_total_adpter = Integer.parseInt(viewHolder.count_txt.getText().toString())* Integer.parseInt(os_versions.get(position).getPrice()) + Integer.parseInt(os_versions.get(position).getDeliveryCharge());
                                check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                check_out_activity.tot_price_items.setText("\u20B9" + total_without_del);


                            } else {
                                Boolean result1 = db.update_server_table(viewHolder.count_txt.getText().toString(), os_versions.get(position).getCartID());

                                Cursor c = db.getall_data_server();


                                int grand_total_adpter = 0, total_without_del = 0;

                                if (c != null)
                                    if (c.moveToFirst()) {
                                        do {


                                            c.getString(c.getColumnIndex("quantity"));
                                            c.getString(c.getColumnIndex("regular_price"));
                                            total_without_del = total_without_del + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));


                                            grand_total_adpter = grand_total_adpter + Integer.parseInt(c.getString(c.getColumnIndex("del_charge"))) + Integer.parseInt(c.getString(c.getColumnIndex("quantity"))) * Integer.parseInt(c.getString(c.getColumnIndex("regular_price")));


                                        }
                                        while (c.moveToNext());


                                    }

                                Log.d("dddddddd", String.valueOf(grand_total_adpter));


                                if ("cart".equals(check_value)) {

                                    add_to_cart.total_amt.setText("\u20B9" + grand_total_adpter);
                                    add_to_cart.tot_price_items.setText("\u20B9" + total_without_del);
                                } else if ("check_out".equals(check_value)) {
                                    check_out_activity.total_amt.setText("\u20B9" + grand_total_adpter);
                                    check_out_activity.tot_price_items.setText("\u20B9" + total_without_del);


                                }
                                SharedPreferences.Editor editor1 = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor1.putInt("total_price", grand_total_adpter);
                                editor1.putInt("total_price_with_del", total_without_del);

                                editor1.putString("check_value", "price_update");
                                editor1.commit();


                            }
                            //
                        }

                        //   notifyDataSetChanged();

                    }}
            });
*/



  /*      viewHolder.image.setOnClickListener(new View.OnClickListener() {
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
        });*/


        //  Log.d("bhs",fp.getCat_name());
        // viewHolder.cat_image.setImageResource(fp.getCat_image());


        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Do you want to remove ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ProgressDialog progressDialog = new ProgressDialog(mContext,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Removing.. Please Wait");
                                progressDialog.show();


                                // TODO: Implement your own authentication logic here.

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {

                                                if("database_cart".equals(check_value)) {

                                                    DatabaseHandler db = new DatabaseHandler(mContext);

                                                    Cursor c=db.select_id_go_cart_log_out(os_versions.get(position).getProductID());

                                                    if (c != null)
                                                        if (c.moveToFirst()) {
                                                            do {


                                                                Boolean resultt=  db.delete_for_go_cart_log_out(c.getInt(c.getColumnIndex("id")));

                                                               // Log.d("fjigo", String.valueOf(resultt));


                                                            }
                                                            while (c.moveToNext());

                                                        }
                                                    Boolean check = db.delete_cart(os_versions.get(position).getCartID());
                                                  //  product_details.add_to_cart.setText("ADD TO CART");

                                                    Log.d("delete", String.valueOf(check));
                                                    Intent i = new Intent(mContext, add_to_cart.class);
                                                    mContext.startActivity(i);

                                                    ((Activity)mContext).finish();



                                                }

                                                else
                                                {

                                                    delete_cart_item(os_versions.get(position).getCartID(),new CallBack() {
                                                        @Override
                                                        public void onSuccess(String data) {

                                                            Log.d("loginnnnninnnter",data);
                                                            try {
                                                                JSONObject obj=new JSONObject(data);
                                                                String success_val=obj.getString("success");

                                                                if ("true".equals(success_val))
                                                                {


                                                                    Cursor c=db.select_id_go_cart(os_versions.get(position).getProductID());

                                                                    if (c != null)
                                                                        if (c.moveToFirst()) {
                                                                            do {


                                                                              Boolean resultt=  db.delete_for_go_cart(c.getInt(c.getColumnIndex("id")));

                                                                              Log.d("fjigo", String.valueOf(resultt));


                                                                            }
                                                                            while (c.moveToNext());

                                                                        }
                                                                   // product_details.add_to_cart.setText("ADD TO CART");

                                                                               //
                                                                    Intent i = new Intent(mContext, add_to_cart.class);
                                                                   // i.putExtra("test","");
                                                                    mContext.startActivity(i);

                                                                    ((Activity)mContext).finish();


                                                                }
                                                                else
                                                                {

                                                                    Toast.makeText(mContext, "Something ", Toast.LENGTH_SHORT).show();


                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            // Creating user login session
                                                            // For testing i am stroing name, email as follow
                                                            // Use user real data



                                                            Log.d("jhvfff",data);


                                                            //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onFail(String msg) {

                                                          //  Toast.makeText(LoginActivity.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                                            Log.d("jhvfff","failed");
                                                            // Do Stuff
                                                        }
                                                    });

                                                    // onLoginFailed();




                            }



                                                progressDialog.dismiss();
                                            }
                                        }, 3000);


                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text1)
        TextView cat_name;
        @BindView(R.id.tot)
        TextView tot;
        /*@BindView(R.id.price2)
        TextView price_cut;*/
        @BindView(R.id.price1)
        TextView price;
        @BindView(R.id.code)
        TextView code;
        /*@BindView(R.id.discount)
        TextView discount;*/
        @BindView(R.id.image1)
        ImageView image;
       /* @BindView(R.id.subtract)
        TextView subtract;*/
        /*@BindView(R.id.count)
        TextView count_txt;*/
        /*@BindView(R.id.add)
        TextView add;*/
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

    private void delete_cart_item(final String id, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/updateCartApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("loginnnnnnnnn",response);

                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("cartid",id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

}




