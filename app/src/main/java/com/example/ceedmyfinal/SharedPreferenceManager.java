package com.example.ceedmyfinal;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager
{
    private static SharedPreferenceManager sharedPreferenceManager;
    private static Context context;

    private static final String SHARED_PREF_NAME = "userinfo";

    private static final String USER_ID = "userid";
    private static final String USER_NAME = "username";
    private static final String USER_MOBILE_NUMBER = "mobilenumber";
    private static final String USER_EMAIL = "email";
    private static final String USER_LOGIN = "login";
    private static final String USER_SEND_EMAIL = "sendemail";
    private static final String USER_SET_GOAL = "setgoal";
    private static final String USER_SET_PASSWORD = "setpassword";
    private static final String USER_EXAM_CATEGORY = "examcategory";
    private static final String USER_EXAM_DEMO_VIDEO = "demovideo";

    private static final String USER_EXAM_NAME= "examname";

    private static final String USER_SUBSCRIBED_COURSE_VIDEO_NO = "videono";

    private static final String USER_COURSE_NAME = "coursename";

    private static final String USER_FCM_TOKEN = "usertoken";




    private SharedPreferenceManager(Context context)
    {
        this.context = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context)
    {
        if(sharedPreferenceManager == null)
        {
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }


    /**
     * Store data
     */

    public boolean userID(String id)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, id);
        editor.apply();

        return true;
    }

    public boolean userInfo(String name, String mobile, String email)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_NAME, name);
        editor.putString(USER_MOBILE_NUMBER, mobile);
        editor.putString(USER_EMAIL, email);
        editor.apply();

        return true;
    }

    public boolean userLogin(String loginState)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_LOGIN, loginState);
        editor.apply();

        return true;
    }

    public boolean userSendEmail(String emailState)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_SEND_EMAIL, emailState);
        editor.apply();

        return true;
    }

    public boolean userSetGoal(String goalState)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_SET_GOAL, goalState);
        editor.apply();

        return true;
    }

    public boolean userSetPassword(String passwordState)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_SET_PASSWORD, passwordState);
        editor.apply();

        return true;
    }

    public boolean userExamCategoryName(String examNameState)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_EXAM_CATEGORY, examNameState);
        editor.apply();

        return true;
    }

    public boolean userExamName(String examName)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_EXAM_NAME, examName);
        editor.apply();

        return true;
    }

    public boolean userSubscribedCourseVideoNo(int position)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USER_SUBSCRIBED_COURSE_VIDEO_NO, position);
        editor.apply();

        return true;
    }

    public boolean userCourseName(String course)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_COURSE_NAME, course);
        editor.apply();

        return true;
    }


    public boolean userFCMToken(String token)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_FCM_TOKEN, token);
        editor.apply();

        return true;
    }



    /**
     * Retrieve data
     */

    public String getUserId()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID, null);
    }


    public String getUserName()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, null);
    }

    public String getUserMobileNumber()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_MOBILE_NUMBER, null);
    }

    public String getUserEmail()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public String getisEmailSend()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_SEND_EMAIL, null);
    }

    public String getIsUserSetGoal()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_SET_GOAL, null);
    }

    public String getIsUserSetPassword()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_SET_PASSWORD, null);
    }

    public String getUserExamCategory()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EXAM_CATEGORY, null);
    }

    public String getUserExamDemoVideo()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EXAM_DEMO_VIDEO, null);
    }

    public String getUserExamName()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EXAM_NAME, null);
    }

    public int getSubcribedCourseVideoNO()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_SUBSCRIBED_COURSE_VIDEO_NO, -1);
    }


    public String getUserCourseName()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_COURSE_NAME, null);
    }

    public String getUserFcmToken()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_FCM_TOKEN, null);
    }

    /*
        Login or Logout methods
     */

    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        if(sharedPreferences.getString(USER_LOGIN,null) != null)
        {
            return true;
        }
        return false;
    }

    public boolean logout()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

}
