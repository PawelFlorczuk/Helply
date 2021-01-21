package com.example.helply;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankingHolder extends RecyclerView.ViewHolder {
     TextView description, street;



    public RankingHolder(@NonNull View itemView) {
        super(itemView);
        this.description = itemView.findViewById(R.id.streetTV);
        this.street = itemView.findViewById(R.id.descriptionTV);


    }
}
