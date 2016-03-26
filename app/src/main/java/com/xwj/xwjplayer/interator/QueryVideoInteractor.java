package com.xwj.xwjplayer.interator;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface QueryVideoInteractor {
    public void query(QueryListener listener);

    public void stop();
}
