package com.example.helply.components;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;

public class OpinionsHolder extends RecyclerView.ViewHolder {

     TextView opinionTV;
    public OpinionsHolder(@NonNull View itemView) {
        super(itemView);
        this.opinionTV = itemView.findViewById(R.id.neederOpinion);
    }
}
