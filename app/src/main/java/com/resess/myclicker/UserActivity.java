package com.resess.myclicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.utils.CheckNetworkStatus;
import net.utils.Info;

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

        if (net) Info.log("Application Started");
        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Info.log("Student Login");
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
    }
}
