package com.kk_cards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/20/2018.
 */

public class forgot_password extends AppCompatActivity {

    @BindView(R.id.mobile)
    EditText edit_text;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.resent_otp)
    TextView resent_otp;
    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        ButterKnife.bind(this);

        if ("otp".equals(getIntent().getStringExtra("check_val")))
        {

            edit_text.setHint("Enter OTP");
            note.setVisibility(View.VISIBLE);
            resent_otp.setVisibility(View.VISIBLE);
    }

        if ("password".equals(getIntent().getStringExtra("check_val")))
        {edit_text.setHint("Enter Password");
            edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


        }

    }

    @OnClick(R.id.submit)
    public void send_mail() {

        if("forgot".equals(getIntent().getStringExtra("check_val"))) {

            if (edit_text.length() == 0) {

                Toast.makeText(this, "Please Enter your Mobile", Toast.LENGTH_SHORT).show();
            } else {

                final ProgressDialog progressDialog = new ProgressDialog(forgot_password.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Sending OTP..");
                progressDialog.show();


                // TODO: Implement your own authentication logic here.

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                forgot(edit_text.getText().toString(), new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {

                                        Log.d("passssssssss", data);
                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String success_val = obj.getString("success");

                                            if ("true".equals(success_val)) {
                                                Intent i = new Intent(getApplicationContext(), forgot_password.class);
                                                i.putExtra("check_val", "otp");
                                                i.putExtra("mobile", edit_text.getText().toString());
                                                startActivity(i);
                                                finish();
                                            }

                                            else
                                            {

                                                Toast.makeText(forgot_password.this, "Mail not sent, Please try again", Toast.LENGTH_SHORT).show();
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

                                // onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        }

        else if("otp".equals(getIntent().getStringExtra("check_val")))
        {


               Log.d("passssss",edit_text.getText().toString());

                if (edit_text.length() == 0) {

                    Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(forgot_password.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Checking OTP..");
                    progressDialog.show();


                    // TODO: Implement your own authentication logic here.

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    send_otp(getIntent().getStringExtra("mobile"),edit_text.getText().toString(), new CallBack() {
                                        @Override
                                        public void onSuccess(String data) {

                                            Log.d("passssssssss", data);
                                            try {
                                                JSONObject obj = new JSONObject(data);
                                                String success_val = obj.getString("success");

                                                if ("true".equals(success_val)) {
                                                    Intent i = new Intent(getApplicationContext(), forgot_password.class);
                                                    i.putExtra("check_val", "password");
                                                    i.putExtra("mobile", getIntent().getStringExtra("mobile"));

                                                    startActivity(i);
                                                    finish();
                                                }

                                                else
                                                {

                                                    Toast.makeText(forgot_password.this, "OTP not matches, please try again", Toast.LENGTH_SHORT).show();
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

                                    // onSignupFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);

                }
        }

        else if("password".equals(getIntent().getStringExtra("check_val")))
        {
            String pattern="(?=.*\\d)(?=.*[a-z]).{6,}";

            if (!edit_text.getText().toString().matches(pattern)) {

                Toast.makeText(this, "Atleast 6 alphanumeric characters(atleast 1 lowercase and 1 digit)", Toast.LENGTH_SHORT).show();
            } else {

                final ProgressDialog progressDialog = new ProgressDialog(forgot_password.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Changing Password..");
                progressDialog.show();


                // TODO: Implement your own authentication logic here.

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                change_password(getIntent().getStringExtra("mobile"),edit_text.getText().toString(), new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {

                                        Log.d("passssssssss", data);
                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String success_val = obj.getString("success");
                                            if ("true".equals(success_val)) {
                                                Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                                startActivity(i);



                                            } else {
                                                Toast.makeText(getApplicationContext(), "Password Not Changed", Toast.LENGTH_SHORT).show();

                                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                                startActivity(i);

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

                                // onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        }
    }

    @OnClick(R.id.resent_otp)

    protected void resend()
    {

        final ProgressDialog progressDialog = new ProgressDialog(forgot_password.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending OTP..");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        forgot(getIntent().getStringExtra("mobile"), new CallBack() {
                            @Override
                            public void onSuccess(String data) {

                                Log.d("passssssssss", data);
                                try {
                                    JSONObject obj = new JSONObject(data);
                                    String success_val = obj.getString("success");

                                    if ("true".equals(success_val)) {
                                        Toast.makeText(forgot_password.this, "OTP Resend", Toast.LENGTH_SHORT).show();
                                    }

                                    else
                                    {

                                        Toast.makeText(forgot_password.this, "Mail not sent, Please try again", Toast.LENGTH_SHORT).show();
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

                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);




    }

    private void send_otp(final String mobile,final String key_value, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/processApi.php",
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
                params.put("otp",key_value);
                params.put("mobile",mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void change_password(final String mobile,final String key_value, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/updatePassApi.php",
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
                params.put("password",key_value);
                params.put("mobile",mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void forgot(final String mobile, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/forgotPassApi.php",
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
                params.put("mobile",mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



}
}


