package com.sthakur.clickerapp;

import android.util.Log;

import com.sthakur.clickerapp.constants.AppConstants;

import net.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by soumyadeep on 12/7/18.
 */

public class SurveyLoader
{
    private String studentId, sessId, courseId;
    private int success;
    private ArrayList<String> questions;
    public SurveyLoader(String studentId, String sessId, String courseId)
    {
        this.studentId = studentId;
        this.sessId = sessId;
        this.courseId = courseId;
        success = 0;
        questions = new ArrayList<String>();

    }
    public boolean load()
    {
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        Log.d("survey load()", "Populating request parameters");
        httpParams.put(AppConstants.KEY_SKEY, sessId);
        httpParams.put(AppConstants.KEY_STUDENT_ID, studentId);
        httpParams.put(AppConstants.KEY_COURSE_ID, courseId);
        Log.d("survey load()", AppConstants.KEY_SKEY+" - "+sessId + " , " + AppConstants.KEY_STUDENT_ID + " - "+studentId);
        JSONObject jsonObject = httpJsonParser.makeHttpRequest(AppConstants.BASE_URL + "survey.php", "POST", httpParams);
        try {
            success = jsonObject.getInt(AppConstants.KEY_SUCCESS);
            Log.d("survey load()", ""+success);
            if (success == 1)
            {
                JSONArray data = jsonObject.getJSONArray(AppConstants.KEY_DATA);
                //String secretKey = jsonObject.getString(AppConstants.KEY_SKEY);
                Log.d("survey load()", ""+data+" - "+data);
                for (int i=0; i<data.length(); i++)
                {
                    JSONObject obj = data.getJSONObject(i);

                    String ques = obj.getString("ques_string");
                    String[] qtemp = ques.split("#");
                    for (String x : qtemp)
                        questions.add(x);
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
    public ArrayList<String> getQuestions()
    {return questions;}
    public int getSuccess()
    {
        return success;
    }
}
