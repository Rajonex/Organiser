package lesson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import rest.LessonRetrofitService;
import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
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
//    List<StudentPresent> listOfStudentsPresent;
    Context context;
    SharedPreferences teacherToken;
    String token;
    EditText lessonTopic;
    EditText lessonDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lesson);

        initializeElements();
        initializeActions();

    }

    public void initializeElements()
    {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);
        lessonTopic = (EditText) findViewById(R.id.lesson_topic);
        lessonDescription = (EditText) findViewById(R.id.lesson_description);

        listStudents = (ListView)findViewById(R.id.lesson_attendance_list);
       // listStudents.setOnItemClickListener(new CheckBoxClick());


        listOfStudents = new ArrayList<>();
        getAllStudents();

        listStudents.setAdapter(new CheckboxAdapter(listOfStudents));
        listStudents.setItemsCanFocus(false);
        listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }

    public void initializeActions()
    {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddLessonActivity.this, FirstScreenActivity.class));

            }

        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                public Lesson(long id, List<StudentPresent> studentPresent, String topic, String description, long date,
//                long groupId, String teacherToken




                String topic = lessonTopic.getText().toString();
                String description = lessonDescription.getText().toString();

                Lesson lesson = new Lesson(1L, new ArrayList<StudentPresent>(), topic, description, 2L, 1L, token);

                addLesson(lesson);
            }

        });



    }


    private void addLesson(Lesson lesson)
    {
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
                if(ack != null)
                {
                    if(ack.isConfirm())
                    {
                       startActivity(new Intent(AddLessonActivity.this, ListGroupInLessonActivity.class));
                    }
                    else
                    {
//                        txtView.setText("Nie dodano lekcji");
                    }
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

    public class Item
    {
        boolean itemChecked;
        String itemName;

        Item(String itemName, boolean itemChecked)
        {

        }
    }
    public class CheckboxAdapter extends BaseAdapter {

        private List<Student> students;
//        ArrayList<Student> selectedStudents = new ArrayList<Student>();


        public CheckboxAdapter(List<Student> students)
        {
            this.students = students;
        }


        @Override
        public int getCount()
        {
            return students.size();
        } //to

        @Override
        public Student getItem(int position)
        {
            return students.get(position);
        } //to

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //final ViewHolder holder;

            if(convertView==null)
            {
                //holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.checkbox_element, null);
            }
//            else
//                holder = (ViewHolder)convertView.getTag();

            TextView name = (TextView) convertView.findViewById(R.id.checkedTextView1);
            name.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());
            //holder.checkBox.setTag(R.integer.btnplusview, convertView);

//        holder.checkBox.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //View tempView = (View) holder.checkBox.getTag(R.integer.)
//
//
//            }
//        });

            return convertView;
        }
        private class ViewHolder {

            protected CheckBox checkBox;
            private TextView tvAnimal;

        }

    }
}
