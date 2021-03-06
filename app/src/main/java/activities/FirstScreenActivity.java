package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.organizerkorepetytora.R;


public class FirstScreenActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";
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
        setContentView(R.layout.first_screen);

        final Button buttonAddLesson = (Button) findViewById(R.id.button_lesson);
        final Button buttonPayment = (Button) findViewById(R.id.button_payment);
        final Button buttonGroups = (Button) findViewById(R.id.button_group);
        final Button buttonStudents = (Button) findViewById(R.id.button_student);
        final Button buttonNotes = (Button) findViewById(R.id.button_notes);
        final Button buttonLogOut = (Button) findViewById(R.id.button_log_out);
        final Button buttonSettings = (Button) findViewById(R.id.button_settings);

        buttonAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, ListGroupInLessonActivity.class));
            }
        });

        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, ListStudentPaymentActivity.class));
            }
        });


        buttonGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, ListGroupActivity.class));
            }
        });

        buttonStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, ListStudentsActivity.class));
            }
        });

        buttonNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, ListNotesActivity.class));
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences teacherToken = getSharedPreferences(PREFS, 0);
                SharedPreferences.Editor editor = teacherToken.edit();
                editor.clear();
                editor.commit();


                startActivity(new Intent(FirstScreenActivity.this, MainActivity.class));
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, SettingsActivity.class));
            }
        });
    }


}
