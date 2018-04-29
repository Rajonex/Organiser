package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Date;

public class LessonEditActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    SharedPreferences teacherToken;
    String token;
    String topic;
    String description;
    long date;

    EditText lessonTopic;
    EditText lessonDescription;
    EditText lessonDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_edit);


        Bundle bundle = getIntent().getExtras();
        topic = bundle.getString("topic");
        description = bundle.getString("description");
        date = bundle.getLong("date");

        initializeElements();
        initializeActions();

    }

    public void initializeElements()
    {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBarHomeSave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton)findViewById(R.id.button_home);
//
//
//
        lessonTopic = (EditText)  findViewById(R.id.lesson_topic);
        lessonDescription = (EditText) findViewById(R.id.lesson_data);
        lessonDate = (EditText) findViewById(R.id.lesson_date);
        Date dateSql = new Date(date);
        lessonTopic.setText(topic, TextView.BufferType.EDITABLE);
        lessonDescription.setText(description, TextView.BufferType.EDITABLE);
        lessonDate.setText(dateSql.toString(), TextView.BufferType.EDITABLE);


    }

    public void initializeActions()
    {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LessonEditActivity.this, First_Screen_Activity.class));

            }

        });
    }


}
