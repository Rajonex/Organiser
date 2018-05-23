package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Student;
import utils.Adress;

public class AddStudentActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    Toolbar toolbar;
    ImageButton buttonHome;
    ImageButton buttonSave;
    EditText studentName;
    EditText studentLastName;
    EditText studentPhone;
    EditText studentEmail;
    ProgressDialog progressDialog;
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
        setContentView(R.layout.add_student);

        InitializeElements();
        InitializeActions();


    }


    public void InitializeElements() {
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonSave = (ImageButton) findViewById(R.id.button_save);

        studentName = (EditText) findViewById(R.id.add_student_name);
        studentLastName = (EditText) findViewById(R.id.add_student_lastname);
        studentPhone = (EditText) findViewById(R.id.add_student_phone);
        studentEmail = (EditText) findViewById(R.id.add_student_email);

    }


    public void InitializeActions() {

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddStudentActivity.this, FirstScreenActivity.class));
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                String token = teacherToken.getString("token", "brak tokenu");
                String name = studentName.getText().toString();
                String lastName = studentLastName.getText().toString();
                String phone = studentPhone.getText().toString();
                String email = studentEmail.getText().toString();

                Student student = new Student(1L, name, lastName, phone, email, token, true);

                progressDialog = new ProgressDialog(AddStudentActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Dodawanie");
                progressDialog.show();
                addStudent(student);
            }

        });
    }


    private void addStudent(Student student) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        StudentRetrofitService studentRetrofitService = retrofit.create(StudentRetrofitService.class);


//        Student student = new Student(1L, "Jarek", "Niemam", "0700", "jarekmail@mail.com", "e2e42a07-5508-33f8-b67f-5eb252581f6d", true);

        Call<Ack> studentCall = studentRetrofitService.addStudent(student);

        studentCall.enqueue(new Callback<Ack>() {
            @Override
            public void onResponse(Call<Ack> call, Response<Ack> response) {
                Ack ack = response.body();
                if (ack != null) {
                    if (ack.isConfirm()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(AddStudentActivity.this, ListStudentsActivity.class));
                    } else {
                        Toast.makeText(AddStudentActivity.this, "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddStudentActivity.this, "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Ack> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddStudentActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
