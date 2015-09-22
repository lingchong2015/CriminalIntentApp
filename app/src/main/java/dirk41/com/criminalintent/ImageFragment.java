package dirk41.com.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/4 0004.
 */
public class ImageFragment extends DialogFragment {
    private ImageView mImageView;
    public static final String EXTRA_IMAGE_PATH = "dirk41.com.criminalintent.image_fragment";
    public static final String EXTRA_ROTATE_ANGLE = "dirk41.com.criminalintent.rotate_angle";

    public static ImageFragment newInstance(String imagePath, float rotateAngle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        bundle.putSerializable(EXTRA_ROTATE_ANGLE, rotateAngle);

        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(bundle);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mImageView = new ImageView(this.getActivity());
        String path = (String) this.getArguments().getSerializable(EXTRA_IMAGE_PATH);
        float rotateAngle = getArguments().getFloat(EXTRA_ROTATE_ANGLE);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(this.getActivity(), path, rotateAngle);
        mImageView.setImageDrawable(bitmapDrawable);
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        PictureUtils.cleanImageView(mImageView);
    }
}
