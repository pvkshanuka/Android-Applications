package CustomClasses;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.wooker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class DatePickerFragment extends DialogFragment {

    Calendar calendar;
    SimpleDateFormat sdf;
    String from;

    public DatePickerFragment(){
    }
    @SuppressLint("ValidFragment")
    public DatePickerFragment(String from) {
        this.from = from;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.format(calendar.getTime());

                if (from.equals("Client_View_All_Jobs")){
                    ((Button) getActivity().findViewById(R.id.btn_c_v_a_j_s_date)).setText(sdf.format(calendar.getTime()));

                }else if(from.equals("Client_Home")) {
                    if (Calendar.getInstance().getTime().before(calendar.getTime()) || sdf.format(Calendar.getInstance().getTime()).equals(sdf.format(calendar.getTime()))) {

                        ((Button) getActivity().findViewById(R.id.btn_c_w_select_date)).setText(sdf.format(calendar.getTime()));
                    } else {
                        Toasty.error(getActivity(), "Invalid Date.!").show();
                    }
                }


            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        return datePickerDialog;

    }
}
