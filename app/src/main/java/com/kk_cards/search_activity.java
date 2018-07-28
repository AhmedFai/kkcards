package com.kk_cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kk_cards.Adapter.product_adapter;
import com.kk_cards.Fragment.product_details;
import com.kk_cards.Fragment.product_list;
import com.kk_cards.Listener.RecyclerItemClickListener;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2/5/2018.
 */

public class search_activity extends AppCompatActivity {

    @BindView(R.id.img_tool_back)
    ImageView back;
    @BindView(R.id.edt_tool_search)
    EditText edt_tool_search;

    @BindView(R.id.list_search)
    ListView search_listview;

    @BindView(R.id.parent_toolbar_search)
    LinearLayout parent_toolbar_search;

    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_toolbar_search);
        ButterKnife.bind(this);
        os_versions = new ArrayList<ItemData>();

        search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                edt_tool_search.setText(search_listview.getItemAtPosition(i).toString());
                Intent j=new Intent(getApplicationContext(),product_list.class);
                j.putExtra("id_value","search_api");
                j.putExtra("sub_cat_val", "");
                j.putExtra("sub_cat_name",edt_tool_search.getText().toString());
                startActivity(j);

                Log.d("checkkkk",edt_tool_search.getText().toString());

              /*  mAdapter.setVisibility(View.VISIBLE);
                search_listview.setVisibility(View.GONE);
                get_data(edt_tool_search.getText().toString(),Config.Base_Url+"/API/searchApi.php");
*/
            }
        });



        edt_tool_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d("fsgsgsgsg","dddddd");
                    Intent j=new Intent(getApplicationContext(),product_list.class);
                    j.putExtra("id_value","search_api");
                    j.putExtra("sub_cat_val", "");
                    j.putExtra("sub_cat_name",edt_tool_search.getText().toString());
                    startActivity(j);
                   // Log.d("checkkkk",edt_tool_search.getText().toString());
                 /*   mAdapter.setVisibility(View.VISIBLE);
                    search_listview.setVisibility(View.GONE);
                    get_data(edt_tool_search.getText().toString(),Config.Base_Url+"/API/searchApi.php");
*/
                    // TODO do something
                    handled = true;
                }
                return handled;
            }
        });

        edt_tool_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here


             //   Log.d("see", String.valueOf(s));
                search_listview.setVisibility(View.VISIBLE);
                getdata(String.valueOf(s),Config.Base_Url+"/API/searchApi.php");



            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i=new Intent(search_activity.this,product_details.class);
                        i.putExtra("id_value",os_versions.get(position).getProductID());
                        i.putExtra("catId",os_versions.get(position).getCategoryID());
                        i.putExtra("sub_cat_val", "0");
                        i.putExtra("image_path", "no_images");
                        startActivity(i);




                        // TODO Handle item click
                    }
                })
        );


    }


    @OnClick(R.id.img_tool_back)
        public  void goback()
            {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);


            }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
       /* SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconified(false);*/
        return super.onCreateOptionsMenu(menu);
    }



    public void get_data(final String search, String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj=new JSONObject(response);


                            JSONArray product_list=obj.getJSONArray("products");

                            for(int i=0;i<product_list.length();i++)
                            {
                                ItemData feed = new ItemData();
                                JSONObject objj=product_list.getJSONObject(i);

                                feed.setId(objj.getString("id"));
                                feed.setCat_name(objj.getString("name"));
                                feed.setCat_image(objj.getString("image"));

                                int discount= Integer.parseInt(objj.getString("discount"));
                                int price=Integer.parseInt(objj.getString("price"));

                                feed.setDiscount(objj.getString("discount"));


                                int price_cut=(100-discount)*price/100;
                                feed.setPrice(objj.getString("price"));
                                feed.setPrice_cut(String.valueOf(price_cut));

                                os_versions.add(feed);


                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
                                //



                            }


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                            product_adapter mAdapter1 = new product_adapter(getApplicationContext(), os_versions,mAdapter,"");
                            mAdapter.setHasFixedSize(true);
                            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                            mAdapter.setLayoutManager(gridLayoutManager);


                            mAdapter.setAdapter(mAdapter1);




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
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("search",search);

                return params;
            }

        };

// Add the request to the queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }








    public void getdata(final String search, String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                              try {
                                  JSONObject obj = new JSONObject(response);


                                  JSONArray product_list = obj.getJSONArray("products");
                                  String[] list_data = new String[product_list.length()];

                                  Log.d("ffff", String.valueOf(product_list.length()));

                                  for (int i = 0; i < product_list.length(); i++) {
                                      ItemData feed = new ItemData();
                                      JSONObject objj = product_list.getJSONObject(i);

                                      feed.setProductID(objj.getString("productID"));
                                      feed.setCategoryID(objj.getString("categoryID"));
                                      list_data[i] = objj.getString("productName");
                                      //    list_data(objj.getString("name"));


                                      os_versions.add(feed);
                                  }

                                  ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list_data);
                                  search_listview.setAdapter(adapter);


                              } catch (JSONException e) {

                                  String  []   list_data= new String[1];
                                  list_data[0]="No data Found";

                                  ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list_data);
                                  search_listview.setAdapter(adapter);

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
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("search",search);

                return params;
            }

        };

// Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);


    }
}