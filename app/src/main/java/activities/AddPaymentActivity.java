package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dell.organizerkorepetytora.R;

public class AddPaymentActivity extends AppCompatActivity {
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
        setContentView(R.layout.add_payment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button buttonAccept = (Button) findViewById(R.id.buttonZatwierdz);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPaymentActivity.this, PaymentActivity.class));
            }
        });

        ImageButton buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPaymentActivity.this, FirstScreenActivity.class));
            }
        });

    }
}
