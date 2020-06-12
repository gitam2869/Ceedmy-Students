package com.example.ceedmyfinal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAccountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>
{

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutMobile;
    private TextInputLayout textInputLayoutEmail;
    private MaterialButton materialButtonSubmit;
    private MaterialButton materialButtonGoToLogin;

//    String name;
    public static final int OPERATION_SEARCH_LOADER = 22;

    private boolean isCrateUser = true;

    private ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        getSupportActionBar().hide();

        textInputLayoutName = findViewById(R.id.idTextInputLayoutNameNewAccountActivity);
        textInputLayoutMobile = findViewById(R.id.idTextInputLayoutMobileNewAccountActivity);
        textInputLayoutEmail = findViewById(R.id.idTextInputLayoutEmailNewAccountActivity);
        materialButtonSubmit = findViewById(R.id.idButtonSubmitNewAccountActivity);
        materialButtonGoToLogin = findViewById(R.id.idButtonGoToLoginNewAccountActivity);

        progressDialog = new ProgressDialog(this);


        textInputLayoutName.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutName.setErrorEnabled(false);
                return false;
            }
        });

        textInputLayoutMobile.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutMobile.setErrorEnabled(false);
                return false;
            }
        });

        textInputLayoutEmail.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutEmail.setErrorEnabled(false);
                return false;
            }
        });



        materialButtonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onClick(View v)
            {
                if(isCrateUser)
                {
                    creatUser();
                }

            }
        });

        materialButtonGoToLogin.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });
//                LoaderManager.getInstance(NewAccountActivity.this).initLoader(OPERATION_SEARCH_LOADER, null, NewAccountActivity.this);

    }




    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void creatUser()
    {
        textInputLayoutName.setErrorEnabled(false);
        textInputLayoutMobile.setErrorEnabled(false);
        textInputLayoutEmail.setErrorEnabled(false);

        final String name = textInputLayoutName.getEditText().getText().toString().trim();
        final String mobile = textInputLayoutMobile.getEditText().getText().toString().trim();
        final String email = textInputLayoutEmail.getEditText().getText().toString().trim();

        if(name.length() == 0)
        {
            textInputLayoutName.setError("Name not blank");
            textInputLayoutName.requestFocus();
            textInputLayoutName.setErrorEnabled(false);
            return;
        }
        if(!name.matches("[a-zA-Z][a-zA-Z ]*"))
        {
            textInputLayoutName.setError("Name not contains other than characters");
            textInputLayoutName.requestFocus();
            return;
        }

        if(mobile.length() == 0)
        {
            textInputLayoutMobile.setError("Mobile number not blank");
            textInputLayoutMobile.requestFocus();
            textInputLayoutMobile.setErrorEnabled(false);
            return;
        }
        if(mobile.length() < 10 || mobile.length() > 10)
        {
            textInputLayoutMobile.setError("Mobile number must be 10 digit");
            textInputLayoutMobile.requestFocus();
            return;
        }

        if(email.length() == 0)
        {
            textInputLayoutEmail.setError("Email ID not blank");
            textInputLayoutEmail.requestFocus();
            textInputLayoutEmail.setErrorEnabled(false);
            return;
        }
        if(!isValidEmail(email))
        {
            textInputLayoutEmail.setError("Enter Valid Email ID");
            textInputLayoutEmail.requestFocus();
            return;
        }

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String finalURL = Constants.URL_VALIDITY_USER+"?name="+name+"&mobile="+mobile+"&email="+email;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                finalURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userInfo(name, mobile, email);

                                if(isCrateUser)
                                {
                                    isCrateUser = false;
                                    sendEmailOTP();
                                }

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }


                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                    }
                }
        );

//        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        if(isCrateUser)
        {
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }


//        // Create a bundle called queryBundle
//        Bundle queryBundle = new Bundle();
//
//        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
//        //url value here is https://jsonplaceholder.typicode.com/posts
//        queryBundle.putString("OPERATION_QUERY_URL_EXTRA", Constants.URL_VALIDITY_USER);
//        queryBundle.putString("name", name);
//        queryBundle.putString("mobile", mobile);
//        queryBundle.putString("email", email);
//
////        String url = Constants.URL_VALIDITY_USER+"?name="+name+"&mobile="+mobile+"&email+"+email+"&password"+password;
//
//
//        // Call getSupportLoaderManager and store it in a LoaderManager variable
//        LoaderManager loaderManager = LoaderManager.getInstance(this);
//
//        // Get our Loader by calling getLoader and passing the ID we specified
//        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);
//
//        // If the Loader was null, initialize it. Else, restart it.
//        if(loader==null)
//        {
//            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
//        }
//        else
//        {
//            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
//        }

    }

    public static boolean isValidEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable final Bundle args)
    {
        final String[] stringResponse = new String[1];

        Log.d("TAG", "onCreateLoader: email");

        return new AsyncTaskLoader<String[]>(this)
        {
            @Nullable
            @Override
            public String[] loadInBackground()
            {
                Log.d("TAG", "readData: "+"8");

                if(args == null || "".equals(args))
                {
                    return null;
                }
                final boolean[] b = {true};


                String url = args.getString("OPERATION_QUERY_URL_EXTRA");
                final String name = args.getString("name");
                final String mobile = args.getString("mobile");
                final String email = args.getString("email");

                String finalURL = Constants.URL_VALIDITY_USER+"?name="+name+"&mobile="+mobile+"&email="+email;

                Log.d("TAG", "loadInBackgrounddd: "+finalURL);
//
//                StringRequest stringRequest = new StringRequest(
//                        Request.Method.GET,
//                        url,
//
//                )

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        finalURL,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {

                                Log.d("TAG", "onResponse: "+"Dd");
                                stringResponse[0] = response;

                                b[0] = false;

                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userInfo(name, mobile, email);


                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                progressDialog.dismiss();

                                Log.d("TAG", "loadInBackground: 14");
                            }
                        }
                );

                int a = 0;

                if(b[0])
                {
                    while (b[0])
                    {
                        if (a == 0)
                        {
                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                            a++;
                        }
                    }
                }

                return stringResponse;
            }

            @Override
            protected void onStartLoading()
            {
                super.onStartLoading();

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                if(stringResponse[0] == null)
                {
                    Log.d("TAG", "onStartLoading: CHecking");
                    forceLoad();
                }

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data)
    {
//        progressDialog.dismiss();

        try
        {
            JSONObject jsonObject = new JSONObject(data[0]);

            if(!jsonObject.getBoolean("error"))
            {

//                sendMobileOTP();

                sendEmailOTP();

//                startActivity(new Intent(getApplicationContext(), MobileVerificationActivity.class));
//                Toast toast = Toast.makeText(
//                        getApplicationContext(),
//                        jsonObject.getString("message"),
//                        Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
            }
            else
            {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        jsonObject.getString("message"),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader)
    {

    }


    private void sendEmailOTP()
    {
        final String email = SharedPreferenceManager.getInstance(this).getUserEmail();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SEND_EMAIL_OTP,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                                SharedPreferenceManager.getInstance(getApplicationContext())
                                        .userSendEmail("yes");

                                finish();
                                startActivity(new Intent(getApplicationContext(), EmailVerificationActivity.class));
                            }
                            else
                            {
                                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        "Pleas Try Again..",
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            progressDialog.dismiss();
                        }
                        catch (JSONException e)
                        {
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
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
