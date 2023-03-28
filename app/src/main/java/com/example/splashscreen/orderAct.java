package com.example.splashscreen;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.splashscreen.SplashScreen;

import org.w3c.dom.Text;

public class orderAct extends Activity {
    ImageButton cheeseburg, cheesyveg, chknoverlod, dblepatty, beefchknoverlod, spcydblepattyoverload;
    TextView cheeseburgtxt, cheesyvegtxt, chknoverlodtxt, dblepattytxt, beefchknoverlodtxt, spcydblepattyoverlodtxt;
    String burger;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        getActionBar().hide();

        cheeseburg = findViewById(R.id.cheeseburger);
        cheesyveg = findViewById(R.id.cheesyveggie);
        chknoverlod = findViewById(R.id.chickenoverload);
        dblepatty = findViewById(R.id.doublepatty);
        beefchknoverlod = findViewById(R.id.beefandchickenoverload);
        spcydblepattyoverload = findViewById(R.id.spicydoublepattyoverload);
        cheeseburgtxt = findViewById(R.id.txtcheseburg);
        cheesyvegtxt = findViewById(R.id.txtcheseveg);
        chknoverlodtxt = findViewById(R.id.txtchickenoverload);
        dblepattytxt = findViewById(R.id.txtdoublepatty);
        beefchknoverlodtxt = findViewById(R.id.txtbeefandchickenoverload);
        spcydblepattyoverlodtxt = findViewById(R.id.txtspicydoublepattyoverload);

        cheeseburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = cheeseburgtxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 1);
                startActivity(intent);
            }
        });
        cheesyveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = cheesyvegtxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 2);
                startActivity(intent);
            }
        });
        chknoverlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = chknoverlodtxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 3);
                startActivity(intent);
            }
        });
        spcydblepattyoverload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = spcydblepattyoverlodtxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 4);
                startActivity(intent);
            }
        });
        dblepatty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = dblepattytxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 5);
                startActivity(intent);
            }
        });
        beefchknoverlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burger = beefchknoverlodtxt.getText().toString();
                Intent intent = new Intent(orderAct.this, checkOutAct.class);
                intent.putExtra("burger", burger);
                intent.putExtra("value", 6);
                startActivity(intent);
            }
        });
    }
}