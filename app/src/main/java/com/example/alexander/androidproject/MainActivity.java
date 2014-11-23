package com.example.alexander.androidproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    Button Submit;
    EditText longUrl;
    TextView shortUrl;
    static String api_address="https://www.googleapis.com/urlshortener/v1/url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Submit = (Button)findViewById(R.id.submit);
        longUrl = (EditText)findViewById(R.id.LongUrl);
        shortUrl = (TextView)findViewById(R.id.ShortUrl);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new URLShort().execute();
            }
        });
    }

    private class URLShort extends AsyncTask<String, String, JSONObject>{
        private ProgressDialog pDialog;
        String _longUrl;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Contacting Google Servers ...");
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
        String ShortUrl;
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json != null) {
                    ShortUrl = json.getString("id");
                    shortUrl.setText(ShortUrl);
                    pDialog.dismiss();
                } else {
                    shortUrl.setText("Network Error");
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
