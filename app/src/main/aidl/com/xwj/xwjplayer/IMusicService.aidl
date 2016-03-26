// IMusicService.aidl
package com.xwj.xwjplayer;

// Declare any non-default types here with import statements


interface IMusicService {

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void play(String musicPath);
    void pause();
    void stop();
    void pre();
    void next();
    int getCurrentDuration();
    int getDuration();
}
