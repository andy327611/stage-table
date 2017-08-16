package com.thurmer.gedvindbor_table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class StandardDataActivity extends AppCompatActivity {
    String path;
    String nominalDiameter, pitch;
    String predrillingDiameter;
    String predrillingFormingDiameter;
    String minimumDiameter;
    String maximumDiameter;
    Integer rowIndex;

    JSONArray jsonarray;
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_data);

        path = getIntent().getStringExtra("path");
        rowIndex = getIntent().getIntExtra("rowIndex", 0);

        TextView standardNameTextView = (TextView)findViewById(R.id.standardNameTextView);
        TextView predrillingDiameterTextView = (TextView)findViewById(R.id.predrillingDiameterTextView);
        TextView predrillingFormingDiameterTextView = (TextView)findViewById(R.id.predrillingFormingDiameterTextView);
        TextView minTextView = (TextView)findViewById(R.id.minTextView);
        TextView maxTextView = (TextView)findViewById(R.id.maxTextView);

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

        try {
            jsonarray = new JSONArray(json);
            jsonobject = jsonarray.getJSONObject(rowIndex);
            nominalDiameter = jsonobject.getString("nominalDiameter");
            pitch = jsonobject.getString("pitch");
            predrillingDiameter = jsonobject.getString("predrillingDiameter");
            predrillingFormingDiameter = jsonobject.getString("predrillingFormingDiameter");
            minimumDiameter = jsonobject.getString("minimumDiameter");
            maximumDiameter = jsonobject.getString("maximumDiameter");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        standardNameTextView.setText("["+nominalDiameter+"] "+pitch+" mm");
        predrillingDiameterTextView.setText(predrillingDiameter);
        predrillingFormingDiameterTextView.setText(predrillingFormingDiameter);
        minTextView.setText(minimumDiameter);
        maxTextView.setText(maximumDiameter);
    }
}
