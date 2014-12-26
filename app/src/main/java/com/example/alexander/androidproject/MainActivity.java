package com.example.alexander.androidproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    Button submit;
    Button save;
    Button db;
    Button clear;
    EditText longUrl;
    TextView shortUrl;
    Link newLink;

    static String api_address="https://www.googleapis.com/urlshortener/v1/url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button)findViewById(R.id.submit);
        save = (Button)findViewById(R.id.save);
        clear = (Button)findViewById(R.id.clearDb);
        db = (Button)findViewById(R.id.db);
        longUrl = (EditText)findViewById(R.id.LongUrl);
        shortUrl = (TextView)findViewById(R.id.ShortUrl);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new URLShort().execute();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newLink = new Link(longUrl.getText().toString() + '(' + shortUrl.getText().toString() + ')');
                    MyDBHelper dbHelper = MyDBHelper.getInstance(MainActivity.this);
                    dbHelper.getLinkDAO().create(newLink);
                    //MyDBHelper.getInstance(MainActivity.this).getLinkDAO().clear();
                    Toast.makeText(MainActivity.this, "Link added to db", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    MyDBHelper dbHelper = MyDBHelper.getInstance(MainActivity.this);
                    dbHelper.deleteDatabase("links.db");
                    Toast.makeText(MainActivity.this, "db cleared", Toast.LENGTH_SHORT).show();
            }
        });
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, dbActivity.class);
                    startActivity(intent);

            }
        });
        shortUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(shortUrl.toString()));
                internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
                internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(internetIntent);
            }
        });
    }

    private class URLShort extends AsyncTask<String, String, JSONObject>{
        private ProgressDialog pDialog; //вращающийся индикатор загрузки
        String _longUrl;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Connecting to Google Servers ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            _longUrl = longUrl.getText().toString();
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UrlShortener jParser = new UrlShortener();
            JSONObject json = jParser.getJSONFromUrl(api_address, _longUrl);
            return json;
        }
        String _shortUrl;
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json != null) {
                    _shortUrl = json.getString("id");
                    shortUrl.setText(_shortUrl);
                    pDialog.dismiss();
                } else {
                    shortUrl.setText("Error");
                    pDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
