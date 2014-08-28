package com.nyuen.five00dp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nyuen.five00dp.R;
import com.nyuen.five00dp.base.BaseActivity;
import com.nyuen.five00dp.fragment.PhotoDetailFragment;

public class PhotoDetailActivity extends BaseActivity {
    
    @Override
    protected Fragment onCreatePane() {
        return new PhotoDetailFragment();
    }

}
