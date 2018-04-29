package lesson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.organizerkorepetytora.First_Screen_Activity;
import com.example.dell.organizerkorepetytora.R;

import java.util.ArrayList;
import java.util.List;

import rest.GroupRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Group;
import sends.Lesson;
import sends.MiniGroup;
import sends.Note;
import utils.Adress;

public class Lesson_Add_To_Group_Activity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ListView listGroups;
    List<MiniGroup> listOfGroups;
    ArrayAdapter<String> adapter;
    ImageButton buttonHome;
    SharedPreferences teacherToken;
    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_add_to_group);

         initializeElements();
         initializeActions();
    }

    public void initializeElements()
    {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar)findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton)findViewById(R.id.button_home);

        listGroups = (ListView)findViewById(R.id.list);

        listOfGroups = new ArrayList<>();
        getMiniGroups();
        listGroups.setAdapter(new GroupListAdapter(listOfGroups));

    }

    public void initializeActions()
    {
        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(Lesson_Add_To_Group_Activity.this, Add_Lesson_Screen_Activity.class);
                startActivity(appInfo);
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Lesson_Add_To_Group_Activity.this, First_Screen_Activity.class));

            }

        });

    }






    private void getMiniGroups()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        //String token = "d56b6998-30e7-3ba5-b855-679cb1d252da";

        Call<List<MiniGroup>> groupCall = groupRetrofitService.getMiniGroups(token);

        groupCall.enqueue(new Callback<List<MiniGroup>>() {
            @Override
            public void onResponse(Call<List<MiniGroup>> call, Response<List<MiniGroup>> response) {
                List<MiniGroup> miniGroupList = response.body();
                if(miniGroupList != null)
                {
                    if(miniGroupList.size() > 0)
                    {
                        for(MiniGroup miniGroup : miniGroupList)
                        {
                            listOfGroups.add(miniGroup);
                            listGroups.invalidateViews();
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<List<MiniGroup>> call, Throwable t) {

            }
        });
    }


    public class GroupListAdapter extends BaseAdapter {

        private List<MiniGroup> groups;


        public GroupListAdapter(List<MiniGroup> groups)
        {
            this.groups = groups;
        }



        @Override
        public int getCount()
        {
            return groups.size();
        }

        @Override
        public MiniGroup getItem(int position)
        {
            return groups.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView==null)
            {
                //LayoutInflater inflater = LayoutInflater.from(getC)
                convertView = getLayoutInflater().inflate(R.layout.list_element_layout, null);
            }

            TextView group = (TextView) convertView.findViewById(R.id.list_row_view);
            group.setText(getItem(position).getName());

            return convertView;
        }


    }

}
