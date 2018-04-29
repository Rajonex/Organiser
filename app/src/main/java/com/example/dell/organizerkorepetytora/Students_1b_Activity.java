package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Note;
import sends.Student;
import utils.Adress;

public class Students_1b_Activity extends AppCompatActivity {

    ImageButton buttonHome;
    ImageButton buttonAdd;
    Toolbar toolbar;
    ListView listStudents;
    List<Student> listOfStudents;
    SharedPreferences teacherToken;
    String token;

    public static final String PREFS = "teacherToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_1b);

        toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeElements();
        initializeActions();


    }

    @Override
    protected void onStart() {
        super.onStart();
        listStudents.invalidateViews();
    }

    private void initializeElements() {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonAdd = (ImageButton) findViewById(R.id.button_add);

        listStudents = (ListView) findViewById(R.id.list);

        listOfStudents = new ArrayList<>();
        getAllStudents();
//
        listStudents.setAdapter(new StudentListAdapter(listOfStudents));


    }

    private void initializeActions() {

        listStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(Students_1b_Activity.this, ViewStudent.class);

                String name = ((Student) adapter.getItemAtPosition(position)).getFirstname();
                String lastName = ((Student) adapter.getItemAtPosition(position)).getLastname();
                String phone = ((Student) adapter.getItemAtPosition(position)).getPhone();
                String email = ((Student) adapter.getItemAtPosition(position)).getEmail();
                String token = ((Student) adapter.getItemAtPosition(position)).getTeacherToken();
                long id = ((Student) adapter.getItemAtPosition(position)).getId();

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("lastname", lastName);
                bundle.putString("phone", phone);
                bundle.putString("email", email);
                bundle.putString("token", token);
                bundle.putLong("id", id);
                appInfo.putExtras(bundle);

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                startActivity(appInfo);



            }
        });


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Students_1b_Activity.this, First_Screen_Activity.class));

            }

        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                startActivity(new Intent(Students_1b_Activity.this, Student_Add_Edit_2b_Activity.class));

            }

        });

    }

    private void getAllStudents()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        //String token = teacherToken;

        Call<List<Student>> studentCall = studentRetrofitService.getStudents(token);

        studentCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> students = response.body();
                if(students != null)
                {
                    for(Student student : students)
                    {

                        listOfStudents.add(student);
                        listStudents.invalidateViews();
                    }
                } else
                {
//
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                System.out.println("Blad studenta");
            }
        });
    }


    public class StudentListAdapter extends BaseAdapter {

        private List<Student> students;


        public StudentListAdapter(List<Student> students) {
            this.students = students;
        }


        @Override
        public int getCount() {
            return students.size();
        }

        @Override
        public Student getItem(int position) {
            return students.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_element_layout, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.list_row_view);

            name.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());

            return convertView;
        }
    }
}
