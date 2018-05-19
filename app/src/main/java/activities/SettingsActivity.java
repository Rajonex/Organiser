package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.dell.organizerkorepetytora.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by micha on 19.05.2018.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFSTheme = "theme";
    private Spinner spinner;
    private Map<String, Integer> themesMap;
    private ImageButton buttonHome;
    private int themeCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences ThemePreference = getSharedPreferences(PREFSTheme, 0);
        themeCode = ThemePreference.getInt("theme", R.style.DefaultTheme);

        setTheme(themeCode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        initializeElements();
        initializeActions();

    }

    private void initializeElements() {
        themesMap = new HashMap<>();
        themesMap.put("Domyślny", R.style.DefaultTheme);
        themesMap.put("Jasny", R.style.MyTheme1);
        themesMap.put("Zrównoważony", R.style.MyTheme);
        themesMap.put("Ciemny", R.style.DarkTheme);



//        themesMap.entrySet()

        buttonHome = (ImageButton) findViewById(R.id.button_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        List<String> arraySpinner = new ArrayList<>();
        arraySpinner.addAll(themesMap.keySet());

        spinner = (Spinner) findViewById(R.id.spinnerTheme);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for(Map.Entry<String, Integer> entry: themesMap.entrySet())
        {
            if(entry.getValue() == themeCode)
            {
                spinner.setSelection(adapter.getPosition(entry.getKey()), false);
            }
        }

    }

    private void initializeActions() {

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, FirstScreenActivity.class));
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                    String selectedValue = adapter.getItem(i);
                    int selectedTheme = themesMap.get(selectedValue);
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFSTheme, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("theme", selectedTheme);
                    editor.commit();
//                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                finish();
                startActivity(getIntent());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
}
