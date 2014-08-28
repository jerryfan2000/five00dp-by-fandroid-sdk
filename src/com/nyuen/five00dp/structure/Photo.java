package com.nyuen.five00dp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.nyuen.five00dp.util.ParcelUtils;

public class Photo implements Parcelable {
    public enum Category {
        Uncategorized("Uncategorized"),
        Celebrities("Celebrities"),
        Film("Film"),
        Journalism("Journalism"),
        Nude("Nude"),
        BlackAndWhite("Black and White"),
        StillLife("Still Life"),
        People("People"),
        Landscapes("Landscapes"),
        CityAndArchitecture("City and Architecture"),
        Abstract("Abstract"),
        Animals("Animals"),
        Macro("Macro"),
        Travel("Travel"),
        Fashion("Fashion"),
        Commercial("Commercial"),
        Concert("Concert"),
        Sport("Sport"),
        Nature("Nature"),
        PerformingArts("Performing Arts"),
        Family("Family"),
        Street("Family"),
        Underwater("Underwater"),
        Food("Food"),
        FineArt("Fine Art"),
        Wedding("Wedding"),
        Transporation("Transporation"),
        UrbanExploration("Urban Exploration");
        
        private final String mValue;
        
        Category(String value) {
            mValue = value;
        }
        
        public String getString() {
            return mValue;
        }
    }
    
    public int id;
    public String name;
    public String description;
    public int times_viewed;
    public double rating;
    public String created_at;
    public int category;
    public boolean privacy;
    public int width;
    public int height;
    public int votes_count;
    public int favorites_count;
    public int comments_count;
    public boolean nsfw;
    public String image_url;
    public User user;

    //extended members
    public String camera;
    public String lens;
    public String focal_length;
    public String iso;
    public String shutter_speed;
    public String aperture;
    public int status;
    public String location;
    public float latitude;
    public float longitude;
    public String taken_at;
    public int hi_res_uploaded;
    public boolean for_sale;
    public int sales_count;
    public String for_sale_date;
    public double highest_rating;
    public String highest_rating_date;
    public boolean store_download;
    public boolean store_print;
    public boolean voted = false;
    public boolean favorited = false;
    public boolean purchased;

    public Photo (Parcel in) {
        readFromParcelable(in);
    }

    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        name = ParcelUtils.readStringFromParcel(in);
        description = ParcelUtils.readStringFromParcel(in);
        times_viewed = in.readInt();
        rating = in.readDouble();
        created_at = ParcelUtils.readStringFromParcel(in);
        category = in.readInt();
        privacy = in.readInt() == 0; 
        width = in.readInt();
        height = in.readInt();
        votes_count = in.readInt();
        favorites_count = in.readInt();
        comments_count = in.readInt();
        nsfw = in.readInt() == 0;
        image_url = ParcelUtils.readStringFromParcel(in);
        user = in.readParcelable(User.class.getClassLoader());


        camera = ParcelUtils.readStringFromParcel(in);
        lens = ParcelUtils.readStringFromParcel(in);
        focal_length = ParcelUtils.readStringFromParcel(in);
        iso = ParcelUtils.readStringFromParcel(in);
        shutter_speed = ParcelUtils.readStringFromParcel(in);
        aperture = ParcelUtils.readStringFromParcel(in);
        status = in.readInt();
        location = ParcelUtils.readStringFromParcel(in);
        latitude = in.readFloat();
        longitude = in.readFloat();
        taken_at = ParcelUtils.readStringFromParcel(in);
        hi_res_uploaded = in.readInt();
        for_sale = in.readInt() == 0;
        sales_count = in.readInt();
        for_sale_date = ParcelUtils.readStringFromParcel(in);
        highest_rating = in.readDouble();
        highest_rating_date = ParcelUtils.readStringFromParcel(in);
        store_download = in.readInt() == 0;
        store_print = in.readInt() == 0;
        voted = in.readInt() == 0;
        favorited = in.readInt() == 0;
        purchased = in.readInt() == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        ParcelUtils.writeStringToParcel(dest, name);
        ParcelUtils.writeStringToParcel(dest, description);
        dest.writeInt(times_viewed);
        dest.writeDouble(rating);
        ParcelUtils.writeStringToParcel(dest, created_at);
        dest.writeInt(category);
        dest.writeInt(privacy ? 1 : 0);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(votes_count);
        dest.writeInt(favorites_count);
        dest.writeInt(comments_count);
        dest.writeInt(nsfw ? 1 : 0);
        ParcelUtils.writeStringToParcel(dest, image_url); 
        dest.writeParcelable(user, flags);

        ParcelUtils.writeStringToParcel(dest, camera);
        ParcelUtils.writeStringToParcel(dest, lens);
        ParcelUtils.writeStringToParcel(dest, focal_length);
        ParcelUtils.writeStringToParcel(dest, iso);
        ParcelUtils.writeStringToParcel(dest, shutter_speed);
        ParcelUtils.writeStringToParcel(dest, aperture);
        dest.writeInt(status);
        ParcelUtils.writeStringToParcel(dest, location);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        ParcelUtils.writeStringToParcel(dest, taken_at);
        dest.writeInt(hi_res_uploaded);
        dest.writeInt(for_sale ? 1 : 0);
        dest.writeInt(sales_count);
        ParcelUtils.writeStringToParcel(dest, for_sale_date);
        dest.writeDouble(highest_rating);
        ParcelUtils.writeStringToParcel(dest, highest_rating_date);
        dest.writeInt(store_download ? 1 : 0);
        dest.writeInt(store_print ? 1 : 0);
        dest.writeInt(voted ? 1 : 0);
        dest.writeInt(favorited ? 1 : 0);
        dest.writeInt(purchased ? 1 : 0);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in); 
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo [size];
        }
    };

}
