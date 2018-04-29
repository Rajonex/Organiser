package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class List_Student_Payment_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_student_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ListView listPayment = (ListView) findViewById(R.id.listPayment);
        ArrayAdapter<String> adapter;


        String[] groupsPayment = {"Grupa 1", "Grupa 2", "Grupa 3"};
        adapter = new ArrayAdapter<String>(listPayment.getContext(), android.R.layout.simple_list_item_1, groupsPayment);
        listPayment.setAdapter(adapter);
//
//
        listPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(List_Student_Payment_Activity.this, Payment_Screen_Activity.class);
                startActivity(appInfo);
            }
        });

        ImageButton buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(List_Student_Payment_Activity.this, First_Screen_Activity.class));

            }

        });
    }
}
