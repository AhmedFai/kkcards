package com.kk_cards;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Set;

import butterknife.ButterKnife;



public class splash extends AppCompatActivity {
    Handler handler = new Handler();
    LocationManager locationManager;
    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;

    public static String str_login_test,checking;

    public static boolean check;
    public static Set<String> name_value;
    public static Set<String> url_value;
    public static Set<String> image_path;


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private String MY_PREFS_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // here initializing the shared preference
        sh = getSharedPreferences("myprefe", 0);
        editor = sh.edit();

        // check here if user is login or not
        str_login_test = sh.getString("loginTest", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

      /*  SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("cart_size",0);

        editor.commit();*/



        isInternetOn();


    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

    //        Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                  /*  SessionManagement session=new SessionManagement(getApplicationContext());
                    DatabaseHandler db=new DatabaseHandler(getApplicationContext());
                    if(!session.isLoggedIn()==true)
                    {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("cart_size",db.count_rows());

                        editor.commit();


                    }

                    else
                    {
                        SharedPreferences pref=getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("cart_size",pref.getInt("cart_size_after_log",0));

                        editor.commit();


                    }
*/

                    Intent send = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(send);

















                /*
                 * if user login test is true on oncreate then redirect the user
                 * to result page
                 */

                  /*  if (str_login_test != null && !str_login_test.toString().trim().equals("")) {

                        Intent send = new Intent(getApplicationContext(),MainActivity.class);

                        startActivity(send);



                    }

                    else {

                        Intent send = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(send);


                    }
*/

                }

            }, 2000);

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            Intent send = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(send);

            return false;
        }
        return false;
    }
}





