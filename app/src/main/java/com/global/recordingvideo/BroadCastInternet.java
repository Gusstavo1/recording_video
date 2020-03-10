package com.global.recordingvideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;
import static com.global.recordingvideo.Principal.miSharedPreferences;

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

    /*public void workManagerState(){
        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(BroadCastInternet.super , new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if(workInfo != null){
                            Log.d(TAG,"Estado workManager  "+workInfo.getState().name());
                            if(workInfo.getState().name().equals("ENQUEUED")){
                                Log.d(TAG,"El workmanager esta en espera...");
                            }else if(workInfo.getState().name().equals("CANCELLED")){
                                Log.d(TAG,"Workmanager en otro estado...");
                            }
                        }else{
                            Log.d(TAG,"Workmanager nulo");
                        }
                    }
                });

    }*/


}
