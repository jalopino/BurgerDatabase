package com.example.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class checkOutAct extends Activity {
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/insert.php";
    private static String TAG_MESSAGE = "message" , TAG_SUCCESS = "success";
    private static String online_dataset = "";
    Button order;
    TextView burgername, name;
    EditText qty;
    ImageView burgerimage;
    String fullname, burger, month, day, year, quantity;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);
        getActionBar().hide();
        Calendar calendar = Calendar.getInstance();
        name = findViewById(R.id.name);
        order = findViewById(R.id.confirm);
        burgername = findViewById(R.id.burgertext);
        qty = findViewById(R.id.qty);
        burgerimage = findViewById(R.id.burgerimage);
        Intent intent = getIntent();
        burger = intent.getStringExtra("burger");
        int value = intent.getIntExtra("value", 0);
        burgername.setText(burger);
        switch(value) {
            case 1:
                burgerimage.setImageResource(R.drawable.cheeseburger);
                break;
            case 2:
                burgerimage.setImageResource(R.drawable.cheesyveggie);
                break;
            case 3:
                burgerimage.setImageResource(R.drawable.chickenoverload);
                break;
            case 4:
                burgerimage.setImageResource(R.drawable.spicydoublepattyoverload);
                break;
            case 5:
                burgerimage.setImageResource(R.drawable.doublepatty);
                break;
            case 6:
                burgerimage.setImageResource(R.drawable.beefandchickenoverload);
                break;
        }
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = name.getText().toString();
                if ((calendar.get(Calendar.MONTH) + 1) < 10) {
                    month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
                } else {
                    month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                }
                if ((calendar.get(Calendar.DAY_OF_MONTH)) < 10) {
                    day = "0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                }
                year = String.valueOf(calendar.get(Calendar.YEAR));
                quantity = qty.getText().toString();
                if (quantity.equals("")) {
                    quantity = "0";
                    qty.setText(quantity);
                }
                new uploadDatatoURL().execute();
            }
        });
    }
    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(checkOutAct.this);

        public uploadDatatoURL() { }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = " '" + fullname + "' , '" + burger + "' , '" + quantity + "' , '" + day + "' , '" + month + "' , '" + year + "'  ";
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if(nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(checkOutAct.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(checkOutAct.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}