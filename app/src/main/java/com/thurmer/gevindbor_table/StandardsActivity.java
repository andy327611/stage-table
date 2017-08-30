package com.thurmer.gevindbor_table;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StandardsActivity extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    JSONArray jsonarray;
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standards);

        //Fills up an arrayList with the given standards' names to apply its adapter to the listView

        listView = (ListView) findViewById(R.id.list);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        //Lists all the standards based on the master json file info
        jsonarray = getJSONArray("0_Standards.json");

        try {
            for(int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                    listItems.add(jsonobject.getString("name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //Makes the list items clickable to pass the standard to the next activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View searchListV, int position, long id)
            {
                Intent intent = new Intent(getBaseContext(), PitchActivity.class);
                intent.putExtra("selectedStandard", listView.getItemAtPosition(position).toString());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    //converts the JSON to a string
    private JSONArray getJSONArray(String path) {
        String json = null;
        try {
            InputStream is = getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try{
            return new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
