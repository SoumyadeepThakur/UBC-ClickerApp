package com.sthakur.clickerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.helper.CheckNetworkStatus;
import net.helper.Logger;

/**
 * Created by soumyadeep on 18/7/18.
 */

public class UserActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        boolean net = CheckNetworkStatus.isNetworkAvailable(getApplicationContext());

        if (net) Logger.log("Application Started");
        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Logger.log("Student Login");
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
    }
}
