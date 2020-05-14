package com.example.ceedmyfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity
{

    private MaterialButton materialButtonSignIn;
    private MaterialButton materialButtonSignUp;

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

        if(SharedPreferenceManager.getInstance(this).isLoggedIn())
        {
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
            return;
        }

        if(SharedPreferenceManager.getInstance(this).getIsUserSetGoal() != null)
        {
            startActivity(new Intent(this, GoalSetActivity.class));
            finish();
            return;
        }

        if(SharedPreferenceManager.getInstance(this).getIsUserSetPassword() != null)
        {
            startActivity(new Intent(this, CreatePasswordActivity.class));
            finish();
            return;
        }

        if(SharedPreferenceManager.getInstance(this).getisEmailSend() != null)
        {
            startActivity(new Intent(this, EmailVerificationActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        materialButtonSignIn = findViewById(R.id.idButtonSignInMainActivity);
        materialButtonSignUp = findViewById(R.id.idButtonSignUpMainActivity);

        materialButtonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                finish();
            }
        });

        materialButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),NewAccountActivity.class));
//                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: dd");

        if(SharedPreferenceManager.getInstance(this).isLoggedIn())
        {
            Log.d("TAG", "onResume: 1111");
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
            return;
        }

    }

    //    @Override
//    protected void onRestart()
//    {
//        super.onRestart();
//        Log.d("TAG", "onRestart: sssssssss");
//        finish();
//    }

    public boolean isConnected()
    {
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }

        return connected;
    }

    public void alertMessage()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

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

}
