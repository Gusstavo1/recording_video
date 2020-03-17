package com.global.recordingvideo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import static com.global.recordingvideo.ui.main.VideoUploading.mListvUploading;
import static com.global.recordingvideo.ui.main.VideoUploading.menssage;
import static com.global.recordingvideo.ui.main.VideoUploading.recyclerAdapter;

public class BroadcastUpdateUi extends BroadcastReceiver {

    private String TAG = "BroadcastUpdateUi";
    private LinearLayoutManager linearLayoutManager;
    @Override

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"Lanzando broadcast: "+intent.getAction());
        Toast.makeText(context, "Update UI", Toast.LENGTH_SHORT).show();
        String quitarItem = intent.getStringExtra("VIDEO");

        for(int i = 0; i< mListvUploading.size(); i++){

            if(mListvUploading.size() > 0){

                if(mListvUploading.size() == 1){
                    menssage.setVisibility(View.VISIBLE);
                }

                if(quitarItem.equals(mListvUploading.get(i).getNombreArchivo())){

                    Log.d(TAG,"mListaUploanding: "+mListvUploading.size());
                    recyclerAdapter.notifyItemRemoved(i);
                    mListvUploading.remove(i);
                    recyclerAdapter.notifyDataSetChanged();

                    Intent mIntent = new Intent("com.global.recordingvideo.UPDATE_UI_UP");
                    mIntent.putExtra("item",quitarItem);
                    context.sendBroadcast(mIntent);

                }
            }else{
                Log.d(TAG,"Muestra mensaje");
            }
        }

    }
}
