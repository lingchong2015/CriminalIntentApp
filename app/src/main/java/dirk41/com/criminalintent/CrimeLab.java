package dirk41.com.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/18 0018.
 */
public class CrimeLab {
    private ArrayList<Crime> mCrimes;
    private Context mAppContext;
    private CriminalIntentJSONSerializer mCriminalIntentJSONSerializer;
    private static CrimeLab sCrimeLab;
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private CrimeLab(Context appContext) {
        this.mAppContext = appContext;
//        this.mCrimes = new ArrayList<Crime>(100);
        mCriminalIntentJSONSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimes = mCriminalIntentJSONSerializer.loadCrimes();
        } catch (Exception ex) {
            mCrimes = new ArrayList<Crime>();
        }

//        for (int i = 0; i < 100; ++i) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            this.mCrimes.add(crime);
//        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            return CrimeLab.sCrimeLab = new CrimeLab(context.getApplicationContext());
        } else {
            return sCrimeLab;
        }
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : this.mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public boolean saveCrimes() {
        try {
            mCriminalIntentJSONSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "Crimes saved to file");
            return true;
        } catch (Exception ex) {
            Log.e(TAG, "Error saving crimes: " + ex);
            return false;
        }
    }
}
