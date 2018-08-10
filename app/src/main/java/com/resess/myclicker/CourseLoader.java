package com.resess.myclicker;

//import android.util.Log;

import com.resess.myclicker.constants.AppConstants;

import net.utils.HttpsJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soumyadeep on 10/7/18.
 */

public class CourseLoader
{
    private String studentId, sessId;
    private int success;
    private HashMap<Integer, String> courseList;
    public CourseLoader(String studentId, String sessId)
    {
        this.studentId = studentId;
        this.sessId = sessId;
        success = 0;
        courseList = new HashMap<Integer, String>();
    }
    public boolean load()
    {
        HttpsJsonParser httpsJsonParser = new HttpsJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        //Log.d("load()", "Populating request parameters");
        httpParams.put(AppConstants.KEY_SKEY, sessId);
        httpParams.put(AppConstants.KEY_STUDENT_ID, studentId);
        //Log.d("load()", AppConstants.KEY_SKEY+" - "+sessId + " , " + AppConstants.KEY_STUDENT_ID + " - "+studentId);
        JSONObject jsonObject = httpsJsonParser.makeHttpRequest(AppConstants.BASE_URL + "courses.php", "POST", httpParams);
        try {
            success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
            //Log.d("authenticate()", ""+success);
            if (success == 1)
            {
                JSONArray data = jsonObject.getJSONArray(AppConstants.KEY_DATA);
                //String secretKey = jsonObject.getString(AppConstants.KEY_SKEY);
                //Log.d("authenticate()", ""+data+" - "+data);
                for (int i=0; i<data.length(); i++)
                {
                    JSONObject obj = data.getJSONObject(i);
                    int cNo = obj.getInt("course_id");
                    String cName = obj.getString("course_name");

                    courseList.put(cNo, cName);
                }
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
    public HashMap<Integer, String> getCourseList()
    {return courseList;}
}
