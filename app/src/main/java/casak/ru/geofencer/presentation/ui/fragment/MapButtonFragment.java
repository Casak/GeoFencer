package casak.ru.geofencer.presentation.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.activities.SettingsActivity;

/**
 * Created on 15.02.2017.
 */

public class MapButtonFragment extends Fragment {
    private static final String TAG = MapButtonFragment.class.getSimpleName();

    private final int MAP_3D = 90;
    private final int MAP_2D = 0;

    @Inject
    GoogleMapPresenter mGoogleMapPresenter;
    @Inject
    CameraPresenter mCameraPresenter;
    @Inject
    FieldRepository mFieldRepository;
    @BindView(R.id.button_follow_type)
    Button mButtonFollow;
    @BindView(R.id.button_3d_2d)
    Button mButton3d2d;

    private boolean mFollow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_button, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.button_tilt_more)
    public void onTiltMoreClicked() {
        mGoogleMapPresenter.onTiltMore();
    }

    @OnClick(R.id.button_tilt_less)
    public void onTiltLessClicked() {
        mGoogleMapPresenter.onTiltLess();
    }

    @OnClick(R.id.button_3d_2d)
    public void on3DClicked() {
        switch (mButton3d2d.getText().toString()) {
            case "3D":
                mGoogleMapPresenter.changeTilt(MAP_3D);
                mButton3d2d.setText("2D");
                break;
            case "2D":
            default:
                mGoogleMapPresenter.changeTilt(MAP_2D);
                mButton3d2d.setText("3D");
        }
    }

    @OnClick(R.id.button_change_type)
    public void onChangeTypeClicked() {
        mGoogleMapPresenter.changeMapType();
    }

    @OnClick(R.id.button_zoom_more)
    public void onZoomMoreClicked() {
        mGoogleMapPresenter.onZoomMore();
    }

    @OnClick(R.id.button_zoom_less)
    public void onZoomLessClicked() {
        mGoogleMapPresenter.onZoomLess();
    }

    @OnClick(R.id.button_settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_load)
    public void onLoadClicked() {
        final int[] ids = mFieldRepository.getAllFieldIds();
        CharSequence[] idsString = new CharSequence[ids.length];

        //TODO Map field names instead (create field name column too)
        for (int i = 0; i < ids.length; i++) {
            idsString[i] = ids[i] + "";
        }

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("Please, choose Field!")
                .setItems(idsString,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mGoogleMapPresenter.onFieldLoad(ids[which]);
                            }
                        })
                .create();

        alert.show();
    }

    @OnClick(R.id.button_follow_type)
    public void onFollowClicked() {
        if (mFollow) {
            switch (mButtonFollow.getText().toString()) {
                case "NonFollow":
                    mCameraPresenter.setFollowType(CameraPresenter.FollowType.FOLLOW_POINT);
                    mButtonFollow.setText("PntFollow");
                    break;
                case "PntFollow":
                    mCameraPresenter.setFollowType(CameraPresenter.FollowType.FOLLOW_ROUTE);
                    mButtonFollow.setText("RtFollow");
                    break;
                case "RtFollow":
                default:
                    mFollow = false;
                    mButtonFollow.setText("NonFollow");
                    mCameraPresenter.setFollowType(CameraPresenter.FollowType.NON_FOLLOW);
            }
        } else {
            mFollow = true;
            mButtonFollow.setText("PntFollow");
            mCameraPresenter.setFollowType(CameraPresenter.FollowType.FOLLOW_POINT);
        }
    }
}
