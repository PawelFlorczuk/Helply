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
                if(Integer.parseInt(v.get(i)[2]) < Integer.parseInt(v.get(j)[2])){
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
        holder.description.setText(v.get(position)[0]);
        holder.street.setText(v.get(position)[2]);

    }

    @Override
    public int getItemCount() {
        return v.size();
    }
}

