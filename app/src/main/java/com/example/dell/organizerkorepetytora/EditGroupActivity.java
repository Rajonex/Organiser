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
import sends.Student;
import utils.Adress;

public class EditGroupActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ImageButton buttonSave;
    SharedPreferences teacherToken;
    long id;
    String token;
    EditText groupName;
    EditText groupRate;
    ListView listStudents;
    List<Student> listOfStudents;
    List<Student> studendsInGroup;
    List<Student> selectedStudents;
    Group singleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");


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

        groupName = (EditText) findViewById(R.id.add_group_name);
        groupRate = (EditText) findViewById(R.id.add_group_payment);

        getGroup(this);
//        //TODO pobrac wszystkich studentow, zaznaczyc tych, ktorzy sa w danej grupie. Wykonac to najlepiej synchronicznie, zeby po pierwszym pobraniu wykonac drugie, a dopiero po drugim pobraniu stworzyc i odswiezyc widok


        listStudents = (ListView) findViewById(R.id.lesson_attendance_list);
        // listStudents.setOnItemClickListener(new CheckBoxClick());


//        listOfStudents = new ArrayList<>();
//        getAllStudents();
//        public Student(long id, String firstname, String lastname, String phone, String email, String teacherToken,
//        boolean activity) {
//        listOfStudents.add(new Student(1, "student", "nazwisko", "111", "aaaa", token, false));


    }

    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditGroupActivity.this, FirstScreenActivity.class));
            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (singleGroup != null) {
                    String name = groupName.getText().toString();
                    double rate = Double.parseDouble(groupRate.getText().toString());

                    //TODO zmienic kalendarz z singleGroup na nowy kalendarz
                    Group editedGroup = new Group(singleGroup.getId(), name, ((CheckboxAdapterEditGroup) listStudents.getAdapter()).getStudentsInGroup(), rate, token, true, singleGroup.getGroupCalendar());
                    editGroup(editedGroup);
                }
            }

        });


    }

    private void getGroup(Context context) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        Call<Group> groupCall = groupRetrofitService.getGroup(id, token);

        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Group group = response.body();
                if (group != null) {
                    singleGroup = group;

                    studendsInGroup = singleGroup.getStudents();
                    groupName.setText(singleGroup.getName(), TextView.BufferType.EDITABLE);
                    Double rateDouble = new Double(singleGroup.getRate());
                    groupRate.setText(rateDouble.toString(), TextView.BufferType.EDITABLE);

                    getAllStudents(context, studendsInGroup);
                } else {//TODO pop-up
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });
    }

    private void editGroup(Group group) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        Call<Ack> groupCall = groupRetrofitService.updateGroup(group);

        groupCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if (ack.isConfirm()) {
                    // Aktualizacja pomyslna
                    startActivity(new Intent(EditGroupActivity.this, ListGroupActivity.class));
                } else {
//                    txtView.setText("Nie dodano grupy, przyczyny - np. wymienieni studenci nalezacy do grupy nie istnieja, niepoprawny token nauczyciela lub inne");
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    private void getAllStudents(Context context, List<Student> studendsInGroup) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        // String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0";

        Call<List<Student>> studentCall = studentRetrofitService.getStudents(token);

        studentCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> students = response.body();
                if (students != null) {
                    listOfStudents = students;
                    listStudents.setAdapter(new EditGroupActivity.CheckboxAdapterEditGroup(listOfStudents, studendsInGroup, context));
                    listStudents.setItemsCanFocus(false);
                    listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
//                    txtView.setText("NULL list");
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
//                txtView.setText("Failure");
            }
        });
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }


    public class CheckboxAdapterEditGroup extends BaseAdapter {

        private List<Student> students;
        private List<Student> studentsInGroup;
        //ArrayList<Student> selectedStudents = new ArrayList<Student>();
        private Context context;

        public CheckboxAdapterEditGroup(List<Student> students, List<Student> studentsInGroup, Context context) {
            this.students = students;
            this.context = context;
            this.studentsInGroup = studentsInGroup;
        }

        public List<Student> getCheckedStudents() {
            return selectedStudents;
        }

        public List<Student> getStudentsInGroup() {
            return studentsInGroup;
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

            View rowView = convertView;

            AddGroupActivity.ViewHolder viewHolder = new AddGroupActivity.ViewHolder();

            if (rowView == null) {

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.checkbox_element, null);
                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkedTextView1);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (AddGroupActivity.ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new EditGroupActivity.CheckboxAdapterEditGroup.AdapterOnClickListener(viewHolder, position));

            if (studentsInGroup.contains(getItem(position))) {
                viewHolder.checkBox.setChecked(true);
            }
            viewHolder.text.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());

            return rowView;
        }

        private class AdapterOnClickListener implements View.OnClickListener {
            AddGroupActivity.ViewHolder viewHolder;
            int position;

            public AdapterOnClickListener(AddGroupActivity.ViewHolder viewHolder, int position) {
                this.viewHolder = viewHolder;
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    studendsInGroup.add(getItem(position));
                } else {
                    studendsInGroup.remove(getItem(position));
                }
            }
        }
    }
}
