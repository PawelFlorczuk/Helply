package com.example.helply.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Vector;

public class Adapter extends RecyclerView.Adapter<Holder> {

    Integer windowNum;
    Context c;
    Vector<String[]> v;
    String target;

    public Adapter(Context c, Vector<String[]> v, String target) {
        this.c = c;
        this.v = v;
        this.target = target;
        this.windowNum = -1;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.announcement_object, parent, false);
        windowNum = windowNum + 1;
        return new Holder(view, v.get(windowNum));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String creatingDateString(int position) {

        String[] timeArray = v.get(position)[0].split("T");
        String time = timeArray[0];
        String timeNow = LocalDateTime.now().toString().split("T")[0];
        if (time.equals(timeNow)) {
            time = "Today";
        } else if (time.equals(LocalDateTime.now().minusDays(1).toString().split("T")[0])) {
            time = "Yesterday";
        } else {
            String[] temp = v.get(position)[0].split("T");
            time = temp[1].substring(0,5)  + " "+ temp[0];
        }
        return time;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String time = creatingDateString(position);
        String[] address = v.get(position)[1].split("-");
        String finalAddress = address[2] + " " + address[3] + " " + address[4];
        holder.address.setText(finalAddress);
        holder.need.setText(v.get(position)[5]);
        holder.time.setText(time);
        holder.target = target;

        switch (v.get(position)[5]) {
            case "Shopping": {
                holder.announcementObjectCL.setBackgroundResource(R.drawable.shopping);
                break;
            }
            case "Walking the dog": {
               holder.announcementObjectCL.setBackgroundResource(R.drawable.dog);
                break;
            }
            default: {
                holder.announcementObjectCL.setBackgroundResource(R.drawable.charity);
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
            }).addOnFailureListener(e -> holder.imageView.setImageResource(R.drawable.user_default_logo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return v.size();
    }
}
