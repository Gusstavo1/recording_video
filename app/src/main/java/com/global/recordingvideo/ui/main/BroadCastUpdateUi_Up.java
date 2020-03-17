package com.global.recordingvideo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.global.recordingvideo.ItemVideo;

import static com.global.recordingvideo.ui.main.VideoUploaded.mListvUploaded;
import static com.global.recordingvideo.ui.main.VideoUploaded.recyclerAdapterUp;


public class BroadCastUpdateUi_Up extends BroadcastReceiver {

    private String TAG = "BroadCastUpdateUi_Up";

    @Override
    public void onReceive(Context context, Intent intent) {

        String newItem = intent.getStringExtra("item");
        Log.d(TAG,"Run: "+intent.getAction()+" nuevo item: "+newItem);

        recyclerAdapterUp.notifyItemInserted((mListvUploaded.size()-1));
        mListvUploaded.add(new ItemVideo(newItem));
        recyclerAdapterUp.notifyDataSetChanged();
    }
}
