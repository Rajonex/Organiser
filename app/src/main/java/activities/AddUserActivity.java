package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import rest.TeacherRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.ResultTeacher;
import sends.Teacher;
import utils.Adress;

public class AddUserActivity extends AppCompatActivity {

    Button buttonZatwierdz;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextTeachername;
    ProgressDialog progressDialog;
    public static final String PREFSTheme = "theme";
    private int themeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        initializeElements();
        initializeActions();


    }

    private void initializeElements() {

        buttonZatwierdz = (Button) findViewById(R.id.buttonZatwierdz);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextTeachername = (EditText) findViewById(R.id.editText_teachername);

    }

    private void initializeActions() {

        buttonZatwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AddUserActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Dodawanie");
                progressDialog.show();
                addTeacher();

            }
        });

    }


    private void addTeacher() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        TeacherRetrofitService teacherRetrofitService = retrofit.create(TeacherRetrofitService.class);

        String login = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextTeachername.getText().toString();

        String token = "byleco"; // server doesn't look on this value during creating teacher account
//        String name = "NazywamSie";

        Teacher teacher = new Teacher(login, password, token, name);

        Call<ResultTeacher> resultTeacherCall = teacherRetrofitService.addTeacher(teacher);

        resultTeacherCall.enqueue(new Callback<ResultTeacher>() {
            @Override
            public void onResponse(Call<ResultTeacher> call, Response<ResultTeacher> response) {
                ResultTeacher resultTeacher = response.body();
                if (resultTeacher != null) {
                    int resultCode = resultTeacher.getResult();
                    switch (resultCode) {
                        case 0: // it's ok, get teacher and save his token (save by SharedPreferences)
                        {
                            Teacher teacherForSave = resultTeacher.getTeacher();
                            progressDialog.dismiss();
                            startActivity(new Intent(AddUserActivity.this, MainActivity.class));
                            break;
                        }
                        case 1: {
                            // name exists in database - can't create
                            Toast.makeText(AddUserActivity.this, "Podana nazwa użytkownika już istnieje", Toast.LENGTH_LONG).show();
                            break;
                        }
                        case 2: {
                            // problem with create token - server problem
                            Toast.makeText(AddUserActivity.this, "Błąd, nie utworzono tokenu bezpieczeństwa", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 3: {
                            Toast.makeText(AddUserActivity.this, "Błąd danych", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else
                {
                    Toast.makeText(AddUserActivity.this, "Błąd danych", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultTeacher> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddUserActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
