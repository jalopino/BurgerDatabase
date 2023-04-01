package com.example.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class edtOrderAct extends Activity implements AdapterView.OnItemSelectedListener {
    private Spinner burger;
    private static Button btnQuery;
    private static EditText fullname, qty;
    private static TextView tv_civ, defburger;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/UpdateQty.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    String[] StringStatus = new String[] {"Single", "Married", "Widow", "Divorced"};
    public static String String_isempty = "";
    public static final String FULLNAME = "FULLNAME";
    public static final String BURGER = "BURGER";
    public static final String QTY = "QTY";
    public static String ID = "ID";
    private String flnme, brgr, quantity, aydi;
    public static String FullName = "";
    public static String Burger = "";
    public static String Qty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edtorder_activity);
        fullname = (EditText) findViewById(R.id.fullname);
        burger = (Spinner) findViewById(R.id.burger);
        qty = (EditText) findViewById(R.id.qty);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        defburger = (TextView) findViewById(R.id.defburger);
        Intent i = getIntent();
        flnme = i.getStringExtra(FULLNAME);
        brgr = i.getStringExtra(BURGER);
        quantity = i.getStringExtra(QTY);
        aydi = i.getStringExtra(ID);
        fullname.setText(flnme);
        defburger.setText(brgr);
        qty.setText(quantity);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[]
                        {"Select a burger","Cheese Burger","Veggie",
                                "Cheesy Veggie","Chicken Overload",
                                "Double Patty", "Spicy Double Patty Overload"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        burger.setAdapter(adapter);
        burger.setSelection(0);
        burger.setOnItemSelectedListener(this);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Burger.equals("Select a burger")) {
                    Burger = brgr;
                }
                FullName = fullname.getText().toString();
                Qty = qty.getText().toString();
                new uploadDataToURL().execute();
            }
        });
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedText = parent.getItemAtPosition(position).toString();
        Burger = selectedText;
        Toast.makeText(this, "Selected:" + selectedText, Toast.LENGTH_SHORT).show();
    }
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtOrderAct.this);

        public uploadDataToURL() {}
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
                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + FullName + "' ";
                cv.put("fullname", cPostSQL);

                cPostSQL = " '" + Burger + "' ";
                cv.put("burger", cPostSQL);

                cPostSQL =  " '" + Qty + "' ";
                cv.put("qty", cPostSQL);


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
            AlertDialog.Builder alert = new AlertDialog.Builder(edtOrderAct.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {}
                Toast.makeText(edtOrderAct.this, s ,Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
