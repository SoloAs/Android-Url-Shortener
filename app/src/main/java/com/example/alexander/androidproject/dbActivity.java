package com.example.alexander.androidproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class dbActivity extends Activity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        lv = (ListView)findViewById(R.id.list);
        try {
            MyDBHelper dbHelper = MyDBHelper.getInstance(dbActivity.this);
            List<Link> links = dbHelper.getLinkDAO().getAllLinks();
            if (!links.isEmpty()){
                List<String> string_links = new ArrayList<String>();
                for(Link l : links)
                {
                    string_links.add(l.getText());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string_links);
                lv.setAdapter(adapter);
            }
            else  Toast.makeText(dbActivity.this, "db is empty", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.db, menu);
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
