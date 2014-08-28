package com.nyuen.five00dp.base;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nyuen.five00dp.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseActivity extends SherlockFragmentActivity {
    private Fragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        if (savedInstanceState == null) {
            performFragmentTransaction();
        } else {
            mFragment = getSupportFragmentManager().findFragmentByTag("single_pane");
        }
    }
    
    protected void performFragmentTransaction() {
        mFragment = onCreatePane();
        mFragment.setArguments(intentToFragmentArguments(getIntent()));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_frame, mFragment, "single_pane")
                .commit();
    }
    
    protected abstract Fragment onCreatePane();
    
    protected int getLayoutId() {
        return R.layout.activity_fragment_frame;
    }

    public Fragment getFragment() {
        return mFragment;
    }
    
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }
    

}
