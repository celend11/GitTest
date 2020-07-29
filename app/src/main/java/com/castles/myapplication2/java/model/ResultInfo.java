package com.castles.myapplication2.java.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultInfo implements Parcelable {

    public int resultCode;
    public String message;

    public static final Parcelable.Creator<ResultInfo> CREATOR = new Parcelable.Creator<ResultInfo>() {
        @Override
        public ResultInfo createFromParcel(Parcel in) {
            return new ResultInfo(in);
        }
        @Override
        public ResultInfo[] newArray(int size) {
            return new ResultInfo[size];
        }
    };

    public ResultInfo(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    private ResultInfo(Parcel in) {
        this.resultCode = in.readInt();
        this.message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(resultCode);
        out.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
