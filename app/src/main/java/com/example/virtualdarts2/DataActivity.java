package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {
    TextView dataReadTV;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        data = getIntent().getStringExtra("file_data");
        if(data == null)
            data = "Nothing to see here";

        dataReadTV = (TextView) findViewById(R.id.viewReadDataTV);
        dataReadTV.setText(data);

    }
}