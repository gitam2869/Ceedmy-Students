package com.example.ceedmyfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String []>
{

    private BottomNavigationView bottomNavigationView;

    ShimmerFrameLayout shimmerFrameLayout;

    //adapter
    private MyCourseAdapter adapter;

    //a list to store all the prs users name
    public List<MyCourseInfo> myCourseInfoList;

    //the recyclerview
    RecyclerView recyclerView;

    public static final int OPERATION_SEARCH_LOADER = 622;

    private ImageView imageViewNoMyCourse;
    private TextView textViewNoMyCourse;


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

        setContentView(R.layout.activity_my_courses);

        bottomNavigationView = findViewById(R.id.idBottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMyCourseMenuId);

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

//                        Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        startActivity(intent);
//                        overridePendingTransition(0,0);

                        MyCoursesActivity.super.onBackPressed();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottomNavigationMyCourseMenuId:
                        return true;

                    case R.id.bottomNavigationLiveSessionMenuId:
//
//                        Intent intent1 = new Intent(getApplicationContext(), MyCoursesActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        startActivity(intent1);
//                        overridePendingTransition(0,0);

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
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        shimmerFrameLayout = findViewById(R.id.idShimmerMyCourses);
        imageViewNoMyCourse= findViewById(R.id.idImageViewNoMyCourseActivity);
        textViewNoMyCourse = findViewById(R.id.idTextViewNoMyCourseActivity);

        myCourseInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.idRecycleViewMyCourseList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new MyCourseAdapter(myCourseInfoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyCourseAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                MyCourseInfo myCourseInfo = myCourseInfoList.get(position);

                String id = myCourseInfo.getId();
                String categoryname = myCourseInfo.getCategoryname();
                String examname = myCourseInfo.getExamname();
                String coursename = myCourseInfo.getCoursename();
                String coursedescription = myCourseInfo.getCoursedescription();
                String demovideo = myCourseInfo.getDemovideo();
                String image = myCourseInfo.getImage();
                String mentor = myCourseInfo.getMentor();

                Intent intent = new Intent(MyCoursesActivity.this, SubscribedCourseActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("categoryname", categoryname);
                intent.putExtra("examname", examname);
                intent.putExtra("coursename", coursename);
                intent.putExtra("coursedescription", coursedescription);
                intent.putExtra("demovideo", demovideo);
                intent.putExtra("image", image);
                intent.putExtra("mentor", mentor);

                startActivity(intent);

                overridePendingTransition(0,0);

            }
        });

        getMyCourses();
    }


    private void getMyCourses()
    {
        Bundle queryBundle = new Bundle();

        queryBundle.putString("OPERATION_QUERY_URL_EXTRA", Constants.URL_GET_SUBSCRIBE_COURSES);

        LoaderManager loaderManager = LoaderManager.getInstance(this);

        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);

        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null)
        {
            Log.d("TAG", "getExamList: nullloader");
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
        else
        {
            Log.d("TAG", "getExamList: resetloader");

            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable final Bundle args)
    {
        final String userid = SharedPreferenceManager.getInstance(this).getUserId();

        return new AsyncTaskLoader<String[]>(this)
        {
            final String[] stringResponse = new String[1];

            @Nullable
            @Override
            public String[] loadInBackground()
            {
                Log.d("TAG", "loadInBackground: "+args);
                if(args == null || "".equals(args))
                {
                    return null;
                }
                String url = args.getString("OPERATION_QUERY_URL_EXTRA");


                String finaUrl = url + "?userid=" + userid;

                Log.d("TAG", "courses: "+finaUrl);

                final boolean[] b = {true};

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        finaUrl,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                stringResponse[0] = response;

                                b[0] = false;
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
//                                progressDialog.dismiss();

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

//                progressDialog.setMessage("Please Wait...");
//                progressDialog.show();
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
        try
        {
            //converting the string to json array object
            JSONArray array = new JSONArray(data[0]);
            Log.d("TAG", "onLoadFinisheda111: "+data[0]);

            if(array.length() == 0)
            {
                emptyMyCourses();
            }

            for (int i = 0; i < array.length(); i++)
            {

                //getting product object from json array
                JSONObject jsonObject = array.getJSONObject(i);

                myCourseInfoList.add(new MyCourseInfo(
                                jsonObject.getString("id"),
                                jsonObject.getString("userid"),
                                jsonObject.getString("categoryname"),
                                jsonObject.getString("examname"),
                                jsonObject.getString("coursename"),
                                jsonObject.getString("coursedescription"),
                                jsonObject.getString("demovideo"),
                                jsonObject.getString("image"),
                                jsonObject.getString("mentor")

                        )
                );
            }

            Log.d("TAG", "onResponse: "+"Dd");

            recyclerView.setAdapter(adapter);

            shimmerFrameLayout.setVisibility(View.GONE);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        getLoaderManager().destroyLoader(OPERATION_SEARCH_LOADER);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader)
    {

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
//                .Builder(MyCoursesActivity.this);
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
//
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
                .Builder(MyCoursesActivity.this);

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


    private void emptyMyCourses()
    {
        shimmerFrameLayout.setVisibility(View.GONE);
        imageViewNoMyCourse.setVisibility(View.VISIBLE);
        textViewNoMyCourse.setVisibility(View.VISIBLE);
    }

}
