package com.resess.myclicker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
//
/**
 * Created by soumyadeep on 12/7/18.
 */

public class GPSManager implements LocationListener
{
    String s;
    @Override
    public void onLocationChanged(Location loc) {
        //editLocation.setText("");
        //pb.setVisibility(View.INVISIBLE);
        //Toast.makeText(getBaseContext(),  "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        //+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        //Log.d("gps", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        //Log.v("gps", latitude);

        /*------- To get city name from coordinates -------- */

        s = longitude + "\n" + latitude;

        //Log.d("gps", s);
        //editLocation.setText(s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public String getValues()
    {return s; }
}
