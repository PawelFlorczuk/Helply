package com.example.helply.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.details.AnnouncementDetailsActivity;
import com.example.helply.details.MyAnnouncementDetailsActivity;
import com.example.helply.details.TaskToDoDetailsActivity;

public class Holder extends RecyclerView.ViewHolder {

    TextView need, address, time;
    ImageView imageView;
    Integer is;
    String[] data;
    Bitmap bitmap;
    ConstraintLayout announcementObjectCL;
    public Holder(@NonNull View itemView, String[] v,Bitmap bitmap) {
        super(itemView);
        this.data = v;
        this.need = itemView.findViewById(R.id.pointsTV);
        this.address = itemView.findViewById(R.id.nickTV);
        this.time = itemView.findViewById(R.id.timeTV);
        this.imageView = itemView.findViewById(R.id.personImage);
        this.bitmap = bitmap;

        this.announcementObjectCL = itemView.findViewById(R.id.announcement_object_cl);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                if(is == 0 ){
                    Intent intent = new Intent(context, AnnouncementDetailsActivity.class);
                    intent.putExtra("Bitmap",bitmap);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }
                else if (is == 1) {
                    Intent intent = new Intent(context, TaskToDoDetailsActivity.class);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, MyAnnouncementDetailsActivity.class);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }

            }
        });

    }
}
