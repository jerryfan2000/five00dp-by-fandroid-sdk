package com.nyuen.five00dp.structure;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable{

    public int id;
    public int user_id;
    public int to_whom_user_id;
    public String body;
    public String created_at;
    public int parent_id;
    public User user;
    
    public Comment (Parcel in) {
        readFromParcelable(in);
    }
    
    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        to_whom_user_id = in.readInt();
        body = in.readString();
        created_at = in.readString();
        parent_id = in.readInt();
        user = new User(in);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {        
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeInt(to_whom_user_id);
        dest.writeString(body);
        dest.writeString(created_at);
        dest.writeInt(parent_id);
        user.writeToParcel(dest, flags);
    }

}
