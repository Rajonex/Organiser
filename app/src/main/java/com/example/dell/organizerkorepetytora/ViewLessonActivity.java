package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Date;

public class ViewLessonActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ImageButton buttonEdit;
    SharedPreferences teacherToken;
    String token;
    String topic;
    String description;
    long date;

    TextView lessonTopic;
    TextView lessonDescription;
    TextView lessonDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lesson);


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

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBarHomeEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonEdit = (ImageButton) findViewById(R.id.button_edit);



        lessonTopic = (TextView)  findViewById(R.id.lesson_topic);
        lessonDescription = (TextView) findViewById(R.id.lesson_data);
        lessonDate = (TextView) findViewById(R.id.lesson_date);
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

                startActivity(new Intent(ViewLessonActivity.this, FirstScreenActivity.class));

            }

        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                Intent appInfo = new Intent(ViewLessonActivity.this, LessonEditActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("topic", topic);
                bundle.putString("description", description);
                bundle.putLong("date", date);


                appInfo.putExtras(bundle);


                startActivity(appInfo);

            }

        });
    }


}
