package dirk41.com.criminalintent;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2015/5/18 0018.
 */
public class CrimeListActivity extends SingleFragmentActiviry implements CrimeListFragment.CallBacks, CrimeFragment.CallBacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragement_container) == null) {
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment oldDetailFragment = fragmentManager.findFragmentById(R.id.detail_fragement_container);
            Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId());

            if (oldDetailFragment != null) {
                fragmentTransaction.remove(oldDetailFragment);
            }

            fragmentTransaction.add(R.id.detail_fragement_container, newDetailFragment).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CrimeListFragment crimeListFragment = (CrimeListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        crimeListFragment.upDateUI();
    }
}
