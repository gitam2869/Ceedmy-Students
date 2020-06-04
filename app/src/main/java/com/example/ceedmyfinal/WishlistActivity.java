package com.example.ceedmyfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String []>
{

    ShimmerFrameLayout shimmerFrameLayout;

    private BottomNavigationView bottomNavigationView;

    //adapter
    private WishlistAdapterr adapter;

    //a list to store all the prs users name
    public List<WishlistInfo> wishlistInfoList;

    //the recyclerview
    RecyclerView recyclerView;

    public static final int OPERATION_SEARCH_LOADER = 722;

    private ImageView imageViewNoWishlist;
    private TextView textViewNoWishlist;

    private ProgressBar progressBar;

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
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
//                        finish();

                        WishlistActivity.super.onBackPressed();
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

        shimmerFrameLayout = findViewById(R.id.idShimmerWishlist);
        imageViewNoWishlist = findViewById(R.id.idImageViewNoWishlistActivity);
        textViewNoWishlist = findViewById(R.id.idTextViewNoWishlistActivity);
        progressBar = findViewById(R.id.idProgressbarMainWishlistActivity);


        wishlistInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.idRecycleViewWishlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new WishlistAdapterr(wishlistInfoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WishlistAdapterr.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                WishlistInfo wishlistInfo = wishlistInfoList.get(position);

                String id = wishlistInfo.getId();
                String categoryname = wishlistInfo.getCategoryname();
                String examname = wishlistInfo.getExamname();
                String coursename = wishlistInfo.getCoursename();
                String coursedescription = wishlistInfo.getCoursedescription();
                String demovideo = wishlistInfo.getDemovideo();
                String image = wishlistInfo.getImage();
                String mentor = wishlistInfo.getMentor();

                Intent intent = new Intent(WishlistActivity.this, CourseDescriptionActivity.class);

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

            @Override
            public void onItemClick1(int position)
            {
                removeCourse(position);
            }
        });



        getWishlistCourses();
    }


    private void getWishlistCourses()
    {
        Bundle queryBundle = new Bundle();

        queryBundle.putString("OPERATION_QUERY_URL_EXTRA", Constants.URL_GET_WISHLIST_COURSES);

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

                Log.d("TAG", "loadIgg: "+finaUrl);

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
                emptyWishlist();
            }

            for (int i = 0; i < array.length(); i++)
            {

                //getting product object from json array
                JSONObject jsonObject = array.getJSONObject(i);

                wishlistInfoList.add(new WishlistInfo(
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



    private void removeCourse(final int position)
    {



        Log.d("TAG", "wishlistgggggggggggg: "+wishlistInfoList.size());
        WishlistInfo wishlistInfo = wishlistInfoList.get(position);

        final String id = wishlistInfo.getId();
//        String userid = wishlistInfo.getUserid();
//        String categoryname = wishlistInfo.getCategoryname();
//        String examname = wishlistInfo.getExamname();
//        String coursename = wishlistInfo.getCoursename();
//        String coursedescription = wishlistInfo.getCoursedescription();
//        String demovideo = wishlistInfo.getDemovideo();
//        String image = wishlistInfo.getImage();
//        String mentor = wishlistInfo.getMentor();


        AlertDialog.Builder builder = new AlertDialog.Builder(WishlistActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure to remove selected course from your wishlist?");

        // Set Alert Title
        builder.setTitle("Notice!");

        builder.setCancelable(true);


        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_REMOVE_WISHLIST_COURSE,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(wishlistInfoList.size() == 0)
                        {
//                            progressBar.setVisibility(View.GONE);
                            finish();
                            startActivity(getIntent());
//                            overridePendingTransition(0,0);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);

                return params;
            }
        };


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                wishlistInfoList.remove(position);
                adapter.notifyItemRemoved(position);

                if(wishlistInfoList.size() == 0)
                {
                    Log.d("TAG", "removeCourse: 1111111");
                    progressBar.setVisibility(View.VISIBLE);
                }
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

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
//                .Builder(WishlistActivity.this);
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
                .Builder(WishlistActivity.this);

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

    private void emptyWishlist()
    {
        shimmerFrameLayout.setVisibility(View.GONE);
        imageViewNoWishlist.setVisibility(View.VISIBLE);
        textViewNoWishlist.setVisibility(View.VISIBLE);
    }
}
