package dirk41.com.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/6/2 0002.
 */

@SuppressWarnings("deprecation")
public class CrimeCameraFragment extends Fragment {
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressBarContainer;
    private MyOrientationDetector myOrientationDetector;
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    };
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String filename = UUID.randomUUID().toString() + ".jpg";
            File file = new File(getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(), filename);
            FileOutputStream fileOutputStream = null;
            boolean success = true;

            try {
//                fileOutputStream = CrimeCameraFragment.this.getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
//                fileOutputStream.write(data);
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception ex) {
                Log.e(TAG, "Error writing to file " + filename, ex);
                success = true;
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        success = false;
                    }
                }
            }

            if (success) {
//                detectDeviceOrientation();

                Log.i(TAG, "JPEG saved at " + filename);
//                Log.i(TAG, "Orientation is at " + mOrientationDescription.toString());
                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME, filename);
//                intent.putExtra(EXTRA_ORIENTATION_DESCRIPTION, mOrientationDescription);
                intent.putExtra(EXTRA_ROTATE_ANGLE, mRotateAngle);
                CrimeCameraFragment.this.getActivity().setResult(Activity.RESULT_OK, intent);
            } else {
                CrimeCameraFragment.this.getActivity().setResult(Activity.RESULT_CANCELED);
            }

            CrimeCameraFragment.this.getActivity().finish();
        }
    };
//    private OrientationDescription mOrientationDescription = OrientationDescription.normal;
    private float mRotateAngle = 0;
    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "dirk41.com.criminalintent.photo_filename";
    public static final String EXTRA_ORIENTATION_DESCRIPTION = "dirk41.com.criminalintent.orientation_description";
    public static final String EXTRA_ROTATE_ANGLE = "dirk41.com.criminalintent.rotate_angle";

    //    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);

        myOrientationDetector = new MyOrientationDetector(this.getActivity());

        mProgressBarContainer = view.findViewById(R.id.crime_camera_progress_container);
        mProgressBarContainer.setVisibility(View.INVISIBLE);

        (view.findViewById(R.id.crime_camera_take_picture_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mPictureCallback);
                }
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surface_view);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) {
                    return;
                }

                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(size.width, size.height);
                size = getBestSupportedSize(parameters.getSupportedPictureSizes());
                parameters.setPictureSize(size.width, size.height);
                mCamera.setParameters(parameters);

                try {
                    mCamera.startPreview();
                } catch(Exception ex) {
                    Log.e(TAG, ex.toString());
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });

        return view;
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size size : sizes) {
            int area = size.width * size.height;
            if (area > largestArea) {
                bestSize = size;
                largestArea = area;
            }
        }

        return bestSize;
    }

//    @TargetApi(9)
//    private String detectDeviceOrientation() {
//        int orientation = this.getActivity().getResources().getConfiguration().orientation;
//        String orientName;
//        switch (orientation) {
//            case Configuration.ORIENTATION_LANDSCAPE:
//                orientName = "landscape";
//                break;
//            case Configuration.ORIENTATION_PORTRAIT:
//                orientName = "portrait";
//                break;hyj
//            default:
//                orientName = "undefine";
//                break;
//        }
//
//        return orientName;
//    }

    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }

        myOrientationDetector.enable();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

        myOrientationDetector.disable();
    }

    private class MyOrientationDetector extends OrientationEventListener {

        public MyOrientationDetector(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            mRotateAngle = orientation;
        }
    }
}
