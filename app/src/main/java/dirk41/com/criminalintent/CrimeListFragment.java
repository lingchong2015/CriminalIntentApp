package dirk41.com.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/18 0018.
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;
    private CallBacks mCallbacks;

    private static final String TAG = "CrimeListFragment";
    private static final int REQUSET_CRIME = 1;

    public interface CallBacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        setRetainInstance(true);
        mSubtitleVisible = false;

        this.getActivity().setTitle(R.string.crimes_title);
        this.mCrimes = CrimeLab.get(this.getActivity()).getCrimes();

//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(this.getActivity(), android.R.layout.simple_list_item_1, this.mCrimes);
        CrimeAdapter adapter = new CrimeAdapter(this.mCrimes);
        this.setListAdapter(adapter);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Button addButton = (Button) view.findViewById(R.id.add_crime_from_no_data_view);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(CrimeListFragment.this.getActivity()).addCrime(crime);
                Intent intent = new Intent(CrimeListFragment.this.getActivity(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                CrimeListFragment.this.startActivityForResult(intent, REQUSET_CRIME);
            }
        });

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                ((ActionBarActivity) this.getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            }
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter crimeAdapter = (CrimeAdapter) CrimeListFragment.this.getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(CrimeListFragment.this.getActivity());
                            for (int i = crimeAdapter.getCount() - 1; i >= 0; --i) {
                                if (CrimeListFragment.this.getListView().isItemChecked(i)) {
                                    crimeLab.deleteCrime(crimeAdapter.getItem(i));
                                }
                            }
                            mode.finish();
                            crimeAdapter.notifyDataSetChanged();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } else {
            registerForContextMenu(listView);
        }

        //默认选中首行。
        if (mCrimes.size() > 0) {
            onListItemClick(listView, view, 0, 0);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((CrimeAdapter) this.getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(this.getActivity()).saveCrimes();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CallBacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void upDateUI() {
        ((CrimeAdapter) this.getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        Log.d(TAG, ((Crime) this.getListAdapter().getItem(position)).toString() + "was clicked.");
//        Intent intent = new Intent(this.getActivity(), CrimeActivity.class);
//        Intent intent = new Intent(this.getActivity(), CrimePagerActivity.class);
//        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, ((Crime) this.getListAdapter().getItem(position)).getId());
//        this.startActivity(intent);
//        this.startActivityForResult(intent, REQUSET_CRIME);
//        this.startActivityForResult(intent, REQUSET_CRIME);

        mCallbacks.onCrimeSelected((Crime) getListAdapter().getItem(position));
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(CrimeListFragment.this.getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = CrimeListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            Crime crime = this.getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(crime.getTitle());
            TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateTextView.setText(simpleDateFormat.format(crime.getDate()));
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(crime.isSolved());

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUSET_CRIME:
                Log.d(TAG, String.valueOf(resultCode));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(this.getActivity()).addCrime(crime);
//                Intent intent = new Intent(this.getActivity(), CrimePagerActivity.class);
//                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//                this.startActivityForResult(intent, REQUSET_CRIME);
                ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
                mCallbacks.onCrimeSelected(crime);
                break;
            case R.id.menu_item_show_subtitle:
                if (((ActionBarActivity) this.getActivity()).getSupportActionBar().getSubtitle() == null) {
                    ((ActionBarActivity) this.getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    menuItem.setTitle(R.string.hide_subtitle);
                    mSubtitleVisible = true;
                } else {
                    ((ActionBarActivity) this.getActivity()).getSupportActionBar().setSubtitle(null);
                    menuItem.setTitle(R.string.show_subtitle);
                    mSubtitleVisible = false;
                }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        this.getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = adapterContextMenuInfo.position;
        CrimeAdapter crimeAdapter = (CrimeAdapter) this.getListAdapter();
        Crime crime = crimeAdapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(this.getActivity()).deleteCrime(crime);
                crimeAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }
}
