# RTPermission
Android6.0 运行时权限申请工具，Android Marshmallow runtime permission utils

# RTPermission<br />
想了解该项目可参考下面的博客：<br />
http://www.jianshu.com/p/1841a74543c1 <br />
http://blog.csdn.net/lwk520136/article/details/72621729 <br />
## 前言
Android6.0发布后带来的运行时权限机制，使得开发者的工作又多了一点，在程序需要使用到9类敏感权限时必须去动态申请权限使用权，然而按照标准写法实在是太繁琐了，要是每一个权限都写一大堆申请步骤，我估计开发者分分钟暴走。所以，封装一个统一的请求框架是很有必要的，**RTPermission**就是为了简化申请步骤而做的，有需要的可参考一下，欢迎star和fork。
<br />
## 使用方式：<br />
**1. Gradle中引入库**

```
          compile 'com.lwkandroid:RTPermissionLib:1.0.0'
```
<br />

**2. 代码中调用**<br />

```
          new RTPermission.Builder()
                .permissions(new String[]{各种需要申请的权限})
                .build()
                .start(Activity activity, new OnPermissionResultListener()
                {
                     @Override
                      public void onAllGranted(String[] allPermissions)
                     {
                         //所有权限都已获得使用权后的回调
                     }

                     @Override
                     public void onDeined(String[] dinedPermissions)
                     {
                         //有权限未获得试用期的回调
                     }
               });

```
<br />
## 效果图 <br />
![](https://github.com/Vanish136/RTPermission/raw/master/picture/sample01.png)
<br />
![](https://github.com/Vanish136/RTPermission/raw/master/picture/sample02.png)
<br />

### 附录：9大组需要适配的权限

```
//通讯录权限组
group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS

//通话权限组
group:android.permission-group.PHONE
  permission:android.permission.READ_CALL_LOG
  permission:android.permission.READ_PHONE_STATE
  permission:android.permission.CALL_PHONE
  permission:android.permission.WRITE_CALL_LOG
  permission:android.permission.USE_SIP
  permission:android.permission.PROCESS_OUTGOING_CALLS
  permission:com.android.voicemail.permission.ADD_VOICEMAIL

//日历、日程信息权限组
group:android.permission-group.CALENDAR
  permission:android.permission.READ_CALENDAR
  permission:android.permission.WRITE_CALENDAR

//摄像头权限组
group:android.permission-group.CAMERA
  permission:android.permission.CAMERA

//身体传感器权限组
group:android.permission-group.SENSORS
  permission:android.permission.BODY_SENSORS

//定位权限组
group:android.permission-group.LOCATION
  permission:android.permission.ACCESS_FINE_LOCATION
  permission:android.permission.ACCESS_COARSE_LOCATION

//外部存储卡权限组
group:android.permission-group.STORAGE
  permission:android.permission.READ_EXTERNAL_STORAGE
  permission:android.permission.WRITE_EXTERNAL_STORAGE

//录音权限组
group:android.permission-group.MICROPHONE
  permission:android.permission.RECORD_AUDIO

//短信权限组
group:android.permission-group.SMS
  permission:android.permission.READ_SMS
  permission:android.permission.RECEIVE_WAP_PUSH
  permission:android.permission.RECEIVE_MMS
  permission:android.permission.RECEIVE_SMS
  permission:android.permission.SEND_SMS
  permission:android.permission.READ_CELL_BROADCASTS
```