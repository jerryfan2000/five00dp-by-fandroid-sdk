package com.nyuen.five00dp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nyuen.five00dp.R;

public class FocusableImageView extends ImageView {

    private Drawable mOverlay;
    
    public FocusableImageView(Context context) {
        this(context, null);
    }

    public FocusableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mOverlay = getResources().getDrawable(R.drawable.image_overlay);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mOverlay != null && mOverlay.isStateful()) {
            mOverlay.setState(getDrawableState());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isEnabled()) {
            // not clickable? don't show overlay
            return;
        }

        if (mOverlay == null || mOverlay.getIntrinsicWidth() == 0 ||
                mOverlay.getIntrinsicHeight() == 0) {
            // nothing to draw
            return;
        }

        mOverlay.setBounds(0, 0, getWidth(), getHeight());

        if (getPaddingTop() == 0 && getPaddingLeft() == 0) {
            mOverlay.draw(canvas);
        } else {
            int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            mOverlay.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }
}
