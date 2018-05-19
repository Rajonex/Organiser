package activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import java.text.SimpleDateFormat;
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
    ListView listStudents;

    Button buttonDate;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateListener;

    ProgressDialog progressDialogGetLesson;
    ProgressDialog progressDialogEditLesson;

    public static final String PREFSTheme = "theme";
    private int themeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_edit);


        Bundle bundle = getIntent().getExtras();

        lessonId = bundle.getLong("id");
        groupId = bundle.getLong("groupId");

        initializeElements();
        initializeActions();

    }

    public void initializeElements() {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHomeSave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);
//
//
//
        lessonTopic = (EditText) findViewById(R.id.lesson_topic);
        lessonDescription = (EditText) findViewById(R.id.lesson_data);
        buttonDate = (Button) findViewById(R.id.button_date);


        listStudents = (ListView) findViewById(R.id.lesson_attendance_list);


        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        buttonDate.setText(simpleDateFormat.format(calendar.getTime()));

        dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                buttonDate.setText(simpleDateFormat.format(calendar.getTime()));
            }

        };

        progressDialogGetLesson = new ProgressDialog(LessonEditActivity.this);
        progressDialogGetLesson.setIndeterminate(true);
        progressDialogGetLesson.setMessage("Pobieranie");
        progressDialogGetLesson.show();
        getLesson(lessonId);

    }

    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LessonEditActivity.this, FirstScreenActivity.class));
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LessonEditActivity.this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = lessonTopic.getText().toString();
                String description = lessonDescription.getText().toString();

                long date = calendar.getTimeInMillis();

                Lesson editedLesson = new Lesson(singleLesson.getId(), ((CheckboxAdapterEditLesson) listStudents.getAdapter()).getStudentsInGroup(), topic, description, date, singleLesson.getGroupId(), token);

                progressDialogEditLesson = new ProgressDialog(LessonEditActivity.this);
                progressDialogEditLesson.setIndeterminate(true);
                progressDialogEditLesson.setMessage("Ładowanie...");
                progressDialogEditLesson.show();
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
                progressDialogEditLesson.dismiss();
                Ack ack = response.body();
                if (ack != null) {
                    if (ack.isConfirm()) {

                        Intent appInfo = new Intent(LessonEditActivity.this, ListGroupLessonsActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", groupId);
                        appInfo.putExtras(bundle);

                        startActivity(appInfo);
                    } else {
                        Toast.makeText(LessonEditActivity.this, "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LessonEditActivity.this, "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {
                progressDialogEditLesson.dismiss();
                Toast.makeText(LessonEditActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLesson(long lessonId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        Call<Lesson> lessonCall = lessonRetrofitService.getLesson(lessonId, token);

        lessonCall.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                progressDialogGetLesson.dismiss();
                Lesson lesson = response.body();
                if (lesson != null) {
                    singleLesson = lesson;


                    calendar.setTimeInMillis(singleLesson.getDate());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    buttonDate.setText(simpleDateFormat.format(calendar.getTime()));

                    lessonTopic.setText(singleLesson.getTopic(), TextView.BufferType.EDITABLE);
                    lessonDescription.setText(singleLesson.getDescription(), TextView.BufferType.EDITABLE);


                    studentsPresent = singleLesson.getStudentPresent();

                    listStudents.setAdapter(new LessonEditActivity.CheckboxAdapterEditLesson(studentsPresent, LessonEditActivity.this));
                    listStudents.setItemsCanFocus(false);
                    listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                } else
                {
                    Toast.makeText(LessonEditActivity.this, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                progressDialogGetLesson.dismiss();
                Toast.makeText(LessonEditActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
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
