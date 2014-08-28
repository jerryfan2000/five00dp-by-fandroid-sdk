package com.nyuen.five00dp.structure;

import com.nyuen.five00dp.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    
    public int id;
    public String username;
    public String firstname;
    public String lastname;
    public String city;
    public String country;
    public String fullname;
    public String userpic_url;
    public String about;
    public int upgrade_status;
    public boolean fotomoto_on; 
    public String locale;
    public boolean show_nude;
    public boolean store_on;
    //public Contacts contacts; 
    //public equipment
    public String email;
    public int photo_count;
    public int affection;
    public int in_favorites_count;
    public int friends_count;
    public int followers_count;
    public int upload_limit;
    public String upload_limit_expiry;
    public String upgrade_status_expiry;
    //auth
    
    public User (Parcel in) {
        readFromParcelable(in);
    }
    
    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        username = ParcelUtils.readStringFromParcel(in);
        firstname = ParcelUtils.readStringFromParcel(in);
        lastname = ParcelUtils.readStringFromParcel(in);
        city = ParcelUtils.readStringFromParcel(in);
        country = ParcelUtils.readStringFromParcel(in);
        about = ParcelUtils.readStringFromParcel(in);
        fullname = ParcelUtils.readStringFromParcel(in);
        userpic_url = ParcelUtils.readStringFromParcel(in);        
        upgrade_status = in.readInt();
        
//        fotomoto_on 
//        locale
//        show_nude
//        store_on
//        contacts
//        equipment
//        email
//        photo_count
//        affection
//        in_favorites_count
//        friends_count
//        followers_count
//        upload_limit
//        upload_limit_expiry
//        upgrade_status_expiry
//        auth
        
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        ParcelUtils.writeStringToParcel(dest, username);
        ParcelUtils.writeStringToParcel(dest, firstname);
        ParcelUtils.writeStringToParcel(dest, lastname);
        ParcelUtils.writeStringToParcel(dest, city);
        ParcelUtils.writeStringToParcel(dest, country);
        ParcelUtils.writeStringToParcel(dest, about);
        ParcelUtils.writeStringToParcel(dest, fullname);
        ParcelUtils.writeStringToParcel(dest, userpic_url);
        dest.writeInt(upgrade_status);
        
//        fotomoto_on 
//        locale
//        show_nude
//        store_on
//        contacts
//        equipment
//        email
//        photo_count
//        affection
//        in_favorites_count
//        friends_count
//        followers_count
//        upload_limit
//        upload_limit_expiry
//        upgrade_status_expiry
//        auth
        
    }
    
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in); 
        }

        @Override
        public User[] newArray(int size) {
            return new User [size];
        }
    };
    
    
}
