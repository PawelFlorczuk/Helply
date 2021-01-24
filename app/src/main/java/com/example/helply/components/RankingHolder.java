package com.example.helply.components;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;

public class RankingHolder extends RecyclerView.ViewHolder {
     TextView pointsTV, nickTV;
     ImageView medal;



    public RankingHolder(@NonNull View itemView) {
        super(itemView);

        this.medal = itemView.findViewById(R.id.medalImageView);
        this.pointsTV = itemView.findViewById(R.id.pointsTV);
        this.nickTV = itemView.findViewById(R.id.nickTV);


    }
}
