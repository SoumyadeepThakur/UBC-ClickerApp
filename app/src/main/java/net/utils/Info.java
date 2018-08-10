package net.utils;

//import android.util.Log;

import com.resess.myclicker.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soumyadeep on 16/7/18.
 */

public class Info
{

    private static String url = AppConstants.BASE_URL + "logger.php";

    public static boolean log(String data)
    {
        try {
            HttpsJsonParser httpsJsonParser = new HttpsJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            //Log.d("log()", "Populating request parameters");
            httpParams.put("data", data);
            //Log.d("log()", "data - " + data);
            JSONObject jsonObject = httpsJsonParser.makeHttpRequest(url, "POST", httpParams);
            try {
                int success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
                boolean v = success == 1;
                return v;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        catch (Exception e)
        {return false;}

    }
}

