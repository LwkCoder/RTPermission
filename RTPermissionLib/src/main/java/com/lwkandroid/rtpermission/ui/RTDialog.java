package com.lwkandroid.rtpermission.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lwkandroid.rtpermission.R;
import com.lwkandroid.rtpermission.data.RTContants;

import java.util.LinkedList;
import java.util.List;

/**
 * 权限提醒弹窗
 */

public class RTDialog extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "RTDialog";
    private List<PermissionItem> mPermissionList = new LinkedList<>();
    private DialogInterface.OnClickListener mPosClickListener;
    private DialogInterface.OnClickListener mNegClickListener;
    @StringRes
    private int mPosTvResId = -1;
    @StringRes
    private int mNegTvResId = -1;
    private String mMessage;
    private TextView mTvTitle;
    private TextView mTvMessage;
    private ListView mListView;
    private TextView mTvPositive;
    private TextView mTvNegative;

    private void setPermissionList(List<String> list)
    {
        mPermissionList.clear();
        //过滤同组权限
        for (String permission : list)
        {
            PermissionItem item = getPermissionData(permission);
            if (!mPermissionList.contains(item))
                mPermissionList.add(item);
        }
    }

    private void setPosClickListener(DialogInterface.OnClickListener listener)
    {
        this.mPosClickListener = listener;
    }

    private void setNegClickListener(DialogInterface.OnClickListener listener)
    {
        this.mNegClickListener = listener;
    }

    private void setPosTvResId(int resId)
    {
        this.mPosTvResId = resId;
    }

    private void setNegTvResId(int resId)
    {
        this.mNegTvResId = resId;
    }

    private void setMessage(String message)
    {
        this.mMessage = message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);

        View contentView = inflater.inflate(R.layout.layout_rtdialog, container, false);
        mTvTitle = (TextView) contentView.findViewById(R.id.tv_rtdialog_title);
        mTvMessage = (TextView) contentView.findViewById(R.id.tv_rtdialog_message);
        mListView = (ListView) contentView.findViewById(R.id.lv_rtdialog);
        mTvPositive = (TextView) contentView.findViewById(R.id.tv_rtdialog_positive);
        mTvNegative = (TextView) contentView.findViewById(R.id.tv_rtdialog_negative);

        mTvTitle.setText(R.string.rtpermission_dialog_title);
        mTvMessage.setText(mMessage);
        if (mPosTvResId != -1)
            mTvPositive.setText(mPosTvResId);
        if (mNegTvResId != -1)
            mTvNegative.setText(mNegTvResId);
        mTvPositive.setOnClickListener(this);
        mTvNegative.setOnClickListener(this);
        mListView.setAdapter(new Adapter());

        return contentView;
    }

    @Override
    public void onStart()
    {
        //设置Dialog宽度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        getDialog().getWindow().getAttributes().width = Math.min(metrics.widthPixels, metrics.heightPixels) - 100;
        //按下back不消失
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    return true;
                }
                return false;
            }
        });
        super.onStart();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.tv_rtdialog_positive)
        {
            if (mPosClickListener != null)
                mPosClickListener.onClick(getDialog(), -1);
        } else if (id == R.id.tv_rtdialog_negative)
        {
            if (mNegClickListener != null)
                mNegClickListener.onClick(getDialog(), -1);
        }
    }

    private class Adapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPermissionList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mPermissionList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Holder holder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_rtdialog, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else
            {
                holder = (Holder) convertView.getTag();
            }

            ImageView imgLogo = holder.findView(R.id.img_permission_logo);
            TextView tvName = holder.findView(R.id.tv_permission_name);
            TextView tvDesc = holder.findView(R.id.tv_permission_desc);
            PermissionItem permissionItem = mPermissionList.get(position);
            imgLogo.setImageResource(permissionItem.getLogo());
            tvName.setText(permissionItem.getName());
            tvDesc.setText(permissionItem.getDesc());

            return convertView;
        }
    }

    private class Holder
    {
        private SparseArray<View> mViews;
        private View mConvertView;

        Holder(View converView)
        {
            this.mConvertView = converView;
            mViews = new SparseArray<>();
        }

        public <T extends View> T findView(int viewId)
        {
            View view = mViews.get(viewId);
            if (view == null)
            {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }

    private class PermissionItem
    {
        @DrawableRes
        private int logo;
        @StringRes
        private int name;
        @StringRes
        private int desc;
        @RTContants.Permisson.Group
        private int gruop;

        public PermissionItem(@DrawableRes int logo, @StringRes int name,
                              @StringRes int desc, @RTContants.Permisson.Group int gruop)
        {
            this.logo = logo;
            this.name = name;
            this.desc = desc;
            this.gruop = gruop;
        }

        public int getLogo()
        {
            return logo;
        }

        public int getName()
        {
            return name;
        }

        public int getDesc()
        {
            return desc;
        }

        public int getGruop()
        {
            return gruop;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof PermissionItem)
                return gruop == ((PermissionItem) obj).getGruop();

            return super.equals(obj);
        }
    }

    //获取各种权限的解释信息
    private PermissionItem getPermissionData(String permission)
    {
        //通讯录权限
        if (permission.equals(Manifest.permission.WRITE_CONTACTS)
                || permission.equals(Manifest.permission.READ_CONTACTS)
                || permission.equals(Manifest.permission.GET_ACCOUNTS))
            return new PermissionItem(R.drawable.ic_contact, R.string.permission_contact,
                    R.string.rationale_contact, RTContants.Permisson.CONTACT);
            //拨打电话、查看设备信息权限
        else if (permission.equals(Manifest.permission.READ_CALL_LOG)
                || permission.equals(Manifest.permission.READ_PHONE_STATE)
                || permission.equals(Manifest.permission.CALL_PHONE)
                || permission.equals(Manifest.permission.WRITE_CALL_LOG)
                || permission.equals(Manifest.permission.USE_SIP)
                || permission.equals(Manifest.permission.PROCESS_OUTGOING_CALLS))
            return new PermissionItem(R.drawable.ic_phone, R.string.permission_phone,
                    R.string.rationale_phone, RTContants.Permisson.PHONE);
            //日历权限
        else if (permission.equals(Manifest.permission.READ_CALENDAR)
                || permission.equals(Manifest.permission.WRITE_CALENDAR))
            return new PermissionItem(R.drawable.ic_calendar, R.string.permission_calendar
                    , R.string.rationale_calendar, RTContants.Permisson.CALENDAR);
            //相机权限
        else if (permission.equals(Manifest.permission.CAMERA))
            return new PermissionItem(R.drawable.ic_camera, R.string.permission_camera
                    , R.string.rationale_camera, RTContants.Permisson.CAMERA);
            //身体传感器权限
        else if (permission.equals(Manifest.permission.BODY_SENSORS))
            return new PermissionItem(R.drawable.ic_body_sensor, R.string.permission_sensors
                    , R.string.rationale_sensors, RTContants.Permisson.SENSORS);
            //定位权限
        else if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)
                || permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION))
            return new PermissionItem(R.drawable.ic_location, R.string.permission_location,
                    R.string.rationale_location, RTContants.Permisson.LOCATION);
            //外部存储权限
        else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                || permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            return new PermissionItem(R.drawable.ic_sdcard, R.string.permission_sdcard
                    , R.string.rationale_sdcard, RTContants.Permisson.STORAGE);
            //录音权限
        else if (permission.equals(Manifest.permission.RECORD_AUDIO))
            return new PermissionItem(R.drawable.ic_audio, R.string.permission_audio,
                    R.string.rationale_audio, RTContants.Permisson.MICPHONE);
            //短信权限
        else if (permission.equals(Manifest.permission.READ_SMS)
                || permission.equals(Manifest.permission.RECEIVE_WAP_PUSH)
                || permission.equals(Manifest.permission.RECEIVE_MMS)
                || permission.equals(Manifest.permission.RECEIVE_SMS)
                || permission.equals(Manifest.permission.SEND_SMS))
            return new PermissionItem(R.drawable.ic_sms, R.string.permission_sms,
                    R.string.rationale_sms, RTContants.Permisson.SMS);

        return new PermissionItem(R.drawable.ic_unkonw_permission, R.string.permission_unkown,
                R.string.rationale_unknow, RTContants.Permisson.UNKOWN);
    }

    public static class Builder
    {
        private RTDialog mDialog;

        public Builder()
        {
            mDialog = new RTDialog();
        }

        public Builder permissions(List<String> list)
        {
            mDialog.setPermissionList(list);
            return this;
        }

        public Builder message(String message)
        {
            mDialog.setMessage(message);
            return this;
        }

        public Builder positiveButton(@StringRes int resId, DialogInterface.OnClickListener listener)
        {
            mDialog.setPosTvResId(resId);
            mDialog.setPosClickListener(listener);
            return this;
        }

        public Builder negativeButton(@StringRes int resId, DialogInterface.OnClickListener listener)
        {
            mDialog.setNegTvResId(resId);
            mDialog.setNegClickListener(listener);
            return this;
        }

        public RTDialog show(Activity activity)
        {
            mDialog.show(activity.getFragmentManager(), "RTDialog");
            return mDialog;
        }
    }
}
