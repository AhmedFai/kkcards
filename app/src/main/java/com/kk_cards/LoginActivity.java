package com.kk_cards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Modal.ItemData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.forgot_password)
    TextView forgot_password;
    SessionManagement session;
    private static final String REGISTER_URL = Config.Base_Url + "/API/signinApi.php";
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    @BindView(R.id.cbShowPwd)
    CheckBox cbShowPwd;
    String otp;

    ArrayList<ItemData> os_versions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new SessionManagement(getApplicationContext());


        cbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    _passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    // hide password
                    _passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("kya locha hai", "kuch to hai re baba");
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        String pattern = "(?=.*\\d)(?=.*[a-z]).{6,}";

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || _emailText.length() == 0) {
            _emailText.setError("enter a valid mobile number");

        } else if (!_passwordText.getText().toString().matches(pattern)) {
            _passwordText.setError("atleast 6 alphanumeric characters(atleast 1 lowercase and 1 digit)");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();


            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            login(new CallBack() {
                                @Override
                                public void onSuccess(String data) {

                                    Log.d("loginnnnninnnter", data);
                                    try {
                                        JSONObject obj = new JSONObject(data);
                                        String success_val = obj.getString("success");

                                        if ("true".equals(success_val)) {

                                            session.createLoginSession(obj.getString("name"), obj.getString("email"), obj.getString("mobile"));


                                            final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                            if (db.count_rows() > 0) {

                                                JSONObject jo = new JSONObject();


                                                try {

                                                    Collection<JSONObject> items = new ArrayList<JSONObject>();


                                                    ArrayList<ItemData> os_versions = new ArrayList<>();
                                                    Cursor c = db.getall_data1();

                                                    if (c != null)
                                                        if (c.moveToFirst()) {
                                                            do {


                                                                ItemData feed = new ItemData();


                                                                feed.setProduct_id(c.getString(c.getColumnIndex("product_id")));
                                                                feed.setQuantity(c.getString(c.getColumnIndex("quantity")));

                                                                os_versions.add(feed);
                                                            }
                                                            while (c.moveToNext());

                                                            for (int i = 0; i < os_versions.size(); i++) {

                                                                JSONObject item1 = new JSONObject();
                                                                item1.put("product_id", os_versions.get(i).getProduct_id());
                                                                item1.put("quantity", os_versions.get(i).getQuantity());
                                                                items.add(item1);
                                                            }

                                                            jo.put("cart_data", new JSONArray(items));
                                                            System.out.println(jo.toString());
                                                        }
                                                } catch (Exception e) {


                                                }

                                                add_cart(String.valueOf(jo), new CallBack() {
                                                    @Override
                                                    public void onSuccess(String data) {

                                                        Log.d("loginnnnninnnter", data);
                                                        try {
                                                            JSONObject obj = new JSONObject(data);
                                                            String success_val = obj.getString("success");


                                                            db.delete1();


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                        Log.d("jhvfff", data);


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


                                            //    get_popular_latest(Config.Base_Url+"/API/latestApi.php?mobile="+session.getUserDetails().get(SessionManagement.KEY_MOBILE));


                                            // Staring MainActivity


                                           // display_address(Config.Base_Url + "/API/addressApi.php?mobile=" + obj.getString("mobile"));



                                            SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                                            if ("buy_now".equals(pref.getString("from_where", null))) {

                                                Intent i = new Intent(getApplicationContext(), check_out_buy_now.class);
                                                startActivity(i);


                                            } else if ("add_to_cart".equals(pref.getString("from_where", null))) {
                                                Intent i = new Intent(getApplicationContext(), check_out_activity.class);
                                                startActivity(i);

                                            } else {
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(i);
                                                finish();

                                            }


                                        } else {

                                            Toast.makeText(LoginActivity.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    // Creating user login session
                                    // For testing i am stroing name, email as follow
                                    // Use user real data


                                    Log.d("jhvfff", data);


                                    //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFail(String msg) {

                                    Toast.makeText(LoginActivity.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                    Log.d("jhvfff", "failed");
                                    // Do Stuff
                                }
                            });

                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }



       /* if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

  /*  @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        login(new CallBack() {
            @Override
            public void onSuccess(String data) {

                Log.d("loginnnnninnnter", data);


//    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFail(String msg) {
                Log.d("jhvfff", "failed");
// Do Stuff
            }
        });

    }


    public void display_address(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);


                            if (obj.has("success"))

                                if ("false".equals(obj.getString("success"))) {

                                    //count_address.setText("No Address Found");
                                }


                            JSONArray product_list = obj.getJSONArray("products");

                            for (int i = 0; i < product_list.length(); i++) {
                                ItemData feed = new ItemData();
                                JSONObject objj = product_list.getJSONObject(i);

                                feed.setId(objj.getString("add_id"));
                                feed.setCat_name(objj.getString("name"));

                                feed.setCity(objj.getString("city"));
                                feed.setArea(objj.getString("locality"));
                                feed.setLandmark(objj.getString("landmark"));

                                feed.setPincode(objj.getString("pincode"));


                                feed.setState(objj.getString("state"));
                                feed.setFlat_no(objj.getString("flatno"));

                                feed.setMobile(objj.getString("amobile"));


                                feed.setAdd_type(objj.getString("addtype"));


                                os_versions.add(feed);


                                if (i == 0) {


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("name", os_versions.get(i).getCat_name());
                                    editor.putString("phone", os_versions.get(i).getMobile());
                                    editor.putString("address", os_versions.get(i).getFlat_no() + " , " + os_versions.get(i).getArea() + " , " + os_versions.get(i).getCity() + " , " + os_versions.get(i).getState() + " - " + os_versions.get(i).getPincode() + " , " + os_versions.get(i).getLandmark());

                                    editor.commit();

                                }


                                SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                                if ("buy_now".equals(pref.getString("from_where", null))) {

                                    Intent i1 = new Intent(getApplicationContext(), check_out_buy_now.class);
                                    startActivity(i1);


                                } else if ("add_to_cart".equals(pref.getString("from_where", null))) {
                                    Intent i1 = new Intent(getApplicationContext(), check_out_activity.class);
                                    startActivity(i1);

                                } else {
                                    Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i1);
                                    finish();

                                }


                            }


                        } catch (JSONException e) {


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


            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);


    }


    @OnClick(R.id.forgot_password)
    public void forgot() {

        Intent i = new Intent(getApplicationContext(), forgot_password.class);
        i.putExtra("check_val", "forgot");
        i.putExtra("mobile", "");

        startActivity(i);
        finish();

    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || _emailText.length() == 0) {
            _emailText.setError("enter a valid mobile number");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("enter a password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void login(final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("loginnnnnnnnn", response);

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
                params.put("mobile", _emailText.getText().toString());
                params.put("password", _passwordText.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void add_cart(final String cart_data, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/cartApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("derailss", response);

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
                params.put("cart", cart_data);
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void get_popular_latest(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);


                            if (obj.has("cartCount")) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("cart_counter_value", obj.getString("cartCount"));
                                editor.commit();

                            }


                        } catch (JSONException e) {
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


            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);


    }

    private void send_otp(final String mobile, final String key_value, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/processApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("loginnnnnnnnn", response);

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
                params.put("otp", key_value);
                params.put("mobile", mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void change_password(final String mobile, final String key_value, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/updatePassApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("loginnnnnnnnn", response);

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
                params.put("password", key_value);
                params.put("mobile", mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void forgot(final String mobile, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/forgotPassApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("loginnnnnnnnn", response);

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
                params.put("mobile", mobile);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
