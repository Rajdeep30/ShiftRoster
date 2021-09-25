package com.example.shiftroster;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShiftSearchListView extends AppCompatActivity {

    ListView listview;
    TextView toptext;
    ArrayList<String> test = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiftsearch_listview);

        listview = findViewById(R.id.listview_shiftsearch);
        toptext = findViewById(R.id.toptext);

        ArrayList<String> urls = getIntent().getStringArrayListExtra("personName");

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("shiftName");
        String date = bundle.getString("date");

        toptext.setText("Resource having "+ message+ " Shift on "+ date + " are:");

        if (urls.size()!=0) {
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ShiftSearchListView.this,
                    android.R.layout.simple_list_item_1, urls);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            listview.setAdapter(myAdapter);
        }else
        {
            test.add("No Persons are in the selected shift");
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ShiftSearchListView.this,
                    android.R.layout.simple_list_item_1, test);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            listview.setAdapter(myAdapter);
        }
    }
}
