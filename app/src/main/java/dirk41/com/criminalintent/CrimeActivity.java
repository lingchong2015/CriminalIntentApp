package dirk41.com.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.UUID;
import android.view.Menu;
import android.view.MenuItem;


public class CrimeActivity extends /*FragmentActivity*/ SingleFragmentActiviry {
    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();
        UUID id = (UUID) this.getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(id);
    }

//    private static final String TAG = "CrimeActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        if (fragment == null) {
//            fragment = new CrimeFragment();
//            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
//        }
//    }

//    @Override`
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy()");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause()");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume()");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart()");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop()");
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fragment_crime_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
