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
    ArrayList<String> pathList;
    ArrayList<String> standardsList;
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

        pathList = new ArrayList<>();
        standardsList = new ArrayList<>();
        pathList.add("1_M.json");
        pathList.add("2_MF.json");
        pathList.add("3_UNC.json");
        pathList.add("4_UNF.json");
        pathList.add("5_UNEF.json");
        pathList.add("6_G-Pipe.json");
        pathList.add("7_TR.json");
        pathList.add("8_W-WF-Rough.json");
        pathList.add("9_W-WF-Fine.json");

        findStandardET = (EditText) findViewById(R.id.findStandardET);
        findStandardBttn = (Button) findViewById(R.id.findStandardBttn);
        findStandardLV = (ListView) findViewById(R.id.findStandardLV);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                standardsList);

        findStandardBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                standardsList.clear();
                if(findStandardET.getText().toString().matches("^[0-9]{1,2}([.][0-9]{1,2})?$")) {
                    usrValue = findStandardET.getText().toString();

                    for (Iterator<String> i = pathList.iterator(); i.hasNext();) {
                        String path = i.next();

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
                                    standardsList.add(jsonobject.getString("nominalDiameter"));
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
