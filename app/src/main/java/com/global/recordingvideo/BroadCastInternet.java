package com.global.recordingvideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;

public class BroadCastInternet extends BroadcastReceiver {

    private String TAG = "BROADCAST";
    OneTimeWorkRequest oneTimeWorkRequest;
    //private SharedPreferences miShared;
    private boolean inicioWorknager = false;
    private Context context;

    public Context getContext() {
        return context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            if(networkInfo == null){
                Log.d(TAG,"RED NO DETECTADA");
                androidx.work.WorkManager.getInstance().cancelAllWorkByTag("uploadFiles");
            }else {
                if(networkInfo.getState().toString().equals("CONNECTED")){
                    Log.d(TAG,"Estado de conexion: "+networkInfo.getState());

                    if(miSharedPreferences.getAll().size() > 0){
                        //Cancela si el workmanager esta en ejecución!

                        androidx.work.WorkManager.getInstance().cancelAllWorkByTag("uploadFiles");

                        //Se inicio el workmanager
                        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkUpload.class)
                                .addTag("uploadFiles")
                                .setInitialDelay(1, TimeUnit.SECONDS)
                                .build();
                        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);

                    }else{
                        Log.d(TAG,"No existen rutas en cache.");
                    }
                }
            }
        }
    }

}
