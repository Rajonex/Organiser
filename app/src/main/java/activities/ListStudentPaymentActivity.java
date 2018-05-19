package activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.dell.organizerkorepetytora.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rest.GroupRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.MiniGroup;
import utils.Adress;

public class ListStudentPaymentActivity extends AppCompatActivity {

    public static final String PREFS = "teacherToken";

    ImageButton buttonHome;
    SharedPreferences teacherToken;
    String token;
    ProgressDialog progressDialog;
    ListView listGroups;
    List<MiniGroup> listOfGroups;
    public static final String PREFSTheme = "theme";
    private int themeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_student_payment);


        initializeElements();
        initializeActions();

    }

    public void initializeElements() {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton) findViewById(R.id.button_home);

        listGroups = (ListView) findViewById(R.id.list);

        listOfGroups = new ArrayList<>();

        progressDialog = new ProgressDialog(ListStudentPaymentActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Ładowanie...");
        progressDialog.show();
        getMiniGroups();
    }


    public void initializeActions() {
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ListStudentPaymentActivity.this, FirstScreenActivity.class));

            }

        });


        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(ListStudentPaymentActivity.this, PaymentActivity.class);
                Calendar calendar = Calendar.getInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("year", calendar.get(Calendar.YEAR));
                bundle.putInt("month", calendar.get(Calendar.MONTH) + 1);
                MiniGroup miniGroup = ((GroupListAdapter) listGroups.getAdapter()).getItem(position);
                bundle.putLong("groupId", miniGroup.getId());
                appInfo.putExtras(bundle);

                startActivity(appInfo);
            }
        });
    }


    private void getMiniGroups() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        GroupRetrofitService groupRetrofitService = retrofit.create(GroupRetrofitService.class);

        Call<List<MiniGroup>> groupCall = groupRetrofitService.getMiniGroups(token);

        groupCall.enqueue(new Callback<List<MiniGroup>>() {
            @Override
            public void onResponse(Call<List<MiniGroup>> call, Response<List<MiniGroup>> response) {
                progressDialog.dismiss();
                List<MiniGroup> miniGroupList = response.body();
                if (miniGroupList != null) {

                    listOfGroups = miniGroupList;
                    listGroups.setAdapter(new ListStudentPaymentActivity.GroupListAdapter(listOfGroups));
                }
                else
                {
                    Toast.makeText(ListStudentPaymentActivity.this, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MiniGroup>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListStudentPaymentActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class GroupListAdapter extends BaseAdapter {

        private List<MiniGroup> groups;


        public GroupListAdapter(List<MiniGroup> groups) {
            this.groups = groups;
        }


        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public MiniGroup getItem(int position) {
            return groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_element_layout, null);
            }

            TextView group = (TextView) convertView.findViewById(R.id.list_row_view);
            group.setText(getItem(position).getName());

            return convertView;
        }


    }
}
