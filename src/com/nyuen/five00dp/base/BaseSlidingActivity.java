package com.nyuen.five00dp.base;

import com.nyuen.five00dp.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseSlidingActivity extends SlidingFragmentActivity {
    private Fragment mFragment;
    private Fragment mMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setBehindContentView(R.layout.sliding_menu_frame);

        if (savedInstanceState == null) {
            performFragmentTransaction();
        } else {
            mFragment = getSupportFragmentManager().findFragmentByTag("single_pane");
            mMenuFragment= getSupportFragmentManager().findFragmentByTag("menu_pane");
        }
    }
    
    protected void performFragmentTransaction() {
        mFragment = onCreatePane();
        mFragment.setArguments(intentToFragmentArguments(getIntent()));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_frame, mFragment, "single_pane")
                .commit();
        
        mMenuFragment = onCreateMenuPane();
        mMenuFragment.setArguments(intentToFragmentArguments(getIntent()));
        getSupportFragmentManager().beginTransaction()       
                .add(R.id.sliding_menu_frame, mMenuFragment, "menu_pane")
                .commit();        
    }
    
    protected abstract Fragment onCreatePane();
    protected abstract Fragment onCreateMenuPane();
    
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
