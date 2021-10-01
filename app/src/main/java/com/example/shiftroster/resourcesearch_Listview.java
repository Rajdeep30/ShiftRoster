package com.example.shiftroster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class resourcesearch_Listview extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resourcesearch__listview);

        textView= findViewById(R.id.mainText);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("name");
        String date = bundle.getString("date");
        String shiftName = bundle.getString("shift");

        textView.setText("@"+message+" is on " + shiftName+" on "+ date);
    }
}