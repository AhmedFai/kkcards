package com.kk_cards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/14/2018.
 */

public class invoice_pdf extends AppCompatActivity {


    private Bitmap screen;
    private View MyView;
    private View mRootView;

    java.util.List<String> near_hos;
    java.util.List<String> id1;
    Image image;
    int count = 0;

    public WebView wv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_pdf);



        if(getSupportActionBar()!=null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }


        //Log.d("iddd_val",getIntent().getStringExtra("id_value"));

        get_pdf(Config.Base_Url+"/API/invoiceApi.php?i_id="+getIntent().getStringExtra("id_value"), new CallBack() {
            @Override
            public void onSuccess(String data) {

                Log.d("pdfffffffff",data);


                JSONObject obj = null;
                try {
                    obj = new JSONObject(data);
                    String invoice_val = obj.getString("invoice");

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(invoice_val));
                    startActivity(browserIntent);

                  /*  Uri path = Uri.parse(invoice_val);



                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path , "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try
                    {
                        startActivity(pdfIntent); }
                    catch (ActivityNotFoundException e)
                    {


                }*/


            } catch (JSONException e) {

                    e.printStackTrace();
                }



            }

            @Override
            public void onFail(String msg) {
                Log.d("jhvfff", "failed");
                // Do Stuff
            }
        });


     /*   wv1 = (WebView) findViewById(R.id.webView);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.loadUrl("https://docs.google.com/viewer?url=http://kkactiveindia.in/admin/invoice.php?i_id="+getIntent().getStringExtra("id_value"));
   */    // wv1.setWebViewClient(new MyBrowser());

    }




    private void get_pdf(String url,final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        onCallBack.onSuccess(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

/*
        if (id == android.R.id.home) {

            finish();

        }*/


        return super.onOptionsItemSelected(item);

}


  /*@Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.containerView);
        if (f instanceof order_details)
        {
            ((order_details)f).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }*/
}
