package com.example.ceedmyfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoalSetAdapter extends RecyclerView.Adapter<GoalSetAdapter.LanguageViewHolder>
{
    private List<GoalSetInfo> goalSetInfoList;

    public GoalSetAdapter(List<GoalSetInfo> goalSetInfoList)
    {
        this.goalSetInfoList = goalSetInfoList;
    }


    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.goal_list,parent,false);
        return new LanguageViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, int position)
    {
        GoalSetInfo goalSetInfo = goalSetInfoList.get(position);

        holder.textViewTopinName.setText(goalSetInfo.getname());

        Picasso.get().load(goalSetInfo.getpath()).into(holder.imageViewExamLogo, new Callback() {
            @Override
            public void onSuccess() {
                holder.shimmerFrameLayout.setVisibility(View.GONE);
                holder.textViewTopinName.setVisibility(View.VISIBLE);
                holder.imageViewExamLogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public int getItemCount()
    {

        return goalSetInfoList.size();
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
        MaterialCardView materialCardVideoList;
        TextView textViewTopinName;
        ImageView imageViewExamLogo;
        ShimmerFrameLayout shimmerFrameLayout;


        public LanguageViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            materialCardVideoList = itemView.findViewById(R.id.idCardViewGoalList);
            textViewTopinName = itemView.findViewById(R.id.idTextViewExamNameGoalList);
            imageViewExamLogo = itemView.findViewById(R.id.idImageViewExamLogoGoalList);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);


            materialCardVideoList.setOnClickListener(new View.OnClickListener()
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
