package com.example.helply.popup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.helply.R;

public class ChoosePartPopUpWindow extends Activity {
       private Spinner chooseSpinner;
       private Button saveBtn;
       private String province;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choose_part_pop_up_window);

            chooseSpinner = findViewById(R.id.choosePopUpWindowSpinner);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .6));

            String[] provinces = {"Choose your province","Dolnośląskie","Kujawsko-Pomorskie","Lubelskie",
                    "Lubuskie","Łódzkie", "Małopolskie", "Mazowieckie","Opolskie","Podkarpackie","Podlaskie",
                     "Pomorskie","Śląskie","Świętokrzyskie","Warmińsko-Mazurskie","Wielkopolskie","Zachodniopomorskie"};

            chooseSpinner.setAdapter(new ArrayAdapter<>(ChoosePartPopUpWindow.this,
                    android.R.layout.simple_spinner_dropdown_item, provinces));
            chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        province = provinces[position];
                        saveBtn.setEnabled(true);
                    } else {
                        saveBtn.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            saveBtn = findViewById(R.id.savePartOfTheCountryBtn);

            saveBtn.setOnClickListener(v -> {
                SharedPreferences preferences = getSharedPreferences("Province", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("province", province);
                editor.apply();
                setResult(1113);
                finish();
            });


        }

}
