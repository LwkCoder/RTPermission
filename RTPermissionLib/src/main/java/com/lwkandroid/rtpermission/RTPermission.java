package com.lwkandroid.rtpermission;

import android.app.Activity;

import com.lwkandroid.rtpermission.data.PermissionOptions;
import com.lwkandroid.rtpermission.listener.OnPermissionResultListener;
import com.lwkandroid.rtpermission.ui.PermissionActivity;

/**
 * 功能入口
 */

public class RTPermission
{
    private PermissionOptions mOptions;

    private RTPermission(PermissionOptions options)
    {
        this.mOptions = options;
    }

    public void start(Activity activity, OnPermissionResultListener listener)
    {
        PermissionActivity.setOnGrandResultListener(listener);
        PermissionActivity.start(activity, mOptions);
    }

    public static class Builder
    {
        private PermissionOptions mOptions;

        public Builder()
        {
            mOptions = new PermissionOptions();
        }

        public Builder permissions(String[] permissions)
        {
            mOptions.setPermissions(permissions);
            return this;
        }

        public RTPermission build()
        {
            return new RTPermission(mOptions);
        }
    }
}
