package com.lwkandroid.rtpermission.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Intent传递的参数
 */

public class PermissionOptions implements Parcelable
{
    private String[] permissions;

    public PermissionOptions()
    {
    }

    public PermissionOptions(String[] permissions)
    {
        this.permissions = permissions;
    }

    public String[] getPermissions()
    {
        return permissions;
    }

    public void setPermissions(String[] permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public String toString()
    {
        return "PermissionOptions{" +
                "permissions=" + Arrays.toString(permissions) +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeStringArray(this.permissions);
    }

    protected PermissionOptions(Parcel in)
    {
        this.permissions = in.createStringArray();
    }

    public static final Parcelable.Creator<PermissionOptions> CREATOR = new Parcelable.Creator<PermissionOptions>()
    {
        @Override
        public PermissionOptions createFromParcel(Parcel source)
        {
            return new PermissionOptions(source);
        }

        @Override
        public PermissionOptions[] newArray(int size)
        {
            return new PermissionOptions[size];
        }
    };
}
