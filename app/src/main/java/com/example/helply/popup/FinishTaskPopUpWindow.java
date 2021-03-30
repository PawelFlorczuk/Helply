package com.example.helply.popup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.helply.R;

public class FinishTaskPopUpWindow extends Activity {
    private EditText opinionET;
    private CheckBox opinionCB;
    private CheckBox pointCB;
    private Button finishBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_finish_task_pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .6));

        opinionET = findViewById(R.id.opinionET);
        opinionCB = findViewById(R.id.opinCheckBox);
        pointCB = findViewById(R.id.pointCheckBox);
        finishBtn = findViewById(R.id.finishBtn);

        opinionCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                opinionET.setVisibility(View.VISIBLE);
            } else {
                opinionET.setVisibility(View.INVISIBLE);
            }
        });

        finishBtn.setOnClickListener(v -> {
            String point, description;
            description = opinionET.getText().toString();
            if(opinionCB.isChecked() && ( description.equals("") || description.equals(" ") )) {
                Toast.makeText(getApplicationContext(),"Description field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!opinionCB.isChecked()) {
                description = " ";
            }
            point = pointCB.isChecked() ? "1" : "0";
            SharedPreferences preferences = getSharedPreferences("PointAndDescription", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("opinion", description);
            editor.putString("point", point);
            editor.apply();
            setResult(1112);
            finish();
        });
    }
}
