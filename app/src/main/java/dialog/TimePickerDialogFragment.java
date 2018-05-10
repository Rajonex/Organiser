package dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by micha on 09.05.2018.
 */

public class TimePickerDialogFragment extends DialogFragment {

    private int hourOfDay;
    private int minute;

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public TimePickerDialogFragment()
    {
        this.hourOfDay = 0;
        this.minute = 0;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay1, int minute1) {
                hourOfDay = hourOfDay1;
                minute = minute1;
//                Toast.makeText(getActivity(), "Wybrano: " + formattedTime, Toast.LENGTH_SHORT).show();
            }
        }, hourOfDay, minute, true);
        return dialog;
    }
}
