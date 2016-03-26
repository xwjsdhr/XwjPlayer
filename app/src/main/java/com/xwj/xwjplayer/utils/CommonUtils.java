package com.xwj.xwjplayer.utils;

import android.text.TextUtils;

import org.w3c.dom.Text;

/**
 * Created by xiaweijia on 16/3/25.
 */
public class CommonUtils {
    public static String getTimeString(long duration) {
        int minutes = (int) Math.floor(duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) - (minutes * 60));
        return minutes + ":" + String.format("%02d", seconds);
    }
}
