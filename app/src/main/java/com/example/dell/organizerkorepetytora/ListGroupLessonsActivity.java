package com.example.dell.organizerkorepetytora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rest.LessonRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Lesson;
import sends.MiniLesson;
import utils.Adress;

public class ListGroupLessonsActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    ListView listLessons;
    List<MiniLesson> listOfLessons;
    SharedPreferences teacherToken;
    String token;
    Lesson singleLesson;
    long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_group_lessons);

        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getLong("id");

        initializeElements();
        initializeActions();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        listLessons.invalidateViews();
    }

    public void initializeElements() {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        //TODO zmienic na toolbar home add edit
        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonHome = (ImageButton)findViewById(R.id.button_home);
        listLessons = (ListView) findViewById(R.id.list);

        listOfLessons = new ArrayList<>();
        getGroupLessons(groupId);


    }


    public void initializeActions() {
        listLessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                long id = ((MiniLesson) adapter.getItemAtPosition(position)).getId();

                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();


                getLesson(id);


            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListGroupLessonsActivity.this, FirstScreenActivity.class));
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
                Lesson lesson = response.body();
                if (lesson != null) {
                    singleLesson = lesson;


                    String topic = singleLesson.getTopic();
                    String description = singleLesson.getDescription();
                    long date = singleLesson.getDate();
                    long id = singleLesson.getId();

                    Intent appInfo = new Intent(ListGroupLessonsActivity.this, ViewLessonActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("topic", topic);
                    bundle.putString("description", description);
                    bundle.putLong("date", date);
                    bundle.putLong("id", id);
                    bundle.putLong("groupId", groupId);

                    appInfo.putExtras(bundle);


                    startActivity(appInfo);
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });
    }


    private void getGroupLessons(long groupId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        LessonRetrofitService lessonRetrofitService = retrofit.create(LessonRetrofitService.class);

        Call<List<MiniLesson>> lessonCall = lessonRetrofitService.getGroupLessons(groupId, token);

        lessonCall.enqueue(new Callback<List<MiniLesson>>() {
            @Override
            public void onResponse(Call<List<MiniLesson>> call, Response<List<MiniLesson>> response) {
                List<MiniLesson> miniLessons = response.body();
                if (miniLessons != null) {
                    listOfLessons = miniLessons;

                    listLessons.setAdapter(new LessonListAdapter(listOfLessons));
                } else {
                    //txtView.setText("Size < 1");
                }

            }

            @Override
            public void onFailure(Call<List<MiniLesson>> call, Throwable t) {
                //txtView.setText("Failure");
            }
        });
    }


    public class LessonListAdapter extends BaseAdapter {

        private List<MiniLesson> lessons;


        public LessonListAdapter(List<MiniLesson> lessons) {
            this.lessons = lessons;
        }

        @Override
        public int getCount() {
            return lessons.size();
        }

        @Override
        public MiniLesson getItem(int position) {
            return lessons.get(position);
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
            title.setText(getItem(position).getTopic());

            return convertView;
        }


    }
}
