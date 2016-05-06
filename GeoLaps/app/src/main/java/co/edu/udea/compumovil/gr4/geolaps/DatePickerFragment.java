package co.edu.udea.compumovil.gr4.geolaps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MattNP on 5/05/2016.
 */
public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Log.d("DatePicker",Integer.toString(year));
        Log.d("DatePicker",getTag());
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat(NuevoRecordatorio.FORMATO_FECHA);
        TextView txt_fecha = (TextView)getActivity().findViewById(R.id.txt_fecha);
        txt_fecha.setText(format.format(calendar.getTime()));
    }
}
