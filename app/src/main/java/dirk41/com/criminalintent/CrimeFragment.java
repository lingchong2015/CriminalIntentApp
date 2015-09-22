package dirk41.com.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/16 0016.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mEditText;
    private Button mCrimeDateButton;
    private CheckBox mCrimeSolvedChectBox;
    private ImageButton mImageButton;
    private ImageView mImageView;
    private Button mDeletePhotoButton;
    private Button mSuspectButton;
    private Button mReportButton;
    private CallBacks mCallBacks;

    //    private static final String TAG = "CrimeFragment";
    public static final String EXTRA_CRIME_ID = "com.dirk41.criminalintent.crime_id";
    public static final String DIALOG_SET = "set";
    public static final String DIALOG_IMAGE = "image";
    private static final int DATE_REQUEST_CODE = 0;
    private static final int TIME_REQUEST_CODE = 1;
    private static final int SET_REQUEST_CODE = 2;
    private static final int PHOTO_REQUEST_CODE = 3;
    private static final int CONTACT_REQUEST_CODE = 4;

    public interface CallBacks {
        void onCrimeUpdated(Crime crime);
    }

    public static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, id);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);

//        this.mCrime = new Crime();
//        UUID id = (UUID)this.getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID id = (UUID) this.getArguments().getSerializable(EXTRA_CRIME_ID);
        this.mCrime = CrimeLab.get(this.getActivity()).getCrime(id);
    }

    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView()");
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(this.getActivity()) != null) {
                ((ActionBarActivity) this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mEditText = (EditText) view.findViewById(R.id.crime_title);
        mCrimeDateButton = (Button) view.findViewById(R.id.crime_date);
        mCrimeSolvedChectBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mImageButton = (ImageButton) view.findViewById(R.id.crime_image_button);
        mImageView = (ImageView) view.findViewById(R.id.crime_image_view);
        mDeletePhotoButton = (Button) view.findViewById(R.id.delete_photo_button);

        mEditText.setText(this.mCrime.getTitle());
        mCrimeSolvedChectBox.setChecked(this.mCrime.isSolved());

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                mCallBacks.onCrimeUpdated(mCrime);
//                getActivity().setTitle(mCrime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateDate(mCrime.getDate());
//        mCrimeDateButton.setEnabled(false);
        mCrimeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = CrimeFragment.this.getActivity().getSupportFragmentManager();
//                DatePickerFragment datePickerFragment = new DatePickerFragment();
//                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
//                datePickerFragment.setTargetFragment(CrimeFragment.this, DATE_REQUEST_CODE);
//                datePickerFragment.show(fragmentManager, DIALOG_DATE);

                SetDateTime setDateTime = SetDateTime.newInstance(mCrime.getDate());
                setDateTime.setTargetFragment(CrimeFragment.this, SET_REQUEST_CODE);
                setDateTime.show(fragmentManager, DIALOG_SET);
            }
        });

        mCrimeSolvedChectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                mCallBacks.onCrimeUpdated(mCrime);
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrimeFragment.this.getActivity(), CrimeCameraActivity.class);
                CrimeFragment.this.startActivityForResult(intent, PHOTO_REQUEST_CODE);
            }
        });

        mDeletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCrime.getPhoto() != null) {
                    String path = getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/" + mCrime.getPhoto().getFilename();
                    File file = new File(path);
                    file.delete();
                    mImageView.setImageDrawable(null);
                    mCrime.setPhoto(null);
                }
            }
        });

        PackageManager packageManager = this.getActivity().getPackageManager();
        boolean hasACamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
                android.hardware.Camera.getNumberOfCameras() > 0;

        if (!hasACamera) {
            mImageButton.setEnabled(false);
        }

        mImageView = (ImageView) view.findViewById(R.id.crime_image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = mCrime.getPhoto();
                if (photo != null) {
                    FragmentManager fragmentManager = CrimeFragment.this.getActivity().getSupportFragmentManager();
                    String path = getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/" + photo.getFilename();
                    ImageFragment.newInstance(path, photo.getRotateAngle()).show(fragmentManager, DIALOG_IMAGE);
                }
            }
        });

        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect_button);

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                if (!isIntentSafe(intent)) {
                    Toast.makeText(getActivity(), "没有相关应用支持！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = Intent.createChooser(intent, getString(R.string.choose_contact));
                startActivityForResult(intent, CONTACT_REQUEST_CODE);
            }
        });

        mReportButton = (Button) view.findViewById(R.id.crime_report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                if (!isIntentSafe(intent)) {
                    Toast.makeText(getActivity(), "没有相关应用支持！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        returnResult();

        return view;
    }

    private boolean isIntentSafe(Intent intent) {
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        int size = resolveInfoList.size();
        return size > 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this.getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(this.getActivity());
                }
                break;
            case R.id.menu_item_delete_crime:
                CrimeLab.get(this.getActivity()).deleteCrime(this.mCrime);
                this.getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case DATE_REQUEST_CODE:
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                modifyDate(date);
                mCallBacks.onCrimeUpdated(mCrime);
                break;
            case TIME_REQUEST_CODE:
                Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATA);
                modifyTime(time);
                mCallBacks.onCrimeUpdated(mCrime);
                break;
            case PHOTO_REQUEST_CODE:
                String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
//                OrientationDescription orientationDescription = (OrientationDescription) data.getSerializableExtra(CrimeCameraFragment.EXTRA_ORIENTATION_DESCRIPTION);
                float rotateAngle = data.getFloatExtra(CrimeCameraFragment.EXTRA_ROTATE_ANGLE, 0);
                if (filename != null) {
//                    Log.i(TAG, "filename: " + filename);
                    if (mCrime.getPhoto() != null) {
                        String path = this.getActivity().getFileStreamPath(mCrime.getPhoto().getFilename()).getAbsolutePath();
                        File file = new File(path);
                        file.delete();
                    }
                    Photo photo = new Photo(filename, rotateAngle);
                    mCrime.setPhoto(photo);
//                    Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
                    showPhoto();
                }
                mCallBacks.onCrimeUpdated(mCrime);
                break;
            case CONTACT_REQUEST_CODE:
                Uri contactUri = data.getData();
                String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
                Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
                if (cursor.getCount() == 0) {
                    cursor.close();
                    return;
                } else {
                    cursor.moveToFirst();
                    String suspect = cursor.getString(0);
                    mCrime.setSuspect(suspect);
                    cursor.close();
                }
                mCallBacks.onCrimeUpdated(mCrime);
                break;
            default:
                break;
        }
    }

    private void modifyDate(Date date) {
        Calendar originCalendar = Calendar.getInstance();
        originCalendar.setTime(mCrime.getDate());

        Calendar currCalendar = Calendar.getInstance();
        currCalendar.setTime(date);

        int year = currCalendar.get(Calendar.YEAR);
        int month = currCalendar.get(Calendar.MONTH);
        int day = currCalendar.get(Calendar.DAY_OF_MONTH);

        originCalendar.set(Calendar.YEAR, year);
        originCalendar.set(Calendar.MONTH, month);
        originCalendar.set(Calendar.DAY_OF_MONTH, day);

        Date resultDate = originCalendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mCrimeDateButton.setText(simpleDateFormat.format(resultDate));
        mCrime.setDate(resultDate);
    }

    private void modifyTime(Date time) {
        Calendar originCalendar = Calendar.getInstance();
        originCalendar.setTime(mCrime.getDate());

        Calendar currCalendar = Calendar.getInstance();
        currCalendar.setTime(time);

        int hour = currCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = currCalendar.get(Calendar.MINUTE);

        originCalendar.set(Calendar.HOUR_OF_DAY, hour);
        originCalendar.set(Calendar.MINUTE, minute);

        Date resultDate = originCalendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mCrimeDateButton.setText(simpleDateFormat.format(resultDate));
        mCrime.setDate(resultDate);
    }

    private void updateDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mCrimeDateButton.setText(simpleDateFormat.format(date));
    }

    private void returnResult() {
        this.getActivity().setResult(Activity.RESULT_OK, null);
    }

    private void showPhoto() {
        Photo photo = mCrime.getPhoto();
        if (photo != null) {
//            String path = this.getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            String path = getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/" + photo.getFilename();
            BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(this.getActivity(), path, photo.getRotateAngle());
            mImageView.setImageDrawable(bitmapDrawable);

        }
    }

    private void cleanPhoto() {
        PictureUtils.cleanImageView(mImageView);
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d(TAG, "onActivityCreated(Bundle)");
//    }
//
    @Override
    public void onStart() {
        super.onStart();

        showPhoto();
    }

    //
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(this.getActivity()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();

        cleanPhoto();
    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d(TAG, "onDestroyView()");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy()");
//    }
//

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBacks = (CallBacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String title = mCrime.getTitle();
        if (title == null || title.length() == 0) {
            title = "Hello";
        }

        String report = getString(R.string.crime_report, title, dateString, solvedString, suspect);

        return report;
    }
}