package com.example.helply;


import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankingHolder extends RecyclerView.ViewHolder {
     TextView description, street;
     ImageView medal;



    public RankingHolder(@NonNull View itemView) {
        super(itemView);

        this.medal = itemView.findViewById(R.id.medalImageView);
        this.description = itemView.findViewById(R.id.needTV);
        this.street = itemView.findViewById(R.id.addressObjectTV);


    }
}
