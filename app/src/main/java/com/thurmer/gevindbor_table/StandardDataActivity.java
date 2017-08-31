package com.thurmer.gevindbor_table;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class StandardDataActivity extends AppCompatActivity {
    String selectedStandard, path, nominalDiameter, threadsPrInch, hardness;
    Double  pitch, outerDiameter, predrillingDiameter, cuttingDiameter, formingDiameter, minimumDiameter, maximumDiameter;
    Integer rowIndex, inclination;

    JSONArray jsonarray;
    JSONObject jsonobject;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_data);

        //Gets default localization format
        NumberFormat format = NumberFormat.getInstance();

        //Gets extra data from the previous activity
        selectedStandard = getIntent().getStringExtra("selectedStandard");
        rowIndex = getIntent().getIntExtra("rowIndex", 0);

        //Gains access to the needed textViews
        TextView standardNameTV = (TextView)findViewById(R.id.standardNameTV);
        TextView inclinationTV = (TextView)findViewById(R.id.inclinationTV);
        TextView nominalDiameterTV = (TextView)findViewById(R.id.nominalDiameterTV);
        TextView pitchTitleTV = (TextView)findViewById(R.id.pitchTitleTV);
        TextView pitchTV = (TextView)findViewById(R.id.pitchTV);
        TextView threadsPrInchTitleTV = (TextView)findViewById(R.id.threadsPrInchTitleTV);
        TextView threadsPrInchTV = (TextView)findViewById(R.id.threadsPrInchTV);
        TextView outerDiameterTitleTV = (TextView) findViewById(R.id.outerDiameterTitleTV);
        TextView outerDiameterTV = (TextView) findViewById(R.id.outerDiameterTV);
        TextView innerDiameterTitleTV = (TextView) findViewById(R.id.innerDiameterTitleTV);
        TextView minTitleTV = (TextView) findViewById(R.id.minTitleTV);
        TextView maxTitleTV = (TextView)findViewById(R.id.maxTitleTV);
        TextView minTV = (TextView)findViewById(R.id.minTV);
        TextView maxTV = (TextView)findViewById(R.id.maxTV);
        TextView predrillingDiameterTV = (TextView)findViewById(R.id.predrillingDiameterTV);
        TextView cuttingDiameterTitleTV = (TextView) findViewById(R.id.cuttingDiameterTitleTV);
        TextView formingDiameterTitleTV = (TextView)findViewById(R.id.formingDiameterTitleTV);
        TextView cuttingDiameterTV = (TextView) findViewById(R.id.cuttingDiameterTV);
        TextView formingDiameterTV = (TextView)findViewById(R.id.formingDiameterTV);

        //Identifies the selected standard inside the master json file
        jsonarray = getJSONArray("0_Standards.json");
        try {
            for(int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                if(jsonobject.getString("name").equals(selectedStandard)) {
                    inclination = jsonobject.getInt("inclination");

                    //Displays hardness if present
                    if(jsonobject.has("hardness")) {
                        hardness = jsonobject.getString("hardness");
                        minTitleTV.setText(minTitleTV.getText() + " (" + hardness + ")");
                        maxTitleTV.setText(maxTitleTV.getText() + " (" + hardness + ")");
                    }

                    path = (i + 1) + "_" + selectedStandard + ".json";

                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Converts JSON file into string
        jsonarray = getJSONArray(path);

        //Extracts information from the JSON string and then hides or displays textViews accordingly
        try {
            jsonobject = jsonarray.getJSONObject(rowIndex);
            nominalDiameter = jsonobject.getString("nominalDiameter");

            if (jsonobject.has("pitch")) {
                pitch = Double.valueOf(jsonobject.getString("pitch"));
                pitchTV.setText(format.format(pitch));
            } else {
                pitchTitleTV.setVisibility(View.GONE);
                pitchTV.setVisibility(View.GONE);
            }

            if (jsonobject.has("threadsPrInch")) {
                threadsPrInch = jsonobject.getString("threadsPrInch");
                threadsPrInchTV.setText(String.valueOf(threadsPrInch));
            } else {
                threadsPrInchTitleTV.setVisibility(View.GONE);
                threadsPrInchTV.setVisibility(View.GONE);
            }

            if (jsonobject.has("outerDiameter")) {
                outerDiameter = Double.valueOf(jsonobject.getString("outerDiameter"));
                outerDiameterTV.setText(format.format(outerDiameter));
            } else {
                outerDiameterTitleTV.setVisibility(View.GONE);
                outerDiameterTV.setVisibility(View.GONE);
            }

            if (jsonobject.has("predrillingDiameter")) {
                predrillingDiameter = Double.valueOf(jsonobject.getString("predrillingDiameter"));
                predrillingDiameterTV.setText(format.format(predrillingDiameter));
            } else {
                predrillingDiameterTV.setVisibility(View.GONE);
            }

            if (jsonobject.has("cuttingDiameter")) {
                cuttingDiameter = Double.valueOf(jsonobject.getString("cuttingDiameter"));
                cuttingDiameterTV.setText(format.format(cuttingDiameter));
            } else {
                cuttingDiameterTitleTV.setVisibility(View.GONE);
                cuttingDiameterTV.setVisibility(View.GONE);
            }

            if (jsonobject.has("formingDiameter")) {
                formingDiameter = Double.valueOf(jsonobject.getString("formingDiameter"));
                formingDiameterTV.setText(format.format(formingDiameter));
            } else {
                formingDiameterTitleTV.setVisibility(View.GONE);
                formingDiameterTV.setVisibility(View.GONE);
            }

            if(jsonobject.has("minimumDiameter") && jsonobject.has("maximumDiameter")) {
                minimumDiameter = Double.valueOf(jsonobject.getString("minimumDiameter"));
                maximumDiameter = Double.valueOf(jsonobject.getString("maximumDiameter"));
                minTV.setText(format.format(minimumDiameter));
                maxTV.setText(format.format(maximumDiameter));
            } else {
                innerDiameterTitleTV.setVisibility(View.GONE);
                minTitleTV.setVisibility(View.GONE);
                maxTitleTV.setVisibility(View.GONE);
                minTV.setVisibility(View.GONE);
                maxTV.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        standardNameTV.setText(selectedStandard);
        inclinationTV.setText(String.valueOf(inclination) + "°");
        nominalDiameterTV.setText(nominalDiameter);
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
