package com.global.recordingvideo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class ServiceCheckInternet extends Service {

    private BroadCastInternet broadCastInternet = null;
    private String TAG = "ServiceCheckInternet";
    public static SharedPreferences miSharedPreferences;
    public static SharedPreferences.Editor editor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service id: "+startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        miSharedPreferences = getApplicationContext().getSharedPreferences("RUTAS_VIDEO", Context.MODE_PRIVATE);
        editor = miSharedPreferences.edit();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.setPriority(100);
        broadCastInternet = new BroadCastInternet();
        registerReceiver(broadCastInternet,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadCastInternet!=null){
            unregisterReceiver(broadCastInternet);
        }
    }
}
