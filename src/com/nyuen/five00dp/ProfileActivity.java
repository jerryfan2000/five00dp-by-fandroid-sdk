package com.nyuen.five00dp;

import android.support.v4.app.Fragment;

import com.nyuen.five00dp.base.BaseActivity;
import com.nyuen.five00dp.fragment.ProfileFragment;

public class ProfileActivity extends BaseActivity {

    @Override
    protected Fragment onCreatePane() {
        return new ProfileFragment();
    }

}
