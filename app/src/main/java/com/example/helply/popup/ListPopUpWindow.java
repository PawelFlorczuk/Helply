package com.example.helply.popup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.helply.R;

public class ListPopUpWindow extends Activity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        final Button saveListBtn = findViewById(R.id.save_shopping_list);
        final EditText shoppingList = findViewById(R.id.shopping_list_edit_text);


        saveListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("ShoppingList", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("shopping_list", shoppingList.getText().toString());
                editor.apply();
                setResult(1110);
                finish();
            }
        });


    }


}
