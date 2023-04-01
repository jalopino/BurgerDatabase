package com.example.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;


public class manageOrderAct extends Activity {
    private static Button btnQuery;

    TextView textView, txtDefault, txtDefault_burger, txtDefault_qty, txtDefault_ID;
    private static EditText fullname;
    private static JSONParser jsonParser = new JSONParser();
    private static String urlHostFullname = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/SelectItemDetails.php";
    private static String urlHostDelete = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/delete.php";
    private static String urlHostBurger = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/selectBurger.php";
    private static String urlHostQty = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/selectQty.php";
    private static String urlHostID = "https://78c7-49-145-173-94.ngrok.io/burgerdatabase/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";


    //how to globalize string
    public static String wew = "";
    public static String gender = "";
    public static String civilstats = "";

    private String flnme, brgr, qty, aydi;

    String cItemSelected_fullname, cItemSelected_burger, cItemSelected_qty, cItemSelected_ID;
    ArrayAdapter<String> adapter_fullname;
    ArrayAdapter<String> adapter_burger;
    ArrayAdapter<String> adapter_qty;
    ArrayAdapter<String> adapter_ID;
    ArrayList<String> list_fullname;
    ArrayList<String> list_burger;
    ArrayList<String> list_qty;
    ArrayList<String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_title;
    Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_activity);
        btnQuery = (Button) findViewById(R.id.btnSearch);
        fullname = (EditText) findViewById(R.id.fullname);
        txtDefault = (TextView) findViewById(R.id.tv_default);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_burger = (TextView) findViewById(R.id.txt_burger);
        txtDefault_qty = (TextView) findViewById(R.id.txt_qty);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtDefault.setVisibility(View.GONE);
        txtDefault_burger.setVisibility(View.GONE);
        txtDefault_qty.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        Toast.makeText(manageOrderAct.this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cItemcode = fullname.getText().toString();
                new uploadDataToURL().execute();
                new Burger().execute();
                new Qty().execute();
                new id().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected_fullname = adapter_fullname.getItem(position);
                cItemSelected_burger = adapter_burger.getItem(position);
                cItemSelected_qty = adapter_qty.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);
                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " " + cItemSelected_fullname);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        txtDefault.setText(cItemSelected_fullname);
                        txtDefault_burger.setText(cItemSelected_burger);
                        txtDefault_qty.setText(cItemSelected_qty);
                        txtDefault_ID.setText(cItemSelected_ID);
                        flnme = txtDefault.getText().toString().trim();
                        brgr = txtDefault_burger.getText().toString().trim();
                        qty = txtDefault_qty.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();
                        Intent intent = new Intent(manageOrderAct.this, edtOrderAct.class);
                        intent.putExtra(edtOrderAct.FULLNAME, flnme);
                        intent.putExtra(edtOrderAct.BURGER, brgr);
                        intent.putExtra(edtOrderAct.QTY, qty);
                        intent.putExtra(edtOrderAct.ID, aydi);
                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected_fullname = adapter_fullname.getItem(position);
                cItemSelected_burger = adapter_burger.getItem(position);
                cItemSelected_qty = adapter_qty.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected_fullname);
                alert_confirm.setPositiveButton(R.string.msg2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefault_ID.setText(cItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
            }
        });
    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = " ", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(manageOrderAct.this);

        public uploadDataToURL() {
        }

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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostFullname, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(manageOrderAct.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                String wew = s;
                String str = wew;
                final String fnames[] = str.split("-");
                list_fullname = new ArrayList<String>(Arrays.asList(fnames));
                adapter_fullname = new ArrayAdapter<String>(manageOrderAct.this, android.R.layout.simple_list_item_1, list_fullname);
                listView.setAdapter(adapter_fullname);
                textView.setText(listView.getAdapter().getCount() + " " + "record(s) found.");
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Burger extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(manageOrderAct.this);

        public Burger() {
        }

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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostBurger, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
        protected void onPostExecute(String Gender) {
            super.onPostExecute(Gender);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(manageOrderAct.this);
            if (Gender != null) {
                if (isEmpty.equals("") && !Gender.equals("HTTPSERVER_ERROR")) { }
                String gender = Gender;
                String str = gender;
                final String Genders[] = str.split("-");
                list_burger = new ArrayList<String>(Arrays.asList(Genders));
                adapter_burger = new ArrayAdapter<String>(manageOrderAct.this,
                        android.R.layout.simple_list_item_1, list_burger);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Qty extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(manageOrderAct.this);

        public Qty() {
        }
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostQty, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(manageOrderAct.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) { }
                String CivitStat = CS;
                String str = CivitStat;
                final String Civs[] = str.split("-");
                list_qty= new ArrayList<String>(Arrays.asList(Civs));
                adapter_qty = new ArrayAdapter<String>(manageOrderAct.this,
                        android.R.layout.simple_list_item_1, list_qty);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(manageOrderAct.this);

        public id() {
        }
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostID, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(manageOrderAct.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(manageOrderAct.this, "Data selected", Toast.LENGTH_SHORT);
                String AYDI = aydi;
                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(manageOrderAct.this,
                        android.R.layout.simple_list_item_1, list_ID);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class delete extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(manageOrderAct.this);

        public delete() {
        }

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
                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostDelete, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(manageOrderAct.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(manageOrderAct.this, "Data Deleted", Toast.LENGTH_SHORT);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
