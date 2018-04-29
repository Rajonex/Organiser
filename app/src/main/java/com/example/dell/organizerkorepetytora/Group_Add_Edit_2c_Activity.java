package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

import rest.GroupRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Group;
import sends.Student;
import utils.Adress;

public class Group_Add_Edit_2c_Activity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    SharedPreferences teacherToken;
    String token;
    Group singleGroup;

    Button buttonEditLesson;
    Button buttonShowLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_add_edit_2c);

        initializeElements();
        initializeActions();

    }

    public void initializeElements()
    {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        buttonShowLesson = (Button)findViewById(R.id.button_show);
        buttonEditLesson = (Button)findViewById(R.id.button_edit);
    }

    public void initializeActions()
    {
        buttonShowLesson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();
                startActivity(new Intent(Group_Add_Edit_2c_Activity.this, List_Lessons_Screen_Activity.class));
            }
        });


        buttonEditLesson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                getGroup();

            }
        });
    }


    private void getGroup()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        long id = 1L;
        //String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<Group> groupCall = groupRetrofitService.getGroup(id, token);

        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Group group = response.body();
                if(group != null)
                {
                        singleGroup = group;

                    String name = singleGroup.getName();
                    List<Student> studentList = singleGroup.getStudents();
                    double rate = singleGroup.getRate();

                    Intent appInfo = new Intent(Group_Add_Edit_2c_Activity.this, Group_Add_Edit_Params_2_Activity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putDouble("rate", rate);
                    //bundle.putList<Student>

                    appInfo.putExtras(bundle);


                    startActivity(appInfo);
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });
    }
}
