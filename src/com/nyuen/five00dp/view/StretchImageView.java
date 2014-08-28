package com.nyuen.five00dp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @see http://stackoverflow.com/questions/5554682/android-imageview-adjusting-
 *      parents-height-and-fitting-width
 */
public class StretchImageView extends ImageView {

	public StretchImageView(Context context) {
		super(context);
	}

	public StretchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = getDrawable();

		if (d != null) {
			// ceil not round - avoid thin vertical gaps along the left/right
			// edges
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = (int) Math.ceil((float) width 
					* (float) d.getIntrinsicHeight()
					/ (float) d.getIntrinsicWidth());
			
			height = Math.max(height, getSuggestedMinimumHeight());
			
			setMeasuredDimension(width, height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
