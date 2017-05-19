package com.lwkandroid.rtpermission.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 常量
 */

public class RTContants
{
    public static final String INTENT_KEY = "options";

    /**
     * 权限组分类
     */
    public static final class Permisson
    {
        public static final int CONTACT = 0X0;
        public static final int PHONE = 0X1;
        public static final int CALENDAR = 0X2;
        public static final int CAMERA = 0X3;
        public static final int SENSORS = 0X4;
        public static final int LOCATION = 0X5;
        public static final int STORAGE = 0X6;
        public static final int MICPHONE = 0X7;
        public static final int SMS = 0X8;
        public static final int UNKOWN = 0X9;

        @IntDef({CONTACT, PHONE, CALENDAR, CAMERA, SENSORS, LOCATION, STORAGE, MICPHONE, SMS, UNKOWN})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Group
        {
        }
    }
}
