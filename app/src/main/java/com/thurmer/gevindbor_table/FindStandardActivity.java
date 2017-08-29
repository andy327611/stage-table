package com.thurmer.gevindbor_table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class FindStandardActivity extends AppCompatActivity {
    ArrayList<String> standardsList;
    ArrayList<String> nominalList;
    ArrayAdapter<String> adapter;

    String json;

    JSONArray jsonarray;
    JSONObject jsonobject;
    String cuttingDiameter;
    String usrValue;

    EditText findStandardET;
    Button findStandardBttn;
    ListView findStandardLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_standard);

        standardsList = new ArrayList<>();
        nominalList = new ArrayList<>();
        standardsList.add("M");
        standardsList.add("MF");
        standardsList.add("UNC");
        standardsList.add("UNF");
        standardsList.add("UNEF");
        standardsList.add("G-Pipe");
        standardsList.add("TR");
        standardsList.add("W-WF(Rough)");
        standardsList.add("W-WF(Fine)");

        findStandardET = (EditText) findViewById(R.id.findStandardET);
        findStandardBttn = (Button) findViewById(R.id.findStandardBttn);
        findStandardLV = (ListView) findViewById(R.id.findStandardLV);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                nominalList);

        findStandardBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nominalList.clear();
                if(findStandardET.getText().toString().matches("^[0-9]{1,2}([.][0-9]{1,2})?$")) {
                    usrValue = findStandardET.getText().toString();

                    for (int i = 0; i < standardsList.size(); i++) {
                        try {
                            InputStream is = getAssets().open((i+1)+"_"+standardsList.get(i)+".json");
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();
                            json = new String(buffer, "UTF-8");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            jsonarray = new JSONArray(json);
                            for (int j = 0; j < jsonarray.length(); j++) {
                                jsonobject = jsonarray.getJSONObject(j);
                                if(jsonobject.has("cuttingDiameter")) {
                                    cuttingDiameter = jsonobject.getString("cuttingDiameter");
                                } else if (jsonobject.has("predrillingDiameter")) {
                                    cuttingDiameter = jsonobject.getString("predrillingDiameter");
                                }

                                if((Math.abs(Double.valueOf(cuttingDiameter)-Double.valueOf(usrValue)) <= 0.000001)) {
                                    nominalList.add("["+standardsList.get(i) + "] " + jsonobject.getString("nominalDiameter"));
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        adapter.notifyDataSetChanged();
                        findStandardLV.setAdapter(adapter);
                    }
                } else {
                    findStandardET.setText("");
                    findStandardET.setError("Incorrect value");
                }
            }
        });
    }

}
