package my.edu.uitm.friendmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.function.Consumer;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Consumer<LocalDate> onDateSetCallback;
    private String currentDate;

    public DatePickerFragment(Consumer<LocalDate> onDateSetCallback, String currentDate) {
        this.onDateSetCallback = onDateSetCallback;
        this.currentDate = currentDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(currentDate));
        } catch (ParseException e) {
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDateSet(DatePicker view, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month+1, day);
        onDateSetCallback.accept(date);
    }
}
