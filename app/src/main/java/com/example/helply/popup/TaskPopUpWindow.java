package com.example.helply.popup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.helply.R;

public class TaskPopUpWindow extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .6));

        final Button saveContactET = findViewById(R.id.saveContactET);
        final EditText contactET = findViewById(R.id.contactET);

        saveContactET.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("VolunteerContact", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("volunteer_contact", contactET.getText().toString());
            editor.apply();
            setResult(1111);
            finish();
        });


    }
}
