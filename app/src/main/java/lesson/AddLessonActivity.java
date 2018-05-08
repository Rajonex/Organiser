package lesson;

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

import com.example.dell.organizerkorepetytora.FirstScreenActivity;
import com.example.dell.organizerkorepetytora.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rest.GroupRetrofitService;
import rest.LessonRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Group;
import sends.Lesson;
import sends.Student;
import sends.StudentPresent;
import utils.Adress;


public class AddLessonActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ImageButton buttonSave;
    ListView listStudents;
    List<Student> listOfStudents;
    Group singleGroup;
    Context context;
    SharedPreferences teacherToken;
    String token;
    EditText lessonTopic;
    EditText lessonDescription;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lesson);

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
        lessonTopic = (EditText) findViewById(R.id.lesson_topic);
        lessonDescription = (EditText) findViewById(R.id.lesson_description);

        listStudents = (ListView) findViewById(R.id.lesson_attendance_list);
        // listStudents.setOnItemClickListener(new CheckBoxClick());

        getGroup(this);


    }

    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddLessonActivity.this, FirstScreenActivity.class));

            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(singleGroup!=null && listStudents.getAdapter()!=null) {

                    String topic = lessonTopic.getText().toString();
                    String description = lessonDescription.getText().toString();

                    Lesson lesson = new Lesson(1L, ((CheckboxAdapter) listStudents.getAdapter()).getSelectedStudents(), topic, description, 2L, singleGroup.getId(), token);

                    addLesson(lesson);
                }
                else{
                    //TODO pop up (nie pobrano danych grupy)
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
                System.out.println("test");
                Group group = response.body();
                if (group != null) {
                    System.out.println("grupa nie jest nullem");
                    singleGroup = group;

                    listStudents.setAdapter(new CheckboxAdapter(singleGroup.getStudents(), context));
                    listStudents.setItemsCanFocus(false);
                    listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                } else {//TODO pop-up
                    System.out.println("grupa jest nullem");
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });
    }

    private void addLesson(Lesson lesson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 11, 10);

        //Lesson lesson = new Lesson(1L, new ArrayList<StudentPresent>(), "Temat z telefonu", "Treść z telefonu", calendar.getTimeInMillis(), 1L, "e2e42a07-5508-33f8-b67f-5eb252581f6d"); // do stworzenia cala lekcja

        Call<Ack> lessonCall = lessonRetrofitService.addLesson(lesson);

        lessonCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if (ack != null) {
                    if (ack.isConfirm()) {
                        startActivity(new Intent(AddLessonActivity.this, ListGroupInLessonActivity.class));
                    } else {
//                        txtView.setText("Nie dodano lekcji");
                    }
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {

            }
        });
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }


    public class CheckboxAdapter extends BaseAdapter {

//        private List<Student> students;
        ArrayList<StudentPresent> selectedStudents;
        private Context context;

        public CheckboxAdapter(List<Student> students, Context context) {
//            this.students = students;
            selectedStudents = new ArrayList<StudentPresent>();
            for(Student student : students)
            {
                selectedStudents.add(new StudentPresent(1L, student, false));
            }
            this.context = context;
        }

        public List<StudentPresent> getSelectedStudents() {
            return selectedStudents;
        }

        @Override
        public int getCount() {
            return selectedStudents.size();
        }

        @Override
        public StudentPresent getItem(int position) {
            return selectedStudents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            AddLessonActivity.ViewHolder viewHolder = new AddLessonActivity.ViewHolder();

            if (rowView == null) {

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.checkbox_element, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkedTextView1);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (AddLessonActivity.ViewHolder) rowView.getTag();
            }

//            viewHolder.checkBox.setChecked(students.get(position).checked);

            viewHolder.checkBox.setTag(position);


            viewHolder.checkBox.setOnClickListener(new AddLessonActivity.CheckboxAdapter.AdapterOnClickListener(viewHolder, position));

//            TextView name = (TextView) convertView.findViewById(R.id.rowTextView);
            viewHolder.text.setText(getItem(position).getStudent().getFirstname() + " " + getItem(position).getStudent().getLastname());

//
            return rowView;
        }

        private class AdapterOnClickListener implements View.OnClickListener {
            AddLessonActivity.ViewHolder viewHolder;
            int position;

            public AdapterOnClickListener(AddLessonActivity.ViewHolder viewHolder, int position) {
                this.viewHolder = viewHolder;
                this.position = position;
            }

//            StudentPresent studentPresent = new StudentPresent(1L, getItem(position), true);

            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    getItem(position).setPresence(true);
                } else {
                    getItem(position).setPresence(false);
                }
            }
        }
    }
}
