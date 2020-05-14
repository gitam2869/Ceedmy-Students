package com.example.ceedmyfinal;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DemoTestAdapter extends RecyclerView.Adapter<DemoTestAdapter.LanguageViewHolder>
{
    private List<DemoTestInfo> demoTestInfoList;

    private int lastCheckedPosition = -1;
    int row_index = 0;
    private int lastPosition = -1;



    public DemoTestAdapter(List<DemoTestInfo> demoTestInfoList)
    {
        this.demoTestInfoList = demoTestInfoList;
    }


    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.demo_test_list,parent,false);
        return new LanguageViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, final int position)
    {
        DemoTestInfo demoTestInfo = demoTestInfoList.get(position);

        holder.textViewQuestioNo.setText(demoTestInfo.getNo());

        if (position == 0)
        {
            int color = Color.rgb(240,22,33);
            int textcolor = Color.rgb(255,255,255);

            GradientDrawable gradientDrawable = (GradientDrawable) holder.textViewQuestioNo.getBackground().getCurrent();
            gradientDrawable.setColor(color);

            holder.textViewQuestioNo.setTextColor(textcolor);

        }
        else
        {
            int color = Color.rgb(236,233,233);
            int textcolor = Color.rgb(0,0,0);

            GradientDrawable gradientDrawable = (GradientDrawable) holder.textViewQuestioNo.getBackground().getCurrent();
            gradientDrawable.setColor(color);

            holder.textViewQuestioNo.setTextColor(textcolor);
        }

//        holder.textViewQuestioNo.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d("TAG", "onClick: gggggggggggggggg");
//
//                row_index = position;
//                notifyDataSetChanged();
//            }
//        });
//
//        if (row_index==position)
//        {
//            int color = Color.rgb(240,22,33);
//            int textcolor = Color.rgb(255,255,255);
//
//            GradientDrawable gradientDrawable = (GradientDrawable) holder.textViewQuestioNo.getBackground().getCurrent();
//            gradientDrawable.setColor(color);
//
//            holder.textViewQuestioNo.setTextColor(textcolor);
//
//
//
////            holder.linearLayout.setBackgroundColor(Color.RED);
//        }
//        else
//        {
//            int color = Color.rgb(236,222,233);
//            int textcolor = Color.rgb(255,0,255);
//
//            GradientDrawable gradientDrawable = (GradientDrawable) holder.textViewQuestioNo.getBackground().getCurrent();
//            gradientDrawable.setColor(color);
//
//            holder.textViewQuestioNo.setTextColor(textcolor);
//        }

        setAnimation(holder.itemView, position);

//        else
//        {
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#dddddd"));
//        }


//        holder.textViewQuestioNo.setTextColor(color);
//        holder.textViewQuestioNo.setTextColor(getResources().getColor(R.color.whiteColor));

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;

        }
    }

    @Override
    public int getItemCount()
    {

        return demoTestInfoList.size();
    }


    //create variable

    private OnItemClickListener mListener;

    //interface

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewQuestioNo;

        public LanguageViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            textViewQuestioNo = itemView.findViewById(R.id.idTextViewQuestionNODemoTestList);

            textViewQuestioNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}
