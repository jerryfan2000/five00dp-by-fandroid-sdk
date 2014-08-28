package com.nyuen.five00dp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiumiu.ca.api500px.primitiveDataType.Comment;
import com.kiumiu.ca.api500px.primitiveDataType.UserShort;
import com.nyuen.five00dp.PhotoDetailActivity;
import com.nyuen.five00dp.ProfileActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.fragment.PhotoDetailFragment;
import com.nyuen.five00dp.fragment.ProfileFragment;
import com.nyuen.five00dp.response.CommentResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.ImageFetcher;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    private final ImageFetcher mImageFetcher;
    
    private List<Comment> mComments;
    
    private OnClickListener mOnProfileClickListener;
    
    public CommentAdapter(final Context context, ImageFetcher imageFetcher) {
               
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        mComments = new ArrayList<Comment>();
        
        mOnProfileClickListener = new OnClickListener() {   
            @Override
            public void onClick(View v) {
                Integer profileID = (Integer) v.getTag();
                Intent photoDetailIntent = new Intent(context, ProfileActivity.class);
                photoDetailIntent.putExtra(ProfileFragment.INTENT_PROFILE_ID, profileID);
                context.startActivity(photoDetailIntent);
            }
        };
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Comment getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void setDetails(CommentResponse response) {
        
    }
    
    public void setComments(List<Comment> comments) {
        mComments.clear();
        appendComments(comments);
    }
    
    public void appendComments(List<Comment> comments) {
        for (Comment c : comments) {
            mComments.add(c);
        }
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoDetailHolder holder;
        
        if (convertView == null) {
            holder = new PhotoDetailHolder();
            convertView = mInflater.inflate(R.layout.list_cell_photo_comment, null, false);
            
            holder.commentUserPhotoView = (ImageView) convertView.findViewById(R.id.commentUserPhotoView);
            holder.imageViewUserAwesome = (ImageView) convertView.findViewById(R.id.imageViewUserStatus);
            
            holder.commentBodyView = (TextView) convertView.findViewById(R.id.commentBodyView);
            holder.commentBodyView.setMovementMethod(LinkMovementMethod.getInstance());
            holder.commentUserNameView = (TextView) convertView.findViewById(R.id.commentUserNameView);
            holder.likeCountView = (TextView) convertView.findViewById(R.id.likeCountView);
            
            convertView.setTag(holder);
        } else {
            holder = (PhotoDetailHolder) convertView.getTag();
        }
        
        Comment comment = getItem(position);
        UserShort user = getItem(position).user;
        mImageFetcher.loadImage(user.userpic_url, holder.commentUserPhotoView, R.drawable.ic_userpic);
        
        if (user.upgrade_status > 0) {
            if (user.upgrade_status == 1) {
                holder.imageViewUserAwesome.setImageResource(R.drawable.ic_plus);
            }
            holder.imageViewUserAwesome.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewUserAwesome.setVisibility(View.GONE);
        }
        
        holder.commentUserPhotoView.setTag(Integer.valueOf(user.id));
        holder.commentUserPhotoView.setOnClickListener(mOnProfileClickListener);
        holder.commentBodyView.setText(Html.fromHtml(comment.body));
        holder.commentUserNameView.setText(user.fullname);
        holder.likeCountView.setText("" + 0);
        
        return convertView;
    }
        
    private class PhotoDetailHolder {
        ImageView commentUserPhotoView, imageViewUserAwesome;
        TextView likeCountView, commentUserNameView, commentBodyView;
    }
 
}
