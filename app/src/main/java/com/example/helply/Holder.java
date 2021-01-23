package com.example.helply;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class Holder extends RecyclerView.ViewHolder {

    TextView need, address, time;
    ImageView imageView;
    Integer is;
    String[] data;

    public Holder(@NonNull View itemView, String[] v) {
        super(itemView);
        this.data = v;
        this.need = itemView.findViewById(R.id.needTV);
        this.address = itemView.findViewById(R.id.addressObjectTV);
        this.time = itemView.findViewById(R.id.timeTV);
        this.imageView = itemView.findViewById(R.id.personImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                if(is == 0 ){
                    Intent intent = new Intent(context, TaskDescriptionActivity.class);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }
                else if (is == 1) {
                    Intent intent = new Intent(context, TaskInformactionActivity.class);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, FinishTaskActivity.class);
                    intent.putExtra("TaskData",data);
                    context.startActivity(intent);
                }

            }
        });

    }
}
