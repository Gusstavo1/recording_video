package com.global.recordingvideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import static com.global.recordingvideo.ui.main.VideoUploading.mListvUploaded;
import static com.global.recordingvideo.ui.main.VideoUploading.recyclerAdapter;

public class BroadcastUpdateUi extends BroadcastReceiver {

    private String TAG = "BroadcastUpdateUi";
    private LinearLayoutManager linearLayoutManager;
    @Override

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"Lanzando broadcast: "+intent.getAction());
        Toast.makeText(context, "Update UI", Toast.LENGTH_SHORT).show();
        String quitarItem = intent.getStringExtra("VIDEO");

        for(int i = 0; i<mListvUploaded.size(); i++){

            if(quitarItem.equals(mListvUploaded.get(i).getNombreArchivo())){
                recyclerAdapter.notifyItemRemoved(i);
                mListvUploaded.remove(i);
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}
