package com.resess.myclicker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.resess.myclicker.constants.AppConstants;

import net.utils.CheckNetworkStatus;
import net.utils.Info;

import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Task t;
    private int backpress=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Task t;
        t = new Task("MainActivity");
        t.execute((Void)null);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            t.execute((Void)null);
        }
        catch(Exception e)
        {}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            backpress = 0;
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

            if (backpress>1) {
                //while (!Info.log("Exit"));
                //android.os.Process.killProcess(android.os.Process.myPid());
                Task t = new Task("Exit");
                t.execute((Void)null);
                this.finishAffinity();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //while (!Info.log("Exit"));
            //boolean x = Info.log("Exit");
            //if (x) x=true;
            Task t = new Task("Exit");
            t.execute((Void)null);
            this.finishAffinity();
            //android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id  == R.id.nav_login)
        {
            Task nt = new Task("LoginActivity");
            nt.execute();
            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            if (sharedPref.contains("sidKey"))
            {
                startActivity(new Intent(getApplicationContext(), CourseListActivity.class));
            }
            else
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
        else if (id == R.id.nav_info)
        {
            //Toast.makeText(getApplicationContext(), "Info", Toast.LENGTH_SHORT).show();
            //Info.log("InfoActivity - onCreate");
            startActivity(new Intent(getApplicationContext(), InfoActivity.class));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onDestroy()
    {
        Task t = new Task("Destroyed - MainActivity");
        t.execute((Void)null);
        super.onDestroy();
    }
    @Override
    public void onStop()
    {
        Task t = new Task("Stopped - MainActivity");
        t.execute((Void)null);
        super.onStop();
    }
    @Override
    public void onPause()
    {
        Task t = new Task("Paused - MainActivity");
        t.execute((Void)null);
        super.onPause();
    }

    public class Task extends AsyncTask<Void, Void, Boolean>
    {
        private String str;
        Task(String str) {this.str = str;}
        @Override
        protected Boolean doInBackground(Void... params) {
            // attempt authentication against a network service.
            //return placeVerifier();
            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            if (sharedPref.contains("lastloc"))
            Info.log(str + " Location: " + sharedPref.getString("lastloc", null));
            else Info.log(str);
            return true;
        }
        /*
        private boolean placeVerifier()
        {
            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()))
            {
                if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Info.log("MenuActivity");
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MenuActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                        Info.log("MenuActivity");

                    }
                } else {
                    // Permission has already been granted


                    try {
                        LocationListener locationListener = new GPSManager();

                        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        List<String> providers = locationManager.getProviders(true);
                        Location best=null;
                        for (String provider : providers)
                        {
                            Location l = locationManager.getLastKnownLocation(provider);
                            if (l == null) {
                                continue;
                            }
                            if (best == null || l.getAccuracy() < best.getAccuracy()) {

                                best= l;
                            }
                        }
                        String p = best.toString();
                        //Log.d("gps", p);
                        return Info.log(str + " - location - " + p);


                    } catch (SecurityException se) {
                        //Log.d(str + " ", "cant get loc");
                        Info.log("MenuActivity");
                    }
                    finally {
                        //Info.log("MenuActivity");
                        return false;
                    }
                }
                //attemptLogin();
            }
            return false;
        }
        */
        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);


        }

        @Override
        protected void onCancelled() {

        }
    }
}



