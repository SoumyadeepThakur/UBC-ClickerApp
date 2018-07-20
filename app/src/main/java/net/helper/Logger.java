package net.helper;

import android.util.Log;

import com.sthakur.clickerapp.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soumyadeep on 16/7/18.
 */

public class Logger
{

    private static String url = AppConstants.BASE_URL + "logger.php";

    public static boolean log(String data)
    {
        try {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            Log.d("log()", "Populating request parameters");
            httpParams.put("data", data);
            Log.d("log()", "data - " + data);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(url, "POST", httpParams);
            try {
                int success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
                return (success == 1);

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } finally {
                return false;
            }
        }
        catch (Exception e)
        {}
        finally
        {return false;}
    }
}

