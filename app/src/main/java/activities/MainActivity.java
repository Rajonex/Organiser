package activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import java.util.List;

import rest.CalendarRetrofitService;
import rest.TeacherRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.GroupCalendar;
import sends.Teacher;
import utils.Adress;
import utils.Day;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";
    public static final String PREFSTheme = "theme";

    ProgressDialog progressDialog;
    ProgressDialog progressDialogFirstRun;
//    TextView txtView;
    Button button_logIn;
    Button button_signUp;
    EditText ed1;
    EditText ed2;

    int[] styleThemeTab = {R.style.DarkTheme, R.style.DefaultTheme, R.style.MyThemeLight, R.style.MyTheme};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean flag = false;
        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        int themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);
        for(int i: styleThemeTab)
        {
            if(i == themeCode)
                flag = true;
        }
        if(flag) {
            setTheme(themeCode);
        }
//        SharedPreferences sharedPreferences = getSharedPreferences(PREFSTheme, 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("theme", R.style.DefaultTheme);
//        editor.commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize field of class before initialize actions of these elements
        initializeElements();
        // Initialize actions for fields of class (e.g. buttons, edittext etc)
        initializeActions(this);

//        setContentView(R.layout.activity_main);
//
//
//        // Initialize field of class before initialize actions of these elements
//        initializeElements();
//        // Initialize actions for fields of class (e.g. buttons, edittext etc)
//        initializeActions();





    }


    private void initializeElements() {
        button_logIn = (Button) findViewById(R.id.button_logIn);
        button_signUp = (Button) findViewById(R.id.button_signUp);
        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
//        txtView = (TextView) findViewById(R.id.textView9);
        SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
        String token = teacherToken.getString("token", "brak tokenu");

//        txtView.setText(token);



        progressDialogFirstRun = new ProgressDialog(MainActivity.this);
        progressDialogFirstRun.setIndeterminate(true);
        progressDialogFirstRun.setMessage("Ładowanie");
        progressDialogFirstRun.show();

        teacherToken = getSharedPreferences(PREFS, 0);
        String login = teacherToken.getString("login", null);
        String password = teacherToken.getString("password", null);

        Teacher firstLoginTeacher = new Teacher(login, password, "", "");
        getTeacherFirstRun(firstLoginTeacher);
    }

    private void initializeActions(Context context) {
        button_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String login = ed1.getText().toString();
                String password = ed2.getText().toString();
                Teacher loginTeacher = new Teacher(login, password, "", "");
                getTeacher(loginTeacher);

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Ładowanie");
                progressDialog.show();


                //deleteNote();
            }

        });


        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, AddUserActivity.class));
            }
        });
    }






    private void getCalendarForDay()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        CalendarRetrofitService calendarRetrofitService = retrofit.create(CalendarRetrofitService.class);

        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0";

        Day day = Day.FRIDAY;

        Call<List<GroupCalendar>> calendarCall = calendarRetrofitService.getCalendarByDate(token, day.toString());

        calendarCall.enqueue(new Callback<List<GroupCalendar>>() {
            @Override
            public void onResponse(Call<List<GroupCalendar>> call, Response<List<GroupCalendar>> response) {
                List<GroupCalendar> calendars = response.body();
                if(calendars != null)
                {
                    if(calendars.size() > 0)
                    {
//                        txtView.setText(calendars.get(0).getGroupId() + ":" + calendars.get(0).getDay());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupCalendar>> call, Throwable t) {

            }
        });
    }

    private void getWholeCalendar()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        CalendarRetrofitService calendarRetrofitService = retrofit.create(CalendarRetrofitService.class);

        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0";

        Call<List<GroupCalendar>> calendarCall = calendarRetrofitService.getTeachersCalendar(token);

        calendarCall.enqueue(new Callback<List<GroupCalendar>>() {
            @Override
            public void onResponse(Call<List<GroupCalendar>> call, Response<List<GroupCalendar>> response) {
                List<GroupCalendar> calendars = response.body();
                if(calendars != null)
                {
                    if(calendars.size() > 0)
                    {
//                        txtView.setText(calendars.get(0).getGroupId() + ":" + calendars.get(0).getDay());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupCalendar>> call, Throwable t) {

            }
        });
    }



    private void getTeacher(Teacher teacher) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        TeacherRetrofitService teacherRetrofitService = retrofit.create(TeacherRetrofitService.class);


        Call<Teacher> teacherCall = teacherRetrofitService.getTeacher(teacher.getLogin(), teacher.getPassword());

        teacherCall.enqueue(new Callback<Teacher>() {
            @Override
            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                progressDialog.dismiss();
                Teacher resultTeacher = response.body();
                if (resultTeacher != null) // in case if you insert correct name and password
                {
                    String token = resultTeacher.getToken();
                    SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = teacherToken.edit();
                    editor.putString("token", token);
                    editor.putString("login", resultTeacher.getLogin());
                    editor.putString("password", resultTeacher.getPassword());
                    editor.commit();

//                    txtView.setText(resultTeacher.getToken() + ":" + resultTeacher.getName() + resultTeacher.getLogin() + ":" + resultTeacher.getPassword());
                    startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));

                } else // if password or name or both are not correctly
                {
                    Toast.makeText(MainActivity.this, "Błędny login lub hasło", Toast.LENGTH_SHORT).show();
                }

            }


            // Fail, for example if you don't have access to Wi-Fi
            @Override
            public void onFailure(Call<Teacher> call, Throwable t) {
//                txtView.setText("Błąd, spróbuj ponownie");
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Błąd podczas łączenie z serwerem", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getTeacherFirstRun(Teacher teacher) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        TeacherRetrofitService teacherRetrofitService = retrofit.create(TeacherRetrofitService.class);


        Call<Teacher> teacherCall = teacherRetrofitService.getTeacher(teacher.getLogin(), teacher.getPassword());

        teacherCall.enqueue(new Callback<Teacher>() {
            @Override
            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                progressDialogFirstRun.dismiss();
                Teacher resultTeacher = response.body();
                if (resultTeacher != null) // in case if you insert correct name and password
                {
                    String token = resultTeacher.getToken();
                    SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = teacherToken.edit();
                    editor.putString("token", token);
                    editor.commit();

//                    txtView.setText(resultTeacher.getToken() + ":" + resultTeacher.getName() + resultTeacher.getLogin() + ":" + resultTeacher.getPassword());
                    startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));

                }


            }


            // Fail, for example if you don't have access to Wi-Fi
            @Override
            public void onFailure(Call<Teacher> call, Throwable t) {
//                txtView.setText("Błąd, spróbuj ponownie");
                progressDialogFirstRun.dismiss();
                Toast.makeText(MainActivity.this, "Błąd podczas łączenie z serwerem", Toast.LENGTH_SHORT).show();
            }
        });

    }

}