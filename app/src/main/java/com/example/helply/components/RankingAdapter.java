package com.example.helply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class RankingAdapter extends RecyclerView.Adapter<RankingHolder> {
    public RankingAdapter(Context c, Vector<String[]> v)
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
        this.v = v;
    }

    Context c;
    Vector<String[]> v;

    @NonNull
    @Override
    public RankingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.object2, parent, false);

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


    }

    @Override
    public int getItemCount() {
        return v.size();
    }
}
