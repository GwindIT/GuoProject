package com.idsmanager.eagleeye.ui.fragment.addnewsfragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.constants.Constants;
import com.idsmanager.eagleeye.net.NetService;
import com.idsmanager.eagleeye.ui.fragment.BaseFragment;
import com.idsmanager.eagleeye.utils.SubmitUtils;
import com.idsmanager.eagleeye.utils.ToastUtil;

/**
 * Created by wind on 2016/1/22.
 */
public class TreeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = TreeFragment.class.getSimpleName();
    private static final int TAKE_PHOTO_WITH_DATA = 1;

    protected FrameLayout treeFragment;
    protected Button button;
    protected Button getPhoto;
    protected boolean success;
    protected LatLng latLng;
    protected TextView latitude;
    protected TextView longitude;
    private ImageView imageView;

    @Override
    protected View createAddNewsView() {
        treeFragment = (FrameLayout) View.inflate(getActivity(), R.layout.fragment_tree_add, null);
        button = (Button) treeFragment.findViewById(R.id.bt_submit);
        button.setOnClickListener(this);

        getPhoto = (Button) treeFragment.findViewById(R.id.bt_get_photo);
        getPhoto.setOnClickListener(this);

        latitude = (TextView) treeFragment.findViewById(R.id.press_latitude);
        longitude = (TextView) treeFragment.findViewById(R.id.press_longitude);

        imageView = (ImageView) treeFragment.findViewById(R.id.iv_get_photo);
        latitude.setText(latLng.latitude + "");
        longitude.setText(latLng.longitude + "");
        return treeFragment;
    }

    @Override
    protected Object loadData() {
        return Constants.ADD_PAGE;
    }

    @Override
    protected void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    protected void setTitle(String title) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                success = SubmitUtils.submitData(NetService.ADD_MARK_URL,null);
                if (success) {
                    ToastUtil.showToast(getActivity().getString(R.string.submit_success));
                } else {
                    ToastUtil.showToast(getActivity().getString(R.string.submit_failed));
                }
                break;
            case R.id.bt_get_photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_WITH_DATA);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_WITH_DATA && resultCode == getActivity().RESULT_OK) {
            final Bitmap photo = data.getParcelableExtra("data");
            imageView.setImageBitmap(photo);
        }
    }
}
