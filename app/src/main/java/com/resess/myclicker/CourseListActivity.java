package com.resess.myclicker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.resess.myclicker.constants.AppConstants;

import net.utils.CheckNetworkStatus;
import net.utils.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soumyadeep on 1/7/18.
 */

public class CourseListActivity extends AppCompatActivity
{
    private SharedPreferences sharedPref;
    private HashMap<Integer, String> coursesMap;
    private ArrayAdapter<String> courseAdapter;
    private ListView lv;
    private LocTask lTask;
    private String sec2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_course_list);

        String sid = sharedPref.getString("sidKey", null);
        String sec = sharedPref.getString("secKey", null);
        sec2=sec;

        //lTask = new LocTask();
        //lTask.execute((Void)null);
        List<String> cl = loadCourses(sid, sec);
        final String[] arr = new String[coursesMap.size()];
        final int[] cId = new int[coursesMap.size()];
        final HashMap<String, Integer> reverse = new HashMap<>();
        int k=0;

        for (Map.Entry<Integer, String> entry : coursesMap.entrySet())
        {
            arr[k] = entry.getValue();
            cId[k++] = entry.getKey();
            reverse.put(arr[k-1], cId[k-1]);
        }

        lv = (ListView)findViewById(R.id.course_list);
        courseAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.course_name, cl);

        lv.setAdapter(courseAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                Intent in = new Intent(CourseListActivity.this, Act3.class);


                in.putExtra("id", reverse.get(selectedItem));
                in.putExtra("name", selectedItem);
                //Log.d("CourseListActivity", "Button "+selectedItem+" clicked");
                startActivity(in);

            }
        });
        Button logout = findViewById(R.id.logout_button);
        //logout.setLayoutParams(lparams);
        logout.setText("Logout");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginAuthentication().endSession(sharedPref.getString("secKey", null), sharedPref.getString("sidKey", null));
                AppConstants.LOC = null;
                Info.log("Session closed: " + sharedPref.getString("secKey", null) +" - " + sharedPref.getString("sidKey", null));
                Info.log("LoginActivity");
                SharedPreferences.Editor editor =  sharedPref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(CourseListActivity.this, MainActivity.class));
            }
        });
        //courseLayout.addView(logout);
        //setContentView(courseLayout);

    }
    @Override
    public void onPause()
    {
        Info.log("onPause - CourseListActivity");
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        new LoginAuthentication().endSession(sharedPref.getString("secKey", null), sharedPref.getString("sidKey", null));
        AppConstants.LOC = null;
        Info.log("Session closed: " + sharedPref.getString("secKey", null) +" - " + sharedPref.getString("sidKey", null));
        Info.log("LoginActivity");

        SharedPreferences.Editor editor =  sharedPref.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(CourseListActivity.this, MainActivity.class));
        //Info.log("MainActivity");
    }
    private List<String> loadCourses(String sid, String sec)
    {
        CourseLoader courseLoader = new CourseLoader(sid, sec);
        courseLoader.load();
        coursesMap = courseLoader.getCourseList();
        List<String> s = new ArrayList<String>(coursesMap.values());
        return s;

    }
    @Override
    public void onResume()
    {
        lTask = new LocTask();
        lTask.execute((Void)null);
        super.onResume();
    }


    public class LocTask extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            // attempt authentication against a network service.
            return placeVerifier();
        }
        private boolean placeVerifier()
        {
            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()))
            {
                if (ContextCompat.checkSelfPermission(CourseListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CourseListActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Info.log("CourseListActivity - location - " + sec2);
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(CourseListActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                        Info.log("CourseListActivity - location - " + sec2);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
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
                                // Found best last known location: %s", l);
                                best= l;
                            }
                        }
                        String p = best.toString();
                        //Log.d("gps", p);
                        SharedPreferences myPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lastloc", p);
                        //editor.putString("secKey", loginAuthentication.getSecretKey());
                        editor.commit();
                        return Info.log("CourseListActivity - location - " + p + " : " + sec2);


                    } catch (SecurityException se) {
                        Info.log("CourseListActivity : " + sec2);
                        //Log.d("Act3", "cant get loc");
                    }
                    finally {
                        //Info.log("InfoActivity : " + sec2);
                        return false;
                    }
                }
                //attemptLogin();
            }
            return false;
        }
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


