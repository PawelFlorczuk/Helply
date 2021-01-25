package com.example.helply.components;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.details.AnnouncementDetailsActivity;
import com.example.helply.details.MyAnnouncementDetailsActivity;
import com.example.helply.details.OpinionsActivity;
import com.example.helply.details.TaskToDoDetailsActivity;

public class RankingHolder extends RecyclerView.ViewHolder {
     TextView pointsTV, nickTV;
     ImageView medal;
     String uid;



    public RankingHolder(@NonNull View itemView, Bitmap bitmap) {
        super(itemView);
        this.medal = itemView.findViewById(R.id.medalImageView);
        this.pointsTV = itemView.findViewById(R.id.pointsTV);
        this.nickTV = itemView.findViewById(R.id.nickTV);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, OpinionsActivity.class);
                intent.putExtra("Bitmap",bitmap);
                intent.putExtra("Id",uid);
                context.startActivity(intent);



            }
        });
    }


}
