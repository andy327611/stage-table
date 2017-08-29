package com.thurmer.gevindbor_table;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import java.util.ArrayList;

public class StandardsActivity extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standards);

        //Fills up an arrayList with the given standards' names to apply its adapter to the listView

        listView = (ListView) findViewById(R.id.list);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        listItems.add("M");
        listItems.add("MF");
        listItems.add("UNC");
        listItems.add("UNF");
        listItems.add("UNEF");
        listItems.add("G-Pipe");
        listItems.add("TR");
        listItems.add("W-WF(Rough)");
        listItems.add("W-WF(Fine)");

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //Makes the list items clickable to pass the standard to the next activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View searchListV, int position, long id)
            {
                Intent intent = new Intent(getBaseContext(), PitchActivity.class);
                intent.putExtra("selectedStandard", listView.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
    }
}
