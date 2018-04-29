package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Add_Payment_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button buttonZatwierdz = (Button)findViewById(R.id.buttonZatwierdz);

        buttonZatwierdz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Add_Payment_Activity.this, Payment_Screen_Activity.class));
            }
        });

        ImageButton buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Add_Payment_Activity.this, First_Screen_Activity.class));

            }

        });

    }
}
