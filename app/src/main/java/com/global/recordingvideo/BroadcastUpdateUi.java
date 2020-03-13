package com.global.recordingvideo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;



public class BroadcastUpdateUi extends BroadcastReceiver {

    private String TAG = "BroadcastUpdateUi";
    private LinearLayoutManager linearLayoutManager;
    @Override

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"Lanzando broadcast: "+intent.getAction());
        Toast.makeText(context, "Update UI", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(context,ResultActivity.class);
        intent.putExtra("MESSAGE_BROADCAST","HAY COMUNICACION PERRO");
        context.startActivity(mIntent);

    }
}
