package com.kk_cards.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
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
 * Created by user on 2/7/2018.
 */

public class update_account extends Fragment {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;


    @BindView(R.id.save)
    TextView save;

    SessionManagement session;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;



    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.update_account, container, false);
        ButterKnife.bind(this, v);

        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name

        name.setText(user.get(SessionManagement.KEY_NAME));
        email.setText(user.get(SessionManagement.KEY_EMAIL));





        return v;
    }

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }


    @OnClick(R.id.save)
    public void save() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (name.length() == 0)
            name.setError("Please enter your name");
        else if(!email.getText().toString().matches(emailPattern))
            email.setError("Please enter valid email");
        else {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Account...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            update_account(new CallBack() {
                                @Override
                                public void onSuccess(String data) {

                                   // Log.d("updateeeeeeeee", data);

                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(data);
                                        String success_val = obj.getString("success");

                                        if ("true".equals(success_val)) {

                                            String mobile=session.getUserDetails().get(SessionManagement.KEY_MOBILE);

                                            session.createLoginSession(name.getText().toString(),email.getText().toString(),mobile);



                                            Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_SHORT).show();

                                            account_settings grid = new account_settings();
                                            mFragmentManager = getActivity().getSupportFragmentManager();
                                            mFragmentTransaction = mFragmentManager.beginTransaction();
                                            mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();


                                        } else {
                                            Toast.makeText(getActivity(), "Account not updated , please try again....", Toast.LENGTH_SHORT).show();


                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(getActivity(), "Account not updated , please try again....", Toast.LENGTH_SHORT).show();

                                        e.printStackTrace();
                                    }


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
        // TODO: Implement your own authentication logic here.


    }




    private void update_account(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/updateuser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("updateee",response);


                        onCallBack.onSuccess(response);


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
                params.put("name",name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}