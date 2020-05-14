package com.example.ceedmyfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextView textViewForgotPassword;
    private MaterialButton materialButtonLogin;
    private MaterialButton materialButtonCreateAccount;

    private ProgressDialog progressDialog;


    public Toast toast;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        textInputLayoutEmail = findViewById(R.id.idTextInputLayoutEmailLoginActivity);
        textInputLayoutPassword = findViewById(R.id.idTextInputLayoutPasswordLoginActivity);
        textViewForgotPassword = findViewById(R.id.idTextViewForgotPasswordLoginActivity);
        materialButtonLogin = findViewById(R.id.idButtonLoginLoginActivity);
        materialButtonCreateAccount = findViewById(R.id.idButtonCreateAnAccountLoginActivity);

        textInputLayoutEmail.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutEmail.setErrorEnabled(false);
                return false;
            }
        });

        textInputLayoutPassword.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutPassword.setErrorEnabled(false);
                return false;
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = textInputLayoutEmail.getEditText().getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                toast.cancel();
            }
        });

        materialButtonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userLogin();
            }
        });

        materialButtonCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(new Intent(getApplicationContext(), NewAccountActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);
        toast = new Toast(this);
    }

    private void userLogin()
    {
        textInputLayoutEmail.setErrorEnabled(false);
        textInputLayoutPassword.setErrorEnabled(false);

        final String email = textInputLayoutEmail.getEditText().getText().toString().trim();
        final String password = textInputLayoutPassword.getEditText().getText().toString().trim();

        if(email.length() == 0)
        {
            textInputLayoutEmail.setError("Email ID not blank");
            return;
        }

        if(password.length() == 0)
        {
            textInputLayoutPassword.setError("Password not blank");
            return;
        }

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN_USER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
//                        progressDialog.dismiss();
                        Log.d("TAG", "onResponse: 1");
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Log.d("TAG", "onResponse: 2");

                            if(!jsonObject.getBoolean("error"))
                            {
                                Log.d("TAG", "onResponse: 3");

                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userInfo(
                                                jsonObject.getString("name"),
                                                jsonObject.getString("mobile"),
                                                jsonObject.getString("email")
                                        );

                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userID(jsonObject.getString("id"));
                                Log.d("TAG", "onResponse: 4");


                                getUserStats(jsonObject.getString("id"));

//                                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
//                                finish();

                            }
                            else
                            {
                                Log.d("TAG", "onResponse: 5");

                                toast = Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Log.d("TAG", "onResponse: 6");
                                progressDialog.dismiss();
                            }
                        }
                        catch (JSONException e)
                        {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getUserStats(final String userid)
    {
        String finalUrl = Constants.URL_GET_USER_STATS + "?userid="+userid;
        Log.d("TAG", "getUserStatsga: "+finalUrl);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                finalUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
//                        progressDialog.dismiss();
                        Log.d("TAG", "getUserStatsga: 1"+response);
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Log.d("TAG", "onResponse: 2");

                            if(!jsonObject.getBoolean("error"))
                            {
                                Log.d("TAG", "onResponse: 3");

                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userLogin("yes");

                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userExamName(
                                                jsonObject.getString("examname"),
                                                jsonObject.getString("videolink")
                                        );

                                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                finish();

                            }
                            else
                            {
                                Log.d("TAG", "onResponse: 5");

                                toast = Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Log.d("TAG", "onResponse: 6");

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                    }
                }
        );

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
