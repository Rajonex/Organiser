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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lesson.AddLessonActivity;
import lesson.ListGroupInLessonActivity;
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

public class LessonEditActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ImageButton buttonSave;
    SharedPreferences teacherToken;
    String token;
    long lessonId;
    long groupId;

    EditText lessonTopic;
    EditText lessonDescription;
    EditText lessonDate;

    Lesson singleLesson;
    List<StudentPresent> studentsPresent;
    Group singleGroup;
    ListView listStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_edit);


        Bundle bundle = getIntent().getExtras();

        lessonId = bundle.getLong("id");
        groupId = bundle.getLong("groupId");

        initializeElements();
        initializeActions();

    }

    public void initializeElements()
    {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBarHomeSave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton)findViewById(R.id.button_home);
        buttonSave = (ImageButton)findViewById(R.id.button_save);
//
//
//
        lessonTopic = (EditText)  findViewById(R.id.lesson_topic);
        lessonDescription = (EditText) findViewById(R.id.lesson_data);
        lessonDate = (EditText) findViewById(R.id.lesson_date);


        listStudents = (ListView) findViewById(R.id.lesson_attendance_list);


        getLesson(this, lessonId);

    }

    public void initializeActions()
    {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LessonEditActivity.this, FirstScreenActivity.class));
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = lessonTopic.getText().toString();
                String description = lessonDescription.getText().toString();
                long date = singleLesson.getDate(); //TODO poprawic date

                //TODO zmienic kalendarz z singleGroup na nowy kalendarz
                Lesson editedLesson = new Lesson(singleLesson.getId(), ((CheckboxAdapterEditLesson) listStudents.getAdapter()).getStudentsInGroup(), topic, description, date, singleLesson.getGroupId(), token);
                editLesson(editedLesson);
//                startActivity(new Intent(LessonEditActivity.this, ListGroupLessonsActivity.class));

            }

        });
    }

    private void editLesson(Lesson lesson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        Call<Ack> lessonCall = lessonRetrofitService.updateLesson(lesson);

        lessonCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if (ack != null) {
                    if (ack.isConfirm()) {

                        Intent appInfo = new Intent(LessonEditActivity.this, ListGroupLessonsActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", groupId);
                        appInfo.putExtras(bundle);

                        startActivity(appInfo);
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
    private void getLesson(Context context, long lessonId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        //long lessonId = 7L;
        //String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<Lesson> lessonCall = lessonRetrofitService.getLesson(lessonId, token);

        lessonCall.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                Lesson lesson = response.body();
                if (lesson != null) {
                    singleLesson = lesson;

                    Date dateSql = new Date(singleLesson.getDate());
                    lessonTopic.setText(singleLesson.getTopic(), TextView.BufferType.EDITABLE);
                    lessonDescription.setText(singleLesson.getDescription(), TextView.BufferType.EDITABLE);
                    lessonDate.setText(dateSql.toString(), TextView.BufferType.EDITABLE);

                    studentsPresent = singleLesson.getStudentPresent();

                    listStudents.setAdapter(new LessonEditActivity.CheckboxAdapterEditLesson(studentsPresent, context));
                    listStudents.setItemsCanFocus(false);
                    listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    //getGroup(context, singleLesson);

//                    String topic = singleLesson.getTopic();
//                    String description = singleLesson.getDescription();
//                    long date = singleLesson.getDate();
//                    long id = singleLesson.getId();
//
//                    Intent appInfo = new Intent(ListGroupLessonsActivity.this, ViewLessonActivity.class);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("topic", topic);
//                    bundle.putString("description", description);
//                    bundle.putLong("date", date);
//                    bundle.putLong("id", id);
//
//                    appInfo.putExtras(bundle);
//
//
//                    startActivity(appInfo);
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });
    }

    private void getGroup(Context context, Lesson singleLesson) {


        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        Call<Group> groupCall = groupRetrofitService.getGroup(groupId, token);

        groupCall.enqueue(new Callback<Group>() {

            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                System.out.println("test");
                Group group = response.body();
                if (group != null) {
                    System.out.println("grupa nie jest nullem");
                    singleGroup = group;



                } else {//TODO pop-up
                    System.out.println("grupa jest nullem");
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }

    public class CheckboxAdapterEditLesson extends BaseAdapter {

//        private List<Student> students;
        private List<StudentPresent> studentsInGroup;
//        ArrayList<StudentPresent> selectedStudents = new ArrayList<StudentPresent>();
        private Context context;

        public CheckboxAdapterEditLesson(List<StudentPresent> studentsInGroup, Context context) {
//            this.students = students;
            this.context = context;
            this.studentsInGroup = studentsInGroup;
        }

//        public List<StudentPresent> getCheckedStudents() {
//            return selectedStudents;
//        }

        public List<StudentPresent> getStudentsInGroup() {
            return studentsInGroup;
        }

        @Override
        public int getCount() {
            return studentsInGroup.size();
        }

        @Override
        public StudentPresent getItem(int position) {
            return studentsInGroup.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            LessonEditActivity.ViewHolder viewHolder = new LessonEditActivity.ViewHolder();

            if (rowView == null) {

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.checkbox_element, null);
                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkedTextView1);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (LessonEditActivity.ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new LessonEditActivity.CheckboxAdapterEditLesson.AdapterOnClickListener(viewHolder, position));

            if (getItem(position).isPresence()) {
                viewHolder.checkBox.setChecked(true);
            }
            viewHolder.text.setText(getItem(position).getStudent().getFirstname() + " " + getItem(position).getStudent().getLastname());

            return rowView;
        }

        private class AdapterOnClickListener implements View.OnClickListener {
            LessonEditActivity.ViewHolder viewHolder;
            int position;

            public AdapterOnClickListener(LessonEditActivity.ViewHolder viewHolder, int position) {
                this.viewHolder = viewHolder;
                this.position = position;
            }

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
