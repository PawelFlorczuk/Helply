package com.example.helply.components;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.details.AnnouncementDetailsActivity;
import com.example.helply.details.MyAnnouncementDetailsActivity;
import com.example.helply.details.TaskToDoDetailsActivity;

public class Holder extends RecyclerView.ViewHolder {

    TextView need, address, time;
    ImageView imageView;
    String target;
    String[] data;
    ImageView announcementObjectCL;
    public Holder(@NonNull View itemView, String[] v) {
        super(itemView);
        this.data = v;
        this.need = itemView.findViewById(R.id.pointsTV);
        this.address = itemView.findViewById(R.id.nickTV);
        this.time = itemView.findViewById(R.id.timeTV);
        this.imageView = itemView.findViewById(R.id.personImage);
        this.announcementObjectCL = itemView.findViewById(R.id.announcement_object_cl);

        itemView.setOnClickListener(view -> {
            Context context = view.getContext();

            if(target.equals("AnnouncementDetails")){
                Intent intent = new Intent(context, AnnouncementDetailsActivity.class);
                intent.putExtra("TaskData",data);
                context.startActivity(intent);
            }
            else if (target.equals("TaskToDoDetails")) {
                Intent intent = new Intent(context, TaskToDoDetailsActivity.class);
                intent.putExtra("TaskData",data);
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, MyAnnouncementDetailsActivity.class);
                intent.putExtra("TaskData",data);
                context.startActivity(intent);
            }

        });

    }
}
