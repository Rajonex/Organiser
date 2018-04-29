package rest;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Zofia on 4/5/2018.
 */

public class Util {

    public static void appendToLog(TextView log, String text) {
        SimpleDateFormat formatter = new SimpleDateFormat("[HH:mm:ss.SSS] ", Locale.getDefault());
        String time = formatter.format(new Date(System.currentTimeMillis()));
        log.setText(time + text + "\n" + log.getText().toString());
}}
