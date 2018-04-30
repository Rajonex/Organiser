package com.example.dell.organizerkorepetytora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lesson.ListGroupInLessonActivity;


public class FirstScreenActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        final Button buttonAddLesson = (Button) findViewById(R.id.button_lesson);
        final Button buttonPayment = (Button) findViewById(R.id.button_payment);
        final Button buttonGroups = (Button) findViewById(R.id.button_group);
        final Button buttonStudents = (Button) findViewById(R.id.button_student);
        final Button buttonNotes = (Button) findViewById(R.id.button_notes);
        final Button buttonLogOut = (Button) findViewById(R.id.button_log_out);

        buttonAddLesson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FirstScreenActivity.this, ListGroupInLessonActivity.class));
            }
        });

        buttonPayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FirstScreenActivity.this, ListStudentPaymentActivity.class));
            }
        });


        buttonGroups.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FirstScreenActivity.this, ListGroupActivity.class));
            }
        });

        buttonStudents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FirstScreenActivity.this, ListStudentsActivity.class));
            }
        });

        buttonNotes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FirstScreenActivity.this, ListNotesActivity.class));
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences teacherToken = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = teacherToken.edit();
                editor.clear();
                editor.commit();

                startActivity(new Intent(FirstScreenActivity.this, MainActivity.class));
            }
        });
    }


}