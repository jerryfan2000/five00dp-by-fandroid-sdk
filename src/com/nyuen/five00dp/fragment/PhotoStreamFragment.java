package com.nyuen.five00dp.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.kiumiu.ca.api500px.primitiveDataType.PhotoShort;
import com.kiumiu.ca.api500px.response.photo.get_photos_response;
import com.nyuen.five00dp.LoginActivity;
import com.nyuen.five00dp.ProfileActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.adapter.PhotoAdapter;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.base.BaseSlidingActivity;
import com.nyuen.five00dp.response.PhotoListResponse;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

public class PhotoStreamFragment extends SherlockListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = PhotoStreamFragment.class.getSimpleName();
    
    public static final int IMAGE_PER_PAGE = 25;
    public static final int DEFAULT_IMAGE_SIZE = 4;
    
    private PhotoAdapter mImageAdapter;
    private ImageFetcher mImageFetcher;
    private View mLoadingView;
    
    private int mPage = 1;
    private boolean mLoading = false;
    private String mFeature = "popular";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        // ActionBar customisation
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
   
        // Instantiate members
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mFeature = "popular";
        
        // Attach callback for feature list 
        String[] featureString = getResources().getStringArray(R.array.feature);
        ArrayAdapter<String> featureOptionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.feature_action_bar, featureString);
        getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(featureOptionAdapter, new OnNavigationListener() {         
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                mPage = 1;
                switch (itemPosition) {
                case 0:
                    mFeature = "popular";
                    break;
                case 1:
                    mFeature = "editors";
                    break;
                case 2:
                    mFeature = "upcoming";
                    break;
                case 3:
                    mFeature = "fresh_today";
                    break;
                }
                mImageAdapter = new PhotoAdapter(getActivity(), mImageFetcher);
                new LoadPhotoTask().execute();
                return false;
            }
        });
        
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_stream, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextIntent;
        switch (item.getItemId()) {    
            case android.R.id.home:
                ((BaseSlidingActivity) getActivity()).toggle();
                return true;
            case R.id.menu_refresh:
                mPage = 1;
                new LoadPhotoTask().execute();
                return true;
            case R.id.menu_profile:
                if (AccountUtils.hasAccount(getActivity())){
                    nextIntent = new Intent(getActivity(), ProfileActivity.class);
                } else {
                    nextIntent = new Intent(getActivity(), LoginActivity.class);
                }
                getActivity().startActivity(nextIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) { 
        boolean loadMore = firstVisibleItem + visibleItemCount + 1 >= totalItemCount;
        
        if (loadMore && !mLoading) {
            new LoadPhotoTask().execute();
        }
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Pause disk cache access to ensure smoother scrolling
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
				|| scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			mImageFetcher.setPauseWork(true);
		} else {
			mImageFetcher.setPauseWork(false);
		}
    }  
    
    private void updateList(get_photos_response response) {
        if (mPage == 1) {
            mImageAdapter = new PhotoAdapter(getActivity(), mImageFetcher);
            ArrayList<PhotoShort> list = new ArrayList();
            for(int x=0; x<response.photos.length; x++)
            	list.add(response.photos[x]);
            mImageAdapter.setPhotos(list);     
            setListAdapter(mImageAdapter);
            getListView().setOnScrollListener(this);           
        } else {   
        	ArrayList<PhotoShort> list = new ArrayList();
            for(int x=0; x<response.photos.length; x++)
            	list.add(response.photos[x]);
            mImageAdapter.appendPhotos(list);
            mImageAdapter.notifyDataSetChanged();
        }
    }
    
    private class LoadPhotoTask extends AsyncTask<Void, Void, get_photos_response> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
            
            if (mPage == 1) {
                getSherlockActivity().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                getSherlockActivity().findViewById(android.R.id.list).setVisibility(View.INVISIBLE);
            }
        }

        protected get_photos_response doInBackground(Void... params) {
            return ApiHelper.getPhotoStream(getActivity(), mFeature, IMAGE_PER_PAGE, DEFAULT_IMAGE_SIZE, mPage);
        }

        protected void onPostExecute(get_photos_response response) {
            if(getActivity() == null)
                return;
            
            mLoadingView.setVisibility(View.GONE);
            mLoading = false;
            
            if (response != null) {
                updateList(response);
                mPage++;
            } else {
               this.cancel(false);
               ((ProgressBar) getSherlockActivity().findViewById(R.id.emptyProgressBar)).setVisibility(View.GONE);
               ((TextView) getSherlockActivity().findViewById(R.id.emptyErrorView)).setVisibility(View.VISIBLE);
            }
        }
        
    }
}
