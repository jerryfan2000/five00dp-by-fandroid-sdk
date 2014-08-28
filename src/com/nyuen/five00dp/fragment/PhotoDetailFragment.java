package com.nyuen.five00dp.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.kiumiu.ca.api500px.primitiveDataType.Comment;
import com.kiumiu.ca.api500px.primitiveDataType.PhotoFull;
import com.kiumiu.ca.api500px.primitiveDataType.PhotoShort;
import com.kiumiu.ca.api500px.response.photo.get_photo_id_comments_response;
import com.kiumiu.ca.api500px.response.photo.get_photos_id_response;
import com.nyuen.five00dp.ProfileActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.adapter.CommentAdapter;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.response.CommentResponse;
import com.nyuen.five00dp.response.PostResponse;
import com.nyuen.five00dp.response.PhotoDetailResponse;
import com.nyuen.five00dp.response.PhotoResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.Photo.Category;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.DateHelper;
import com.nyuen.five00dp.util.FontUtils;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;
import com.nyuen.five00dp.view.FadeInTextSwitcher;

import java.util.ArrayList;

public class PhotoDetailFragment extends SherlockListFragment implements AbsListView.OnScrollListener {

    private static final String TAG = PhotoDetailFragment.class.getSimpleName();

    public static final String INTENT_EXTRA_PHOTO = TAG + ".INTENT_EXTRA_PHOTO";

    private CommentAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;

    private boolean mLoadingComments = false;
    private View mHeaderView;
    private View mHeaderExifView;
    private View mLoadingView;
    private View mUserActionBar;
    private PhotoFull mPhoto;
    private PhotoShort photoShort;
    private int mPage = 1;
    private int mTotalPage = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mHeaderView = inflater.inflate(R.layout.header_photo_detail, null);
        mHeaderExifView = inflater.inflate(R.layout.header_photo_exif, null);
        mLoadingView = inflater.inflate(R.layout.list_footer, null);
        photoShort = (PhotoShort) getArguments().getParcelable(INTENT_EXTRA_PHOTO);
        mPhotoDetailAdapter = new CommentAdapter(getActivity(), mImageFetcher);

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(photoShort.name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
        }

        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);   
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addHeaderView(mHeaderView, null, false);
        getListView().addHeaderView(mHeaderExifView, null, false);
        getListView().addFooterView(mLoadingView);

        updateHeaderView();
        updateHeaderExifView();

        setListAdapter(mPhotoDetailAdapter);

        new LoadPhotoDetailTask().execute("INIT");
    }

    private void updateUserActionBar() {
        if(AccountUtils.hasAccount(getActivity())) {
            mUserActionBar = getActivity().findViewById(R.id.userActionBar);
            mUserActionBar.setVisibility(View.VISIBLE);
            Animation appearAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.gd_grow_from_bottom);
            mUserActionBar.startAnimation(appearAnim);

            ImageButton btnLike = (ImageButton) getActivity().findViewById(R.id.btnPhotoLike);
            ImageButton btnFav = (ImageButton) getActivity().findViewById(R.id.btnPhotoFav);
            EditText etComment = (EditText) getActivity().findViewById(R.id.etPhotoComment);

            Log.e(TAG,"id " + mPhoto.id + " v:" + mPhoto.voted);

            if(mPhoto.voted) {
                btnLike.setImageResource(R.drawable.ic_action_liked);
                btnLike.setClickable(false);
            } else {
                btnLike.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ImageButton) v).setImageResource(R.drawable.ic_action_liked);
                        v.setClickable(false);
                        new VotePhotoTask().execute();
                    }
                });
            }

            btnFav.setImageResource(mPhoto.favorited ? R.drawable.ic_action_faved : R.drawable.ic_action_fav);

            btnFav.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ImageButton) v).setImageResource(!mPhoto.favorited ? R.drawable.ic_action_faved : R.drawable.ic_action_fav);
                    v.setClickable(false);
                    new FavPhotoTask().execute();
                }
            });

            etComment.setOnClickListener(new OnClickListener() {               
                Builder builder = new AlertDialog.Builder(getActivity());       
                AlertDialog commentDialog;

                @Override
                public void onClick(View v) {
                    builder.setTitle("Post Comment");
                    builder.setPositiveButton("Post", new OkOnClickListener());
                    builder.setNegativeButton("Cancel", new CancelOnClickListener());
                    builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_photo_comment, null));
                    commentDialog = builder.create();
                    commentDialog.show();
                }

                final class OkOnClickListener implements
                DialogInterface.OnClickListener {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView comment = (TextView) commentDialog.findViewById(R.id.etCommentDialog);
                        //Toast.makeText(getActivity(), comment.getText(), Toast.LENGTH_SHORT).show();
                        new CommPhotoTask().execute(comment.getText().toString());
                        dialog.dismiss();          
                    }
                }

                final class CancelOnClickListener implements
                DialogInterface.OnClickListener {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
            });

        }
    }

    private void updateVotes() {
        FadeInTextSwitcher votesCountView = (FadeInTextSwitcher) mHeaderView.findViewById(R.id.votesCountView);
        votesCountView.setText(getString(R.string.num_votes, mPhoto.votes_count));
    }

    private void updateFavs() {
        ImageButton btnFav = (ImageButton) getActivity().findViewById(R.id.btnPhotoFav);
        FadeInTextSwitcher favsCountView = (FadeInTextSwitcher) mHeaderView.findViewById(R.id.favsCountView);
        favsCountView.setText(getString(R.string.num_favorites, mPhoto.favorites_count));
        btnFav.setClickable(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_photo_details, menu);
    }  

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            getActivity().finish();
            return true;
        case R.id.menu_photo_share:
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareTitle = mPhoto.name;
            String shareBody = "http://500px.com/photo/" + mPhoto.id;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "" + getString(R.string.share_via)));
            return true;    
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount + 2 >= totalItemCount;

        if (loadMore && !mLoadingComments && (mPage <= mTotalPage)) {
            new LoadPhotoCommentsTask().execute();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mImageFetcher.setPauseWork(true);
        } else {
            mImageFetcher.setPauseWork(false);
        }
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

    private void updateHeaderView() {
        ImageView headerPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerPhotoView);
        ImageView headerUserPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerUserPhotoView);
        ImageView imageViewStatus = (ImageView) mHeaderView.findViewById(R.id.imageViewStatus);
        TextView headerUserNameView = (TextView) mHeaderView.findViewById(R.id.headerUserNameView);
        TextView headerDescriptionView = (TextView) mHeaderView.findViewById(R.id.headerDescriptionView);
        FadeInTextSwitcher viewsCountView = (FadeInTextSwitcher) mHeaderView.findViewById(R.id.viewsCountView);
        FadeInTextSwitcher votesCountView = (FadeInTextSwitcher) mHeaderView.findViewById(R.id.votesCountView);
        FadeInTextSwitcher favsCountView = (FadeInTextSwitcher) mHeaderView.findViewById(R.id.favsCountView);
        TextView ratingView = (TextView) mHeaderView.findViewById(R.id.ratingView);

        headerUserNameView.setText(photoShort.user.fullname);
        viewsCountView.setText(getString(R.string.num_views, photoShort.times_viewed));
        votesCountView.setText(getString(R.string.num_votes, photoShort.votes_count));
        favsCountView.setText(getString(R.string.num_favorites, photoShort.favorites_count));

        if(!TextUtils.isEmpty(photoShort.description)) {
            headerDescriptionView.setText(Html.fromHtml(photoShort.description));
            headerDescriptionView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            headerDescriptionView.setVisibility(View.GONE);
        }
        ratingView.setText("" + photoShort.rating);

        FontUtils.setTypefaceRobotoLight(getActivity(), headerDescriptionView, ratingView);

        // Calculate min height for the photo
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int minHeight = photoShort.height * width / photoShort.width;

        headerPhotoView.setMinimumHeight(minHeight);
        mImageFetcher.loadImage(photoShort.image_url, headerPhotoView);        

        headerUserPhotoView.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                Intent photoDetailIntent = new Intent(getActivity(), ProfileActivity.class);
                photoDetailIntent.putExtra(ProfileFragment.INTENT_PROFILE_ID, mPhoto.user.id);
                startActivity(photoDetailIntent);
            }
        });

        if (!photoShort.user.userpic_url.equals("/graphics/userpic.png")) {
            mImageFetcher.loadImage(photoShort.user.userpic_url, headerUserPhotoView, R.drawable.ic_userpic);
        }

        if (photoShort.user.upgrade_status > 0) {
            if (photoShort.user.upgrade_status == 1) {
                imageViewStatus.setImageResource(R.drawable.ic_plus);
            } else if (photoShort.user.upgrade_status == 2) 
                imageViewStatus.setImageResource(R.drawable.ic_awesome);
            imageViewStatus.setVisibility(View.VISIBLE);
        } else {
            imageViewStatus.setVisibility(View.GONE);
        }


        headerPhotoView.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                //                Intent intent = new Intent();
                //                intent.setAction(Intent.ACTION_VIEW);
                //                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                //                intent.setData(Uri.parse("http://500px.com/photo/" + mPhoto.id));
                //                startActivity(intent);

                PhotoDialogFragment photoDialog = new PhotoDialogFragment(mPhoto.image_url);     
                FragmentManager fm = getFragmentManager();  
                photoDialog.show(fm, "photo_dialog");  

                //                WebDialogFragment webDialog = new WebDialogFragment("http://mobilesyrup.com/");
                //                FragmentManager fm = getFragmentManager();         
                //                webDialog.show(fm, "photo_dialog");  

            }
        });
    }

    private void updateHeaderExifView() {
        Button moreInfoButton = (Button) mHeaderExifView.findViewById(R.id.buttonMoreInfo);

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                View infoBox = parent.findViewById(R.id.moreInfoBox);
                Drawable icon;
                if (infoBox.getVisibility() == View.GONE) {                
                    icon = v.getResources().getDrawable(R.drawable.ic_arrow_up);
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.VISIBLE);
                } else {
                    icon= v.getResources().getDrawable(R.drawable.ic_arrow_down);         
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.GONE);
                }
                ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            }
        });

        TextView textView;
        if (!TextUtils.isEmpty(photoShort.camera)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewCamera);
            textView.setText(photoShort.camera);
            mHeaderExifView.findViewById(R.id.tableRowCamera).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.lens)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewLens);
            textView.setText(photoShort.lens);
            mHeaderExifView.findViewById(R.id.tableRowLens).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.focal_length)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewFocalLength);
            textView.setText(photoShort.focal_length);
            mHeaderExifView.findViewById(R.id.tableRowFocalLength).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.shutter_speed)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewShutterSpeed);
            textView.setText(photoShort.shutter_speed);
            mHeaderExifView.findViewById(R.id.tableRowShutterSpeed).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.aperture)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewAperture);
            textView.setText(photoShort.aperture);
            mHeaderExifView.findViewById(R.id.tableRowAperture).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.iso)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewISO);
            textView.setText(photoShort.iso);
            mHeaderExifView.findViewById(R.id.tableRowISO).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty("" + photoShort.category)) {
            String cat = Category.values()[photoShort.category].getString();
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewCategory);
            textView.setText(cat);
            mHeaderExifView.findViewById(R.id.tableRowCategory).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.created_at)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewUploaded);
            textView.setText(DateHelper.DateDifference(photoShort.created_at));
            mHeaderExifView.findViewById(R.id.tableRowUploaded).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(photoShort.taken_at)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewUploaded);
            textView.setText(DateHelper.getHeaderDate(photoShort.taken_at));
            mHeaderExifView.findViewById(R.id.tableRowUploaded).setVisibility(View.VISIBLE);
        }

        //       Copy Right?     
    }


    private void updateCommentsList(ArrayList<Comment> comments, int totalPages) {
        mPhotoDetailAdapter.appendComments(comments);
        mPhotoDetailAdapter.notifyDataSetChanged();
        mTotalPage = totalPages;
        getListView().setOnScrollListener(this);
    }

    private class VotePhotoTask extends AsyncTask<Void, Void, PhotoResponse> {

        protected PhotoResponse doInBackground(Void... params) {
            return ApiHelper.votePhoto(getActivity(), mPhoto.id, (!mPhoto.voted)? 1:0);
        }

        protected void onPostExecute(PhotoResponse response) {
            if(getActivity() == null)
                return;

            if (response != null) {
                mPhoto = response.photo;
                updateVotes();
                Toast.makeText(getActivity(), R.string.voted, Toast.LENGTH_SHORT).show();
            } else {
                ImageButton btnLike = (ImageButton) getActivity().findViewById(R.id.btnPhotoLike);
                btnLike.setEnabled(true);
                btnLike.setImageResource(R.drawable.ic_action_like);
            }
        }
    }

    private class FavPhotoTask extends AsyncTask<Void, Void, PostResponse> {

        protected PostResponse doInBackground(Void... params) {
            if(!mPhoto.favorited)
                return ApiHelper.favPhoto(getActivity(), mPhoto.id);
            else
                return ApiHelper.removeFavPhoto(getActivity(), mPhoto.id);
        }

        protected void onPostExecute(PostResponse response) {
            if(getActivity() == null)
                return;

            if (response != null && response.status == 200) {
                mPhoto.favorited = !mPhoto.favorited;   
                new LoadPhotoDetailTask().execute("FAV");
            } 
        }
    }

    private class CommPhotoTask extends AsyncTask<String, Void, PostResponse> {

        protected PostResponse doInBackground(String... params) {
            return ApiHelper.commentPhoto(getActivity(), mPhoto.id, params[0]);
        }

        protected void onPostExecute(PostResponse response) {
            if (response != null && response.status == 200) {
                updateFavs();
                Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class LoadPhotoDetailTask extends AsyncTask<String, Void, get_photos_id_response> {
        String flag;

        protected get_photos_id_response doInBackground(String... params) {
            flag = params[0];
            return ApiHelper.getFullPhoto(getActivity(), photoShort.id);
        }

        protected void onPostExecute(get_photos_id_response response) {
            //android.os.Debug.waitForDebugger();
            if(getActivity() == null)
                return;

            if(response == null) {}

            if (flag.equals("INIT")) {
                mPhoto = response.photo;
                //updateHeaderView();
                updateHeaderExifView();
                updateUserActionBar();
                ArrayList<Comment> list = new ArrayList();
            	for(int x=0; x<response.comments.length; x++) {
            		list.add(response.comments[x]);
            	}
                updateCommentsList(list, 2);
            } else if (flag.equals("VOTE")) {

            } else if (flag.equals("FAV")) {
                mPhoto = response.photo;
                updateFavs();
                Toast.makeText(getActivity(), mPhoto.favorited ? R.string.faved:R.string.unfaved, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadPhotoCommentsTask extends AsyncTask<Void, Void, get_photo_id_comments_response> {
        protected void onPreExecute() {
            mLoadingComments = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected get_photo_id_comments_response doInBackground(Void... params) {
            return ApiHelper.getComments(getActivity(), photoShort.id, mPage);
        }

        protected void onPostExecute(get_photo_id_comments_response response) {
            if(getActivity() == null)
                return;

            mLoadingView.setVisibility(View.GONE);
            mLoadingComments = false;
            if (response != null) {
            	ArrayList<Comment> list = new ArrayList();
            	for(int x=0; x<response.comments.length; x++) {
            		list.add(response.comments[x]);
            	}
                updateCommentsList(list, response.total_pages);
                mPage++;
            }
        }
    }

}
