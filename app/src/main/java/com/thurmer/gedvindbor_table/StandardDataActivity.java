package com.thurmer.gedvindbor_table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StandardDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_data);

        TextView standardNameTextView = (TextView)findViewById(R.id.standardNameTextView);
        standardNameTextView.setText(getIntent().getStringExtra("selectedStandard"));
    }
}
