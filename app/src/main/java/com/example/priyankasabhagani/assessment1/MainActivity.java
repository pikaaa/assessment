package com.example.priyankasabhagani.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] arrMainActivityList = {"one","two","three"};
        ArrayAdapter adapterMainActivity = new ArrayAdapter(this,R.layout.activity_main,arrMainActivityList);
        ListView listView = (ListView) findViewById(R.id.main_activity_list);
        listView.setAdapter(adapterMainActivity);
        setContentView(R.layout.activity_main);
    }
}
