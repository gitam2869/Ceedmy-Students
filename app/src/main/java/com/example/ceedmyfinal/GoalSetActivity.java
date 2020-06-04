package com.example.ceedmyfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalSetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>
{

    ProgressBar progressBar;

    ShimmerFrameLayout shimmerFrameLayout;

    //adapter
    private GoalSetAdapter adapter;

    //a list to store all the prs users name
    public List<GoalSetInfo> goalSetInfoList;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    public static final int OPERATION_SEARCH_LOADER = 221;

    public String updateState;


    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_set);

        getSupportActionBar().hide();

        updateState = SharedPreferenceManager.getInstance(this).getUserExamCategory();

//        updateState = getIntent().getStringExtra("updatestate");

        shimmerFrameLayout = findViewById(R.id.idShimmerGoalList);
        progressBar = findViewById(R.id.idProgressbarLoadingGoalSetActivity);

        goalSetInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.idRecycleViewGoalList);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.HORIZONTAL));

        adapter = new GoalSetAdapter(goalSetInfoList);

        adapter.setOnItemClickListener(new GoalSetAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                GoalSetInfo goalSetInfo = goalSetInfoList.get(position);

                String categoryName = goalSetInfo.getExamcategory();

//                Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();
//                TextView textViewExamName = recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.idTextViewExamNameGoalList);
//                String examName = textViewExamName.getText().toString();



                SharedPreferenceManager.getInstance(getApplicationContext())
                        .userLogin("yes");

                SharedPreferenceManager.getInstance(getApplicationContext())
                        .userExamCategoryName(categoryName);

                String userid = SharedPreferenceManager.getInstance(getApplicationContext()).getUserId();

                if (updateState == null)
                {
                    insertUserStats(userid, categoryName);
                }
                else
                {
                    updateUserStats(userid, categoryName);
                }

            }
        });

        progressDialog = new ProgressDialog(this);

        Log.d("TAG", "onCreate: 1");
        SharedPreferenceManager.getInstance(getApplicationContext())
                .userSetGoal("yes");

        getExamList();

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void getExamList()
    {
        Log.d("TAG", "getExamList: 11");
        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();

        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
        queryBundle.putString("OPERATION_QUERY_URL_EXTRA", Constants.URL_GET_EXAM_CATEGORY);

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
                Log.d("TAG", "loadInBackground: "+url);
                final boolean[] b = {true};

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        url,
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
//        progressDialog.dismiss();

        try
        {
            //converting the string to json array object
            JSONArray array = new JSONArray(data[0]);


            for (int i = 0; i < array.length(); i++)
            {

                //getting product object from json array
                JSONObject jsonObject = array.getJSONObject(i);

                goalSetInfoList.add(new GoalSetInfo(
                                jsonObject.getString("id"),
                                jsonObject.getString("examcategory"),
                                jsonObject.getString("path")
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

    public void insertUserStats(final String userid, final String categoryname)
    {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_USER_STATS,
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
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0,0);
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                            }
                            else
                            {
                                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        "Please, Again select your goal..",
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
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
                params.put("userid", userid);
                params.put("categoryname", categoryname);

                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void updateUserStats(final String userid, final String categoryname)
    {
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UPDATE_USER_STATS,
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
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0,0);
//                                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                            }
                            else
                            {
                                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        "Please, Again select your goal..",
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
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
                params.put("userid", userid);
                params.put("categoryname", categoryname);

                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed()
    {

        if (updateState == null)
        {
            moveTaskToBack(true);
        }
        else
        {
            startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            finish();
            overridePendingTransition(0,0);
        }

    }
}
