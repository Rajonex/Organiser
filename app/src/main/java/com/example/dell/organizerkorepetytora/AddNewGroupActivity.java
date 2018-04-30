package com.example.dell.organizerkorepetytora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rest.GroupRetrofitService;
import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Group;
import sends.GroupCalendar;
import sends.Student;
import utils.Adress;

public class AddNewGroupActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    SharedPreferences teacherToken;
    String token;

    ListView listStudents;
    List<Student> listOfStudents;

    ImageButton buttonHome;
    ImageButton buttonSave;

    EditText name;
    EditText payment;

    List<Student> studentInGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_group);


        initializeElements();
        initializeActions();

    }


    public void initializeElements() {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");


        Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);

        name = (EditText) findViewById(R.id.add_group_name);
        payment = (EditText) findViewById(R.id.add_group_payment);

        listStudents = (ListView)findViewById(R.id.lesson_attendance_list);


        listOfStudents = new ArrayList<>();
        getAllStudents();

        listStudents.setAdapter(new AddNewGroupActivity.CheckboxAdapter(listOfStudents, this));
        listStudents.setItemsCanFocus(false);
        listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    public void initializeActions() {

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddNewGroupActivity.this, FirstScreenActivity.class));

            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupName = name.getText().toString();
                String groupPayment = payment.getText().toString();
                List<Student> studentsInGroup = ((CheckboxAdapter)listStudents.getAdapter()).getCheckedStudents();
//                List<Student> studentsInGroup = (new CheckboxAdapter(listOfStudents)).getCheckedStudents();
                System.out.println(studentsInGroup);

                Group group = new Group(1L, groupName, studentsInGroup, Double.parseDouble(groupPayment), token, true, new ArrayList<GroupCalendar>());

                //addGroup(group);


            }

        });

    }

    private void addGroup(Group group)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

//        List<Student> students = new ArrayList<>();
//        students.add(new Student(1L, "Jarek", "Niemam", "0700", "jarekmail@mail.com", "e2e42a07-5508-33f8-b67f-5eb252581f6d", true));

       // Group group = new Group(1L, "GrupaTest", students, 35.0, "e2e42a07-5508-33f8-b67f-5eb252581f6d", true, new ArrayList<GroupCalendar>());

        Call<Ack> groupCall = groupRetrofitService.addGroup(group);

        groupCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if(ack.isConfirm())
                {
                    startActivity(new Intent(AddNewGroupActivity.this, ListGroupActivity.class));
                } else{
//                    txtView.setText("Nie dodano grupy, przyczyny - np. wymienieni studenci nalezacy do grupy nie istnieja, niepoprawny token nauczyciela lub inne");
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

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
//                        ((CheckboxAdapter)listStudents.getAdapter()).changeData(listOfStudents);
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


    static class ViewHolder
    {
        CheckBox checkBox;
        TextView text;
    }



    public class CheckboxAdapter extends BaseAdapter {

        private List<Student> students;
        ArrayList<Student> selectedStudents = new ArrayList<Student>();
        private Context context;

//        public void changeData(List<Student> list)
//        {
//            students = list;
//            selectedStudents.clear();
//            notifyDataSetChanged();
//        }

        public CheckboxAdapter(List<Student> students, Context context) {
            this.students = students;
            this.context = context;
        }

        public List<Student> getCheckedStudents()
        {
            return selectedStudents;
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

//        public boolean isChecked(int position) {
//            return students.get(position).;
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            ViewHolder viewHolder = new ViewHolder();

            if (rowView == null) {

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.checkbox_element, null);
//                convertView = getLayoutInflater().inflate(R.layout.checkbox_element, null);
//                rowView = getLayoutInflater().inflate(R.layout.checkbox_element, null);


                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkedTextView1);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);

                rowView.setTag(viewHolder);
            } else
            {
                viewHolder = (ViewHolder) rowView.getTag();
            }

//            viewHolder.checkBox.setChecked(students.get(position).checked);

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    selectedStudents.add(getItem(position));

                }
            });
//            TextView name = (TextView) convertView.findViewById(R.id.checkedTextView1);
//            name.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());

//
            return rowView;
        }
    }
}
