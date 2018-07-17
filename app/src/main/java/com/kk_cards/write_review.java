package com.kk_cards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/24/2018.
 */

public class write_review extends AppCompatActivity {

    @BindView(R.id.image1)
    ImageView image;
    @BindView(R.id.text1)
    TextView name;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.ratingBar1)
    RatingBar ratingBar1;

    SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);
        ButterKnife.bind(this);


        ratingBar1.setStepSize(1);

        session=new SessionManagement(getApplicationContext());

        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("image")).fit().into(image);

        name.setText(getIntent().getStringExtra("name"));
        Log.d("bjhiog", String.valueOf(ratingBar1.getRating()));




//        ratingBar1.setRating(Integer.parseInt(getIntent().getStringExtra("rate_value")));




    }

    @OnClick(R.id.submit)
    public void submitt()
    {

        if(desc.length()==0)
            Toast.makeText(this, "Please write your review", Toast.LENGTH_SHORT).show();
        else if(ratingBar1.getRating()==0.0)
            Toast.makeText(this, "Please choose star", Toast.LENGTH_SHORT).show();

        else
        set_review() ;


    }
    protected void set_review() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/reviewApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try

                        {
                            JSONObject obj=new JSONObject(response);
                            if("true".equals(obj.getString("success")))
                            {

                                final ProgressDialog progressDialog = new ProgressDialog(write_review.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Thank you ....");
                                progressDialog.show();


                                // TODO: Implement your own authentication logic here.

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                               // Toast.makeText(write_review.this, "Thank you for reviewing us", Toast.LENGTH_SHORT).show();
                               Intent i=new Intent(getApplicationContext(),MainActivity.class);
                               startActivity(i);

                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            }

                        }
                        catch (JSONException e)
                        {

                            Toast.makeText(write_review.this, "There is some issue, please try again", Toast.LENGTH_SHORT).show();

                        }







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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("productID", getIntent().getStringExtra("product_id"));
                params.put("mobile",session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                params.put("name",session.getUserDetails().get(SessionManagement.KEY_NAME));
                params.put("rate", String.valueOf(Math.round(ratingBar1.getRating())));
                params.put("review", desc.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}