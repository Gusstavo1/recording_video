package com.global.recordingvideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;

public class BroadCastInternet extends BroadcastReceiver {

    private String TAG = "BROADCAST";
    private SharedPreferences miShared;


    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        miShared = context.getSharedPreferences("RUTAS VIDEO",Context.MODE_PRIVATE);

        Log.d(TAG,"Se inicio Broadcast...");

        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){

            if(networkInfo == null){
                Log.d(TAG,"RED NO DETECTADA");
            }else {

                if(networkInfo.getState().equals("CONNECTED")){

                    String sharedPath = "data/data/"+context.getString(R.string.package_name)+"/RUTAS_VIDEO";
                    File carpeta = new File(sharedPath);

                    if(carpeta.exists()){

                        ManageFiles manageFiles = new ManageFiles();
                        //manageFiles.uploadFile();

                      Log.d(TAG,"Longitud: "+carpeta.length());
                      for (int i = 1; i<carpeta.length(); i++){

                          String rutaVideo = miShared.getString("PATH_VIDEO1"+i,"");
                          String nombreArchivo = rutaVideo.substring(rutaVideo.indexOf("test",rutaVideo.length()));

                          Log.d(TAG,"RUTA DE VIDEO: "+rutaVideo+"\nNombre Archivo: "+nombreArchivo);

                          /*manageFiles.uploadFile(rutaVideo, "video_demo.mp4", new ManageFiles.S3FileKey() {
                              @Override
                              public void resultKey(String key) {
                                  manageFiles.generateS3URL(key);
                              }
                          });*/
                      }

                    }else{
                        Log.d(TAG,"No existe carpeta.");
                    }


                    //Llama la uri del video
                    //Lanza el metodo de subir archivo...
                }

                Log.d(TAG,"netWork state: "+networkInfo.getState());
            }
        }

    }
}
