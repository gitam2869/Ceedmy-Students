package com.example.ceedmyfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WishlistActivity extends AppCompatActivity
{

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        bottomNavigationView = findViewById(R.id.idBottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationWishlistMenuId);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.bottomNavigationHomeMenuId:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationMyCourseMenuId:
                        startActivity(new Intent(getApplicationContext(),MyCoursesActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationWishlistMenuId:
                        return true;

                    case R.id.bottomNavigationAccountMenuId:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }
    @Override
    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(WishlistActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

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
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
//                                finish();
                                moveTaskToBack(true);
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
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}
