package com.example.priyankasabhagani.assessment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName(); //logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] arrMainActivityList = {"one","two","three"};
        final ArrayAdapter adapterMainActivity = new ArrayAdapter(this,R.layout.main_listview,arrMainActivityList);
        ListView listView = (ListView) findViewById(R.id.main_activity_list);
        listView.setAdapter(adapterMainActivity);

        //to the next pg.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentToScrollingActivity = new Intent(getApplicationContext(),ScrollingActivity.class);
                intentToScrollingActivity.putExtra("pika pika","");
                startActivity(intentToScrollingActivity);
                String toast_popup = (String)adapterMainActivity.getItem(position);
                makeText(getApplicationContext(), toast_popup, LENGTH_SHORT).show();
            }
        });


    }
}
