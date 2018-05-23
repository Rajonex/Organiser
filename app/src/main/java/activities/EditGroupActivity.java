package activities;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dialog.TimePickerDialogFragment;
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
import utils.Day;

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
    Spinner spinner;
    Button buttonHour;
    ProgressDialog progressDialogGetGroup;
    ProgressDialog progressDialogGetStudents;
    ProgressDialog progressDialogEditGroup;

    TimePickerDialogFragment timePickerDialogFragment;
    public static final String PREFSTheme = "theme";
    private int themeCode;
    int[] styleThemeTab = {R.style.DarkTheme, R.style.DefaultTheme, R.style.MyThemeLight, R.style.MyTheme};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean flag = false;
        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        int themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);
        for (int i : styleThemeTab) {
            if (i == themeCode)
                flag = true;
        }
        if (flag) {
            setTheme(themeCode);
        }

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

        timePickerDialogFragment = new TimePickerDialogFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);
        buttonHour = (Button) findViewById(R.id.button_hour);

        List<String> arraySpinner = new ArrayList<>();
        for (Day day : Day.values()) {
            arraySpinner.add(day.getDescription());
        }

        spinner = (Spinner) findViewById(R.id.spinner_day);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        groupName = (EditText) findViewById(R.id.add_group_name);
        groupRate = (EditText) findViewById(R.id.add_group_payment);
        listStudents = (ListView) findViewById(R.id.lesson_attendance_list);

        progressDialogGetGroup = new ProgressDialog(EditGroupActivity.this);
        progressDialogGetGroup.setIndeterminate(true);
        progressDialogGetGroup.setMessage("Pobieranie");
        progressDialogGetGroup.show();
        getGroup();


    }

    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditGroupActivity.this, FirstScreenActivity.class));
            }

        });

        buttonHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFragment.show(getFragmentManager(), null);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (singleGroup != null) {

                    List<GroupCalendar> lessonsDays = singleGroup.getGroupCalendar();

                    Day d = null;
                    for (Day day : Day.values()) {
                        if (day.getDescription().equals((String) spinner.getSelectedItem())) {
                            d = day;
                        }
                    }

                    if (d != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerDialogFragment.getHourOfDay());
                        calendar.set(Calendar.MINUTE, timePickerDialogFragment.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);


                        if (lessonsDays.size() > 0) {
                            GroupCalendar groupCalendar = lessonsDays.get(0);
                            groupCalendar.setDay(d);
                            groupCalendar.setTime(calendar.getTime().getTime());
                        } else {
                            GroupCalendar groupCalendar = new GroupCalendar(1L, token, 1L, d, calendar.getTime().getTime());
                            lessonsDays.add(groupCalendar);
                        }
                    }

                    String name = groupName.getText().toString();
                    double rate = Double.parseDouble(groupRate.getText().toString());

                    //TODO zmienic kalendarz z singleGroup na nowy kalendarz
                    Group editedGroup = new Group(singleGroup.getId(), name, ((CheckboxAdapterEditGroup) listStudents.getAdapter()).getStudentsInGroup(), rate, token, true, lessonsDays);
                    progressDialogEditGroup = new ProgressDialog(EditGroupActivity.this);
                    progressDialogEditGroup.setIndeterminate(true);
                    progressDialogEditGroup.setMessage("Ładowanie...");
                    progressDialogEditGroup.show();

                    editGroup(editedGroup);
                }
            }

        });


    }

    private void getGroup() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        Call<Group> groupCall = groupRetrofitService.getGroup(id, token);

        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                progressDialogGetGroup.dismiss();
                Group group = response.body();
                if (group != null && group.getGroupCalendar() != null) { // Zalozenie, ze zawsze podany jest termin
                    singleGroup = group;

                    studendsInGroup = singleGroup.getStudents();
                    groupName.setText(singleGroup.getName(), TextView.BufferType.EDITABLE);
                    Double rateDouble = new Double(singleGroup.getRate());
                    groupRate.setText(rateDouble.toString(), TextView.BufferType.EDITABLE);

                    if (singleGroup.getGroupCalendar().size() > 0) {
                        String myString = singleGroup.getGroupCalendar().get(0).getDay().getDescription(); //the value you want the position for
                        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                        int spinnerPosition = myAdap.getPosition(myString);
                        spinner.setSelection(spinnerPosition);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(singleGroup.getGroupCalendar().get(0).getTime());
                        timePickerDialogFragment.setHourOfDay(calendar.get(Calendar.HOUR_OF_DAY));
                        timePickerDialogFragment.setMinute(calendar.get(Calendar.MINUTE));
                    }
                    progressDialogGetStudents = new ProgressDialog(EditGroupActivity.this);
                    progressDialogGetStudents.setIndeterminate(true);
                    progressDialogGetStudents.setMessage("Ładowanie...");
                    progressDialogGetStudents.show();

                    getAllStudents(EditGroupActivity.this, studendsInGroup);
                } else {
                    Toast.makeText(EditGroupActivity.this, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                progressDialogGetGroup.dismiss();
                Toast.makeText(EditGroupActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
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
                progressDialogEditGroup.dismiss();
                Ack ack = response.body();
                if (ack.isConfirm()) {
                    startActivity(new Intent(EditGroupActivity.this, ListGroupActivity.class));
                } else {
                    Toast.makeText(EditGroupActivity.this, "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {
                progressDialogEditGroup.dismiss();
                Toast.makeText(EditGroupActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllStudents(Context context, List<Student> studendsInGroup) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);

        Call<List<Student>> studentCall = studentRetrofitService.getStudents(token);

        studentCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                progressDialogGetStudents.dismiss();
                List<Student> students = response.body();
                if (students != null) {
                    listOfStudents = students;
                    listStudents.setAdapter(new EditGroupActivity.CheckboxAdapterEditGroup(listOfStudents, studendsInGroup, context));
                    listStudents.setItemsCanFocus(false);
                    listStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
                    Toast.makeText(context, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                progressDialogGetStudents.dismiss();
                Toast.makeText(context, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
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
            } else {
                viewHolder.checkBox.setChecked(false);
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
