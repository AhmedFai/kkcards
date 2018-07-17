package com.kk_cards.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.LoginActivity;
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
 * Created by user on 1/29/2018.
 */

public class change_password extends Fragment {

    @BindView(R.id.current_password)
    EditText current_password;
    @BindView(R.id.con_pass)
    EditText con__new_pass;
    @BindView(R.id.new_pass)
    EditText new_pass;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.cbShowPwd)
    CheckBox cbShowPwd;

    SessionManagement session;
    String mobile;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.change_password, container, false);
        ButterKnife.bind(this, v);
        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
      mobile= user.get(SessionManagement.KEY_MOBILE);

        cbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    current_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    con__new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    // hide password
                    current_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    con__new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

     Log.d("mobile",mobile);

        // email



        return v;

    }

    @OnClick(R.id.save)
    public void save()
    {
        if(current_password.length()==0)
            current_password.setError("Please enter your current password");
        else if(new_pass.length()==0||new_pass.length()<4)
            new_pass.setError("password must be atleast 4 alphanumeric characters");
        else if(!new_pass.getText().toString().equals(con__new_pass.getText().toString())) {
            con__new_pass.setError("password mismatch");
        }
        else {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Changing Password...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            change_password(new CallBack() {
                                @Override
                                public void onSuccess(String data) {

                                    Log.d("channnn",data);

                                    JSONObject obj= null;
                                    try {
                                        obj = new JSONObject(data);
                                        String success_val=obj.getString("success");

                                        if("true".equals(success_val))
                                        {
                                            session.logoutUser();
                                            Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(getActivity(), LoginActivity.class);
                                            startActivity(i);

                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();


                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();

                                        e.printStackTrace();
                                    }




                                }

                                @Override
                                public void onFail(String msg) {
                                    Log.d("jhvfff","failed");
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


    private void change_password(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/passchange.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("changgggggg",response);


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
                params.put("mobile",mobile);
                params.put("password", current_password.getText().toString());
                params.put("new_password", new_pass.getText().toString());
                 return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
