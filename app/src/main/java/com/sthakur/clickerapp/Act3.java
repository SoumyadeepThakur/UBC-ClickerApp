package com.sthakur.clickerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sthakur.clickerapp.constants.AppConstants;

import net.helper.Logger;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

//import org.w3c.dom.Text;

//import mehdi.sakout.fancybuttons.FancyButton;

//  Survey Activity

public class Act3 extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CheckBox ans1;
    private CheckBox ans2;
    private CheckBox ans3;
    private CheckBox ans4;

    private int qno = 0;

    private TextView txt_question;
    private Button btnSubmitSurvey;
    private ArrayList<String> questions;

    String question = "";
    String answer[] = new String[4];
    String course;
    int courseId;
    //private StringRequest stringRequest;
    //private String urlGetNextQ = "http://192.168.1.64/authenticate.php/";     // append it later
    private LocationListener locationListener;
    private LocationManager locationManager;
    //private String surveyURL = AppConstants.BASE_URL+"survey.php";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //setContentView(R.layout.activity_course_list);

        String sid = sharedPref.getString("sidKey", null);
        String sec = sharedPref.getString("secKey", null);
        setContentView(R.layout.activity_act3);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        course = b.getString("name");
        courseId = b.getInt("id");


        //txt_ans_one = findViewById(R.id.txt_ans_one);
        //txt_ans_two = findViewById(R.id.txt_ans_two);
        //txt_ans_three = findViewById(R.id.txt_ans_three);
        ans1 = findViewById(R.id.checkBox);
        ans2 = findViewById(R.id.checkBox2);
        ans3 = findViewById(R.id.checkBox3);
        ans4 = findViewById(R.id.checkBox4);


        txt_question = findViewById(R.id.txt_question);
        btnSubmitSurvey = findViewById(R.id.btn_submit_survey);
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted


                try {
                    locationListener = new GPSManager();

                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    List<String> providers = locationManager.getProviders(true);
                    Location best = null;
                    for (String provider : providers) {
                        Location l = locationManager.getLastKnownLocation(provider);
                        if (l == null) {
                            continue;
                        }
                        if (best == null || l.getAccuracy() < best.getAccuracy()) {
                            // Found best last known location: %s", l);
                            best = l;
                        }
                    }
                    String p = best.toString();
                    Log.d("gps", p);
                    Logger.log("Act3 - location - " + p);

                    //if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    //{

                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    //    Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //    String p = currentLocation.toString();
                    //}
                    //String x = ((GPSManager)locationListener).getValues();
                    //Log.d("Act 3", x);

                } catch (SecurityException se) {
                    Log.d("Act3", "cant get loc");
                }
            }
        }
        catch (Exception e)
        {Log.d("Act3", "cant get loc");}
        loadSurvey(sid, sec, String.valueOf(courseId));
        //updateTxtElements(question, answer[0], answer[1], answer[2]);

        btnSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("Act3", "click");
                int ans = (ans1.isChecked()?1:0)+2*(ans2.isChecked()?1:0)+4*(ans3.isChecked()?1:0)+8*(ans4.isChecked()?1:0);
                if (ans == 0)
                {
                    Toast.makeText(Act3.this, "No Answer Selected",  Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("Act3", "" + ans);

                    Logger.log("Ques: "+qno+" Response: " + ans);
                    loadNextQuestion();
                }
            }
        });
        /*
        btnSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.GET, urlGetNextQ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                // get the q and ans and call updateTxtElements
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Act3.this, "Error "+error.getMessage().toString()+" in Act2 update", Toast.LENGTH_SHORT).show();
                            }
                        });

                Volley.newRequestQueue(Act3.this).add(stringRequest);

            }
        });
        */
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions,
                                            int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try {
                        locationListener = new GPSManager();

                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                        String x = ((GPSManager)locationListener).getValues();
                        Log.d("Act 3", x);
                    } catch (SecurityException se) {
                        Log.d("Act3", "cant get loc");
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Act3.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void loadSurvey(String sid, String sec, String cid)
    {
        SurveyLoader surveyLoader = new SurveyLoader(sid, sec, cid);
        surveyLoader.load();
        questions = surveyLoader.getQuestions();
        String q = questions.get(qno++);
        String[] qa = q.split(":");
        try {
            txt_question.setText(qa[0]);

            ans1.setText(qa[1]);
            ans2.setText(qa[2]);
            ans3.setText(qa[3]);
            ans4.setText(qa[4]);
        }
        catch (Exception e)
        {}


    }
    private void loadNextQuestion()
    {
        if (qno == questions.size())
        {
            // End of survey
            Log.d("survey()","End of survey");
            Logger.log("End of survey");

            LinearLayout courseLayout = new LinearLayout(this);
            courseLayout.setOrientation(LinearLayout.VERTICAL);
            courseLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

            final TextView titleView = new TextView(this);
            //LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleView.setLayoutParams(lparams);
            //titleView.setTextAppearance(this, android.R.attr.textAppearanceLarge);
            titleView.setText("End of Survey!");
            titleView.setAllCaps(true);
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextSize(16);
            courseLayout.addView(titleView);
            setContentView(courseLayout);
            return;
        }
        String q = questions.get(qno++);
        String[] qa = q.split(":");
        try {
            txt_question.setText(qa[0]);

            ans1.setText(qa[1]);
            ans1.setChecked(false);
            ans2.setText(qa[2]);
            ans2.setChecked(false);
            ans3.setText(qa[3]);
            ans3.setChecked(false);
            ans4.setText(qa[4]);
            ans4.setChecked(false);
        }
        catch (Exception e)
        {}
    }
}
