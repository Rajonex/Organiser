package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBarArrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PaymentActivity.this, FirstScreenActivity.class));

            }

        });

//        ListView listStudents = (ListView)findViewById(R.id.list);
//        ArrayAdapter<String> adapter;
//
//
//        String[] students = {"Grupa 1", "Grupa 2", "Grupa 3"};
//        adapter = new ArrayAdapter<String>(listStudents.getContext(), android.R.layout.simple_list_item_1, students);
//        listStudents.setAdapter(adapter);


//        listStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
//                Intent appInfo = new Intent(PaymentActivity.this, AddPaymentActivity.class);
//                startActivity(appInfo);
//            }
//        });
    }
}
