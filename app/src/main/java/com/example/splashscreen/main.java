package com.example.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.splashscreen.SplashScreen;

public class main extends Activity {
    Button order, edtorder;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.main_activity);
        order = findViewById(R.id.order);
        edtorder = findViewById(R.id.edtorder);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main.this, orderAct.class);
                startActivity(intent);
            }
        });
        edtorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main.this, manageOrderAct.class);
                startActivity(intent);
            }
        });
    }
}
