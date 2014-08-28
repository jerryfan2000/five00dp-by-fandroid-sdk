package com.nyuen.five00dp.util;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

public class ParcelUtils {

    public static void writeStringToParcel(Parcel out, String str) {
        if (str != null) {
            out.writeInt(1);
            out.writeString(str);
        } else {
            out.writeInt(0);
        }
    }

    public static String readStringFromParcel(Parcel in) {
        int flag = in.readInt();
        if (flag == 1) {
            return in.readString();
        } else {
            return null;
        }
    }

    public static List<String> readStringArrayListFromParcel(Parcel in) {
        int size = in.readInt();
        if (size > 0) {
            ArrayList<String> vals = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                String val = in.readString();
                vals.add(val);
            }
            return vals;
        } else {
            return null;
        }
    }

    public static void writeStringArrayListToParcel(Parcel out, List<String> val) {
        if (val != null && val.size() > 0) {
            out.writeInt(val.size());
            for (String it : val) {
                out.writeString(it);
            }
        } else {
            out.writeInt(0);
        }
    }

    public static void writeIntegerArrayListToParcel(Parcel out, List<Integer> val) {
        if (val != null && val.size() > 0) {
            out.writeInt(val.size());
            for (Integer it : val) {
                out.writeInt(it);
            }
        } else {
            out.writeInt(0);
        }
    }

    public static List<Integer> readIntegerArrayListFromParcel(Parcel in) {
        int size = in.readInt();
        if (size > 0) {
            ArrayList<Integer> vals = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                int val = in.readInt();
                vals.add(val);
            }
            return vals;
        } else {
            return null;
        }
    }

    public static int[] readIntegerArrayFromParcel(Parcel in) {
        int size = in.readInt();
        if (size > -1) {
            int[] val = new int[size];
            for (int i = 0; i < val.length; i++) {
                val[i] = in.readInt();
            }

            return val;
        } else {
            return null;
        }
    }

    public static void writeIntegerArrayToParcel(Parcel out, int[] val) {
        if (val != null) {
            out.writeInt(val.length);
            for (int i = 0; i < val.length; i++) {
                out.writeInt(val[i]);
            }
        } else {
            out.writeInt(-1);
        }
    }
}