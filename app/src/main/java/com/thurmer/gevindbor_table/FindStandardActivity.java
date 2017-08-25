package com.thurmer.gevindbor_table;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FindStandardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_standard);

        final EditText findStandardET = (EditText) findViewById(R.id.findStandardET);
        final Button findStandardBttn = (Button) findViewById(R.id.findStandardBttn);

        findStandardBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(findStandardET.getText().toString().matches("^[0-9]{1,2}([.,][0-9]{1,2})?$")) {

                    JSONArray jsonarray;
                    JSONObject jsonobject;

                    String cuttingDiameter;



                } else {
                    findStandardET.setText("");
                    findStandardET.setError("Incorrect value");
                }
            }
        });
    }

}
