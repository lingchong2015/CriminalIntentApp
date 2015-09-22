package dirk41.com.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/5/19 0019.
 */
public class DatePickerFragment extends DialogFragment {
    private Date mDate;
    public static final String EXTRA_DATE = "dirk41.com.criminalintent.date";

    public static DatePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.data_picker, null);

        this.mDate = (Date) this.getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.mDate);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DatePickerFragment.this.mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                DatePickerFragment.this.getArguments().putSerializable(EXTRA_DATE, DatePickerFragment.this.mDate);
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.date_picker_title).setPositiveButton(R.string.positive_ok_button,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        }).create();
    }

    private void sendResult(int resultCode) {
        if (this.getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, this.mDate);

        this.getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, intent);
    }
}
