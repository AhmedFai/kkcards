package com.kk_cards;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;



public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    String num;

    @BindView(R.id.input_name) EditText _nameText;
  //  @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
   // @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    private static final String REGISTER_URL = Config.Base_Url+"/API/signupApi.php";
    Uri EMAIL_ACCOUNTS_DATABASE_CONTENT_URI;
    final static int requestcode = 4;
    String possibleEmail;
    @BindView(R.id.cbShowPwd)
    CheckBox cbShowPwd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);



        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

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

        Intent intent = getIntent();
        num = intent.getStringExtra("number");
        _mobileText.setText(num);


    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void signup() {


        String pattern="(?=.*\\d)(?=.*[a-z]).{6,}";


        if (_nameText.getText().toString().isEmpty() || _nameText.length() == 0) {
            _nameText.setError("enter a valid name");

        } else if (_mobileText.getText().toString().isEmpty() || _mobileText.getText().toString().length() == 0 || _mobileText.getText().toString().length() < 10)
            _mobileText.setError("enter a valid mobile no");
       else if(!_passwordText.getText().toString().matches(pattern)){
            _passwordText.setError("atleast 6 alphanumeric characters(atleast 1 lowercase and 1 digit)");
        }

        else if(_emailText.length()==0) {
            Context context = getApplicationContext();

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SignupActivity.this, new String[]
                        {android.Manifest.permission.GET_ACCOUNTS}, requestcode);
            } else {
                possibleEmail = getEmailId(getApplicationContext());

                final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                String email;

                                if (_emailText.length() == 0) {
                                    email = possibleEmail;
                                } else
                                    email = _emailText.getText().toString();


                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                sign_up(email, new CallBack() {
                                    @Override
                                    public void onSuccess(String data) {


                                        Log.d("result.......", data);


                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String success_val = obj.getString("success");
                                            if ("true".equals(success_val)) {

                                                Toast.makeText(SignupActivity.this, "Registered Successfuly", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(i);
                                                finish();

                                            } else {

                                                Toast.makeText(SignupActivity.this, "This Mobile Already Exists", Toast.LENGTH_SHORT).show();


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




            }


        }


        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            String email;


                                email = _emailText.getText().toString();


                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            sign_up(email, new CallBack() {
                                @Override
                                public void onSuccess(String data) {


                                    Log.d("result.......", data);


                                    try {
                                        JSONObject obj = new JSONObject(data);
                                        String success_val = obj.getString("success");
                                        if ("true".equals(success_val)) {

                                            Toast.makeText(SignupActivity.this, "Registered Successfuly", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i);

                                        } else {

                                            Toast.makeText(SignupActivity.this, "This Account Already Exists, Please Login", Toast.LENGTH_SHORT).show();


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
                            try {
                                progressDialog.dismiss();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }, 3000);



        }




            // TODO: Implement your own authentication logic here.


            // onLoginFailed();


    }

    private static String getEmailId(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            return "length is zero";
        }
        return account.name;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestcode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 possibleEmail = getEmailId(getApplicationContext());



                } else {


                }
        }
    }




    private void sign_up(final String email, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("ressss",response);


                        onCallBack.onSuccess(response);


                        // Log.d("nmcksjo",response);

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
                params.put("name",_nameText.getText().toString());
                params.put("email", email);
                params.put("mobile", _mobileText.getText().toString());
                params.put("password", _passwordText.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}