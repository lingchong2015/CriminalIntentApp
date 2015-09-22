package dirk41.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/18 0018./.
 */
public class CrimePagerActivity extends ActionBarActivity implements CrimeFragment.CallBacks {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    private static final String TAG = "CrimePagerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        this.setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                return;
            }

            @Override
            public void onPageSelected(int position) {
                if (mCrimes.get(position).getTitle() != null) {
                    CrimePagerActivity.this.setTitle(mCrimes.get(position).getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                return;
            }
        });

        UUID id = (UUID) this.getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        int i = 0;
        for (; i < mCrimes.size(); ++i) {
            if (mCrimes.get(i).getId().equals(id)) {
                break;
            }
        }
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
