package dirk41.com.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/4 0004.
 */
public class PictureUtils {
    public static BitmapDrawable getScaledDrawable(Activity activity, String path, float rotateAngle) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = rotate(bitmap, rotateAngle);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }

    private static Bitmap rotate(Bitmap bitmap, float rotateAngle) {
        int degree = 0;
        if (rotateAngle >= 90 && rotateAngle < 180) {
            //leftSideUp
            degree = 90;
        } else if (rotateAngle >= 180 && rotateAngle < 270) {
           //bottomSideUp
            degree = 180;
        } else if (rotateAngle >= 270 && rotateAngle < 359){
            //rightSideUp
            degree = 270;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void cleanImageView(ImageView imageView) {
        if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        bitmapDrawable.getBitmap().recycle();
        imageView.setImageBitmap(null);
    }
}
