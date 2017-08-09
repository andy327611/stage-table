package com.thurmer.gedvindbor_table;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StandardsActivity extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standards);
        listView = (ListView) findViewById(R.id.list);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        listItems.add("M");
        listItems.add("MF");
        listItems.add("UNC");
        listItems.add("UNF");

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
