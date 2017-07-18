package com.lwkandroid.rtpermission.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

/**
 * 工具类
 */

public class RTUtils
{
    private RTUtils()
    {
    }

    /**
     * 获取App名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return context.getPackageName();
        }
    }

    /**
     * 跳转到App设置详情界面
     */
    public static void goToSettingDetail(Context context)
    {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检查是否有某项权限的试用权
     *
     * @param context    Context环境
     * @param permission 权限
     * @return true:有 false:没有
     */
    public static boolean hasPermission(Context context, String permission)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        try
        {
            String op = AppOpsManagerCompat.permissionToOp(permission);
            int opResult = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            int rtResult = context.checkSelfPermission(permission);
            if (opResult == AppOpsManagerCompat.MODE_ALLOWED && rtResult == PackageManager.PERMISSION_GRANTED)
                return true;
            else
                return false;
        } catch (Exception e)
        {
            return false;
        }
    }
}
