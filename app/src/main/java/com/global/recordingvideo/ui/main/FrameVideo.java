package com.global.recordingvideo.ui.main;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class FrameVideo {

    public static Bitmap getFrame(String path){

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String METADATA_KEY_DURATION = mediaMetadataRetriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Bitmap bmpOriginal = mediaMetadataRetriever.getFrameAtTime(0);

        return bmpOriginal;
    }
}
