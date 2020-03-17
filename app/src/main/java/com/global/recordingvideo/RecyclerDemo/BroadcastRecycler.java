package com.global.recordingvideo.RecyclerDemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.global.recordingvideo.ItemVideo;

import java.util.ArrayList;

import static com.global.recordingvideo.RecyclerDemo.Demo.mListvUploaded;
import static com.global.recordingvideo.RecyclerDemo.Demo.recyclerAdapter;
import static com.global.recordingvideo.RecyclerDemo.Demo.recyclerView;
import static com.global.recordingvideo.RecyclerDemo.Demo.sh;

public class BroadcastRecycler extends BroadcastReceiver {

    private String TAG = "BroadcastRecycler";
    public static ArrayList<ItemVideo> listaCopy;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"INTENT: "+intent.getAction());

        listaCopy = new ArrayList<>();

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {


                recyclerAdapter.notifyItemRemoved(0);
                mListvUploaded.remove(0);
                recyclerAdapter.notifyDataSetChanged();

                //String dato = intent.getStringExtra("miValue");
                //Log.d(TAG,"Recibiendo valor "+dato);
                //recyclerAdapter.notifyDataSetChanged();
                //

                //mListvUploaded = listaCopy;
                /*mListvUploaded.clear();
                recyclerAdapter.notifyDataSetChanged();
                listaCopy.add(new ItemVideo(dato));

                mListvUploaded = listaCopy;
                recyclerAdapter.notifyDataSetChanged();

                Test.getData();*/

                /*Intent intent = new Intent(context,Demo.class);
                ((Activity) context).finish();
                ((Activity) context).overridePendingTransition(0,0);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0,0);*/

                //Code goes here
                /*Log.d(TAG,"Running ! ");
                Test.getData();
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);*/
            }
        });
    }

}
