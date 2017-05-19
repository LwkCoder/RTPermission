package com.lwkandroid.rtpermission.listener;

/**
 * 权限请求监听
 */
public interface OnPermissionResultListener
{
    void onAllGranted(String[] allPermissions);

    void onDeined(String[] dinedPermissions);
}
