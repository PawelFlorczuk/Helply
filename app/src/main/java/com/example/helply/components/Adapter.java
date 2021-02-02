package com.example.helply.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Vector;

public class Adapter extends RecyclerView.Adapter<Holder> {
    public Adapter(Context c, Vector<String[]> v, int is, Bitmap bitmap) {
        this.c = c;
        this.v = v;
        this.is = is;
        this.windowNum = -1;
        this.bit = bitmap;

    }

    Bitmap bit;
    Integer windowNum;
    Context c;
    Vector<String[]> v;
    Integer is;


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.announcement_object, parent, false);
        windowNum = windowNum + 1;
        return new Holder(view, v.get(windowNum), bit);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String[] address = v.get(position)[1].split("-");
        String finalAddress = address[2] + " " + address[3] + " " + address[4];
        String[] timeArray = v.get(position)[0].split("T");
        String time = timeArray[0];
        String timeNow = LocalDateTime.now().toString().split("T")[0];
        if (time.equals(timeNow)) {
            time = "Today";
        } else if (time.equals(LocalDateTime.now().minusDays(1).toString().split("T")[0])) {
            time = "Yesterday";
        }else {
            String temp [] = v.get(position)[0].split("T");
            String res = temp[1].substring(0,5)  + " "+ temp[0]; //.replace("."," ");
            time = res;

        }

        holder.address.setText(finalAddress);
        holder.need.setText(v.get(position)[5]);
        holder.time.setText(time);
        holder.is = is;
        String announcementType = v.get(position)[5];
        switch (announcementType) {

            case "Shopping": {
                holder.announcementObjectCL.setBackgroundColor(Color.rgb(85, 25, 162));

                break;
            }

            case "Walking the dog": {
          //      holder.announcementObjectCL.setImageResource(R.drawable.dog8);
                holder.announcementObjectCL.setBackgroundResource(R.drawable.dog8);
             //   holder.announcementObjectCL.setBackgroundColor(Color.rgb(25, 162, 137));
                break;
            }

            default: {
                holder.announcementObjectCL.setBackgroundColor(Color.rgb(92, 179, 15));
                break;
            }

        }

        String imageID = v.get(position)[7].split("-")[0];
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profileImage")
                .child(imageID + ".jpg");

        try {
            final File image = File.createTempFile(v.get(position)[5], ".jpg");
            storageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap result = BitmapFactory.decodeFile(image.getAbsolutePath());
                    holder.imageView.setImageBitmap(result);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.imageView.setImageResource(R.drawable.user_default_logo);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return v.size();
    }
}
