package com.example.helply.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;

import java.util.Vector;

public class RankingAdapter extends RecyclerView.Adapter<RankingHolder> {

    Context c;
    Bitmap bitmap;
    Vector<String[]> v;

    public RankingAdapter(Context c, Vector<String[]> v, Bitmap bitmap)
    {
        this.c = c;
        String [] data;

        for(int i = 0 ; i < (v.size() - 1)  ; i++){
            for(int j = (v.size() -1) ; j > i   ; j--){
                if(Integer.parseInt(v.get(i)[1]) < Integer.parseInt(v.get(j)[1])){
                    data = v.get(i);
                    v.set(i,v.get(j));
                    v.set(j,data);
                }
            }
        }
        this.bitmap = bitmap;
        this.v = v;
    }

    @NonNull
    @Override
    public RankingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.ranking_object, parent, false);
        return new RankingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingHolder holder, int position) {
        holder.pointsTV.setText(v.get(position)[1]);
        holder.nickTV.setText(v.get(position)[0]);
        if (position == 0) {
            holder.medal.setImageResource(R.drawable.medal_1);
        } else if (position == 1) {
            holder.medal.setImageResource(R.drawable.medal_2);
        } else if (position == 2) {
            holder.medal.setImageResource(R.drawable.medal_3);
        } else {
            holder.medal.setVisibility(View.INVISIBLE);
        }
        holder.uid = v.get(position)[2];

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

