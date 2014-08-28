package com.nyuen.five00dp;

import android.graphics.Canvas;
import android.view.animation.Interpolator;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nyuen.five00dp.R;
import com.nyuen.five00dp.base.BaseSlidingActivity;
import com.nyuen.five00dp.fragment.MenuFragment;
import com.nyuen.five00dp.fragment.PhotoStreamFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class PhotoStreamActivity extends BaseSlidingActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        //sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(0.0f);
//        sm.setBehindCanvasTransformer(new CanvasTransformer() {
//            @Override
//            public void transformCanvas(Canvas canvas, float percentOpen) {
//                float scale = (float) (percentOpen*0.25 + 0.75);
//                canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
//            }        
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    protected Fragment onCreatePane() {
        return new PhotoStreamFragment();
    }
    
    @Override
    protected Fragment onCreateMenuPane() {
        return new MenuFragment();
    }

}
