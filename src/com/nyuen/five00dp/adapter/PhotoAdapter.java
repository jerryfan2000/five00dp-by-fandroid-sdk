package com.nyuen.five00dp.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kiumiu.ca.api500px.primitiveDataType.PhotoShort;
import com.nyuen.five00dp.PhotoStreamActivity;
import com.nyuen.five00dp.PhotoDetailActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.fragment.PhotoDetailFragment;
import com.nyuen.five00dp.fragment.PhotoStreamFragment;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.PhotoPatternContainer.Pattern;
import com.nyuen.five00dp.structure.PhotoPatternContainer;
import com.nyuen.five00dp.util.ImageFetcher;

public class PhotoAdapter extends BaseAdapter {

    private final int WIDTH_ONE;
    private final int MARGIN_ONE;
    
    private final int[] IMAGEVIEW_IDS = { 
            R.id.imageView0, 
            R.id.imageView1, 
            R.id.imageView2,
            R.id.imageView3 
    };

    private final LayoutInflater mInflater;
    private final ImageFetcher mImageFetcher;

    private List<PhotoShort> mPhotos;
    private List<PhotoPatternContainer> mContainers;

    private int mListItemCount;
    private int mPhotoCount;
    
    private OnClickListener mOnPhotoClickListener;
   
    public PhotoAdapter(final Context context, ImageFetcher imageFetcher) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Resources resources = context.getResources();
        
        // Calculate sizes
        WIDTH_ONE = Math.min(display.getWidth(), display.getHeight());
        MARGIN_ONE = resources.getDimensionPixelSize(R.dimen.image_grid_margin);

        // Instantiate members
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        mContainers = new ArrayList<PhotoPatternContainer>();
        mPhotos = new ArrayList<PhotoShort>();

        // Attach click listener for each photo
        mOnPhotoClickListener = new OnClickListener() {
            public void onClick(View v) {
            	PhotoShort tempPhoto = (PhotoShort) v.getTag();
                Intent photoDetailIntent = new Intent(context, PhotoDetailActivity.class);
                photoDetailIntent.putExtra(PhotoDetailFragment.INTENT_EXTRA_PHOTO, tempPhoto);
                context.startActivity(photoDetailIntent);
            }
        };   

    }

    public int getCount() {
        return mListItemCount;
    }

    public Object getItem(int position) {
        return position;
    }

    public void setPhotos(ArrayList<PhotoShort> photos) {
        mPhotoCount = 0;
        mPhotos.clear();
        appendPhotos(photos);
    }

    public void appendPhotos(ArrayList<PhotoShort> photos) {  
        mPhotos.addAll(photos);

        // Retrieve a list of random patterns 
        List<Pattern> patterns = Pattern.getPatternList();

        // Loop through the patterns, 
        // create a container object that store a pattern and the respective photo index
        Iterator<Pattern> iterator = patterns.iterator();
        while (iterator.hasNext()) {
            Pattern tempPattern = iterator.next();
            PhotoPatternContainer tempContainer = new PhotoPatternContainer(tempPattern);
            mContainers.add(tempContainer);

            for (int i = 0; i < tempPattern.getCount(); i++) {
                mContainers.get(mListItemCount).addPhotoID(mPhotoCount);
                mPhotoCount++;
            }

            mListItemCount++;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder tempHolder;

        if (convertView == null) {
            // Inflate the imageViews
            tempHolder = new ImageHolder();
            tempHolder.imageViews = new ImageView[4];
            convertView = mInflater.inflate(R.layout.list_cell_photo_grid, null, false);	

            // Set click listener for each imageView
            for (int i = 0; i < tempHolder.imageViews.length; i++) {
                tempHolder.imageViews[i] = (ImageView) convertView.findViewById(IMAGEVIEW_IDS[i]);
                tempHolder.imageViews[i].setOnClickListener(mOnPhotoClickListener);
            }

            convertView.setTag(tempHolder);
        } else {
            tempHolder = (ImageHolder) convertView.getTag();
        }

        ImageView[] imageViews = tempHolder.imageViews;
        PhotoPatternContainer tempContainer = mContainers.get(position);
        Pattern tempPattern = tempContainer.getPattern();
        RelativeLayout.LayoutParams[] tempParams = tempPattern.getParams(WIDTH_ONE, MARGIN_ONE);
        List<Integer> photoIndices = tempContainer.getPhotosIdx();

        // Loop through each imageView and retrieve the specific photo url from mPhotos
        // Then, load the image with imageFetcher 
        for (int i = 0; i < 4; i++) {
            if (i < photoIndices.size()) {
                PhotoShort tempPhoto = mPhotos.get(photoIndices.get(i));
                String url = tempPhoto.image_url.replace(PhotoStreamFragment.DEFAULT_IMAGE_SIZE + ".jpg", tempPattern.getSizes()[i] + ".jpg");
                
                imageViews[i].setVisibility(View.VISIBLE);
                imageViews[i].setBackgroundColor(Color.WHITE);
                imageViews[i].setTag(tempPhoto);
                mImageFetcher.loadImage(url, imageViews[i]);
            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }

        // Set LayoutParams based on the pattern
        for (int i = 0; i < tempParams.length; i++) {
            imageViews[i].setLayoutParams(tempParams[i]);
        }
        
        return convertView;
    }

    private class ImageHolder {
        ImageView[] imageViews;
    }
}
