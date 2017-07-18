package com.lwkandroid.rtpermission.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;

/**
 * Created by LWK
 * TODO V4包里copy来的，AppOpsManager implementations for API 23.
 * 2017/7/18
 */
@SuppressLint("NewApi")
public class AppOpsManagerCompat23
{
    public static String permissionToOp(String permission)
    {
        return AppOpsManager.permissionToOp(permission);
    }

    public static int noteOp(Context context, String op, int uid, String packageName)
    {
        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteOp(op, uid, packageName);
    }

    public static int noteProxyOp(Context context, String op, String proxiedPackageName)
    {
        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteProxyOp(op, proxiedPackageName);
    }
}
