package com.kk_cards.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.kk_cards.search_activity;
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




/**
 * Created by user on 3/26/2018.
 */

public class product_all_details extends AppCompatActivity {
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private String MY_PREFS_NAME;

    @BindView(R.id.image1)
    ImageView image;
    @BindView(R.id.text1)
    TextView name;
    @BindView(R.id.price1)
    TextView price1;

    SessionManagement session;
    String cart_counter_real=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_all_details);
        ButterKnife.bind(this);

        session=new SessionManagement(getApplicationContext());

        if(getSupportActionBar()!=null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }

     name.setText(getIntent().getStringExtra("product_name"));
        price1.setText(getIntent().getStringExtra("product_price"));
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("product_image")).into(image);




        TabFragment grid = new TabFragment();
        Bundle args=new Bundle();
        args.putString("id_value",getIntent().getStringExtra("id_value"));
        grid.setArguments(args);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView1, grid).commit();



        this.setTitle("Product Details");



    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
       final TextView cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);



        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_to_cart.class);
                i.putExtra("test","");
                startActivity(i);

            }
        });
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_to_cart.class);
                i.putExtra("test","");
                startActivity(i);

            }
        });
        if(session.isLoggedIn()==true) {
            cart_counter(new CallBack() {
                @Override
                public void onSuccess(String data) {


                    try {
                        JSONObject obj=new JSONObject(data);
                        cart_counter_real=obj.getString("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    if(session.isLoggedIn()==true) {
                        if (!"0".equals(cart_counter_real)) {
                            cartcounterTV.setVisibility(View.VISIBLE);
                            cartcounterTV.setText(cart_counter_real);
                        }
                        else
                            cartcounterTV.setVisibility(View.GONE);

                    }


                }

                @Override
                public void onFail(String msg) {
                    //  Log.d("jhvfff", "failed");

                }
            });

        }



        else
        {
            DatabaseHandler db=new DatabaseHandler(getApplicationContext());

            String cart_value= String.valueOf(db.count_rows());



            if (!"0".equals(cart_value)) {
                cartcounterTV.setVisibility(View.VISIBLE);
                cartcounterTV.setText(cart_value);
            }
            else
                cartcounterTV.setVisibility(View.GONE);



        }

        return true;
    }

    protected void cart_counter(final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/cartCounterApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssssssssss",response);


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
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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

        else if (id == R.id.cart) {
            Intent i=new Intent(getApplicationContext(),add_to_cart.class);
            i.putExtra("test","");
            startActivity(i);


        }

        else if(item.getItemId()==R.id.action_search){
            Intent i = new Intent(this,search_activity.class);
            startActivity(i);
            return true;
        }
      /*  else if(id == R.id.action_search)
        {

            Intent i=new Intent(MainActivity.this,search_activity.class);
            startActivity(i);
          //  loadToolBarSearch();
        }*/

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
}