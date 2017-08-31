package com.thurmer.gevindbor_table;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PitchActivity extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    String path, selectedStandard, nominalDiameter, threadsPrInch;
    Double pitch;

    int position;

    JSONArray jsonarray;
    JSONObject jsonobject;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);

        listView = (ListView) findViewById(R.id.list2);

        //Gets default localization format
        NumberFormat format = NumberFormat.getInstance();

        selectedStandard = getIntent().getStringExtra("selectedStandard");
        position = getIntent().getIntExtra("position", 0);

        path = (position + 1) + "_" + selectedStandard + ".json";

        //Fills an arrayList with the table's nominal diameter and pitch or threads per inch
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        try {
            jsonarray = getJSONArray(path);
            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                nominalDiameter = jsonobject.getString("nominalDiameter");
                if(jsonobject.has("pitch")) {
                    pitch = Double.valueOf(jsonobject.getString("pitch"));
                    listItems.add("["+nominalDiameter+"] "+format.format(pitch));
                } else if(jsonobject.has("threadsPrInch")) {
                    threadsPrInch = jsonobject.getString("threadsPrInch");
                    listItems.add("["+nominalDiameter+"] "+threadsPrInch);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //Adds a clickListener to the items to bring information to the nex activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View searchListV, int position, long id)
            {
                Intent intent = new Intent(getBaseContext(), StandardDataActivity.class);
                intent.putExtra("selectedStandard", selectedStandard);
                intent.putExtra("rowIndex", position);
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
