package dirk41.com.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RadioButton;

import java.util.Date;

/**
 * Created by Administrator on 2015/5/19 0019.
 */
public class SetDateTime extends DialogFragment {
    private Date mDate;
    public static final String EXTRA_DATA = "dirk41.com.criminalintent.SetDateTime.date";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int DATE_REQUEST_CODE = 0;
    private static final int TIME_REQUEST_CODE = 1;

    public static SetDateTime newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATA, date);

        SetDateTime setDateTime = new SetDateTime();
        setDateTime.setArguments(bundle);
        return setDateTime;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) this.getArguments().getSerializable(EXTRA_DATA);

        View view = this.getActivity().getLayoutInflater().inflate(R.layout.date_or_time, null);

        final RadioButton dateRadioButton = (RadioButton) view.findViewById(R.id.date_radio_button);

        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.date_time_tilte).setPositiveButton(R.string.positive_ok_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fragmentManager = SetDateTime.this.getActivity().getSupportFragmentManager();
                        if (dateRadioButton.isChecked()) {
                            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mDate);
                            timePickerFragment.setTargetFragment(SetDateTime.this.getTargetFragment(), TIME_REQUEST_CODE);
                            timePickerFragment.show(fragmentManager, DIALOG_TIME);

                            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mDate);
                            datePickerFragment.setTargetFragment(SetDateTime.this.getTargetFragment(), DATE_REQUEST_CODE);
                            datePickerFragment.show(fragmentManager, DIALOG_DATE);
                        } else {
                            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mDate);
                            datePickerFragment.setTargetFragment(SetDateTime.this.getTargetFragment(), DATE_REQUEST_CODE);
                            datePickerFragment.show(fragmentManager, DIALOG_DATE);

                            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mDate);
                            timePickerFragment.setTargetFragment(SetDateTime.this.getTargetFragment(), TIME_REQUEST_CODE);
                            timePickerFragment.show(fragmentManager, DIALOG_TIME);
                        }
                    }
                }).create();
    }
}
