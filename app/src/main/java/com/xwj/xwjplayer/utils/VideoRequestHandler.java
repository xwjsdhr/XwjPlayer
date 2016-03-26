package com.xwj.xwjplayer.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

/**
 * Created by xiaweijia on 16/3/23.
 */
public class VideoRequestHandler extends RequestHandler {
    public static final String SCHEME_VIDEO = "video";

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return SCHEME_VIDEO.equals(scheme);
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(request.uri.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
        return new Result(bitmap, Picasso.LoadedFrom.DISK);
    }
}
