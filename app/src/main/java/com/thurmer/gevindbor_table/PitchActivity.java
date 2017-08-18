package com.thurmer.gevindbor_table;

import android.content.Intent;
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

    String path;
    String selectedStandard;
    String nominalDiameter, pitch, threadsPrInch, hardness;

    Integer inclination;

    JSONArray jsonarray;
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedStandard = getIntent().getStringExtra("selectedStandard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);
        listView = (ListView) findViewById(R.id.list2);

        //Path selection
        switch(selectedStandard) {
            case "M":
                path = "1_M.json";
                inclination = 60;
                hardness = "6H";
                break;
            case "MF":
                path = "2_MF.json";
                inclination = 60;
                hardness = "6H";
                break;
            case "UNC":
                path = "3_UNC.json";
                inclination = 60;
                hardness = "2B";
                break;
            case "UNF":
                path = "4_UNF.json";
                inclination = 60;
                hardness = "2B";
                break;
            case "UNEF":
                path = "5_UNEF.json";
                inclination = 60;
                hardness = "2B";
                break;
            case "G-Pipe":
                path = "6_G-Pipe.json";
                inclination = 55;
                break;
            case "TR":
                path = "7_TR.json";
                inclination = 30;
                break;
            default:
                path = "";
                break;
        }

        //JSON to string
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

        //adapter
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        //adding items to adapter
        try {
            jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                nominalDiameter = jsonobject.getString("nominalDiameter");
                if(jsonobject.has("pitch")) {
                    pitch = jsonobject.getString("pitch");
                    listItems.add("["+nominalDiameter+"] "+pitch+" mm");
                }
                if(jsonobject.has("threadsPrInch")) {
                    threadsPrInch = jsonobject.getString("threadsPrInch");
                    listItems.add("["+nominalDiameter+"] "+threadsPrInch);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //clicklistener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View searchListV, int position, long id)
            {
                Intent intent = new Intent(getBaseContext(), StandardDataActivity.class);
                intent.putExtra("selectedStandard", selectedStandard);
                intent.putExtra("path", path);
                intent.putExtra("inclination", inclination);
                intent.putExtra("rowIndex", position);
                intent.putExtra("hardness", hardness);
                startActivity(intent);
            }
        });
    }
}
