package com.example.ceedmyfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoTestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String []>
{
    ShimmerFrameLayout shimmerFrameLayout;

    private MaterialCardView materialCardView;
    private TextView textViewOptions;


    private TextView textViewQuestionNO;
    private TextView textViewQuestion;
    private TextView textViewOP1;
    private TextView textViewOP2;
    private TextView textViewOP3;
    private TextView textViewOP4;

    private MaterialCardView materialCardViewOP1;
    private MaterialCardView materialCardViewOP2;
    private MaterialCardView materialCardViewOP3;
    private MaterialCardView materialCardViewOP4;

    private TextView textViewQuestionIcon;

    public MenuItem menuItem;

    //adapter
    private DemoTestAdapter adapter;

    //a list to store all the prs users name
    public List<DemoTestInfo> demoTestInfoList;

    //the recyclerview
    RecyclerView recyclerView;

    public static final int OPERATION_SEARCH_LOADER = 202;

    private ProgressDialog progressDialog;

    int questionPosition;

    public DemoTestInfo demoTestInfo;

    public Map<Integer, ArrayList<Integer>> multMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_test);

        multMap = new HashMap<Integer, ArrayList<Integer>>();


        Log.d("TAG", "onCreate: m  "+multMap);

        shimmerFrameLayout = findViewById(R.id.shimmerDemoTestActivity);

        materialCardView = findViewById(R.id.idCardViewDemoTestActivity);
        textViewOptions = findViewById(R.id.idTextViewOptionsDemoTestActivity);


        textViewQuestionNO = findViewById(R.id.idTextViewQuestionNoDemoTestActivity);
        textViewQuestion = findViewById(R.id.idTextViewQuestionDemoTestActivity);
        textViewOP1 = findViewById(R.id.idTextViewOP1DemoTestActivity);
        textViewOP2 = findViewById(R.id.idTextViewOP2DemoTestActivity);
        textViewOP3 = findViewById(R.id.idTextViewOP3DemoTestActivity);
        textViewOP4 = findViewById(R.id.idTextViewOP4DemoTestActivity);

        materialCardViewOP1 = findViewById(R.id.idCardViewOP1DemoTestActivity);
        materialCardViewOP2 = findViewById(R.id.idCardViewOP2DemoTestActivity);
        materialCardViewOP3 = findViewById(R.id.idCardViewOP3DemoTestActivity);
        materialCardViewOP4 = findViewById(R.id.idCardViewOP4DemoTestActivity);

        demoTestInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.idRecycleViewDemoTesActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new DemoTestAdapter(demoTestInfoList);

        adapter.setOnItemClickListener(new DemoTestAdapter.OnItemClickListener()
        {
            @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
            @Override
            public void onItemClick(final int position)
            {
                Log.d("TAG", "onItemClick: adpter1");

                if(!multMap.containsKey(position))
                {
                    Log.d("TAG", "onItemClick: adpter2");
                    clearCardViewState();
                }
                else
                {
                    Log.d("TAG", "onItemClick: adpter3");
                    ArrayList<Integer> values = multMap.get(position);
                    Log.d("TAG", "onItemClick: va"+values);
                    setQuestionAnswerState(values);
                }

                setColor(position);
                Log.d("TAG", "onItemClick: adpter4");
            }
        });
        recyclerView.setAdapter(adapter);

        materialCardViewOP1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//                materialCardViewOP1.setClickable(false);
//                String s = textViewOP1.getText().toString();
//                Log.d("TAG", "onClick: "+s);
                setAnswerState(questionPosition);


                ArrayList<Integer> value = new ArrayList<>();
                value.add(1);

                demoTestInfo = demoTestInfoList.get(questionPosition);
                String a = demoTestInfo.getAnswer();
                Log.d("TAG", "onClick: "+a);


                if(textViewOP1.getText().toString().equals(a))
                {
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.greenColor));
                    setCardViewNoClickable();
                    value.add(1);
                }
                else if(textViewOP2.getText().toString().equals(a))
                {
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(2);
                }
                else if(textViewOP3.getText().toString().equals(a))
                {
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(3);
                }
                else if(textViewOP4.getText().toString().equals(a))
                {
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(4);
                }

                multMap.put(questionPosition, value);

                Log.d("TAG", "onClick: op1 "+multMap);

                if(multMap.size() == demoTestInfoList.size())
                {
//                    Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_LONG).show();
                    menuItem.setVisible(true);
//                    showAlertDialog();
                }

            }
        });

        materialCardViewOP2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setAnswerState(questionPosition);

                ArrayList<Integer> value = new ArrayList<>();
                value.add(2);

                demoTestInfo = demoTestInfoList.get(questionPosition);
                String a = demoTestInfo.getAnswer();
                Log.d("TAG", "onClick: "+a);


                if(textViewOP1.getText().toString().equals(a))
                {
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(1);
                }
                else if(textViewOP2.getText().toString().equals(a))
                {
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.greenColor));
                    setCardViewNoClickable();
                    value.add(2);
                }
                else if(textViewOP3.getText().toString().equals(a))
                {
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(3);
                }
                else if(textViewOP4.getText().toString().equals(a))
                {
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(4);
                }

                multMap.put(questionPosition, value);
                Log.d("TAG", "onClick: op2 "+multMap);

                if(multMap.size() == demoTestInfoList.size())
                {
//                    Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_LONG).show();
                    menuItem.setVisible(true);

//                    showAlertDialog();
                }

            }
        });

        materialCardViewOP3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setAnswerState(questionPosition);


                ArrayList<Integer> value = new ArrayList<>();
                value.add(3);

                demoTestInfo = demoTestInfoList.get(questionPosition);
                String a = demoTestInfo.getAnswer();
                Log.d("TAG", "onClick: "+a);


                if(textViewOP1.getText().toString().equals(a))
                {
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(1);
                }
                else if(textViewOP2.getText().toString().equals(a))
                {
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.colorPrimary));

                    setCardViewNoClickable();
                    value.add(2);
                }
                else if(textViewOP3.getText().toString().equals(a))
                {
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.greenColor));
                    setCardViewNoClickable();
                    value.add(3);
                }
                else if(textViewOP4.getText().toString().equals(a))
                {
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(4);
                }

                multMap.put(questionPosition, value);
                Log.d("TAG", "onClick: op3 "+multMap);

                if(multMap.size() == demoTestInfoList.size())
                {
                    menuItem.setVisible(true);

//                    showAlertDialog();
                }

            }
        });

        materialCardViewOP4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setAnswerState(questionPosition);

                ArrayList<Integer> value = new ArrayList<>();
                value.add(4);

                demoTestInfo = demoTestInfoList.get(questionPosition);
                String a = demoTestInfo.getAnswer();
                Log.d("TAG", "onClick: "+a);


                if(textViewOP1.getText().toString().equals(a))
                {
                    materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                    setCardViewNoClickable();
                    value.add(1);
                }
                else if(textViewOP2.getText().toString().equals(a))
                {
                    materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.colorPrimary));

                    setCardViewNoClickable();
                    value.add(2);
                }
                else if(textViewOP3.getText().toString().equals(a))
                {
                    materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.greenColor));
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.colorPrimary));

                    setCardViewNoClickable();
                    value.add(3);
                }
                else if(textViewOP4.getText().toString().equals(a))
                {
                    materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.greenColor));
                    setCardViewNoClickable();
                    value.add(4);
                }

                multMap.put(questionPosition, value);
                Log.d("TAG", "onClick: op4 "+multMap);

                if(multMap.size() == demoTestInfoList.size())
                {
//                    Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_LONG).show();
//                    showAlertDialog();
                    menuItem.setVisible(true);

                }
            }
        });


        progressDialog = new ProgressDialog(this);

        getDemoTestQuestions();
    }

    private void setAnswerState(int position)
    {
        TextView textViewQuestionNo = recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.idTextViewQuestionNODemoTestList);

        int color = Color.rgb(74,20,140);
        int textcolor = Color.rgb(255,255,255);

        GradientDrawable gradientDrawable = (GradientDrawable) textViewQuestionNo.getBackground().getCurrent();
        gradientDrawable.setColor(color);

        textViewQuestionNo.setTextColor(textcolor);
    }

    private void setQuestionAnswerState(ArrayList<Integer> stateList)
    {
        clearCardViewState();
        Log.d("TAG", "setQuestionAnswerState: "+stateList);

        int wrongState = stateList.get(0);
        int correctState = stateList.get(1);

        if(wrongState == 1)
        {
            materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(wrongState == 2)
        {
            materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(wrongState == 3)
        {
            materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        }

        if(correctState == 1)
        {
            materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.greenColor));
        }
        else if(correctState == 2)
        {
            materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.greenColor));
        }
        else if(correctState == 3)
        {
            materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.greenColor));
        }
        else
        {
            materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.greenColor));
        }

        setCardViewNoClickable();

//        for (int i = 0 ; i < stateList.size() ; i++)
//        {
//            int state = stateList.get(i);
//            if (state == 1)
//            {
//
//            }
//        }
    }

    private void setCardViewNoClickable()
    {
        materialCardViewOP1.setClickable(false);
        materialCardViewOP2.setClickable(false);
        materialCardViewOP3.setClickable(false);
        materialCardViewOP4.setClickable(false);

    }

    private void clearCardViewState()
    {




        materialCardViewOP1.setStrokeColor(getResources().getColor(R.color.strokeColor));
        materialCardViewOP2.setStrokeColor(getResources().getColor(R.color.strokeColor));

        materialCardViewOP1.setClickable(true);
        materialCardViewOP2.setClickable(true);
        materialCardViewOP3.setClickable(true);
        materialCardViewOP4.setClickable(true);


        materialCardViewOP3.setStrokeColor(getResources().getColor(R.color.strokeColor));
        materialCardViewOP4.setStrokeColor(getResources().getColor(R.color.strokeColor));



    }

    private void setColor(int position)
    {
        questionPosition = position;

        DemoTestInfo demoTestInfo = demoTestInfoList.get(position);
        int tempPosition = position;


        for(int i = 0; i < demoTestInfoList.size(); i++)
        {
            if(multMap.containsKey(i))
            {
                setAnswerState(i);
            }
            else
            {
                if (i == tempPosition)
                {
                    TextView textViewQuestionNo = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idTextViewQuestionNODemoTestList);

                    int color = Color.rgb(240,22,33);
                    int textcolor = Color.rgb(255,255,255);

                    GradientDrawable gradientDrawable = (GradientDrawable) textViewQuestionNo.getBackground().getCurrent();
                    gradientDrawable.setColor(color);

                    textViewQuestionNo.setTextColor(textcolor);
                }
                else
                {
                    TextView textViewQuestionNo = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.idTextViewQuestionNODemoTestList);

                    int color = Color.rgb(236,233,233);
                    int textcolor = Color.rgb(0,0,0);

                    GradientDrawable gradientDrawable = (GradientDrawable) textViewQuestionNo.getBackground().getCurrent();
                    gradientDrawable.setColor(color);

                    textViewQuestionNo.setTextColor(textcolor);
                }
            }

        }


        textViewQuestionNO.setText("Q."+demoTestInfo.getNo());
        textViewQuestion.setText(demoTestInfo.getQuestion());
        textViewOP1.setText(demoTestInfo.getOp1());
        textViewOP2.setText(demoTestInfo.getOp2());
        textViewOP3.setText(demoTestInfo.getOp3());
        textViewOP4.setText(demoTestInfo.getOp4());
    }

    private void getDemoTestQuestions()
    {
        final String examName = SharedPreferenceManager.getInstance(this).getUserExamName();

        Log.d("TAG", "getDemoTestQuestions: "+examName);
        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();

        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
        queryBundle.putString("examname", examName);

//        String url = Constants.URL_VALIDITY_USER+"?name="+name+"&mobile="+mobile+"&email+"+email+"&password"+password;


        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = LoaderManager.getInstance(this);

        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);

        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null)
        {
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
        else
        {
            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }

    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable final Bundle args)
    {
        final String[] stringResponse = new String[1];

        return new AsyncTaskLoader<String[]>(this)
        {
            @Nullable
            @Override
            public String[] loadInBackground()
            {

                if(args == null || "".equals(args))
                {
                    return null;
                }
                final boolean[] b = {true};

                String temp = args.getString("examname");
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

//                final String examname = args.getString("examname");
//                Log.d("TAG", "loadInBackground: "+examname);
//
//                String[] splitstring = examname.split(" ");
//                String str = null;
//                for (int i = 0; i < splitstring.length; i++)
//                {
//                    if(i == 0)
//                    {
//                        str = splitstring[0] + "%";
//                    }
//                    else if(i > 0 && i < splitstring.length-1)
//                    {
//                        str = str + splitstring[i] + "%";
//                    }
//                    else
//                    {
//                        str = str + "20" + splitstring[i];
//                    }
//                }
//
//                Log.d("TAG", "loadInBackgroundstr: "+str);
//
//                Log.d("TAG", "loadInBackgroundssssssss: "+ splitstring[0]);

                String finalURL = Constants.URL_DEMO_QUESTION+"?examname="+str;

                Log.d("TAG", "loadInBackground: "+finalURL);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        finalURL,
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
                                progressDialog.dismiss();
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



                if(stringResponse[0] == null)
                {
//                    progressDialog.setMessage("Please Wait...");
//                    progressDialog.show();
                    forceLoad();
                }
            }
        };
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data)
    {
        Log.d("TAG", "onLoadFinished: "+data[0]);
//        progressDialog.dismiss();
        try
        {
            //converting the string to json array object
            JSONArray array = new JSONArray(data[0]);


            for (int i = 0; i < array.length(); i++)
            {

                //getting product object from json array
                JSONObject jsonObject = array.getJSONObject(i);

                demoTestInfoList.add(new DemoTestInfo(
                                jsonObject.getString("id"),
                                jsonObject.getString("examname"),
                                jsonObject.getString("no"),
                                jsonObject.getString("question"),
                                jsonObject.getString("op1"),
                                jsonObject.getString("op2"),
                                jsonObject.getString("op3"),
                                jsonObject.getString("op4"),
                                jsonObject.getString("answer")
                        )
                );
            }



            questionPosition = 0;

//            arrayListAnswer.add(0);

            demoTestInfo = demoTestInfoList.get(0);

            textViewQuestionNO.setText("Q."+demoTestInfo.getNo());
            textViewQuestion.setText(demoTestInfo.getQuestion());
            textViewOP1.setText(demoTestInfo.getOp1());
            textViewOP2.setText(demoTestInfo.getOp2());
            textViewOP3.setText(demoTestInfo.getOp3());
            textViewOP4.setText(demoTestInfo.getOp4());
//            setColor(0);

            recyclerView.setAdapter(adapter);

            shimmerFrameLayout.setVisibility(View.GONE);
            materialCardView.setVisibility(View.VISIBLE);
            textViewOptions.setVisibility(View.VISIBLE);
            materialCardViewOP1.setVisibility(View.VISIBLE);
            materialCardViewOP2.setVisibility(View.VISIBLE);
            materialCardViewOP3.setVisibility(View.VISIBLE);
            materialCardViewOP4.setVisibility(View.VISIBLE);

//            linearLayout.setVisibility(View.VISIBLE);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

//        getLoaderManager().destroyLoader(OPERATION_SEARCH_LOADER);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader)
    {

    }


    private void showAlertDialog()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(DemoTestActivity.this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_test_completed,null);

        TextView textViewTryAgain = view.findViewById(R.id.idTextViewTryAgainCustomDialogTestCompleted);
        TextView textViewFinish = view.findViewById(R.id.idTextViewFinishCustomDialogTestCompleted);

        alert.setView(view);

        final AlertDialog alertDialog = alert.create();

//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.TextAppearance_AppCompat;

        textViewTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        textViewFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        android.app.AlertDialog.Builder builder
                = new android.app.AlertDialog
                .Builder(DemoTestActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure to finish Test ?");

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
//                                moveTaskToBack(true);
//                                finish();

                                DemoTestActivity.super.onBackPressed();

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
        android.app.AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.demo_test_result,menu);

        menuItem = menu.findItem(R.id.idResultMenuDemoTestResult);

        menuItem.setVisible(false);

//        if (multMap.size() == demoTestInfoList.size())
//        {
//            Log.d("TAG", "onCreateOptionsMenu: ddddddd");
//            menuItem.setVisible(true);
//        }
//        else
//        {
//            menuItem.setVisible(false);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.idResultMenuDemoTestResult:
                showAlertDialog();
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
