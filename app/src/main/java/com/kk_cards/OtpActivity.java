package com.kk_cards;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class OtpActivity extends AppCompatActivity implements OTPListener {

    EditText phn, otp;
    Button get, proceed;
    String value;
    private static final String OTP_URL = Config.Base_Url + "/API/sendCodeApi.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        OtpReader.bind(this, "TX-KKCARD");

        phn = (EditText) findViewById(R.id.number);
        otp = (EditText) findViewById(R.id.potp);
        get = (Button) findViewById(R.id.getOtp);
        proceed = (Button) findViewById(R.id.proceed);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ActivityCompat.requestPermissions(OtpActivity.this,
                        new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},
                        1);

                final ProgressDialog progressDialog = new ProgressDialog(OtpActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Sending OTP...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                final String phone, code;


                                phone = phn.getText().toString();
                                code = otp.getText().toString();

                                if (phn.getText().toString().isEmpty() || phn.getText().toString().length() == 0 || phn.getText().toString().length() < 10)
                                    phn.setError("enter a valid mobile no");


                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                OTP(phone, new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {


                                        Log.d("result.......", data);


                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String success_val = obj.getString("success");
                                            if ("true".equals(success_val)) {

                                                // Toast.makeText(OtpActivity.this, obj.getString("otp"), Toast.LENGTH_SHORT).show();


                                                otp.setVisibility(View.VISIBLE);
                                                get.setVisibility(View.GONE);
                                                proceed.setVisibility(View.VISIBLE);

                                                value = obj.getString("otp");
                                                proceed.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if (otp.getText().toString().isEmpty()) {
                                                            otp.setError("Field is empty");
                                                            otp.requestFocus();
                                                        } else {
                                                            if (otp.getText().toString().matches(value)) {
                                                                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                                                                i.putExtra("number", phn.getText().toString());
                                                                startActivity(i);
                                                                finish();
                                                            } else {
                                                                otp.setError("Enter Valid OTP");
                                                                otp.requestFocus();
                                                            }
                                                        }


                                                    }
                                                });

                                                // otp.setVisibility(View.VISIBLE);
                                                // get.setText("Proceed");

                                               /* if (get.getText().toString().equals("Proceed") ){
                                                       //Toast.makeText(OtpActivity.this, "Registered Successfuly", Toast.LENGTH_SHORT).show();

                                                }*/


                                            } else {

                                                Toast.makeText(OtpActivity.this, "Enter a valid number", Toast.LENGTH_SHORT).show();


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFail(String msg) {
                                        Log.d("jhvfff", "failed");
                                        // Do Stuff
                                    }
                                });
                                // onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);




     /*           Log.d("OnCLICK", "hogyaclick");

                final String m = phn.getText().toString();
                final String o = otp.getText().toString();

                if (!TextUtils.isEmpty(m)){

                  //  if (!TextUtils.isEmpty(o)){

                       // final Bean b = (Bean) getApplicationContext();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Config.Base_Url)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Allapi cr = retrofit.create(Allapi.class);

                        Call<OTPBean> call = cr.mobile(m);
                        call.enqueue(new Callback<OTPBean>() {
                            @Override
                            public void onResponse(Call<OTPBean> call, Response<OTPBean> response) {

                                Log.d("response","nahi aa raha");
                                otp.setVisibility(View.VISIBLE);
                                get.setText("Proceed");
                                Toast.makeText(OtpActivity.this, "Your Otp is" + response.body().getOtp(), Toast.LENGTH_SHORT).show();

                               *//* Intent i = new Intent(OtpActivity.this, SignupActivity.class);
                                i.putExtra("mobile", response.body().getMobile());
                                startActivity(i);*//*
                            }

                            @Override
                            public void onFailure(Call<OTPBean> call, Throwable t) {

                                Log.d("fail", t.toString());

                            }
                        });


                    }else {
                        otp.setError("Field is Empty");
                        otp.requestFocus();
                    }

               *//* }else {
                    phn.setError("Field is Empty");
                    phn.requestFocus();
                }*/


            }
        });


    }

    private void OTP(final String otp, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, OTP_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("ressss", response);


                        onCallBack.onSuccess(response);


                        // Log.d("nmcksjo",response);

                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", phn.getText().toString());
                params.put("otp", otp);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void otpReceived(String messageText) {
        Log.d("otprecieved", "received");
        String mess = messageText;
        Log.d("otp", messageText);

        int length = mess.length();

        String substr = mess.substring(length - 6, length);

        otp.setText(substr);
    }
}
