package com.lwkandroid.rtpermission.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.lwkandroid.rtpermission.R;
import com.lwkandroid.rtpermission.data.PermissionOptions;
import com.lwkandroid.rtpermission.data.RTContants;
import com.lwkandroid.rtpermission.listener.OnPermissionResultListener;
import com.lwkandroid.rtpermission.utils.RTUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 请求权限的Activity
 */
public class PermissionActivity extends Activity
{
    private static final String TAG = "PermissionActivity";
    private static OnPermissionResultListener mListener;
    private static final int REQUEST_CODE = 0x1;
    private PermissionOptions mOptions;
    private List<String> mNeedRequestList;
    private List<String> mDeniedList;

    public static void setOnGrandResultListener(OnPermissionResultListener listener)
    {
        mListener = listener;
    }

    /**
     * 跳转到该界面的公共方法
     *
     * @param activity 发起跳转的Activity
     * @param options  参数
     */
    public static void start(Activity activity, PermissionOptions options)
    {
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(RTContants.INTENT_KEY, options);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtpermission);

        Intent intent = getIntent();
        if (intent != null)
            mOptions = intent.getParcelableExtra(RTContants.INTENT_KEY);

        if (mOptions != null)
        {
            //SDK小于23的默认有权限
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            {
                invokeAllGranted();
            } else
            {
                String[] allPermissions = mOptions.getPermissions();
                mNeedRequestList = new LinkedList<>();
                //筛选所有需要申请的权限
                boolean needShowRationale = false;
                for (String permission : allPermissions)
                {
                    if (!RTUtils.hasPermission(this, permission))
                    {
                        mNeedRequestList.add(permission);
                        if (shouldShowRequestPermissionRationale(permission))
                            needShowRationale = true;
                    }
                }
                Log.i(TAG, "need to request permissions: " + mNeedRequestList.toString());
                //判断筛选结果
                if (mNeedRequestList.size() == 0)
                {
                    invokeAllGranted();
                } else
                {
                    //如果有权限需要解释，那就把所有申请的权限都解释出来
                    if (needShowRationale)
                    {
                        showReqeustRationaleDialog();
                    } else
                    {
                        requestPermissions(mNeedRequestList.toArray(new String[mNeedRequestList.size()]), REQUEST_CODE);
                    }
                }
            }
        } else
        {
            Log.e(TAG, "There is no RTPermissionOptions !");
            finish();
        }
    }

    //对权限申请作出解释
    @SuppressLint("NewApi")
    private void showReqeustRationaleDialog()
    {
        String appName = RTUtils.getAppName(this.getApplicationContext());
        String message = getString(R.string.rtpermission_rationale_dialog_message, appName);
        new RTDialog.Builder()
                .permissions(mNeedRequestList)
                .message(message)
                .positiveButton(R.string.rtpermission_rationale_dialog_next, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        requestPermissions(mNeedRequestList.toArray(new String[mNeedRequestList.size()]), REQUEST_CODE);
                    }
                })
                .show(this);
    }

    @Override
    @SuppressLint("NewApi")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
        {
            //筛选结果
            boolean allGranted = true, nerverAsk = false;
            //            for (int i = 0, l = grantResults.length; i < l; i++)
            //            {
            //                String permission = permissions[i];
            //                int grandResult = grantResults[i];
            //                Log.i(TAG, "onRequestPermissionsResult: permission=" + permission + " result=" + grandResult);
            //                if (grandResult == PackageManager.PERMISSION_DENIED)
            //                {
            //                    allGranted = false;
            //
            //                    if (mDeniedList == null)
            //                        mDeniedList = new LinkedList<>();
            //                    mDeniedList.add(permission);
            //
            //                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
            //                        nerverAsk = true;
            //                }
            //            }

            //唉！为了兼容某些第三方ROM，这里改为再次检查所有权限来判断申请结果
            String[] allPermissions = mOptions.getPermissions();
            for (String permission : allPermissions)
            {
                if (!RTUtils.hasPermission(this, permission))
                {
                    Log.i(TAG, "request permission result：" + permission + " not granted");
                    allGranted = false;
                    if (mDeniedList == null)
                        mDeniedList = new LinkedList<>();
                    mDeniedList.add(permission);

                    if (!shouldShowRequestPermissionRationale(permission))
                        nerverAsk = true;
                }
            }

            if (allGranted)
            {
                invokeAllGranted();
            } else
            {
                //有权限被永久拒绝（不再提醒）后，就把所有被拒绝的权限都解释一下，提示用户可以在设置界面打开
                if (nerverAsk)
                {
                    showDeniedRationaleDialog();
                } else
                {
                    invokeDenied();
                }
            }
        }
    }

    //对被拒绝且有不再提醒的权限做出解释并提醒用户去设置界面打开
    private void showDeniedRationaleDialog()
    {
        String appName = RTUtils.getAppName(this.getApplicationContext());
        String message = getString(R.string.rtpermission_nerverask_dialog_message, appName);
        new RTDialog.Builder()
                .permissions(mDeniedList)
                .message(message)
                .negativeButton(R.string.rtpermission_nerverask_dialog_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        invokeDenied();
                    }
                })
                .positiveButton(R.string.rtpermission_nerverask_dialog_setting, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        //去设置界面
                        RTUtils.goToSettingDetail(PermissionActivity.this);
                        invokeDenied();
                    }
                })
                .show(this);
    }

    //触发成功申请所有权限的监听
    private void invokeAllGranted()
    {
        if (mListener != null)
            mListener.onAllGranted(mOptions.getPermissions());
        finish();
    }

    //触发部分权限被拒绝的监听
    private void invokeDenied()
    {
        if (mListener != null)
            mListener.onDeined(mDeniedList.toArray(new String[mDeniedList.size()]));
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mListener = null;
    }
}
