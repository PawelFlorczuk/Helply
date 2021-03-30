package com.example.helply.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;

import java.util.Vector;

public class OpinionsAdapter  extends RecyclerView.Adapter<OpinionsHolder> {

    Vector<String[]> v;
    Context c;

    public OpinionsAdapter(Context c, Vector<String[]> v) {
        this.v =  v;
        this.c = c;
    }

    @NonNull
    @Override
    public OpinionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.opinion_object, parent, false);
        return new OpinionsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpinionsHolder holder, int position) {
        holder.opinionTV.setText(v.get(position)[0]);
        String[] temp = v.get(position)[1].split("T");
        String res = temp[0] + " " + temp[1].substring(0,5);
        holder.dateTV.setText(res);
    }

    @Override
    public int getItemCount() {
        return v.size();
    }
}
