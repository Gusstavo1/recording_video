package com.global.recordingvideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;


import static com.global.recordingvideo.Principal.miSharedPreferences;

public class BroadCastInternet extends BroadcastReceiver {

    private String TAG = "BROADCAST";
    //private SharedPreferences miShared;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //miShared = context.getSharedPreferences("RUTAS VIDEO",Context.MODE_PRIVATE);

        Log.d(TAG,"Se inicio Broadcast...");

        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            if(networkInfo == null){
                Log.d(TAG,"RED NO DETECTADA");
            }else {

                if(networkInfo.getState().toString().equals("CONNECTED")){

                    //Log.d(TAG,"Shared preferences Tamaño: "+miSharedPreferences.getAll().size());
                    //Map<String, ?> prefsMap = miSharedPreferences.getAll();
                    //ManageFiles manageFiles = new ManageFiles(context);

                    if(miSharedPreferences.getAll().size() > 0){
                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkUpload.class)
                                .addTag("Demo")
                                .setInitialDelay(1, TimeUnit.SECONDS)
                                .build();
                        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
                        Log.d(TAG,"Lanza workmanager!");

                        /*for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                            Log.d(TAG, entry.getKey() + " VALOR: " +
                                    entry.getValue().toString());
                            String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                            Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);

                            manageFiles.uploadFile(entry.getValue().toString(), nombreArchivo, new ManageFiles.S3FileKey() {
                                @Override
                                public void resultKey(String key) {
                                    manageFiles.generateS3URL(key);
                                }
                            });
                            miSharedPreferences.edit().clear().apply();
                        }*/

                    }else{
                        Log.d(TAG,"No existen rutas en cache.");
                    }
                }
                Log.d(TAG,"Estado de conexion: "+networkInfo.getState());
            }
        }
    }

    /*public class UploadVideoFiles extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }*/
}
