package dirk41.com.criminalintent;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;
//    private static final String TAG = "JSONSerializer";

    public CriminalIntentJSONSerializer(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for (Crime crime : crimes) {
            jsonArray.put(crime.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream outputStream = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = mContext.openFileInput(mFilename);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONArray jsonArray = (JSONArray) new JSONTokener(stringBuilder.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); ++i) {
                crimes.add(new Crime(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException ex) {

        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return crimes;
    }
}
