package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rest.CalendarRetrofitService;
import rest.GroupRetrofitService;
import rest.LessonRetrofitService;
import rest.NoteRetrofitService;
import rest.StudentRetrofitService;
import rest.TeacherRetrofitService;
import sends.Ack;
import rest.AckRetrofitService;
import rest.Util;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Group;
import sends.GroupCalendar;
import sends.Lesson;
import sends.MiniGroup;
import sends.MiniLesson;
import sends.Note;
import sends.ResultTeacher;
import sends.Student;
import sends.StudentPresent;
import sends.Teacher;
import utils.Adress;
import utils.Day;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    TextView txtView;
    Button button_logIn;
    Button button_signUp;
    EditText ed1;
    EditText ed2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize field of class before initialize actions of these elements
        initializeElements();
        // Initialize actions for fields of class (e.g. buttons, edittext etc)
        initializeActions();

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
        txtView = (TextView) findViewById(R.id.textView9);
        SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
        String token = teacherToken.getString("token", "brak tokenu");

        txtView.setText(token);
    }

    private void initializeActions() {
        button_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTeacher();



                //deleteNote();
            }

        });


        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addTeacher();
                //addNote();


                addLesson();
//                getLesson();
//                getGroupLessons();
//                getAllStudents();
//                getCalendarForDay();
//                getWholeCalendar();
//                updateStudent();
//                addStudent();
//                getGroup();
//                getMiniGroups();
//                addGroup();
//            startActivity(new Intent(MainActivity.this, Add_User_Activity.class));
            }
        });
    }

    private void addLesson()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 11, 10);

        Lesson lesson = new Lesson(1L, new ArrayList<StudentPresent>(), "Temat z telefonu", "Treść z telefonu", calendar.getTimeInMillis(), 1L, "e2e42a07-5508-33f8-b67f-5eb252581f6d"); // do stworzenia cala lekcja

        Call<Ack> lessonCall = lessonRetrofitService.addLesson(lesson);

        lessonCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack != null)
                {
                    if(ack.isConfirm())
                    {
                        txtView.setText("Dodano lekcje");
                    }
                    else
                    {
                        txtView.setText("Nie dodano lekcji");
                    }
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    private void getLesson()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        long lessonId = 7L;
        String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<Lesson> lessonCall = lessonRetrofitService.getLesson(lessonId, token);

        lessonCall.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                Lesson lesson = response.body();
                if(lesson != null)
                {
                    txtView.setText(lesson.getTopic() + " : " + lesson.getDescription());
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });
    }

    private void getGroupLessons()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        long groupId = 1;
        String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<List<MiniLesson>> lessonCall = lessonRetrofitService.getGroupLessons(groupId, token);

        lessonCall.enqueue(new Callback<List<MiniLesson>>() {
            @Override
            public void onResponse(Call<List<MiniLesson>> call, Response<List<MiniLesson>> response) {
                List<MiniLesson> miniLessons = response.body();
                if(miniLessons != null)
                {
                    if(miniLessons.size() > 1)
                    {
                        txtView.setText(miniLessons.get(0).getTopic() + ":" + miniLessons.get(1).getTopic());
                    }
                    else
                    {
                        txtView.setText("Size < 1");
                    }
                }
                else
                {
                    txtView.setText("NULL");
                }
            }

            @Override
            public void onFailure(Call<List<MiniLesson>> call, Throwable t) {
                txtView.setText("Failure");
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
                        txtView.setText(calendars.get(0).getGroupId() + ":" + calendars.get(0).getDay());
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
                        txtView.setText(calendars.get(0).getGroupId() + ":" + calendars.get(0).getDay());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupCalendar>> call, Throwable t) {

            }
        });
    }

    private void getAllStudents()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0";

        Call<List<Student>> studentCall = studentRetrofitService.getStudents(token);

        studentCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> students = response.body();
                if(students != null)
                {
                    if(students.size() > 1)
                    {
                        txtView.setText(students.get(0).getLastname() + "+" + students.get(1).getLastname());
                    }
                    else
                    {
                        txtView.setText("No size");
                    }
                } else
                {
                    txtView.setText("NULL list");
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                txtView.setText("Failure");
            }
        });
    }

    private void updateStudent()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        Student student = new Student(2L, "Jarek", "Temp", "0700", "jarekmail@mail.com", "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0", true);

        Call<Ack> studentCall = studentRetrofitService.updateStudent(student);

        studentCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack.isConfirm())
                {
                    txtView.setText("Zaktualizowano studenta");
                }
                else
                {
                    txtView.setText("Nie udalo sie zaktualizowac studenta");
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    private void addStudent()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        Student student = new Student(1L, "Jarek", "Niemam", "0700", "jarekmail@mail.com", "e2e42a07-5508-33f8-b67f-5eb252581f6d", true);

        Call<Ack> studentCall = studentRetrofitService.addStudent(student);

        studentCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack.isConfirm())
                {
                    txtView.setText("Dodano studenta");
                }else
                {
                    txtView.setText("Nie dodano studenta");
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });

    }

    private void getGroup()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        long id = 1L;
        String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<Group> groupCall = groupRetrofitService.getGroup(id, token);

        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Group group = response.body();
                if(group != null)
                {
                    txtView.setText(group.getName() + ":" + group.getRate());
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });
    }


    private void getMiniGroups()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        String token = "d56b6998-30e7-3ba5-b855-679cb1d252da";

        Call<List<MiniGroup>> groupCall = groupRetrofitService.getMiniGroups(token);

        groupCall.enqueue(new Callback<List<MiniGroup>>() {
            @Override
            public void onResponse(Call<List<MiniGroup>> call, Response<List<MiniGroup>> response) {
                List<MiniGroup> miniGroupList = response.body();
                if(miniGroupList != null)
                {
                    if(miniGroupList.size() > 0) // test
                        txtView.setText(miniGroupList.get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<MiniGroup>> call, Throwable t) {

            }
        });
    }

    private void addGroup()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Jarek", "Niemam", "0700", "jarekmail@mail.com", "e2e42a07-5508-33f8-b67f-5eb252581f6d", true));

        Group group = new Group(1L, "GrupaTest", students, 35.0, "e2e42a07-5508-33f8-b67f-5eb252581f6d", true, new ArrayList<GroupCalendar>());

        Call<Ack> groupCall = groupRetrofitService.addGroup(group);

        groupCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack.isConfirm())
                {
                    txtView.setText("Dodano grupe poprawnie");
                } else{
                    txtView.setText("Nie dodano grupy, przyczyny - np. wymienieni studenci nalezacy do grupy nie istnieja, niepoprawny token nauczyciela lub inne");
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    private void getTeacher() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        TeacherRetrofitService teacherRetrofitService = retrofit.create(TeacherRetrofitService.class);

        String name = ed1.getText().toString();
        String password = ed2.getText().toString();

        Call<Teacher> teacherCall = teacherRetrofitService.getTeacher(name, password);

        teacherCall.enqueue(new Callback<Teacher>() {
            @Override
            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                Teacher resultTeacher = response.body();
                if (resultTeacher != null) // in case if you insert correct name and password
                {
                    String token = resultTeacher.getToken();
                    SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = teacherToken.edit();
                    editor.putString("token", token);
                    editor.commit();

                    txtView.setText(resultTeacher.getToken() + ":" + resultTeacher.getName());
                    startActivity(new Intent(MainActivity.this, First_Screen_Activity.class));

                } else // if password or name or both are not correctly
                {
                    txtView.setText("Błędny login lub hasło");
                }
            }


            // Fail, for example if you don't have access to Wi-Fi
            @Override
            public void onFailure(Call<Teacher> call, Throwable t) {
//                txtView.setText("Błąd, spróbuj ponownie");
                txtView.setText(t.getMessage());
            }
        });

    }

//    private void deleteNote()
//    {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();
//
//        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);
//
//        long id = 3; // wazne do pobrania z wybranej notatki
//        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0"; // do pobrania z SharedPreferences
//
//
//        Call<Ack> noteCall = noteRetrofitService.deleteNote(id, token);
//
//        noteCall.enqueue(new Callback<Ack>() {
//            @Override
//            public void onResponse(Call<Ack> call, Response<Ack> response) {
//                Ack ack = response.body();
//                if(ack != null)
//                {
//                    if(ack.isConfirm())
//                    {
//                        txtView.setText("Usunieto notatke");
//                    }
//                    else
//                    {
//                        txtView.setText("Nie usunieto notatki");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Ack> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void addNote()
//    {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();
//
//        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);
//
//        long id = 999; // doesn't metter, server ignore this number
//        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0"; // do pobrania z SharedPreferences
//        String title = "Tytul";
//        String text = "Tekst";
//
//        Note note = new Note(id, token, title, text);
//
//        Call<Ack> noteCall = noteRetrofitService.addNote(note);
//
//        noteCall.enqueue(new Callback<Ack>() {
//            @Override
//            public void onResponse(Call<Ack> call, Response<Ack> response) {
//                Ack ack = response.body();
//                if(ack != null)
//                {
//                    if(ack.isConfirm())
//                    {
//                        txtView.setText("Dodano notatke");
//                    }
//                    else
//                    {
//                        txtView.setText("Nie udalo sie dodac notatki");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Ack> call, Throwable t) {
//
//            }
//        });

//    }

//    private void getNotes()
//    {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();
//
//        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);
//
    //        String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0"; // do pobrania z SharedPreferences
//
//        Call<List<Note>> noteCall = noteRetrofitService.getNotes(token);
//
//        noteCall.enqueue(new Callback<List<Note>>() {
//            @Override
//            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//                List<Note> notes = response.body();
//                if(notes != null)
//                {
//                    for(Note note : notes)
//                    {
//                        System.out.println(note.getText());
//                    }
//                }
//                else
//                {
//                    System.out.println("Null");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Note>> call, Throwable t) {
//                System.out.println("Blad notatki");
//            }
//        });
//    }






    // testy. Nie do projektu!!!!!!!!!!!!
    private void sendAcks() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.102:8080/Server_Organiser/rest/").addConverterFactory(GsonConverterFactory.create()).build();

        AckRetrofitService ackRetrofitService = retrofit.create(AckRetrofitService.class);

        List<Ack> ackList = new ArrayList<>();
        ackList.add(new Ack("Pierwsze info", true));
        ackList.add(new Ack("Drugie info", false));

        Call<Ack> ackCall = ackRetrofitService.sendAcks(ackList);

        ackCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ackResponse = response.body();
                if (ackResponse != null) {
                    txtView.setText(ackResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    // to jest do testow!!!!!!!!!!!!!!!!! Nie wykorzystywane w projekcie
    private void refreshAckRetrofit() {
        Util.appendToLog(txtView, "Pobieranie danych za pomocą Retrofit");
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint("http://192.168.1.102:8080/Server_Organiser/rest")
//                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.102:8080/Server_Organiser/rest/").addConverterFactory(GsonConverterFactory.create()).build();

        AckRetrofitService ackRetrofitService = retrofit.create(AckRetrofitService.class);

        Call<List<Ack>> listCall = ackRetrofitService.getAll();

        listCall.enqueue(new Callback<List<Ack>>() {
            @Override
            public void onResponse(Call<List<Ack>> call, Response<List<Ack>> response) {
                List<Ack> ackList = response.body();

                if (ackList != null) {
                    txtView.setText("");
                    for (Ack ack : ackList) {
                        Util.appendToLog(txtView, ack.getMessage());
                    }
                } else {
                    ed2.setText("NULL?");
                }
            }

            @Override
            public void onFailure(Call<List<Ack>> call, Throwable t) {

            }
        });

    }


//        Response<List<Ack>> listResponse = null;
//        try {
//            listResponse = listCall.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<Ack> ackList = listResponse.body();
//
//        txtView.setText("");
//        for (Ack ack : ackList) {
//            Util.appendToLog(txtView, ack.getMessage());
//        }


//        AckRetrofitService ackService = restAdapter.create(AckRetrofitService.class);
//
//        ackService.getAll(new Callback<List<Ack>>() {
//            @Override
//            public void success(List<Ack> acks, Response response) {
//                Util.appendToLog(txtView, "Pobrano. Odświeżanie spinnera...");
//                ed1.setText(acks.get(0).getMessage());
//                txtView.setText("");
//                for(Ack ack : acks)
//                {
//                    Util.appendToLog(txtView, ack.getMessage());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ed1.setText(error.getMessage());
//                txtView.setText(error.getMessage());
//            }
//        });
}