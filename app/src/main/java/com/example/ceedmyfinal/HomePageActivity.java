package com.example.ceedmyfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class HomePageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private MaterialCardView materialCardViewDemoVideo;
    private MaterialCardView materialCardViewDemoTest;
    private MaterialCardView materialCardViewMoreCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.idBottomNavigationView);
        materialCardViewDemoVideo = findViewById(R.id.idCardViewDemoVideoActivity);
        materialCardViewDemoTest = findViewById(R.id.idCardViewDemoTestActivity);
        materialCardViewMoreCourses = findViewById(R.id.idCardViewMoreCoursesActivity);


        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeMenuId);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.bottomNavigationHomeMenuId:
                        return true;

                    case R.id.bottomNavigationMyCourseMenuId:
                        startActivity(new Intent(getApplicationContext(),MyCoursesActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.bottomNavigationWishlistMenuId:
                        startActivity(new Intent(getApplicationContext(),WishlistActivity.class));
                        overridePendingTransition(0,0);
                        finish();
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


        materialCardViewDemoVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), DemoVideoActivity.class));
            }
        });

        materialCardViewDemoTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), DemoTestActivity.class));
            }
        });

        materialCardViewMoreCourses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

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
                .Builder(HomePageActivity.this);

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
