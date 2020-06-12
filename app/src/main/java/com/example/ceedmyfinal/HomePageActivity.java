package com.example.ceedmyfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String []>
{
    ShimmerFrameLayout shimmerFrameLayout;

    public static Activity fa;
    private ImageView imageViewNotAvailable;
    private TextView textViewNotAvailable;

    private BottomNavigationView bottomNavigationView;

    //adapter
    private ExamListAdapter adapter;

    //a list to store all the prs users name
    public List<ExamListInfo> examListInfoList;

    //the recyclerview
    RecyclerView recyclerView;

    public static final int OPERATION_SEARCH_LOADER = 211;


    private MaterialCardView materialCardViewDemoVideo;
    private MaterialCardView materialCardViewDemoTest;
    private MaterialCardView materialCardViewMoreCourses;
    private MaterialButton materialButtonTryAgain;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fa = this;

        NetworkConfiguration networkConfiguration = new NetworkConfiguration(this);

        if(!networkConfiguration.isConnected())
        {
            setContentView(R.layout.main_no_internet);
            getSupportActionBar().hide();
            alertMessage();
            return;
        }


        setContentView(R.layout.activity_home_page);

        examListInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.idRecycleViewExamList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExamListAdapter(examListInfoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ExamListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                ExamListInfo examListInfo = examListInfoList.get(position);

                String examName = examListInfo.getExamname();

                SharedPreferenceManager.getInstance(getApplicationContext())
                        .userExamName(examName);

                Intent intent = new Intent(getApplicationContext(), AllCoursesActivity.class);
                intent.putExtra("examname", examName);
                startActivity(intent);
                overridePendingTransition(0,0);


//                Intent intent = new Intent(getApplicationContext(), CourseDescriptionActivity.class);
//                intent.putExtra("examname", examName);
//                startActivity(intent);
            }
        });


        shimmerFrameLayout = findViewById(R.id.idShimmerExamList);
        imageViewNotAvailable = findViewById(R.id.idImageViewNoHomepageActivity);
        textViewNotAvailable = findViewById(R.id.idTextViewHomepageActivity);
        materialButtonTryAgain = findViewById(R.id.idButtonTryAgainHomepageActivity);

        bottomNavigationView = findViewById(R.id.idBottomNavigationView);


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
//                        finish();
                        return true;

                    case R.id.bottomNavigationLiveSessionMenuId:
                        startActivity(new Intent(getApplicationContext(),LiveSessionActivity.class));
                        overridePendingTransition(0,0);

//                        Intent intent1 = new Intent(getApplicationContext(), MyCoursesActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        startActivity(intent1);
//                        overridePendingTransition(0,0);

//                        finish();
                        return true;


                    case R.id.bottomNavigationWishlistMenuId:
                        startActivity(new Intent(getApplicationContext(),WishlistActivity.class));
                        overridePendingTransition(0,0);
//                        finish();
                        return true;

                    case R.id.bottomNavigationAccountMenuId:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
//                        finish();
                        return true;
                }

                return false;
            }
        });

        materialButtonTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(getIntent());
            }
        });


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        String dateAndTime = String.valueOf(c);

        String temp [] = dateAndTime.split(" ");

        System.out.println("Current timsse => " + Arrays.toString(temp));

        String date = temp[2] +" "+ temp[1] +", "+ temp[5];

        String time = temp[3];

        System.out.println("Current date => " + date);

        System.out.println("Current time => " + time);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

//        System.out.println("Current date => " + formattedDate);

        String token = SharedPreferenceManager.getInstance(this).getUserFcmToken();

        if(token != null)
        {
            sendToken(token);
        }

        getExamList();
    }

    private void sendToken(final String token)
    {
//        final String token = SharedPreferenceManager.getInstance(this).getUserFcmToken();

        Log.d("TAG", "sendToken: login "+token);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADD_TOKEN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        SharedPreferenceManager.getInstance(getApplicationContext())
                                .userFCMToken(null);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeMenuId);
//
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeMenuId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void getExamList()
    {
        String categoryName = SharedPreferenceManager.getInstance(this).getUserExamCategory();
        Log.d("TAG", "getExamList: 11");
        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();

        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
        queryBundle.putString("OPERATION_QUERY_URL_EXTRA", Constants.URL_GET_DIFFERENT_EXAM_LIST);
        queryBundle.putString("categoryname", categoryName);

//        String url = Constants.URL_VALIDITY_USER+"?name="+name+"&mobile="+mobile+"&email+"+email+"&password"+password;


        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = LoaderManager.getInstance(this);

        // Get our Loader by calling getLoader and passing the ID we specified
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
        Log.d("TAG", "onResponse: 1");

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
                String categoryname = args.getString("categoryname");

                String temp = categoryname;
                String [] split = temp.split(" ");
                String str = null;
                for (int i = 0; i < split.length; i++)
                {
                    if (i == 0)
                    {
                        str = split[0];
                    }
                    else
                    {
                        str = str + "+" + split[i];
                    }

                }

                String finaUrl = url + "?categoryname=" + str;

                Log.d("TAG", "loadInBackground: "+finaUrl);

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
                                textViewNotAvailable.setText("Server Not Responde\n Please try again...");
                                shimmerFrameLayout.setVisibility(View.GONE);
                                imageViewNotAvailable.setVisibility(View.VISIBLE);
                                textViewNotAvailable.setVisibility(View.VISIBLE);
                                materialButtonTryAgain.setVisibility(View.VISIBLE);

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
//        progressDialog.dismiss();

        try
        {
            //converting the string to json array object
            JSONArray array = new JSONArray(data[0]);

            if(array.length() == 0)
            {
                examsNotAvailable();
            }

            for (int i = 0; i < array.length(); i++)
            {

                //getting product object from json array
                JSONObject jsonObject = array.getJSONObject(i);

                examListInfoList.add(new ExamListInfo(
                                jsonObject.getString("id"),
                                jsonObject.getString("categoryname"),
                                jsonObject.getString("examname"),
                                jsonObject.getString("path")
                        )
                );
            }

            Log.d("TAG", "onResponse: "+"Dd");

            recyclerView.setAdapter(adapter);



        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        shimmerFrameLayout.setVisibility(View.GONE);

        getLoaderManager().destroyLoader(OPERATION_SEARCH_LOADER);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader)
    {

    }


    private void examsNotAvailable()
    {
        textViewNotAvailable.setText("Exams Not Available for\n "+SharedPreferenceManager.getInstance(getApplicationContext()).getUserExamCategory());
        shimmerFrameLayout.setVisibility(View.GONE);
        imageViewNotAvailable.setVisibility(View.VISIBLE);
        textViewNotAvailable.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed()
    {

        moveTaskToBack(true);
//
//        // Create the object of
//        // AlertDialog Builder class
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(HomePageActivity.this);
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
////                                finish();
//
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
    }

    public void alertMessage()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(HomePageActivity.this);

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
