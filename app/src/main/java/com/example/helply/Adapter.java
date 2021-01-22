package com.example.helply;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Adapter extends RecyclerView.Adapter<Holder> {
    public Adapter(Context c, Vector<String[]> v,int is)
    {
        this.c = c;
        this.v = v;
        this.is = is;

    }

    Context c;
    Vector<String[]> v;
    Integer is;


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.object, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.address.setText(v.get(position)[0]);
        holder.need.setText(v.get(position)[1]);

        Data.Address = v.get(position)[0];
        Data.Description = v.get(position)[1];
        Data.Message = v.get(position)[3];
        Data.Helper = v.get(position)[2];
        Data.Purchase = v.get(position)[4];
        Data.ID = v.get(position)[5];
        holder.is = is;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profileImage")
                .child(v.get(position)[5] + ".jpg");

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
                    holder.imageView.setImageResource(R.drawable.logo);
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
