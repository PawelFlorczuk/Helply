package com.example.helply;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Holder extends RecyclerView.ViewHolder {

    TextView description, street;
    ImageView imageView;
    Integer is;

    public Holder(@NonNull View itemView) {
        super(itemView);

        this.description = itemView.findViewById(R.id.streetTV);
        this.street = itemView.findViewById(R.id.descriptionTV);
        this.imageView = itemView.findViewById(R.id.personImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                if(is == 0 ){
                    Intent intent = new Intent(context, TaskDescriptionActivity.class);
                    context.startActivity(intent);
                }
                else if (is == 1) {
                    Intent intent = new Intent(context, TaskInformactionActivity.class);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, FinishTaskActivity.class);
                    context.startActivity(intent);
                }

            }
        });

    }
}
