package com.example.shiftroster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Spinner spinner_name, spinner_shift, spinner_date, spinner_leave;
    ListView shiftsearchlistview;
    Button btn_shift_search, btn_name_search, btn_leave_search;
    int day, month, YYYY;
    String csvfileString, leaveVar="Leave";
    int selectedDateIndex=1, selectedNameIndex =1, selectedShiftIndex = 1;
    String selectedName, selectedDate;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Integer> leaveIndex = new ArrayList<>();
    ArrayList<String> shift_search = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<ArrayList<String>> allRows = new ArrayList<>();


        shift_search.add("F3");
        shift_search.add("S2");
        shift_search.add("T7");
        shift_search.add("GS");
        shift_search.add("Leave");

        btn_name_search = findViewById(R.id.btn_name_search);
        btn_shift_search = findViewById(R.id.btn_shift_search);
        btn_leave_search = findViewById(R.id.btn_leave_search);
        spinner_name = findViewById(R.id.spinner_name);
        spinner_shift = findViewById(R.id.spinner_shift);
        spinner_date = findViewById(R.id.spinner_date);
        spinner_leave = findViewById(R.id.spinner_leave);
        shiftsearchlistview = findViewById(R.id.listview_shiftsearch);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.shifts));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_name.setAdapter(myAdapter);



        csvfileString = this.getApplicationInfo().dataDir + File.separatorChar + "October_shift_roster_2021.csv";

        try {
            AssetManager am = getAssets();
            InputStream inputStream = am.open("October_shift_roster_2021.csv");
            File csvfile = createFileFromInputStream(inputStream);
            CSVReader reader = new CSVReader(new FileReader(csvfile));
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                ArrayList<String> singleRow = new ArrayList<>();
                for (int i = 0; i <nextLine.length; i++) {
                    singleRow.add(nextLine[i]);
                }
                allRows.add(singleRow);
            }

            for(int i = 0; i<allRows.size(); i++)
            {
                dates.add(allRows.get(i).get(0));
            }
            Toast.makeText(this, "Welcome..." , Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "erro" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,allRows.get(0));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_shift.setAdapter(myAdapter1);

        ArrayAdapter<String> myAdapterLeave = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,allRows.get(0));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_leave.setAdapter(myAdapterLeave);


        ArrayAdapter<String> myAdapterDate = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,dates);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_date.setAdapter(myAdapterDate);






        btn_shift_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> personIndex = new ArrayList<>();
                ArrayList<String> personName = new ArrayList<>();
                selectedShiftIndex = spinner_name.getSelectedItemPosition();
                selectedDateIndex = spinner_date.getSelectedItemPosition();
                selectedDate = spinner_date.getSelectedItem().toString();

                if(selectedDateIndex != 0){
                for(int i=0; i<allRows.get(selectedDateIndex).size();i++)
                {
                    if(shift_search.get(selectedShiftIndex).equalsIgnoreCase(allRows.get(selectedDateIndex).get(i)))
                    {
                        personIndex.add(i);
                    }
                }

                for(Integer i: personIndex){

                    personName.add(allRows.get(0).get(i));
                    //Toast.makeText(MainActivity.this, "Persons having "+ shift_search.get(selectedShiftIndex) + " are: "+allRows.get(0).get(i), Toast.LENGTH_SHORT).show();
                }
               /* ArrayAdapter<String> myAdapterlistview = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1,personName);
                myAdapterlistview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                shiftsearchlistview.setAdapter(myAdapterlistview);*/
                    Intent intent = new Intent(MainActivity.this, ShiftSearchListView.class);
                    intent.putStringArrayListExtra("personName", personName);
                    intent.putExtra("shiftName", shift_search.get(selectedShiftIndex));
                    intent.putExtra("date", selectedDate);// getText() SHOULD NOT be static!!!
                    startActivity(intent);

                   //startActivity(new Intent(MainActivity.this,ShiftSearchListView.class));


                    //Toast.makeText(MainActivity.this, ": selected shift"+ shift_search.get(selectedShiftIndex)+"z,  searched in " +  allRows.get(selectedDateIndex), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Please Select a Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_leave_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveIndex.clear();
                ArrayList<String> leaveDates = new ArrayList<>();
                selectedName = spinner_leave.getSelectedItem().toString();
                selectedNameIndex = spinner_leave.getSelectedItemPosition();
                if (selectedNameIndex != 0){
                    for (int i= 0; i<allRows.size(); i++)
                    {
                        if (leaveVar.equalsIgnoreCase(allRows.get(i).get(selectedNameIndex)))
                        {
                            leaveIndex.add(i);
                        }
                    }

                    for (Integer i:leaveIndex)
                    {
                        leaveDates.add(allRows.get(i).get(0));
                        //Toast.makeText(MainActivity.this, "@"+selectedName+ " has leaves on: "+ allRows.get(i).get(0), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(MainActivity.this, LeaveSearchListView.class);
                    intent.putStringArrayListExtra("leaveDates", leaveDates);
                    intent.putExtra("name",selectedName);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "Please Select Resource Name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_name_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedName = spinner_shift.getSelectedItem().toString();
                selectedNameIndex = spinner_shift.getSelectedItemPosition();
                selectedDateIndex = spinner_date.getSelectedItemPosition();
                selectedDate = spinner_date.getSelectedItem().toString();
                    if (selectedDateIndex != 0 && selectedNameIndex != 0) {
                        Toast.makeText(MainActivity.this, "@"+selectedName+" is on " + allRows.get(selectedDateIndex).get(selectedNameIndex)+" on "+ selectedDate, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Please Select Date and Resource name", Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    private File createFileFromInputStream(InputStream inputStream) {
        try{
            File f = new File(""+csvfileString);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

   /* @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDatepicker() {

        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.datepicker, null);
        builder.setView(view);
        DatePicker datePicker = view.findViewById(R.id.dp_dp);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day = dayOfMonth;
                month = monthOfYear+1;
                YYYY= year;

            }
        });
        builder.setPositiveButton("Set Date", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                date.setText(day + "/" + month+ "/"+ YYYY);
            }
        });

        builder.create().show();
    }*/
}