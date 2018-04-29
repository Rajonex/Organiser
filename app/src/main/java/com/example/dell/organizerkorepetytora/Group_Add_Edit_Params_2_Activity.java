package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lesson.Add_Lesson_Screen_Activity;
import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Student;
import utils.Adress;

public class Group_Add_Edit_Params_2_Activity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    SharedPreferences teacherToken;
    String token;
    String name;
    double rate;
    EditText groupName;
    EditText groupRate;
    ListView listStudents;
    List<Student> listOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_add_edit_params_2);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        rate = bundle.getDouble("rate");
        token = bundle.getString("token");

       initializeElements();
       initializeActions();



    }


    public void initializeElements()
    {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");


        Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton)findViewById(R.id.button_home);

        groupName = (EditText) findViewById(R.id.add_group_name);
        groupRate = (EditText) findViewById(R.id.add_group_payment);

        groupName.setText(name, TextView.BufferType.EDITABLE);
        Double rateDouble = new Double(rate);
        groupRate.setText(rateDouble.toString(), TextView.BufferType.EDITABLE);

        listStudents = (ListView)findViewById(R.id.lesson_attendance_list);
        // listStudents.setOnItemClickListener(new CheckBoxClick());


        listOfStudents = new ArrayList<>();
        getAllStudents();
//        public Student(long id, String firstname, String lastname, String phone, String email, String teacherToken,
//        boolean activity) {
//        listOfStudents.add(new Student(1, "student", "nazwisko", "111", "aaaa", token, false));

        listStudents.setAdapter(new Group_Add_Edit_Params_2_Activity.CheckboxAdapter(listOfStudents));
        listStudents.setItemsCanFocus(false);
        listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void initializeActions()
    {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Group_Add_Edit_Params_2_Activity.this, First_Screen_Activity.class));

            }

        });
    }

    private void getAllStudents()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        // String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0";

        Call<List<Student>> studentCall = studentRetrofitService.getStudents(token);

        studentCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> students = response.body();
                if(students != null)
                {
                    if(students.size() > 1)
                    {
                        for(Student student : students)
                        {
                            listOfStudents.add(student);
                        }
//                        txtView.setText(students.get(0).getLastname() + "+" + students.get(1).getLastname());
                    }
                    else
                    {
                        //txtView.setText("No size");
                    }
                } else
                {
//                    txtView.setText("NULL list");
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
//                txtView.setText("Failure");
            }
        });
    }

    public class CheckboxAdapter extends BaseAdapter {

        private List<Student> students;
        ArrayList<Student> selectedStudents = new ArrayList<Student>();


        public CheckboxAdapter(List<Student> students)
        {
            this.students = students;
        }



        @Override
        public int getCount()
        {
            return students.size();
        }

        @Override
        public Student getItem(int position)
        {
            return students.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView==null)
            {
                convertView = getLayoutInflater().inflate(R.layout.checkbox_element, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.checkedTextView1);
            name.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());

//            CheckBox tv = (CheckBox)convertView.findViewById(R.id.checkedTextView1);
//            tv.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());
//
//            tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        selectedStudents.add(getItem(position));
//                    }
//
//                }
//            });
//
            return convertView;
        }


    }
}
