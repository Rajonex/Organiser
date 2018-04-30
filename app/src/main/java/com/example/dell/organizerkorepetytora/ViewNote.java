package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import rest.NoteRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import utils.Adress;

public class ViewNote extends AppCompatActivity {

    Toolbar toolbar;
    String title;
    String text;
    String teacherToken;
    long noteId;
    TextView noteTitle;
    TextView noteContent;
    ImageButton buttonHome;
    ImageButton buttonDel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

//        toolbar = (Toolbar)findViewById(R.id.appBarHomeTrash);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        text = bundle.getString("text");
        teacherToken=bundle.getString("token");
        noteId=bundle.getLong("id");


    initializeElements();
    initializeActions();



}

    private void initializeElements() {

        toolbar = (Toolbar)findViewById(R.id.appBarHomeTrash);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonDel = (ImageButton) findViewById(R.id.button_trash);

        noteTitle = (TextView) findViewById(R.id.noteTitle);
        noteContent = (TextView) findViewById(R.id.noteContent);

        noteTitle.setText(title);
        noteContent.setText(text);


    }

    private void initializeActions() {


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ViewNote.this, FirstScreenActivity.class));

            }

        });

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteNote();


            }

        });
    }




        private void deleteNote()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);

        long id = noteId; // wazne do pobrania z wybranej notatki
        String token = teacherToken;//"a3b5af4d-4ed6-3497-a21a-6751fea9f7c0"; // do pobrania z SharedPreferences


        Call<Ack> noteCall = noteRetrofitService.deleteNote(id, token);

        noteCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack != null) {

                    if (ack.isConfirm()) {
                        startActivity(new Intent(ViewNote.this, ListNotesActivity.class));
                    }
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }
}
