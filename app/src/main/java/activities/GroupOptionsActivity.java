package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.organizerkorepetytora.R;

import rest.GroupRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Group;
import utils.Adress;

public class GroupOptionsActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    SharedPreferences teacherToken;
    String token;
    Group singleGroup;
    long id;

    Button buttonEditLesson;
    Button buttonShowLesson;
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
        setContentView(R.layout.group_options);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        initializeElements();
        initializeActions();

    }

    public void initializeElements() {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        buttonShowLesson = (Button) findViewById(R.id.button_show);
        buttonEditLesson = (Button) findViewById(R.id.button_edit);
    }

    public void initializeActions() {
        buttonShowLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();
                Intent appInfo = new Intent(GroupOptionsActivity.this, ListGroupLessonsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                appInfo.putExtras(bundle);


                startActivity(appInfo);
            }
        });


        buttonEditLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                Intent appInfo = new Intent(GroupOptionsActivity.this, EditGroupActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                appInfo.putExtras(bundle);

                startActivity(appInfo);
            }
        });
    }


}
