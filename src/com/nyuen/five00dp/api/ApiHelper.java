package com.nyuen.five00dp.api;


import android.content.Context;
import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.kiumiu.ca.api500px.photo.Features;
import com.kiumiu.ca.api500px.response.photo.get_photo_id_comments_response;
import com.kiumiu.ca.api500px.response.photo.get_photos_id_response;
import com.kiumiu.ca.api500px.response.photo.get_photos_response;
import com.kiumiu.ca.api500px.response.user.get_users_show_response;
import com.nyuen.five00dp.response.PostResponse;
import com.nyuen.five00dp.response.PhotoResponse;
import com.nyuen.five00dp.util.AccountUtils;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();
    
    private static PxApi getApi(Context context){
        if(AccountUtils.hasAccount(context)) {
            return new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        }
        else
            return new PxApi(FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);  
    }
    
    public static get_photos_response getPhotoStream(Context context, String feature, int rpp, int size, int page){
        PxApi pxapi = getApi(context);
        get_photos_response photo = pxapi.getPhotoInterface().get_photosEx(feature, null, null, null, null, page, rpp, size, true, true, true, null);
        return photo;
    }
    
    public static get_photos_response getProfilePhotos(Context context, int user_id, int rpp, int size, int page){
        PxApi pxapi = getApi(context);
        get_photos_response photo = pxapi.getPhotoInterface().get_photosEx(Features.user, null, null, null, null, page, rpp, size,true, true, true, new Integer(user_id).toString());
        return photo;
    }
    

    public static get_photos_id_response getFullPhoto(Context context, int photoId){
        PxApi pxapi = getApi(context);
        get_photos_id_response response = pxapi.getPhotoInterface().get_photo_idEx(new Integer(photoId).toString(), 0, true, 0, false);
        return response;
        
    }

    public static get_photo_id_comments_response getComments(Context context, int photoId, int page){
        PxApi pxapi = getApi(context);
        get_photo_id_comments_response response = pxapi.getPhotoInterface().get_photo_id_commentsEx(new Integer(photoId).toString(), false, page);
        return response;
    }
    
    public static get_users_show_response getMyProfile(Context context){        
    	PxApi pxapi = getApi(context);
        get_users_show_response response = pxapi.getUserInterface().get_usersEx();
        return response;
    }
    
    public static get_users_show_response getUserProfileWithAccount(Context context, int userID){        
    	PxApi pxapi = getApi(context);
        get_users_show_response response = pxapi.getUserInterface().get_users_show_by_idEx(new Integer(userID).toString());
        return response;
    }
    
    public static get_users_show_response getUserProfile(Context context, int userID){        
        PxApi pxapi = getApi(context);
        get_users_show_response response = pxapi.getUserInterface().get_users_show_by_idEx(new Integer(userID).toString());
        return response;
    }
    
    //POST Section
    
    public static PhotoResponse votePhoto(Context context, int photoID, int vote) {
    	PxApi pxapi = getApi(context);
        boolean like;
        if(vote == 1)
        	like = true;
        else
        	like = false;
        String json = pxapi.getPhotoInterface().post_photos_id_vote(new Integer(photoID).toString(), like).toString();
        try {
            PhotoResponse out = new Gson().fromJson(json, PhotoResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static PostResponse favPhoto(Context context, int photoID) {
    	PxApi pxapi = getApi(context);
        String json = pxapi.getPhotoInterface().post_photos_id_favorite(new Integer(photoID).toString()).toString();
        try {
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static PostResponse commentPhoto(Context context, int photoID, String comment) {
    	PxApi pxapi = getApi(context);
        String json = pxapi.getPhotoInterface().post_photos_id_comments(new Integer(photoID).toString(), comment).toString();
        try {
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    //DELETE
    
    public static PostResponse removeFavPhoto(Context context, int photoID) {
    	PxApi pxapi = getApi(context);
        String json = pxapi.getPhotoInterface().delete_photo_id_faviorte(new Integer(photoID).toString()).toString();
        try {
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
}
