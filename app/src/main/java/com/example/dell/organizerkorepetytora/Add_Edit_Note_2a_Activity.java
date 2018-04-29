package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import rest.NoteRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Note;
import utils.Adress;

public class Add_Edit_Note_2a_Activity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";


    ImageButton buttonHome;
    ImageButton buttonSave;
    EditText noteTitle;
    EditText noteContent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_note_2a);

        initializeElements();
        initializeActions();

    }

    private void initializeElements() {

        toolbar = (Toolbar)findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);
        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteContent = (EditText) findViewById(R.id.noteContent);

//        noteTitle.setText(title, TextView.BufferType.EDITABLE);
//        noteContent.setText(text, TextView.BufferType.EDITABLE);


    }

    private void initializeActions() {

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Add_Edit_Note_2a_Activity.this, First_Screen_Activity.class));

            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long id = 999; // doesn't metter, server ignore this number
                //String token = token; // do pobrania z SharedPreferences
                SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                String token = teacherToken.getString("token", "brak tokenu");
                String title = noteTitle.getText().toString();
                String text = noteContent.getText().toString();

                Note note = new Note(id, token, title, text);

                addNote(note);


            }

        });

    }


    private void addNote(Note note)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);



        Call<Ack> noteCall = noteRetrofitService.addNote(note);

        noteCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack != null)
                {
                    if(ack.isConfirm())
                    {
                        startActivity(new Intent(Add_Edit_Note_2a_Activity.this, List_Notes_Activity.class));
                    }
                    else
                    {

                    }
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });

    }
}
