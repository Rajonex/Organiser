package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import rest.LessonRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Lesson;
import sends.Student;
import sends.StudentPresent;
import utils.Adress;

public class ViewLessonActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ImageButton buttonEdit;
    SharedPreferences teacherToken;
    String token;
    String topic;
    String description;
    long date;
    long lessonId;
    long groupId;
    Lesson singleLesson;
    List<StudentPresent> studentsPresent;
    ListView listStudents;

    TextView lessonTopic;
    TextView lessonDescription;
    TextView lessonDate;
    ProgressDialog progressDialog;
    public static final String PREFSTheme = "theme";
    private int themeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lesson);


        Bundle bundle = getIntent().getExtras();
        topic = bundle.getString("topic");
        description = bundle.getString("description");
        date = bundle.getLong("date");
        lessonId = bundle.getLong("id");
        groupId = bundle.getLong("groupId");

        initializeElements();
        initializeActions();

    }

    public void initializeElements() {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHomeEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonEdit = (ImageButton) findViewById(R.id.button_edit);


        lessonTopic = (TextView) findViewById(R.id.lesson_topic);
        lessonDescription = (TextView) findViewById(R.id.lesson_data);
        lessonDate = (TextView) findViewById(R.id.lesson_date);

        listStudents = (ListView) findViewById(R.id.list);

        progressDialog = new ProgressDialog(ViewLessonActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Pobieranie");
        progressDialog.show();
        getLesson(lessonId);


    }

    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewLessonActivity.this, FirstScreenActivity.class));
            }

        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                Intent appInfo = new Intent(ViewLessonActivity.this, LessonEditActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong("id", lessonId);
                bundle.putLong("groupId", groupId);


                appInfo.putExtras(bundle);


                startActivity(appInfo);

            }

        });
    }


    private void getLesson(long lessonId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        //long lessonId = 7L;
        //String token = "e2e42a07-5508-33f8-b67f-5eb252581f6d";

        Call<Lesson> lessonCall = lessonRetrofitService.getLesson(lessonId, token);

        lessonCall.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                progressDialog.dismiss();
                Lesson lesson = response.body();
                if (lesson != null) {
                    singleLesson = lesson;

                    Date dateSql = new Date(date);
                    lessonTopic.setText(singleLesson.getTopic(), TextView.BufferType.EDITABLE);
                    lessonDescription.setText(singleLesson.getDescription(), TextView.BufferType.EDITABLE);
                    lessonDate.setText(dateSql.toString(), TextView.BufferType.EDITABLE);

                    List<Student> students = new ArrayList<>();
                    for (StudentPresent studentPresent : singleLesson.getStudentPresent()) {
                        if (studentPresent.isPresence()) {
                            students.add(studentPresent.getStudent());
                        }
                    }
//                    studentsPresent = singleLesson.getStudentPresent();
                    listStudents.setAdapter(new StudentPresentListAdapter(students));


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
                else{
                    Toast.makeText(ViewLessonActivity.this, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ViewLessonActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class StudentPresentListAdapter extends BaseAdapter {

        private List<Student> studentsPresent;


        public StudentPresentListAdapter(List<Student> students) {
            this.studentsPresent = students;
        }


        @Override
        public int getCount() {
            return studentsPresent.size();
        }

        @Override
        public Student getItem(int position) {
            return studentsPresent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                //LayoutInflater inflater = LayoutInflater.from(getC)
                convertView = getLayoutInflater().inflate(R.layout.list_element_layout, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.list_row_view);
            title.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());

            return convertView;
        }


    }
//
//    static class ViewHolder {
//        CheckBox checkBox;
//        TextView text;
//    }
//
//
//    public class CheckboxAdapter extends BaseAdapter {
//
//        private List<Student> students;
//        ArrayList<Student> selectedStudents = new ArrayList<Student>();
//        private Context context;
//
//
//        public List<Student> getCheckedStudents() {
//            return selectedStudents;
//        }
//
//        @Override
//        public int getCount() {
//            return students.size();
//        }
//
//        @Override
//        public Student getItem(int position) {
//            return students.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            View rowView = convertView;
//
//            ViewLessonActivity.ViewHolder viewHolder = new ViewLessonActivity.ViewHolder();
//
//            if (rowView == null) {
//
//                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//                rowView = inflater.inflate(R.layout.checkbox_element, null);
////                convertView = getLayoutInflater().inflate(R.layout.checkbox_element, null);
////                rowView = getLayoutInflater().inflate(R.layout.checkbox_element, null);
//
//
//                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkedTextView1);
//                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
//
//                rowView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewLessonActivity.ViewHolder) rowView.getTag();
//            }
//
//            viewHolder.checkBox.setTag(position);
//
//
//            viewHolder.checkBox.setOnClickListener(new ViewLessonActivity.CheckboxAdapter.AdapterOnClickListener(viewHolder, position));
//
//            viewHolder.text.setText(getItem(position).getFirstname() + " " + getItem(position).getLastname());
//
//
//            return rowView;
//        }
//
//        private class AdapterOnClickListener implements View.OnClickListener{
//            ViewLessonActivity.ViewHolder viewHolder;
//            int position;
//            public AdapterOnClickListener(ViewLessonActivity.ViewHolder viewHolder, int position)
//            {
//                this.viewHolder = viewHolder;
//                this.position = position;
//            }
//
//            @Override
//            public void onClick(View v) {
//                if (viewHolder.checkBox.isChecked()) {
//                    selectedStudents.add(getItem(position));
//                } else {
//                    selectedStudents.remove(getItem(position));
//                }
//            }
//        }
//    }

}
