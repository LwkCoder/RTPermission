package com.lwkandroid.rtpermissiondemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lwkandroid.rtpermission.RTPermission;
import com.lwkandroid.rtpermission.listener.OnPermissionResultListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnPermissionResultListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main01).setOnClickListener(this);
        findViewById(R.id.btn_main02).setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_main01:
                new RTPermission.Builder()
                        .permissions(new String[]{Manifest.permission.BODY_SENSORS})
                        .build()
                        .start(this, this);
                break;
            case R.id.btn_main02:
                new RTPermission.Builder()
                        .permissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.ACCESS_FINE_LOCATION})
                        .build()
                        .start(this, this);
                break;
        }
    }

    @Override
    public void onAllGranted(String[] allPermissions)
    {
        Toast.makeText(MainActivity.this, "所有权限都已通过", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeined(String[] dinedPermissions)
    {
        Toast.makeText(MainActivity.this, "无法获取所有权限", Toast.LENGTH_SHORT).show();
        for (String permission : dinedPermissions)
        {
            Log.e("ss", "被拒绝的权限:" + permission);
        }
    }
}
