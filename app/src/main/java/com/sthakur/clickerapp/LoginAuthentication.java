package com.sthakur.clickerapp;

import net.helper.HttpJsonParser;
import com.sthakur.clickerapp.constants.AppConstants;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;



class LoginAuthentication
{
    private String studentId, passwordRaw;
    private int success;
    private String secretKey;
    public LoginAuthentication()
    {
        studentId = "";
        passwordRaw = "";
        success = 0;
    }
    public LoginAuthentication(String id, String passwd)
    {
        studentId = id;
        passwordRaw = passwd;
        success = 0;
    }
    public boolean authenticate()
    {
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        Log.d("authenticate()", "Populating request parameters");
        httpParams.put(AppConstants.KEY_STUDENT_ID, studentId);
        httpParams.put(AppConstants.KEY_PASSWORD, passwordRaw);
        Log.d("authenticate()", AppConstants.KEY_STUDENT_ID+" - "+studentId + " , " + AppConstants.KEY_PASSWORD + " - "+passwordRaw);
        JSONObject jsonObject = httpJsonParser.makeHttpRequest(AppConstants.BASE_URL + "authenticate.php", "POST", httpParams);
        try {
            success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
            Log.d("authenticate()", ""+success);
            if (success == 1)
            {
                String data = jsonObject.getString(AppConstants.KEY_DATA);
                String secretKey = jsonObject.getString(AppConstants.KEY_SKEY);
                Log.d("authenticate()", ""+data+" - "+secretKey);
                this.secretKey = secretKey;
                boolean x = establishSession();
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            return false;
        }

    }
    private boolean establishSession()
    {
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        Log.d("authenticate()", "Populating session parameters");
        httpParams.put(AppConstants.KEY_SKEY, secretKey);
        httpParams.put(AppConstants.KEY_STUDENT_ID, studentId);
        Log.d("authenticate()", AppConstants.KEY_STUDENT_ID+" - "+studentId + " , " + AppConstants.KEY_SKEY + " - "+secretKey);
        JSONObject jsonObject = httpJsonParser.makeHttpRequest(AppConstants.BASE_URL + "session.php", "POST", httpParams);
        try {
            success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
            Log.d("session()", ""+success);
            if (success == 1)
            {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            return false;
        }
    }
    private boolean endSession()
    {
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        Log.d("endsession()", "Populating endsession parameters");
        httpParams.put(AppConstants.KEY_SKEY, secretKey);
        httpParams.put(AppConstants.KEY_STUDENT_ID, studentId);
        Log.d("endsession()", AppConstants.KEY_STUDENT_ID+" - "+studentId + " , " + AppConstants.KEY_SKEY + " - "+secretKey);
        JSONObject jsonObject = httpJsonParser.makeHttpRequest(AppConstants.BASE_URL + "logout.php", "POST", httpParams);
        try {
            success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
            Log.d("endsession()", ""+success);
            if (success == 1)
            {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            return false;
        }
    }
    public int getSuccessStatus()
    {return success;}
    public String getSecretKey()
    {return secretKey;}
}
