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
    String path, nominalDiameter, pitch;
    Float predrillingDiameter, predrillingFormingDiameter, minimumDiameter, maximumDiameter;
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
            predrillingDiameter = Float.valueOf(jsonobject.getString("predrillingDiameter"));
            if (jsonobject.has("predrillingFormingDiameter")) {
                predrillingFormingDiameter = Float.valueOf(jsonobject.getString("predrillingFormingDiameter"));
            }
            minimumDiameter = Float.valueOf(jsonobject.getString("minimumDiameter"));
            maximumDiameter = Float.valueOf(jsonobject.getString("maximumDiameter"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        standardNameTextView.setText("["+nominalDiameter+"] "+pitch+" mm");
        predrillingDiameterTextView.setText(Float.toString(predrillingDiameter));
        if (predrillingFormingDiameter != null) {
            predrillingFormingDiameterTextView.setText(Float.toString(predrillingFormingDiameter));
        } else {
            predrillingFormingDiameterTextView.setText("N/A");
        }
        minTextView.setText(Float.toString(minimumDiameter));
        maxTextView.setText(Float.toString(maximumDiameter));
    }
}
