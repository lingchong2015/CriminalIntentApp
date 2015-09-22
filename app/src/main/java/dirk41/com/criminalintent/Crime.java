package dirk41.com.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PipedReader;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/16 0016.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mSuspect;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";

    public Crime() {
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
    }

    public Crime(JSONObject jsonObject) throws JSONException {
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_TITLE)) {
            mTitle = jsonObject.getString(JSON_TITLE);
        }
        mDate = new Date(jsonObject.getLong(JSON_DATE));
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        if (jsonObject.has(JSON_PHOTO)) {
            mPhoto = new Photo(jsonObject.getJSONObject(JSON_PHOTO));
        }
        if (jsonObject.has(JSON_SUSPECT)) {
            mSuspect = jsonObject.getString(JSON_SUSPECT);
        }
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    @Override
    public String toString() {
        return this.mTitle;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, mId.toString());
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_DATE, mDate.getTime());
        jsonObject.put(JSON_SOLVED, mSolved);
        jsonObject.put(JSON_PHOTO, mPhoto.toJSON());
        jsonObject.put(JSON_SUSPECT, mSuspect);

        return jsonObject;
    }
}
