package com.kk_cards.Fragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Config;
import com.kk_cards.R;
import com.kk_cards.invoice_pdf;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by user on 3/21/2018.
 */

public class order_details extends Fragment{

    @BindView(R.id.image1)
    ImageView image;
    @BindView(R.id.text1)
    TextView name_product;
    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.quantity)
    TextView quantity;
    @BindView(R.id.delivery_date)
    TextView delivery_date;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tot_price_items)
    TextView tot_price_items;

    @BindView(R.id.sel_price)
    TextView sel_price;
    @BindView(R.id.tot_price)
    TextView tot_price;
    @BindView(R.id.pay_mode)
    TextView pay_mode;

    @BindView(R.id.track_id)
    TextView track_id_txt;
    @BindView(R.id.no_item)
    TextView no_item;


    @BindView(R.id.order_id)
    TextView order_id;

    @BindView(R.id.cancel_order)
    Button cancel_order;
    @BindView(R.id.replacement)
    Button replace_order;
    @BindView(R.id.track_order)
    Button track_order;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;

    @BindView(R.id.layoutttttt)
    RelativeLayout layoutttttt;


    @BindView(R.id.scroll)
    ScrollView scroll;


    String product_id_d,transaction_id;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
         String url_tracking,track_id,order_status;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_details, container, false);
        ButterKnife.bind(this, v);
        p_bar.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        getorder_details(getArguments().getString("id"),getArguments().getString("product_id"));





        return v;
    }



    private void getorder_details(final String txn_id,final String product_id){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/orderDetailApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hidePDialog();

                        Log.d("derailss",response);

                        try {
                            JSONObject obj=new JSONObject(response);
                            //   record_count.setText(obj.getString("count")+" Records found");


                            JSONArray product_list=obj.getJSONArray("product");
                            JSONArray order_list=obj.getJSONArray("order");
                            JSONArray courier_list=obj.getJSONArray("courier");


                            for(int i=0;i<product_list.length();i++) {
                                JSONObject objj = product_list.getJSONObject(i);

                                product_id_d = objj.getString("productID");
                                name_product.setText(objj.getString("pname"));




                                float price_item=0;
                                price_item=Float.parseFloat(objj.getString("price"))/Float.parseFloat(objj.getString("quantity"));
                                price1.setText("\u20B9"+price_item);
                                  no_item.setText("Price("+objj.getString("quantity")+" item)");
                                quantity.setText("Quantity: "+objj.getString("quantity"));

                                Picasso.with(getContext()).load(objj.getString("image")).fit().into(image);
                                if("cod".equals(objj.getString("payuid")))
                                pay_mode.setText("Cash On Delivery");
                                else
                                    pay_mode.setText("Online Payment");

                                tot_price_items.setText("\u20B9"+objj.getString("price"));


                                price_item=Float.parseFloat(objj.getString("price"))+Float.parseFloat(objj.getString("dCharge"));

                                tot_price.setText("\u20B9"+price_item);
                                sel_price.setText("\u20B9"+objj.getString("dCharge"));



                            }

                            for(int i=0;i<order_list.length();i++)
                            {
                                JSONObject objj=order_list.getJSONObject(i);

                                name.setText(objj.getString("name"));
                                phone.setText(objj.getString("amobile"));

                                address.setText(objj.getString("flatno")+" , "+objj.getString("locality")+" , "+objj.getString("bcity")+" , "+objj.getString("state")+" - "+objj.getString("bpincode")+" , "+objj.getString("blandmark"));

                                String deliver_date = objj.getString("date");


                                delivery_date.setText("Ordered On "+deliver_date);



                                transaction_id=objj.getString("txnid");

                                order_id.setText("ORDER ID: "+transaction_id);

                                order_status=objj.getString("orderStatus");


                                if("0".equals(objj.getString("orderStatus"))) {
                                    cancel_order.setVisibility(View.VISIBLE);


                                }

                                else if("1".equals(objj.getString("orderStatus")))
                                {
                                    cancel_order.setVisibility(View.GONE);
                                    track_order.setVisibility(View.VISIBLE);
                                }

                                else if("2".equals(objj.getString("orderStatus")))
                                {
                                    cancel_order.setVisibility(View.GONE);

                                    Date c = Calendar.getInstance().getTime();

                                    String kept = objj.getString("deliveryDate");



                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = dateFormat.format(c);



                                  long dif=  getDaysBetweenDates(kept,formattedDate);

                                  if(dif<=10)
                                  {
                                      cancel_order.setVisibility(View.GONE);
                                      replace_order.setVisibility(View.VISIBLE);

                                  }

                                  else
                                  {
                                      cancel_order.setVisibility(View.GONE);
                                      replace_order.setVisibility(View.GONE);


                                  }
                                    Log.d("differeee", String.valueOf(dif));






                                }

                            }

                            for(int i=0;i<courier_list.length();i++) {
                                JSONObject objj = courier_list.getJSONObject(i);

                                url_tracking=objj.getString("urladd");
                                track_id=objj.getString("trackingID");
                                Log.d("track_iddd",track_id);
                                if(!"null".equals(objj.getString("trackingID")))
                                track_id_txt.setText("Track ID-"+objj.getString("trackingID"));
                                else
                                    track_id_txt.setVisibility(View.GONE);


                            }


                            }

                        catch (JSONException e) {
                            layoutttttt.setBackgroundResource(R.drawable.emptybag);
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        scroll.setVisibility(View.VISIBLE);

                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Edi
                      tor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("txnid",txn_id);
                params.put("productID",product_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        }
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    @OnClick(R.id.cancel_order)
    protected void cancel_order()
    {

            com.kk_cards.Fragment.cancel_order grid = new cancel_order();

            Bundle args = new Bundle();
            args.putString("treansaction_id", transaction_id);
            args.putString("product_id", product_id_d);
            grid.setArguments(args);
            mFragmentManager = getActivity().getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, grid).addToBackStack(null).commit();






    }


    @OnClick(R.id.invoice)
    protected void invoice()
    {

        Intent i=new Intent(getActivity(),invoice_pdf.class);
        i.putExtra("id_value",transaction_id);
        startActivity(i);
        getActivity().finish();







    }

    @OnClick(R.id.track_order)
    protected void track_order()
    {

       // Log.d("gdddhj",url_tracking);

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", track_id);
        clipboard.setPrimaryClip(clip);


        if("1".equals(order_status)) {
            if (url_tracking != null) {

                Toast.makeText(getActivity(),"Kindly fill track id to track your order",Toast.LENGTH_LONG);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_tracking));
                startActivity(browserIntent);

            }
        }



    }
    @OnClick(R.id.replacement)
    protected void repalce_order()
    {

        com.kk_cards.Fragment.replace_order grid = new replace_order();

        Bundle args = new Bundle();
        args.putString("treansaction_id", transaction_id);
        args.putString("product_id", product_id_d);

        grid.setArguments(args);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, grid).addToBackStack(null).commit();






    }

    private void hidePDialog() {
        if (p_bar != null) {
            p_bar.setVisibility(View.GONE);
            layoutttttt.setVisibility(View.VISIBLE);

        }
    }



}


