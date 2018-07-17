package com.kk_cards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 5/12/2018.
 */

public class verify_status_paytm  extends AppCompatActivity {
    SessionManagement session;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    @BindView(R.id.linear)
    LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_status_paytm);
        ButterKnife.bind(this);
        session=new SessionManagement(getApplicationContext());

        final ProgressDialog progressDialog = new ProgressDialog(verify_status_paytm.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying Transaction status...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        send_response();


                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);

    }

    protected void send_response() {

        final JSONObject json = new JSONObject();
        Set<String> keys = getIntent().getBundleExtra("paytm_response").keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                json.put(key, JSONObject.wrap(getIntent().getBundleExtra("paytm_response").get(key)));
            } catch(JSONException e) {
                //Handle exception here
            }
        }
        Log.d("tuiku",getIntent().getBundleExtra("paytm_response").toString());
        Log.d("ndvjgrho",json.toString());


        try {
            if("TXN_SUCCESS".equals(json.getString("STATUS"))) {

                verify_checksum(String.valueOf(json), new CallBack() {
                    @Override
                    public void onSuccess(String data) {

                        try{
                            JSONObject obj=new JSONObject(data);
                            if ("TRUE".equals(obj.getString("success")))
                            {

                                Log.d("STATUS_checkk",data);
                                verify_status(String.valueOf(json), new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {
                                        Log.d("STATUS_VERIFY",data);
                                        try{

                                            final JSONObject obj=new JSONObject(data);
                                            if ("TXN_SUCCESS".equals(obj.getString("STATUS"))) {
                                                Log.d("STATUS_VERIFY",obj.getString("STATUS"));


                                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                                                if("cart".equals(prefs.getString("buy_now_or_cart", null))) {
                                                    send_response_to_server_paytm(obj, new CallBack() {
                                                        @Override
                                                        public void onSuccess(String data) {
                                                            Log.d("fdtyyyyy","khkef89ure");

                                                            Log.d("responseeee_paytm", data);
                                                            try {
                                                                JSONObject obj = new JSONObject(data);
                                                                String success_val = obj.getString("success");

                                                                if ("true".equals(success_val)) {

                                                                    Toast.makeText(verify_status_paytm.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                                                    AppRater.showRateDialog(verify_status_paytm.this);



                                                                    LinearLayout innerLinearLayout = new LinearLayout(verify_status_paytm.this);

                                                                    TextView imageView = new TextView(verify_status_paytm.this);
                                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                    imageView.setText("Thank you for shopping with us....");
                                                                    imageView.setLayoutParams(params);
                                                                    innerLinearLayout.addView(imageView);
                                                                    Button textView = new Button(verify_status_paytm.this);
                                                                    textView.setText("Go To HOME");
                                                                    textView.setLayoutParams(params);
                                                                   linear.removeAllViews();
                                                                    innerLinearLayout.addView(textView);
                                                                    linear.addView(innerLinearLayout);


                                                                    textView.setOnClickListener(new View.OnClickListener() {
                                                                        public void onClick(View view) {
                                                                            Intent i=new Intent(verify_status_paytm.this,MainActivity.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            startActivity(i);
                                                                        }
                                                                    });

                                                                /*    TextView text = new TextView(verify_status_paytm.this);
                                                                    text.setText("Tnank you for Shopping with us");

                                                                    LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
                                                                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                    ll.addView(text, lp);

                                                                    Button myButton = new Button(verify_status_paytm.this);
                                                                    myButton.setText("Home");

                                                                     ll.addView(myButton, lp);
                                                                    myButton.setOnClickListener(new View.OnClickListener() {
                                                                        public void onClick(View view) {
                                                                          Intent i=new Intent(verify_status_paytm.this,MainActivity.class);
                                                                          startActivity(i);
                                                                        }
                                                                    });*/

                                                                                         /*  Intent i = new Intent(payment_page.this, product_details.class);
                                                                                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                           startActivity(i);*/

                                                                } else {

                                                                    Toast.makeText(verify_status_paytm.this, "Payment not successful", Toast.LENGTH_SHORT).show();


                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onFail(String msg) {

                                                            // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                                            Log.d("jhvfff", "failed");
                                                            // Do Stuff
                                                        }
                                                    });

                                                }

                                                else
                                                {
                                                    send_response_to_server_buy_paytm(obj, new CallBack() {
                                                        @Override
                                                        public void onSuccess(String data) {

                                                            Log.d("responseeee_paytm", data);
                                                            Log.d("fdtyyyyy","khkef89ure");
                                                            try {
                                                                JSONObject obj = new JSONObject(data);
                                                                String success_val = obj.getString("success");

                                                                if ("true".equals(success_val)) {


                                                                    Toast.makeText(verify_status_paytm.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                                                    AppRater.showRateDialog(verify_status_paytm.this);



                                                                    LinearLayout innerLinearLayout = new LinearLayout(verify_status_paytm.this);

                                                                    TextView imageView = new TextView(verify_status_paytm.this);
                                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                    imageView.setText("Thank you for shopping with us....");
                                                                    imageView.setLayoutParams(params);
                                                                    innerLinearLayout.addView(imageView);
                                                                    Button textView = new Button(verify_status_paytm.this);
                                                                    textView.setText("Go To HOME");
                                                                    textView.setLayoutParams(params);
                                                                    linear.removeAllViews();
                                                                    innerLinearLayout.addView(textView);
                                                                    linear.addView(innerLinearLayout);


                                                                    textView.setOnClickListener(new View.OnClickListener() {
                                                                        public void onClick(View view) {
                                                                            Intent i=new Intent(verify_status_paytm.this,MainActivity.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            startActivity(i);
                                                                        }
                                                                    });

                                                                } else {

                                                                   Toast.makeText(verify_status_paytm.this, "Payment not successful", Toast.LENGTH_SHORT).show();


                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onFail(String msg) {

                                                            // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                                            Log.d("jhvfff", "failed");
                                                            // Do Stuff
                                                        }
                                                    });


                                                }


                                                // onLoginFailed();

                                            }
                                        }
                                        catch (JSONException e)
                                        {


                                        }







                                                            /*   try{
                                                                   JSONObject obj=new JSONObject(data);
                                                                   if ("TRUE".equals(obj.getString("success")))
                                                                   {


                                                                   }
                                                               }
                                                               catch (JSONException e)
                                                               {


                                                               }*/


                                        Log.d("serverrrrrrr", data);


                                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFail(String msg) {

                                        // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                        Log.d("jhvfff", "failed");
                                        // Do Stuff
                                    }
                                });

                            }
                        }
                        catch (JSONException e)
                        {


                        }


                        Log.d("serverrrrrrr", data);


                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(String msg) {

                        // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                        Log.d("jhvfff", "failed");
                        // Do Stuff
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void verify_checksum(final String bundle, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.kkactionindia.in/checksum/verifyChecksum.php",
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
                params.put("bundle",bundle);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void verify_status(final String bundle, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.kkactionindia.in/PaytmKit/txnStatusApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("vfrhhhhhhhhhhhhhhhhhh",response);



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
                params.put("bundle",bundle);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void send_response_to_server_buy_paytm(final JSONObject obj, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/buyNowApi.php",
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
                try {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                    params.put("id", obj.getString("TXNID"));
                    params.put("mode", obj.getString("PAYMENTMODE"));
                    params.put("status", obj.getString("STATUS"));
                    params.put("unmappedstatus",obj.getString("RESPMSG"));
                    params.put("txnid",obj.getString("ORDERID"));
                    params.put("amount", obj.getString("TXNAMOUNT"));
                    // params.put("cardCategory",obj.getString("cardCategory"));
                    params.put("addedon", obj.getString("TXNDATE"));
                    params.put("productinfo","KK Products");
                    params.put("firstname", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("phone", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("productID",prefs.getString("product_id",null) );
                    params.put("quantity",prefs.getString("quantity_value",null) );
                    // params.put("PG_TYPE", obj.getString("PG_TYPE"));
                    // params.put("bank_ref_no", obj.getString("bank_ref_no"));
                    // params.put("name_on_card", obj.getString("name_on_card"));
                    // params.put("card_no",obj.getString("card_no"));
                    params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("addID",prefs.getString("address_id",null));
                    params.put("baddID",prefs.getString("billing_address_id",null));

                }
                catch(Exception e)
                {


                }
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void send_response_to_server_paytm(final JSONObject obj, final CallBack onCallBack){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/orderApi.php",
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
                try {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                    params.put("id", obj.getString("TXNID"));
                    params.put("mode", obj.getString("PAYMENTMODE"));
                    params.put("status", obj.getString("STATUS"));
                    params.put("unmappedstatus",obj.getString("RESPMSG"));
                    params.put("txnid",obj.getString("ORDERID"));
                    params.put("amount", obj.getString("TXNAMOUNT"));
                    // params.put("cardCategory",obj.getString("cardCategory"));
                    params.put("addedon", obj.getString("TXNDATE"));
                    params.put("productinfo","KK Products");
                    params.put("firstname", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("phone", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    // params.put("PG_TYPE", obj.getString("PG_TYPE"));
                    // params.put("bank_ref_no", obj.getString("bank_ref_no"));
                    // params.put("name_on_card", obj.getString("name_on_card"));
                    // params.put("card_no",obj.getString("card_no"));
                    params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("addID",prefs.getString("address_id",null));
                    params.put("baddID",prefs.getString("billing_address_id",null));

                }
                catch(Exception e)
                {


                }
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}