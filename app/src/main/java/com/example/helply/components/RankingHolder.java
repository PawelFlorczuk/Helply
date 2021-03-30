package com.example.helply.components;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.details.OpinionsActivity;

public class RankingHolder extends RecyclerView.ViewHolder {
     TextView pointsTV, nickTV;
     ImageView medal;
     String uid;



    public RankingHolder(@NonNull View itemView) {
        super(itemView);
        this.medal = itemView.findViewById(R.id.medalIV);
        this.pointsTV = itemView.findViewById(R.id.pointsTV);
        this.nickTV = itemView.findViewById(R.id.nickTV);

        itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, OpinionsActivity.class);
            intent.putExtra("Id",uid);
            context.startActivity(intent);

        });
    }


}
