package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnGoToNearlyBin = findViewById(R.id.btnGoToNearlyBin); // あなたのボタンのIDに置き換えてください

        btnGoToNearlyBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NearlyBinActivity.class);
                startActivity(intent);
            }
        });
    }
}