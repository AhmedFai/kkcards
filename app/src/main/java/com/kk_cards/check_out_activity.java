package com.kk_cards;

/**
 * Created by user on 3/19/2018.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Adapter.cart_adapter;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Modal.ItemData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;





/**
 * Created by pooja on 7/19/2017.
 */

public class check_out_activity extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    cart_adapter mAdapter1;

    private GridLayoutManager gridLayoutManager;

    ArrayList<String> id;
    DatabaseHandler db;

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    @BindView(R.id.checkout)
    TextView checkout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;



    int grand_total = 0;

    @BindView(R.id.no_item)
    TextView no_item;

    @BindView(R.id.del_price)
    TextView del_price;
    SessionManagement session;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.addressss)
    TextView addressss;
    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.chage_add)
    Button chage_add;

    @BindView(R.id.pric_r_layout)
    RelativeLayout pric_r_layout;
     private String MY_PREFS_NAME;
    int MODE_PRIVATE;

    @BindView(R.id.linearrr)
    LinearLayout linear;

    String cart_counter_real=null;

    public static TextView total_amt;
    public static TextView   tot_price_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        total_amt=(TextView) findViewById(R.id.total);
        tot_price_items=(TextView) findViewById(R.id.tot_price_items);

        //   MainActivity.footer.setVisibility(View.GONE);

        os_versions = new ArrayList<ItemData>();
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }





        pric_r_layout.setVisibility(View.GONE);

        this.setTitle("Checkout");



            getcart_data(Config.Base_Url+"/API/fetchCartApi.php");



        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("buy_now_or_cart","cart");
        editor.commit();



            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            if ("price_update".equals(prefs.getString("check_value", null))) {
                total_amt.setText(prefs.getString("check_value", null));
                tot_price_items.setText("\u20B9"+prefs.getString("check_value",null));

            }


            linear.setVisibility(View.VISIBLE);


            name.setText(prefs.getString("name", null));
            phone.setText(prefs.getString("phone", null));
            addressss.setText(prefs.getString("address", null));


            if(prefs.getString("name", null)==null)
            {
                name.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                addressss.setVisibility(View.GONE);

            }

        if(name.getText().toString()==null||name.getText().toString()=="")
        {

            chage_add.setText("Add Address");
        }
        else
        {
            chage_add.setText("Change or Add Address");

        }



    }

    @OnClick(R.id.checkout)
    public void checksum()

    {

         if(name.getText().toString()==null||name.getText().toString()=="")
         {
             Toast.makeText(this, "Please choose a address", Toast.LENGTH_SHORT).show();
         }

         else {

             android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(check_out_activity.this);
             alertDialogBuilder.setMessage("Is your Shipping Address and Billing Address Same?");
             alertDialogBuilder.setPositiveButton("NO",
                     new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface arg0, int arg1) {

                             Intent i = new Intent(getApplicationContext(), select_address.class);
                             i.putExtra("address","billing_address");
                             i.putExtra("check",getIntent().getStringExtra("check"));

                             startActivity(i);
                             finish();
                         }
                     });

             alertDialogBuilder.setNegativeButton("YES",
                     new DialogInterface.OnClickListener() {

                         @Override
                         public void onClick(DialogInterface arg0, int arg1) {


                             final ProgressDialog progressDialog = new ProgressDialog(check_out_activity.this,
                                     R.style.AppTheme_Dark_Dialog);
                             progressDialog.setIndeterminate(true);
                             progressDialog.setMessage("Please Wait.....");
                             progressDialog.show();


                             // TODO: Implement your own authentication logic here.

                             new android.os.Handler().postDelayed(
                                     new Runnable() {
                                         public void run() {




                                             Intent i = new Intent(getApplicationContext(), payment_page.class);
                                            /* i.putExtra("no_of_items", no_item.getText().toString());
                                             i.putExtra("delivery", del_price.getText().toString());*/
                                             i.putExtra("check_vall", "cart");
                                             startActivity(i);
                                             finish();
                                             // onLoginFailed();
                                             progressDialog.dismiss();
                                         }
                                     }, 3000);





                         }
                     });

                          /*
                         }
                     });*/

             android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
             alertDialog.show();





         }

    }






    @OnClick(R.id.chage_add)
    public void chage_adddd()

    {

        Intent i = new Intent(getApplicationContext(), select_address.class);
        i.putExtra("address","shipping_address");
        i.putExtra("check_valuee","cart_to_buy");
        startActivity(i);
      finish();
    }

    public void getcart_data(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();

                        DatabaseHandler db=new DatabaseHandler(getApplicationContext());

                        db.delete_cart_table_server();
                        Log.d("detalllllllllllll", response);
                        try {
                            JSONObject obj = new JSONObject(response);





                            JSONArray product_list = obj.getJSONArray("cart");

                            int total_del=0,total_price_items=0;
                            for (int i = 0; i < product_list.length(); i++) {
                                ItemData feed = new ItemData();
                                JSONObject objj = product_list.getJSONObject(i);


                                feed.setCartID(objj.getString("cartID"));
                                feed.setCardID(objj.getString("cardID"));

                                feed.setProductID(objj.getString("productID"));
                                feed.setQuantity(objj.getString("quantity"));
                                Log.d("quannnnnn_ddddd", feed.getQuantity());


                                feed.setProductName(objj.getString("productName"));
                                feed.setProductImage(objj.getString("image"));
                                feed.setDeliveryCharge(objj.getString("deliveryCharge"));
                                feed.setTot_quantity(objj.getString("productBalance"));

                                total_del=total_del+Integer.parseInt(feed.getDeliveryCharge());



                               // int discount = Integer.parseInt(objj.getString("discount"));
                                int price = Integer.parseInt(objj.getString("price"));


                               // int price_cut = (100 - discount) * price / 100;
                                feed.setPrice(objj.getString("price"));
                               // feed.setPrice_cut(String.valueOf(price_cut));
                               // feed.setDiscount(String.valueOf(discount));

                                os_versions.add(feed);

                                Boolean result=  db.insert_server(os_versions.get(i).getProductName(),os_versions.get(i).getPrice(),os_versions.get(i).getPrice_cut(),os_versions.get(i).getQuantity(),os_versions.get(i).getDiscount(),os_versions.get(i).getCartID(),os_versions.get(i).getProductImage(),objj.getString("deliveryCharge"));
                                Log.d("fffffff", String.valueOf(result));

                                total_price_items = total_price_items + Integer.parseInt(feed.getQuantity()) * Integer.parseInt(feed.getPrice());

                                grand_total = grand_total + Integer.parseInt(feed.getQuantity()) * Integer.parseInt(feed.getPrice());
                                tot_price_items.setText("\u20B9"+total_price_items);






                                grand_total=grand_total+Integer.valueOf(objj.getString("deliveryCharge"));

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putInt("total_price",grand_total);
                                editor.putInt("total_price_with_del",total_price_items);

                                editor.putString("check_value","");


                                editor.commit();

                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                total_amt.setText("\u20B9"+prefs.getInt("total_price",0));
                                no_item.setText("Price("+os_versions.size()+" items)");



                                //   total_amt.setText("Total: " + "Rs." + grand_total);
                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                //



                            }

                            del_price.setText(" \u20B9"+total_del);

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("delivery_price", del_price.getText().toString());
                            editor.putString("product_no_items", no_item.getText().toString());
                            editor.commit();






                            gridLayoutManager = new GridLayoutManager(check_out_activity.this, GridLayoutManager.VERTICAL);
                            mAdapter1 = new cart_adapter(check_out_activity.this, os_versions, "check_out","");
                            mAdapter.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);

                            mAdapter1.notifyDataSetChanged();


                        } catch (JSONException e) {


                            ImageView iv = new ImageView(getApplicationContext());
                            RelativeLayout layot = (RelativeLayout) findViewById(R.id.linear);

                            // Set an image for ImageView
                            iv.setImageResource(R.drawable.cart_empty);

                            // Create layout parameters for ImageView
                            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            // Add rule to layout parameters
                            // Add the ImageView below to Button

                            // Add layout parameters to ImageView
                            iv.setLayoutParams(lp);

                            layot.addView(iv);

                            total_amt.setVisibility(View.GONE);
                            checkout.setVisibility(View.GONE);
                            pric_r_layout.setVisibility(View.GONE);

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putInt("cart_size", 0);

                            // Log.d("cart_sizeeee", String.valueOf(os_versions.size()+1));

                            editor.commit();


                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("Response", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
                hidePDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));


                return params;
            }

        };
// Add the request to the queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
       final TextView cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);


        MenuItem item = menu.findItem(R.id.action_search);

        cart_icon.setVisibility(View.GONE);
        item.setVisible(false);

        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    private void hidePDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);
            pric_r_layout.setVisibility(View.VISIBLE);

        }
    }
    @Override
    protected void onPause() {


        os_versions=new ArrayList<>();
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        Cursor c = db.getall_data_server();

        int count=db.count_rows();
        Log.d("countt", String.valueOf(count));


        if (c != null)
            if (c.moveToFirst()) {
                do {


                    ItemData feed = new ItemData();

                    feed.setProductID(c.getString(c.getColumnIndex("product_id")));
                    feed.setQuantity(c.getString(c.getColumnIndex("quantity")));


                    Log.d("quantityyy",feed.getQuantity());
                    os_versions.add(feed);
                }
                while (c.moveToNext());

            }


        JSONObject jo = new JSONObject();


        try {

            Collection<JSONObject> items = new ArrayList<JSONObject>();




            for(int i=0;i<os_versions.size();i++) {

                JSONObject item1 = new JSONObject();
                item1.put("tempid", os_versions.get(i).getCartID());
                item1.put("quantity",os_versions.get(i).getQuantity());
                items.add(item1);
            }

            jo.put("cart_data", new JSONArray(items));
            System.out.println(jo.toString());
        }

        catch(Exception e)
        {


        }

        add_cart(String.valueOf(jo),new CallBack() {
            @Override
            public void onSuccess(String data) {

                Log.d("loginnnnninnnter",data);
                try {
                    JSONObject obj=new JSONObject(data);
                    String success_val=obj.getString("success");

                    if ("true".equals(success_val))
                    {


                        //                Toast.makeText(add_to_cart.this, " added in cart", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {

                        //       Toast.makeText(add_to_cart.this, "not added", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                Log.d("jhvfff",data);


                //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFail(String msg) {


                // Do Stuff
            }
        });







        super.onPause();
    }
    private void add_cart(final String cart_data, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/updateCartApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("derailss",response);

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
                params.put("cart",cart_data);
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}