package dirk41.com.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/4 0004.
 */
public class Photo {
    private String mFilename;
    private float mRotateAngle;
    private static final String JSON_FINENAME = "filename";
    private static final String JSON_ORIENTATION_DESCRIPTION = "orientation_description";
    //    private OrientationDescription mOrientationDescription = OrientationDescription.normal;
    private static final String JSON_ROTATE_ANGLE = "rotate_angle";

    public Photo(String filename, float rotateAngle) {
        mFilename = filename;
//        mOrientationDescription = orientationDescription;
        mRotateAngle = rotateAngle;
    }

    public Photo(JSONObject jsonObject) throws JSONException {
        mFilename = jsonObject.getString(JSON_FINENAME);
        mRotateAngle = (float) jsonObject.getDouble(JSON_ROTATE_ANGLE);
//        switch (jsonObject.getString(JSON_ORIENTATION_DESCRIPTION)) {
//            case "normal":
//                mOrientationDescription = OrientationDescription.normal;
//                break;
//            case "leftSideUp":
//                mOrientationDescription = OrientationDescription.leftSideUp;
//                break;
//            case "bottomSideUp":
//                mOrientationDescription = OrientationDescription.bottomSideUp;
//                break;
//            case "rightSideUp":
//                mOrientationDescription = OrientationDescription.rightSideUp;
//                break;
//            default:
//                mOrientationDescription = OrientationDescription.unkown;
//                break;
//        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_FINENAME, mFilename);
//        jsonObject.put(JSON_ORIENTATION_DESCRIPTION, mOrientationDescription.toString());
        jsonObject.put(JSON_ROTATE_ANGLE, mRotateAngle);
        return jsonObject;
    }

    public String getFilename() {
        return mFilename;
    }

    public float getRotateAngle() {
        return mRotateAngle;
    }
}
