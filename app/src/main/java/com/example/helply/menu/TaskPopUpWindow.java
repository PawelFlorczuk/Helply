package com.example.helply.menu;

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

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        final Button saveListBtn = findViewById(R.id.saveContactET);
        final EditText shoppingList = findViewById(R.id.contactET);


        saveListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("VolunteerContact", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("volunteer_contact", shoppingList.getText().toString());
                editor.apply();
                setResult(1111);
                finish();
            }
        });


    }
}
