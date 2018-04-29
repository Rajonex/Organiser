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

import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Student;
import utils.Adress;

public class ViewStudent extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    Toolbar toolbar;
    String name;
    String lastname;
    String phone;
    String email;
    String teacherToken;
    long studentId;
    EditText studentName;
    EditText studentLastname;
    EditText studentPhone;
    EditText studentEmail;
    ImageButton buttonHome;
    ImageButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_student);

        toolbar = (Toolbar)findViewById(R.id.appBarHomeSave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        lastname = bundle.getString("lastname");
        phone = bundle.getString("phone");
        email = bundle.getString("email");
        teacherToken = bundle.getString("token");
        studentId = bundle.getLong("id");







//        Bundle bundle = getIntent().getExtras();
//        title = bundle.getString("title");
//        text = bundle.getString("text");
//        teacherToken=bundle.getString("token");
//        noteId=bundle.getLong("id");
//
//
        initializeElements();
        initializeActions();
    }


    public void initializeElements()
    {
        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);

        studentName = (EditText) findViewById(R.id.student_name);
        studentLastname = (EditText) findViewById(R.id.student_lastname);
        studentPhone = (EditText) findViewById(R.id.student_phone);
        studentEmail = (EditText) findViewById(R.id.student_email);

        studentName.setText(name, TextView.BufferType.EDITABLE);
        studentLastname.setText(lastname, TextView.BufferType.EDITABLE);
        studentPhone.setText(phone, TextView.BufferType.EDITABLE);
        studentEmail.setText(email, TextView.BufferType.EDITABLE);

    }

    public void initializeActions()
    {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ViewStudent.this, First_Screen_Activity.class));

            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                String token = teacherToken.getString("token", "brak tokenu");
                String name = studentName.getText().toString();
                String lastname = studentLastname.getText().toString();
                String phone = studentPhone.getText().toString();
                String email = studentEmail.getText().toString();
                long id = studentId;

                Student student = new Student(id, name, lastname, phone, email, token, true);
                updateStudent(student);


            }

        });

    }


    private void updateStudent(Student student)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);


        Call<Ack> studentCall = studentRetrofitService.updateStudent(student);

        studentCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack != null) {

                    if (ack.isConfirm()) {
                        startActivity(new Intent(ViewStudent.this, Students_1b_Activity.class));
                    }
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }
}
