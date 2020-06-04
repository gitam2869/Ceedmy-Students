package com.example.ceedmyfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity
{

    private BottomNavigationView bottomNavigationView;

    private TextView textViewUserName;
    private TextView textViewUserEmail;
    private TextView textViewUserPhoneNumber;
    private TextView textViewUserGoal;
    private TextView textViewChangeYourGoal;
    private TextView textViewChangePassword;
    private ImageView imageViewCloseChangePassword;
    private TextInputLayout textInputLayoutCurrentPassword;
    private MaterialButton materialButtonVerify;

    private MaterialCardView materialCardViewSignOut;

    private ProgressDialog progressDialog;

    public Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        NetworkConfiguration networkConfiguration = new NetworkConfiguration(this);

        if(!networkConfiguration.isConnected())
        {
            setContentView(R.layout.main_no_internet);
            getSupportActionBar().hide();
            alertMessage();
            return;
        }

        setContentView(R.layout.activity_account);

        bottomNavigationView = findViewById(R.id.idBottomNavigationView);

        textViewUserName = findViewById(R.id.idTextViewUserNameAccountActivity);
        textViewUserEmail = findViewById(R.id.idTextViewUserEmailAccountActivity);
        textViewUserPhoneNumber = findViewById(R.id.idTextViewUserPhoneNumberAccountActivity);
        textViewUserGoal = findViewById(R.id.idTextViewUserGoalAccountActivity);
        textViewChangeYourGoal = findViewById(R.id.idTextViewChangeYourGoalAccountActivity);
        textViewChangePassword = findViewById(R.id.idTextViewChangePasswordAccountActivity);
        materialCardViewSignOut = findViewById(R.id.idCardviewSignOutAccountActivity);
        imageViewCloseChangePassword = findViewById(R.id.idImageViewCloseChangePasswordAccountActivity);

        textInputLayoutCurrentPassword = findViewById(R.id.idTextInputLayoutCurrentPasswordAccountActivity);
        materialButtonVerify = findViewById(R.id.idButtonVerifyAccountActivity);


        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationAccountMenuId);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.bottomNavigationHomeMenuId:
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
//                        finish();

                        AccountActivity.super.onBackPressed();
                        overridePendingTransition(0,0);

                        return true;

                    case R.id.bottomNavigationMyCourseMenuId:
                        startActivity(new Intent(getApplicationContext(),MyCoursesActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationLiveSessionMenuId:
                        startActivity(new Intent(getApplicationContext(),LiveSessionActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationWishlistMenuId:
                        startActivity(new Intent(getApplicationContext(),WishlistActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationAccountMenuId:
                        return true;
                }

                return false;
            }
        });


        textViewChangeYourGoal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String s = "yes";
                Intent intent = new Intent(getApplicationContext(), GoalSetActivity.class);
                intent.putExtra("updatestate", s);
                startActivity(intent);
                finish();
                overridePendingTransition(0,0);

            }
        });

        textViewChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textViewChangePassword.setVisibility(View.GONE);
                imageViewCloseChangePassword.setVisibility(View.VISIBLE);
                textInputLayoutCurrentPassword.setVisibility(View.VISIBLE);
                materialButtonVerify.setVisibility(View.VISIBLE);
            }
        });

        imageViewCloseChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textInputLayoutCurrentPassword.getEditText().setText("");
                textInputLayoutCurrentPassword.setErrorEnabled(false);


                textViewChangePassword.setVisibility(View.VISIBLE);
                imageViewCloseChangePassword.setVisibility(View.GONE);
                textInputLayoutCurrentPassword.setVisibility(View.GONE);
                materialButtonVerify.setVisibility(View.GONE);
            }
        });

        materialButtonVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verifyCurrentPassword();
            }
        });

        materialCardViewSignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signOutUser();
            }
        });

        textInputLayoutCurrentPassword.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutCurrentPassword.setErrorEnabled(false);
                return false;
            }
        });


        textViewUserName.setText(SharedPreferenceManager.getInstance(this).getUserName());
        textViewUserEmail.setText(SharedPreferenceManager.getInstance(this).getUserEmail());
        textViewUserPhoneNumber.setText(SharedPreferenceManager.getInstance(this).getUserMobileNumber());
        textViewUserGoal.setText(SharedPreferenceManager.getInstance(this).getUserExamCategory());

        progressDialog = new ProgressDialog(this);
    }




    private void verifyCurrentPassword()
    {
        textInputLayoutCurrentPassword.setErrorEnabled(false);

        final String email = SharedPreferenceManager.getInstance(this).getUserEmail();
        final String password = textInputLayoutCurrentPassword.getEditText().getText().toString().trim();

        if(password.length() == 0)
        {
            textInputLayoutCurrentPassword.setError("Password not blank");
            return;
        }

        progressDialog.setMessage("Verifying...");
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
//                                toast = Toast.makeText(
//                                        getApplicationContext(),
//                                        "Password Matches",
//                                        Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                Log.d("TAG", "onResponse: 6");
                                progressDialog.dismiss();

                                changePassword();

                            }
                            else
                            {
                                Log.d("TAG", "onResponse: 5");

                                toast = Toast.makeText(
                                        getApplicationContext(),
                                        "Enter Correct Password",
                                        Toast.LENGTH_SHORT);
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

    @SuppressLint("ClickableViewAccessibility")
    private void changePassword()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);

        View view = getLayoutInflater().inflate(R.layout.custom_dialog_change_password, null);

//        views
        final TextInputLayout textInputLayoutNewPassword = view.findViewById(R.id.idTextInputLayoutNewPassword);
        final TextInputLayout textInputLayoutConfirmPassword = view.findViewById(R.id.idTextInputLayoutConfirmPassword);
        MaterialButton materialButtonChangePasswrod = view.findViewById(R.id.idButtonChangePassword);
        TextView textView = view.findViewById(R.id.idTextViewCancelChangePassword);

        textInputLayoutNewPassword.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutNewPassword.setErrorEnabled(false);
                return false;
            }
        });

        textInputLayoutConfirmPassword.getEditText().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                textInputLayoutConfirmPassword.setErrorEnabled(false);
                return false;
            }
        });

        builder.setView(view);

        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.TextAppearance_AppCompat_Body1;

        materialButtonChangePasswrod.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textInputLayoutNewPassword.setErrorEnabled(false);
                textInputLayoutConfirmPassword.setErrorEnabled(false);

                final String password = textInputLayoutNewPassword.getEditText().getText().toString().trim();
                final String confirmPassword = textInputLayoutConfirmPassword.getEditText().getText().toString().trim();

                if(password.length() == 0)
                {
                    textInputLayoutNewPassword.setError("Password not blank");
                    return;

                }
                if(password.length() < 8)
                {
                    textInputLayoutNewPassword.setError("Enter Atleast 8 Characters");
                    return;
                }

                if(!password.equals(confirmPassword))
                {
                    textInputLayoutConfirmPassword.setError("Password not match");
                    return;
                }

                closkKeyboard();

                progressDialog.setMessage("Changing Password...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_CHANGE_PASSWORD,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d("TAG", "onResponse111: "+response);
                                try
                                {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if(!jsonObject.getBoolean("error"))
                                    {
                                        progressDialog.dismiss();

                                        toast = Toast.makeText(
                                                getApplicationContext(),
                                                "Password Update Successfully.",
                                                Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();

                                        finish();
                                        startActivity(getIntent());
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();

                                        toast = Toast.makeText(
                                                getApplicationContext(),
                                                "Password NOT update Successfully, please try again...",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();

                                        finish();
                                        startActivity(getIntent());
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
                )
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", SharedPreferenceManager.getInstance(getApplicationContext()).getUserEmail());
                        params.put("password", password);
                        return params;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                alertDialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textInputLayoutCurrentPassword.getEditText().setText("");
                textInputLayoutCurrentPassword.setErrorEnabled(false);

                textViewChangePassword.setVisibility(View.VISIBLE);
                imageViewCloseChangePassword.setVisibility(View.GONE);
                textInputLayoutCurrentPassword.setVisibility(View.GONE);
                materialButtonVerify.setVisibility(View.GONE);
                closkKeyboard();
                alertDialog.dismiss();
            }
        });



        alertDialog.show();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        overridePendingTransition(0,0);
    }

//    @Override
//    public void onBackPressed()
//    {
//
//        // Create the object of
//        // AlertDialog Builder class
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(AccountActivity.this);
//
//        // Set the message show for the Alert time
//        builder.setMessage("Do you want to exit ?");
//
//        // Set Alert Title
//        builder.setTitle("Alert !");
//
//        // Set Cancelable false
//        // for when the user clicks on the outside
//        // the Dialog Box then it will remain show
//        builder.setCancelable(true);
//
//        // Set the positive button with yes name
//        // OnClickListener method is use of
//        // DialogInterface interface.
//
//        builder
//                .setPositiveButton(
//                        "Yes",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // When the user click yes button
//                                // then app will close
////                                finish();
//                                moveTaskToBack(true);
//                                finish();
//                            }
//                        });
//
//        // Set the Negative button with No name
//        // OnClickListener method is use
//        // of DialogInterface interface.
//        builder
//                .setNegativeButton(
//                        "No",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // If user click no
//                                // then dialog box is canceled.
//                                dialog.cancel();
//                            }
//                        });
//
//        // Create the Alert dialog
//        AlertDialog alertDialog = builder.create();
//
//        // Show the Alert Dialog box
//        alertDialog.show();
//    }

    public void alertMessage()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AccountActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Please connect with to working internet connection");

        // Set Alert Title
        builder.setTitle("Network Error!");

        // Set Cancelable false// Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.

        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                finish();
                                startActivity(getIntent());
                            }
                        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void signOutUser()
    {
// Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AccountActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure to Sign Out?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(true);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
//                                finish();
                                SharedPreferenceManager.getInstance(getApplicationContext()).logout();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.dismiss();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void closkKeyboard()
    {
        Log.d("TAG", "closkKeyboard1: ");
        View view = this.getCurrentFocus();
        if (view != null)
        {
            Log.d("TAG", "closkKeyboard2: ");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.d("TAG", "closkKeyboard3: ");

            assert inputMethodManager != null;
            Log.d("TAG", "closkKeyboard4: ");

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Log.d("TAG", "closkKeyboard5: ");

        }
    }
}
