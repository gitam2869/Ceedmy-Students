package com.example.ceedmyfinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private MaterialButton materialButtonSignIn;
    private MaterialButton materialButtonSignUp;


    private MaterialButton materialButtonTryAgain;
    private TextView textView;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fa = this;

//        materialButtonTryAgain = findViewById(R.id.idButtonTryAgainNOInternet);
//        textView = findViewById(R.id.textview);

        NetworkConfiguration networkConfiguration = new NetworkConfiguration(this);

//        materialButtonTryAgain.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d("TAG", "onClick: dddddddddddddddd");
//                finish();
//                startActivity(getIntent());
//            }
//        });

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


//        readExternalStorage();

//        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();


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



    public  boolean isReadStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("TAG","Permission is granted1");
                return true;
            }
            else
            {

                Log.v("TAG","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else
        { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("TAG","Permission is granted2");
                return true;
            }
            else
            {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else
        { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("TAG", "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                }else{
                }
                break;

            case 3:
                Log.d("TAG", "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                }else{
                }
                break;
        }
    }



//    private void readExternalStorage()
//    {
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            {
////                readExternalStorage();
//                Toast.makeText(getApplicationContext(), "s", Toast.LENGTH_LONG).show();
////                alertMessagePermission();
//
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            }
//            else
//            {
////              / alertMessagePermission();
//
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//
//    }



    public void alertMessagePermission()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Permission is required for media resources");

        builder.setTitle("Permission!");

        builder.setCancelable(true);

        builder.setPositiveButton("Give Permission", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

            }
        });

//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//
//            }
//        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }


//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//
//        textView.setVisibility(View.VISIBLE);
//        materialButtonTryAgain.setVisibility(View.VISIBLE);
//    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        Log.d("TAG", "onResume: dd");
//
//
//
////        if(SharedPreferenceManager.getInstance(this).isLoggedIn())
////        {
////            Log.d("TAG", "onResume: 1111");
////            startActivity(new Intent(this, HomePageActivity.class));
////            finish();
////            return;
////        }
//
//    }

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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}
