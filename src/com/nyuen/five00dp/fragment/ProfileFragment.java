package com.nyuen.five00dp.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.kiumiu.ca.api500px.primitiveDataType.UserFullProfile;
import com.kiumiu.ca.api500px.response.user.get_users_show_response;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.response.ProfileResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.FontUtils;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

public class ProfileFragment extends SherlockFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    public static final String INTENT_PROFILE_ID = TAG + ".INTENT_PROFILE_ID";
    
    private ImageFetcher mImageFetcher;
    private ImageView mProfileImage;
    
    private Integer mProfileID;
    private UserFullProfile mUserProfile;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
       
        mProfileID = (Integer) getArguments().getInt(INTENT_PROFILE_ID, 0);
        
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        if (actionBar != null) {
            if(mProfileID == 0)
                actionBar.setTitle(R.string.my_profile);
            else
                actionBar.setTitle(R.string.profile);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);

        }
        
        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new LoadProfileTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu); 
    }  

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            getActivity().finish();
            return true;
        case R.id.menu_profile_share:
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareTitle = mUserProfile.fullname;
            String shareBody = "http://500px.com/" + mUserProfile.username;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "" + getString(R.string.share_via)));
            return true;
        case R.id.menu_profile_browser:      
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("http://500px.com/" + mUserProfile.username));
            startActivity(intent);
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
    
    private void updateHeader(UserFullProfile user) {
        mProfileImage = (ImageView) getActivity().findViewById(R.id.imageViewProfileImage);
        
        TextView profileNameView = (TextView) getActivity().findViewById(R.id.textViewProfileName);
        TextView profileAffection = (TextView) getActivity().findViewById(R.id.textViewProfileAffection);
        TextView profileBio = (TextView) getActivity().findViewById(R.id.textViewProfileBio);
//        TextView profileViews = (TextView) getActivity().findViewById(R.id.textViewProfileViews);
//        TextView profileLikes = (TextView) getActivity().findViewById(R.id.textViewProfileLikes);
//        TextView profileFavs = (TextView) getActivity().findViewById(R.id.textViewProfileFavs);
//        TextView profileComms = (TextView) getActivity().findViewById(R.id.textViewProfileComms);
        
        ImageView profileStatusView = (ImageView) getActivity().findViewById(R.id.imageViewProfileStatus);
        
        profileNameView.setText(user.fullname);
        
        FontUtils.setTypefaceRobotoLight(getActivity(), profileAffection);
        profileAffection.setText("" + user.affection);     
        
        if(!TextUtils.isEmpty(user.about)) {
            profileBio.setText(user.about);
        }
        
        Log.e(TAG, "" + user.upgrade_status);
        if (user.upgrade_status > 0) {
            if (user.upgrade_status == 1) {
                profileStatusView.setImageResource(R.drawable.ic_plus);
            }
            profileStatusView.setVisibility(View.VISIBLE);
        } else {
            profileStatusView.setVisibility(View.GONE);
        }
        
        mImageFetcher.loadImage(user.userpic_url, mProfileImage, R.drawable.ic_userpic);   
    }
    
    private class LoadProfileTask extends AsyncTask<Void, Void, get_users_show_response> {
        protected void onPreExecute() {
            
        }

        protected get_users_show_response doInBackground(Void... params) {
            //android.os.Debug.waitForDebugger();
            if(mProfileID == 0)
                return ApiHelper.getMyProfile(getActivity());
            else {
                if(AccountUtils.hasAccount(getActivity()))
                    return ApiHelper.getUserProfileWithAccount(getActivity(), mProfileID);
                else
                    return ApiHelper.getUserProfile(getActivity(), mProfileID);
            }
        }

        protected void onPostExecute(get_users_show_response response) {
            mUserProfile = response.user;
            updateHeader(response.user);
        }
    }
}
