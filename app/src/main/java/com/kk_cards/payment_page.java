package com.kk_cards;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Database.DatabaseHandler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




/**
 * Created by user on 3/21/2018.
 */


/**
 * Created by pooja on 7/19/2017.
 */

public class payment_page extends AppCompatActivity {


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;


    String cart_counter_real = null;

    ArrayList<String> id;
    DatabaseHandler db;

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    @BindView(R.id.checkout)
    TextView checkout;

    @BindView(R.id.total)
    TextView total_amt;

    String amt;

    int grand_total = 0;

    @BindView(R.id.no_item)
    TextView no_item;
    @BindView(R.id.tot_price_items)
    TextView tot_price_items;

    @BindView(R.id.del_price)
    TextView del_price;
    SessionManagement session;


    @BindView(R.id.pric_r_layout)
    RelativeLayout pric_r_layout;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;

    @BindView(R.id.linearrr)
    LinearLayout linear;


    @BindView(R.id.radios)
    RadioGroup rd_grp;
    @BindView(R.id.cod)
    RadioButton cod;
    @BindView(R.id.paytm)
    RadioButton paytm;
    @BindView(R.id.pay_u_radio)
    RadioButton pay_u_radio;

    String radio_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        Payu.setInstance(this);

        cod.setEnabled(false);
        //   MainActivity.footer.setVisibility(View.GONE);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }


        this.setTitle("Payments");


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        total_amt.setText("\u20B9" + prefs.getInt("total_price", 0));
        tot_price_items.setText("\u20B9" + String.valueOf(prefs.getInt("total_price_with_del", 0)));
        no_item.setText(prefs.getString("product_no_items", null));
        del_price.setText(prefs.getString("delivery_price", null));

       // Intent intent = getIntent();
       // amt = intent.getStringExtra("amt");
        //Toast.makeText(this, "total amount is" + amt, Toast.LENGTH_SHORT).show();

        amt = String.valueOf(prefs.getInt("total_price",0));



        rd_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.cod) {


                    radio_value = "cod";

                    //some code
                } else if (i == R.id.pay_u_radio) {

                    radio_value = "payu";
                    //some code
                } else if (i == R.id.paytm) {

                    radio_value = "paytm";
                    //some code
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @OnClick(R.id.checkout)
    public void checksum()

    {

        if (radio_value == null)
            Toast.makeText(getApplicationContext(), "Please Choose payment type", Toast.LENGTH_SHORT).show();

        else {

            final ProgressDialog progressDialog = new ProgressDialog(payment_page.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();


            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if ("cod".equals(radio_value)) {

                                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(payment_page.this);
                                alertDialogBuilder.setMessage("Are you sure you want to Confirm this Order?");
                                alertDialogBuilder.setPositiveButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {


                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("YES",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                final ProgressDialog progressDialog = new ProgressDialog(payment_page.this,
                                                        R.style.AppTheme_Dark_Dialog);
                                                progressDialog.setIndeterminate(true);
                                                progressDialog.setMessage("Confirming Order.....");
                                                progressDialog.show();


                                                // TODO: Implement your own authentication logic here.

                                                new android.os.Handler().postDelayed(
                                                        new Runnable() {
                                                            public void run() {


                                                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


                                                                if ("cart".equals(prefs.getString("buy_now_or_cart", null))) {
                                                                    send_data_for_cod(new CallBack() {
                                                                        @Override
                                                                        public void onSuccess(String data) {

                                                                            Log.d("loginnnnninnnter", data);
                                                                            try {
                                                                                JSONObject obj = new JSONObject(data);
                                                                                String success_val = obj.getString("success");

                                                                                if ("true".equals(success_val)) {


                                                                                    Toast.makeText(payment_page.this, "Order successful", Toast.LENGTH_SHORT).show();

                                                                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                                    editor.putString("cart_counter_value", "0");
                                                                                    editor.commit();


                                                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(i);


                                                                                } else {

                                                                                    Toast.makeText(payment_page.this, "Order not successful, Please Try again", Toast.LENGTH_SHORT).show();


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
                                                                } else {

                                                                    send_data_for_buy(new CallBack() {
                                                                        @Override
                                                                        public void onSuccess(String data) {

                                                                            Log.d("loginnnnninnnter", data);
                                                                            try {
                                                                                JSONObject obj = new JSONObject(data);
                                                                                String success_val = obj.getString("success");

                                                                                if ("true".equals(success_val)) {


                                                                                    Toast.makeText(payment_page.this, "Order successful", Toast.LENGTH_SHORT).show();

                                                                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                                    editor.putString("cart_counter_value", "0");
                                                                                    editor.commit();


                                                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(i);


                                                                                } else {

                                                                                    Toast.makeText(payment_page.this, "Order not successful, Please Try again", Toast.LENGTH_SHORT).show();


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
                                                                progressDialog.dismiss();
                                                            }
                                                        }, 3000);


                                            }
                                        });

                                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            } else if ("payu".equals(radio_value)) {
                                navigateToBaseActivity();


                            } else if ("paytm".equals(radio_value)) {
                                generate_checksum(new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {
                                        try {
                                            JSONObject obj = new JSONObject(data);
                                              onStartTransaction(obj.getString("CHECKSUMHASH"),obj.getString("ORDER_ID"));


                                        } catch (JSONException e) {
                                        }
                                    }
                                    @Override
                                    public void onFail(String msg) {
                                    }
                                });
                               // onStartTransaction();

                            }
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);


        }
    }
    public void onStartTransaction(String checksumm,String order_idd) {
               PaytmPGService Service =PaytmPGService.getProductionService();

                    Map<String, String> params = new HashMap<String, String>();
                     params.put("MID","Active62296140388219");
                    params.put("ORDER_ID",order_idd);
                    params.put("CUST_ID",session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("INDUSTRY_TYPE_ID","Retail109");
                    params.put("CHANNEL_ID","WAP");
                    params.put("TXN_AMOUNT",amt);
                    params.put("WEBSITE","APPPROD");
                    params.put( "CALLBACK_URL" , "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+order_idd);
                    params.put("EMAIL",session.getUserDetails().get(SessionManagement.KEY_EMAIL));
                    params.put("MOBILE_NO",session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                    params.put("CHECKSUMHASH",checksumm);

                       PaytmOrder Order = new PaytmOrder(params);

                    Service.initialize(Order, null);

                     Service.startPaymentTransaction(this,true,true,new

                         PaytmPaymentTransactionCallback() {

                           @Override
                             public void someUIErrorOccurred(String inErrorMessage){
                                Log.d("LOG", "UI Error Occur.");
                                  Toast.makeText(getApplicationContext(), " UI Error Occur. ", Toast.LENGTH_LONG).show();
                    }

                           @Override
                     public void onTransactionResponse(Bundle inResponse){


                               Intent i=new Intent(getApplicationContext(),verify_status_paytm.class);
                          i.putExtra("paytm_response",inResponse);
                               startActivity(i);


                           Log.d("LOG", "Payment Transaction : " + inResponse);
                      //  Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.d("LOG", "UI Error Occur.");
                        Toast.makeText(getApplicationContext(), " UI Error Occur. ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage){
                        Log.d("LOG", "UI Error Occur.");
                        Toast.makeText(getApplicationContext(), " Severside Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                        String inErrorMessage, String inFailingUrl){

                    }
                    @Override
                    public void onBackPressedCancelTransaction() {
                     // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse){
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });



    }


    private void generate_checksum(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.kkactionindia.in/checksum/generateChecksum.php",
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

                        Log.d("jhbfijg",error .toString());
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String order_id="" + System.currentTimeMillis();
                params.put("MID","Active62296140388219");
                params.put("ORDER_ID",order_id);
                params.put("CUST_ID",session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                params.put("INDUSTRY_TYPE_ID","Retail109");
                params.put("CHANNEL_ID","WAP");
                params.put("TXN_AMOUNT",amt);
                params.put("WEBSITE","APPPROD");
                params.put( "CALLBACK_URL" , "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+order_id);
                params.put("EMAIL",session.getUserDetails().get(SessionManagement.KEY_EMAIL));
                params.put("MOBILE_NO",session.getUserDetails().get(SessionManagement.KEY_MOBILE));



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
        final TextView  cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
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


    public void navigateToBaseActivity() {


        // merchantKey="";
      /*  merchantKey = ((EditText) findViewById(R.id.editTextMerchantKey)).getText().toString();
        String amount = ((EditText) findViewById(R.id.editTextAmount)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
*/
      /*  String value = environmentSpinner.getSelectedItem().toString();
        int environment;
        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
        if (value.equals(TEST_ENVIRONMENT))
            environment = PayuConstants.STAGING_ENV;
        else
            environment = PayuConstants.PRODUCTION_ENV;*/
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String email="armaan.activeindia@gmail.com";
        int environment= PayuConstants.PRODUCTION_ENV;
        merchantKey="i9HRrm";


        userCredentials = merchantKey + ":" + session.getUserDetails().get(SessionManagement.KEY_EMAIL);

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        /**
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id

         */



        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(String.valueOf(prefs.getInt("total_price",0)));
        mPaymentParams.setProductInfo("KK Products");
        mPaymentParams.setFirstName(session.getUserDetails().get(SessionManagement.KEY_NAME));
        mPaymentParams.setEmail(session.getUserDetails().get(SessionManagement.KEY_EMAIL));
        mPaymentParams.setPhone(prefs.getString("phone", null));


        /*
        * Transaction Id should be kept unique for each transaction.
        * */
        mPaymentParams.setTxnId("" + System.currentTimeMillis());

        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        /*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */
        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");

        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        //   payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        generateHashFromServer(mPaymentParams);

        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
        //String salt = "eCwWELxi";
        //   generateHashFromSDK(mPaymentParams, salt);

    }
    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }
    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(payment_page.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                // URL url = new URL("https://tsd.payu.in/GetHash");
                URL url = new URL(Config.Base_Url+"/API/payuTest.php");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }
    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        fetchMerchantHashes(intent);

    }


    //TODO This method is used if integrating One Tap Payments

    /**
     * This method stores merchantHash and cardToken on merchant server.
     *
     * @param cardToken    card token received in transaction response
     * @param merchantHash merchantHash received in transaction response
     */
    private void storeMerchantHash(String cardToken, String merchantHash) {

        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials + "&card_token=" + cardToken + "&merchant_hash=" + merchantHash;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //TODO Deploy a file on your server for storing cardToken and merchantHash nad replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/store_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }


    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method fetches merchantHash and cardToken already stored on merchant server.
     */
    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);

                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method deletes merchantHash and cardToken from server side file.
     *
     * @param cardToken cardToken of card whose merchantHash and cardToken needs to be deleted from merchant server
     */
    private void deleteMerchantHash(String cardToken) {

        final String postParams = "card_token=" + cardToken;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/delete_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method prepares a HashMap of cardToken as key and merchantHash as value.
     *
     * @param merchantKey     merchant key used
     * @param userCredentials unique credentials of the user usually of the form key:userId
     */
    public HashMap<String, String> getAllOneClickHashHelper(String merchantKey, String userCredentials) {

        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        HashMap<String, String> cardTokens = new HashMap<String, String>();

        try {
            //TODO Replace below url with your server side file url.
            URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

            byte[] postParamsByte = postParams.getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postParamsByte);

            InputStream responseInputStream = conn.getInputStream();
            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                responseStringBuffer.append(new String(byteContainer, 0, i));
            }

            JSONObject response = new JSONObject(responseStringBuffer.toString());

            JSONArray oneClickCardsArray = response.getJSONArray("data");
            int arrayLength;
            if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                for (int i = 0; i < arrayLength; i++) {
                    cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                }

            }
            // pass these to next activity

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardTokens;
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * Returns a HashMap object of cardToken and one click hash from merchant server.
     * <p>
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * The function should return a cardToken and corresponding one click hash as a hashMap.
     *
     * @param userCreds a string giving the user credentials of user.
     * @return the Hash Map of cardToken and one Click hash.
     **/

    public HashMap<String, String> getAllOneClickHash(String userCreds) {
        // 1. GET http request from your server
        // GET params - merchant_key, user_credentials.
        // 2. In response we get a
        // this is a sample code for fetching one click hash from merchant server.
        return getAllOneClickHashHelper(merchantKey, userCreds);
    }

    //TODO This method is used only if integrating One Tap Payments

    public void getOneClickHash(String cardToken, String merchantKey, String userCredentials) {

    }


    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function save the oneClickHash corresponding to its cardToken
     *
     * @param cardToken    a string containing the card token
     * @param oneClickHash a string containing the one click hash.
     **/


    public void saveOneClickHash(String cardToken, String oneClickHash) {
        // 1. POST http request to your server
        // POST params - merchant_key, user_credentials,card_token,merchant_hash.
        // 2. In this POST method the oneclickhash is stored corresponding to card token in merchant server.
        // this is a sample code for storing one click hash on merchant server.

        storeMerchantHash(cardToken, oneClickHash);

    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function delete’s the oneClickHash from the merchant server
     *
     * @param cardToken       a string containing the card token
     * @param userCredentials a string containing the user credentials.
     **/


    public void deleteOneClickHash(String cardToken, String userCredentials) {

        // 1. POST http request to your server
        // POST params  - merchant_hash.
        // 2. In this POST method the oneclickhash is deleted in merchant server.
        // this is a sample code for deleting one click hash from merchant server.

        deleteMerchantHash(cardToken);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                //  Log.d("responseee",data.getStringExtra("payu_response"));
                //Log.d("resulttt",data.getStringExtra("result"));


                String response=data.getStringExtra("payu_response");
                Log.d("response_from_pay_u",response);


                try {
                    JSONObject obj=new JSONObject(response);

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                    if("cart".equals(prefs.getString("buy_now_or_cart", null))) {
                        send_response_to_server(obj, new CallBack() {
                            @Override
                            public void onSuccess(String data) {

                                Log.d("serverrrrrrr", data);
                                try {
                                    JSONObject obj = new JSONObject(data);
                                    String success_val = obj.getString("success");

                                    if ("true".equals(success_val)) {

                                        Toast.makeText(payment_page.this, "Payment Successful", Toast.LENGTH_SHORT).show();


                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("cart_counter_value", "0");
                                        editor.commit();


                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                    } else {

                                        //   Toast.makeText(product_details.this, "not added", Toast.LENGTH_SHORT).show();


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
                        send_response_to_server_buy(obj, new CallBack() {
                            @Override
                            public void onSuccess(String data) {

                                Log.d("serverrrrrrr", data);
                                try {
                                    JSONObject obj = new JSONObject(data);
                                    String success_val = obj.getString("success");

                                    if ("true".equals(success_val)) {

                                        Toast.makeText(payment_page.this, "Payment Successful", Toast.LENGTH_SHORT).show();


                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("cart_counter_value", "0");
                                        editor.commit();


                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        AppRater.showRateDialog(payment_page.this);

                                        //AppRater.showRateDialog(pro.this);


                                    } else {

                                        //   Toast.makeText(product_details.this, "not added", Toast.LENGTH_SHORT).show();


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

                    //  Log.d("rse_idddddddddddddd",obj.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }









                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
              /*  new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*/

            } else {


                PayuResponse payuResponse=new PayuResponse();
                if (payuResponse.isResponseAvailable() && payuResponse.getResponseStatus().getCode() == PayuErrors.NO_ERROR) { // ok we are good to go
                    // Toast.makeText(this, payuResponse.getResponseStatus().getResult(), Toast.LENGTH_LONG).show();

                    Toast.makeText(this, "Payment Unsuccessful,Please try again", Toast.LENGTH_LONG).show();

                }}
        }
    }



    private void send_response_to_server_buy(final JSONObject obj, final CallBack onCallBack){


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

                    params.put("id", obj.getString("id"));
                    params.put("mode", obj.getString("mode"));
                    params.put("status", obj.getString("status"));
                    params.put("unmappedstatus",obj.getString("unmappedstatus"));
                    params.put("txnid",obj.getString("txnid"));
                    params.put("amount", obj.getString("amount"));
                    // params.put("cardCategory",obj.getString("cardCategory"));
                    params.put("addedon", obj.getString("addedon"));
                    params.put("productinfo","KK Products");     params.put("firstname", obj.getString("firstname"));
                    params.put("phone", obj.getString("phone"));
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

    private void send_response_to_server(final JSONObject obj, final CallBack onCallBack){


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

                    params.put("id", obj.getString("id"));
                    params.put("mode", obj.getString("mode"));
                    params.put("status", obj.getString("status"));
                    params.put("unmappedstatus",obj.getString("unmappedstatus"));
                    params.put("txnid",obj.getString("txnid"));
                    params.put("amount", obj.getString("amount"));
                    // params.put("cardCategory",obj.getString("cardCategory"));
                    params.put("addedon", obj.getString("addedon"));
                    params.put("productinfo","KK Products"); params.put("firstname", obj.getString("firstname"));
                    params.put("phone", obj.getString("phone"));
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


    private void send_data_for_cod(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/codOrderApi.php",
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

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                params.put("mode","cod");
                params.put("txnid", "" + System.currentTimeMillis());
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                params.put("addID",prefs.getString("address_id",null));
                params.put("baddID",prefs.getString("billing_address_id",null));




                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void send_data_for_buy(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/codBuyNowApi.php",
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

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                params.put("mode","cod");
                params.put("txnid", "" + System.currentTimeMillis());
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                params.put("addID",prefs.getString("address_id",null));
                params.put("baddID",prefs.getString("billing_address_id",null));
                params.put("quantity",prefs.getString("quantity_value",null) );
                params.put("productID",prefs.getString("product_id",null) );




                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}