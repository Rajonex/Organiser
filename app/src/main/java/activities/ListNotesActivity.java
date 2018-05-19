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
import java.util.List;

import rest.NoteRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Note;
import utils.Adress;

public class ListNotesActivity extends AppCompatActivity {


    public static final String PREFS = "teacherToken";

    Toolbar toolbar;
    ListView listNotes;
    ImageButton buttonHome;
    ImageButton buttonAdd;
    List<Note> listOfNotes;
    SharedPreferences teacherToken;
    String token;
    ProgressDialog progressDialog;

    public static final String PREFSTheme = "theme";
    private int themeCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_notes);


        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHomeAdd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeElements();
        initializeActions();

    }

    @Override
    protected void onStart() {
        super.onStart();
        listNotes.invalidateViews();
    }

    private void initializeElements() {

        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        toolbar = (Toolbar) findViewById(R.id.appBarHomeAdd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonAdd = (ImageButton) findViewById(R.id.button_add);
        listNotes = (ListView) findViewById(R.id.list);

        listOfNotes = new ArrayList<>();

        listNotes.setAdapter(new NoteListAdapter(listOfNotes));

        progressDialog = new ProgressDialog(ListNotesActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Pobieranie");
        progressDialog.show();
        getNotes();



    }

    private void initializeActions() {


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ListNotesActivity.this, FirstScreenActivity.class));

            }

        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor editor = teacherToken.edit();
                editor.putString("token", token);
                editor.commit();

                startActivity(new Intent(ListNotesActivity.this, AddNoteActivity.class));


            }
        });

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(ListNotesActivity.this, ViewNote.class);
                System.out.println(((Note) adapter.getItemAtPosition(position)).getTitle());
                String title = ((Note) adapter.getItemAtPosition(position)).getTitle();
                String text = ((Note) adapter.getItemAtPosition(position)).getText();
                String token = ((Note) adapter.getItemAtPosition(position)).getTeacherToken();
                long id = ((Note) adapter.getItemAtPosition(position)).getId();

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("text", text);
                bundle.putString("token", token);
                bundle.putLong("id", id);

                appInfo.putExtras(bundle);
                //appInfo.putExtras(bundle);
                //appInfo.putExtra("Text", text);
                startActivity(appInfo);
            }
        });

    }

    private void getNotes() {


        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        NoteRetrofitService noteRetrofitService = retrofit.create(NoteRetrofitService.class);

        //String token = "a3b5af4d-4ed6-3497-a21a-6751fea9f7c0"; // do pobrania z SharedPreferences

        Call<List<Note>> noteCall = noteRetrofitService.getNotes(token);

        noteCall.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                progressDialog.dismiss();
                List<Note> notes = response.body();
                if (notes != null) {
                    for (Note note : notes) {
                        listOfNotes.add(note);
//                        adapter = new ArrayAdapter<String>(listNotes.getContext(), android.R.layout.simple_list_item_1, listOfNotes);
//                        listNotes.setAdapter(adapter);
                        listNotes.invalidateViews();
                    }
                } else {
                    Toast.makeText(ListNotesActivity.this, "Błąd podczas pobierania danych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListNotesActivity.this, "Błąd podczas łączenia z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class NoteListAdapter extends BaseAdapter {

        private List<Note> notes;


        public NoteListAdapter(List<Note> notes) {
            this.notes = notes;
        }


        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Note getItem(int position) {
            return notes.get(position);
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
            title.setText(getItem(position).getTitle());

            return convertView;
        }


    }


}
