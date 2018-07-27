package com.kk_cards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Fragment.Home;
import com.kk_cards.Fragment.add_to_cart;
import com.kk_cards.Fragment.category_fragment;
import com.kk_cards.Fragment.my_account;
import com.kk_cards.Fragment.my_orders;
import com.kk_cards.Fragment.product_list;
import com.kk_cards.Fragment.sub_cat_list;
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
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private ArrayList<String> mCountries;
    private int cartindex = 0;
    private int bargaincartindex = 0;
    NavigationView navigationView;
    ArrayList<String> id_list, image_list, name_list, count_list;
    SessionManagement session;
    public  TextView cartcounterTV;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    CallBack mResultCallback = null;
    Cart_Counter_class mVolleyService;
    String cart_counter_real;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // AppRater.app_launched(MainActivity.this);

        id_list = new ArrayList<String>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        getdata();

        Boolean result_val=session.isLoggedIn();
        View header = navigationView.getHeaderView(0);

        TextView text1 = (TextView) header.findViewById(R.id.text2);
        ImageView image = (ImageView) header.findViewById(R.id.imageView);
        TextView text =(TextView) header.findViewById(R.id.text1);;
        if(result_val==true)
        {


            HashMap<String, String> user = session.getUserDetails();

            // name
            text.setText(user.get(SessionManagement.KEY_NAME));

            // email
            text1.setText(user.get(SessionManagement.KEY_EMAIL));

            image.setImageResource(R.drawable.dummy_profile);




        }

        else
        {
            Menu menu = navigationView.getMenu();

            // find MenuItem you want to change
            MenuItem nav_camara = menu.findItem(R.id.log_out);

            // set new title to the MenuItem
            nav_camara.setTitle("Login");


        }



        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (session.isLoggedIn()==false)
                {
                    Intent i =new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                }}
        });




        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new Home()).commit();

        // onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_BACKGROUND);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    return true;
                case R.id.category:
                    category_fragment grid1 = new category_fragment();
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView, grid1).addToBackStack(null).commit();

                    return true;

                case R.id.videos:
                    videos_fragment fm = new videos_fragment();
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView, fm).addToBackStack(null).commit();

                    return true;

                case R.id.my_account:
                    Boolean value=session.isLoggedIn();
                    Log.d("dddd", String.valueOf(value));

                    if(value==true)
                    {
                        my_account grid = new my_account();
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView, grid).addToBackStack(null).commit();

                    }

                    else
                    {
                        session.checkLogin();

                    }

                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
        cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);



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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.cart) {
            Intent i=new Intent(getApplicationContext(),add_to_cart.class);
            i.putExtra("test","");
            startActivity(i);


        }

        else if(item.getItemId()==R.id.action_search){
            Intent i = new Intent(this,search_activity.class);
            startActivity(i);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //  int id = item.id

        int currentPosition = 0;
        for (int i = 1; i < navigationView.getMenu().size(); i++) {
            if (item == navigationView.getMenu().getItem(i)) {
                currentPosition = i;
                break;
            }
        }


        if (item.getItemId() == R.id.my_account) {

            Boolean value=session.isLoggedIn();
            Log.d("dddd", String.valueOf(value));

            if(value==true)
            {
                my_account grid = new my_account();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerView, grid).commit();

            }

            else
            {
                session.checkLogin();

            }

        }
        else if (item.getItemId() == R.id.my_orders) {

            Boolean value=session.isLoggedIn();
            Log.d("dddd", String.valueOf(value));

            if(value==true)
            {
                my_orders grid = new my_orders();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerView, grid).addToBackStack(null).commit();

            }

            else
            {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }

        }

        else   if (item.getItemId() == R.id.my_cart) {


            Intent i=new Intent(getApplicationContext(),add_to_cart.class);
            i.putExtra("test","");
            startActivity(i);


        }


        else   if (item.getItemId() == R.id.share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.active_india");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        }
        else   if (item.getItemId() == R.id.rate) {

            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
               startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
               startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }


        }


        else if(item.getItemId() == R.id.home)
        {


            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        }
        else if(item.getItemId() == R.id.log_out)
        {

            if (session.isLoggedIn()==true) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Are you sure you want to logout of this app?");
                alertDialogBuilder.setPositiveButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                            }
                        });

                alertDialogBuilder.setNegativeButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Logout.. Please Wait");
                                progressDialog.show();


                                // TODO: Implement your own authentication logic here.

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                session.logoutUser();
                                                // onLoginFailed();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);


                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            else
            {


                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);

                //  Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            }



        }

        else if(item.getItemId() == R.id.category)
        {



        }

        else if("0".equals(count_list.get(currentPosition-8))) {

            // Toast.makeText(context,"Hey.... Categories",Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),product_list.class);
            i.putExtra("id_value", id_list.get(currentPosition-8));
            i.putExtra("sub_cat_val", "0");
            i.putExtra("sub_cat_name",name_list.get(currentPosition-8));
            startActivity(i);
        }



        else
        {

            Log.d("ffffffffff","hhhh");

            sub_cat_list grid = new sub_cat_list();
            Bundle bg = new Bundle();
            bg.putString("id_value", id_list.get(currentPosition-8));
            grid.setArguments(bg);
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, grid).addToBackStack(null).commit();



        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getdata() {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/categoryBannerApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        image_list = new ArrayList<String>();
                        name_list = new ArrayList<String>();
                        count_list = new ArrayList<String>();


                        try {
                            JSONObject obj = new JSONObject(response);


                            JSONArray arry = obj.getJSONArray("category");


                            for (int i = 0; i < arry.length(); i++) {

                                JSONObject objj = arry.getJSONObject(i);
                                id_list.add(objj.getString("categoryID"));
                                image_list.add(objj.getString("categoryImage"));
                                name_list.add(objj.getString("categoryName"));
                                count_list.add(objj.getString("count"));


                            }


                            //Log.d("menu.....",id_list.get(0));

                            //    Group gp=navigationView.

                            final Menu menu = navigationView.getMenu();



                            for (int i = 0; i < id_list.size(); i++) {


                               /* String uri =image_list.get(0);
                                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
*/
                              /*  int id = getResources().getIdentifier(image_list.get(i), "drawable", getPackageName());
                                Drawable drawable = getResources().getDrawable(id);*/
                                menu.add(name_list.get(i)).setIcon(R.drawable.category);
                                menu.setGroupCheckable(R.id.second_group, true, true);
                                menu.setGroupVisible(R.id.second_group, true);
                            }
                            //  menu.setGroupCheckable(2,true,true);

                            //      menu.add("Log Out").setIcon(R.drawable.log_out);


                          /*  final Menu menu1 = navigationView.getMenu();

                            menu1.add("My Account").setIcon(R.drawable.logo);
*/


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
    static class VersionHelper
    {
        static void refreshActionBarMenu(Activity activity)
        {
            activity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        VersionHelper.refreshActionBarMenu(this);
        // put your code here...

    }






}