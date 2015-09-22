package dirk41.com.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/5/20 0020.
 */
public class TimePickerFragment extends DialogFragment {
    private Date mDate;
    public static final String EXTRA_DATA = "dirk41.com.criminalintent.TimePickerFragment.date";

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATA, date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.time_picker, null);

        if (this.getArguments() != null) {
            this.mDate = (Date) this.getArguments().getSerializable(EXTRA_DATA);
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.mDate);

        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                TimePickerFragment.this.mDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        hourOfDay, minute).getTime();
                TimePickerFragment.this.getArguments().putSerializable(EXTRA_DATA, TimePickerFragment.this.mDate);
            }
        });

        return new AlertDialog.Builder(this.getActivity()).setView(view).setTitle(R.string.time_picker_title).setPositiveButton(R.string.positive_ok_button,
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
        intent.putExtra(EXTRA_DATA, this.mDate);

        this.getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, intent);
    }
}
