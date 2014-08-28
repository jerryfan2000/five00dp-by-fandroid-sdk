package com.nyuen.five00dp.view;

import com.nyuen.five00dp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class FadeInTextSwitcher extends TextSwitcher {
    
    private TextView mTextView;

    public FadeInTextSwitcher(Context context) {
        this(context, null);
    }

    public FadeInTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextView = (TextView) findViewById(android.R.id.text1);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_slow));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_slow));
    }
    
    @Override
    public View getCurrentView() {
        return getChildAt(0);
    }
    
    @Override
    public void setDisplayedChild(int whichChild) {
        super.setDisplayedChild(0);
    }
    
    @Override
    public View getNextView() {
        if(mTextView == null)
            mTextView = (TextView) findViewById(android.R.id.text1);
        
        return mTextView;
    }
}
