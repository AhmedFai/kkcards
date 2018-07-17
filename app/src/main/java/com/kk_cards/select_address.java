package com.kk_cards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Adapter.display_add_adapter;
import com.kk_cards.Modal.ItemData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




/**
 * Created by user on 3/14/2018.
 */

public class select_address extends AppCompatActivity {



    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;

    @BindView(R.id.myCardView)
    CardView card;
    @BindView(R.id.total)
    TextView count_address;

    @BindView(R.id.add_address)
    TextView add_address;

    @BindView(R.id.progressBar)
    ProgressBar p_bar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SessionManagement session;
    ArrayList<ItemData> os_versions;
    View v;
    String key_mobile;
    private String MY_PREFS_NAME;
    String cart_counter_real=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_address_layout);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }



        this.setTitle("Select Address");


        HashMap<String, String> user = session.getUserDetails();

        // name
        key_mobile=user.get(SessionManagement.KEY_MOBILE);




        card.setVisibility(View.VISIBLE);
        p_bar.setVisibility(View.GONE);
        count_address.setVisibility(View.VISIBLE);
        os_versions = new ArrayList<ItemData>();

        display_address(Config.Base_Url+"/API/addressApi.php?mobile="+key_mobile);
       // count_address.setPaintFlags(count_address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        count_address.setTextColor(Color.parseColor("#ffffff"));
        count_address.setTextSize(18);
        count_address.setText("Deliver Here");




    }


    @OnClick(R.id.add_address)
    protected void add_address(){



        Intent i=new Intent(getApplicationContext(),add_address_activity.class);
        i.putExtra("check_value","add");
        i.putExtra("address_id","");
        i.putExtra("check", getIntent().getStringExtra("check_valuee"));


        startActivity(i);
        finish();
    }

  @OnClick(R.id.total)
    protected void count_addressss(){


      if("billing_address".equals(getIntent().getStringExtra("address"))) {


          SharedPreferences pref =getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


          Intent i = new Intent(getApplicationContext(), payment_page.class);
         // i.putExtra("check_vall", "cart");

          startActivity(i);
          finish();


      }
        else {

          SharedPreferences pref =getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         // editor.putString("name", os_versions.get(lastCheckedPosition).getCat_name());

          if("no".equals(pref.getString("radio_select",null)))
          {
              Toast.makeText(getApplicationContext(),"Please Select Address",Toast.LENGTH_LONG).show();

          }
          else {

              if("cart_to_buy".equals(getIntent().getStringExtra("check_valuee"))) {
                  Intent i = new Intent(getApplicationContext(), check_out_activity.class);
                  startActivity(i);
              }

              else
              {
                  Intent i = new Intent(getApplicationContext(), check_out_buy_now.class);
                  startActivity(i);

              }
          }
      }
    }



    public void display_address(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                           mAdapter.setVisibility(View.VISIBLE);

                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray product_list=obj.getJSONArray("products");

                            for(int i=0;i<product_list.length();i++)
                            {
                                ItemData feed = new ItemData();
                                JSONObject objj=product_list.getJSONObject(i);

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


                            //    Log.d("ffffffffffff",feed.getMobile());

                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                //





                            }


                           // count_address.setText(os_versions.size()+" "+"Saved Address");


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                            display_add_adapter mAdapter1 = new display_add_adapter(getApplicationContext(), os_versions,"select_address",getIntent().getStringExtra("address"));
                            mAdapter.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);

                            mAdapter1.notifyDataSetChanged();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("Response",response);


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
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


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

}
