package com.thurmer.gevindbor_table;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

public class FindStandardActivity extends AppCompatActivity {
    ArrayList<String> standardsList;
    ArrayAdapter<String> adapter;
    ArrayList<String> itemList;
    ArrayList<String> foundList;
    ArrayList<Integer> rowList;

    String json;

    JSONArray jsonarray;
    JSONObject jsonobject;
    String cuttingDiameter;

    NumberFormat format;
    Number usrValue;
    String compareString;

    EditText findStandardET;
    Button findStandardBttn;
    ListView findStandardLV;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_standard);

        findStandardET = (EditText) findViewById(R.id.findStandardET);
        findStandardBttn = (Button) findViewById(R.id.findStandardBttn);
        findStandardLV = (ListView) findViewById(R.id.findStandardLV);

        standardsList = new ArrayList<>();
        itemList = new ArrayList<>();
        foundList = new ArrayList<>();
        rowList = new ArrayList<>();

        //Gets default localization format
        format = NumberFormat.getInstance();

        if (format instanceof DecimalFormat) {
            DecimalFormatSymbols sym = ((DecimalFormat) format).getDecimalFormatSymbols();
            compareString = "^[0-9]{1,2}(["+sym.getDecimalSeparator()+"][0-9]{1,2})?$";
        } else {
            compareString = "^[0-9]{1,2}([.,][0-9]{1,2})?$";
        }

        //Gathers standard names from the standards master json
        jsonarray = getJSONArray("0_Standards.json");
        try {
            for(int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                standardsList.add(jsonobject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                itemList);

        findStandardBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Flushes the lists
                itemList.clear();
                foundList.clear();
                rowList.clear();

                if(findStandardET.getText().toString().matches(compareString)) {
                    try {
                        usrValue = format.parse(findStandardET.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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

                                if((Math.abs(Double.valueOf(cuttingDiameter)-usrValue.doubleValue()) <= 0.000001)) {
                                    foundList.add(standardsList.get(i));
                                    rowList.add(j);
                                    itemList.add("["+standardsList.get(i) + "] " + jsonobject.getString("nominalDiameter"));
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        adapter.notifyDataSetChanged();
                        findStandardLV.setAdapter(adapter);

                        //Adds a clickListener to the items to bring information to the nex activity
                        findStandardLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View searchListV, int position, long id)
                            {
                                Intent intent = new Intent(getBaseContext(), StandardDataActivity.class);
                                intent.putExtra("selectedStandard", foundList.get(position));
                                intent.putExtra("rowIndex", rowList.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    findStandardET.setText("");
                    findStandardET.setError("Incorrect value");
                }
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
