package com.global.recordingvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity {

    private String TAG = "Principal";
    private Message message;
    private long LIMITE_GIGABYTE = 30000000; //numero en kilobytes igual a 30 MB

    /*
    * Revisar la clase VideoUploded y VideoUploading
    * el metodo getData la siguiente linea :
    *
    *   String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getSupportActionBar().hide();

        startService(new Intent(this,ServiceCheckInternet.class));

        Button btnOpenCam   = (Button)findViewById(R.id.openCam);
        Button btnVideos    = (Button)findViewById(R.id.verArchivos);
        btnOpenCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!connect()){
                    dialogo();
                }

            }
        });

        btnVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getApplicationContext(),TabsVideos.class);
                startActivity(mIntent);
            }
        });
    }


    public boolean connect(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG,"Hay internet. Tipo de conexión: "+networkInfo.getType());
            return true;
        } else {
            Log.d(TAG,"Sin conexión.");

            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        //unregisterReceiver(broadCastInternet);
    }

    public long availableMemory(){

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;
        Log.e(TAG,"Available MB : "+megAvailable);
        File path = Environment.getDataDirectory();
        StatFs stat2 = new StatFs(path.getPath());
        long blockSize = stat2.getBlockSize();
        long availableBlocks = stat2.getAvailableBlocks();

        String format =  Formatter.formatFileSize(this, availableBlocks * blockSize);
        Log.d(TAG,"Memoria interna disponible : "+format);
        long freeBytesInternal = new File(getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        long memoryBytes = availableBlocks*blockSize;
        return memoryBytes;
    }

    public void dialogo(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setTitle("Sin conexión a internet ")
                .setMessage("¿Desea grabar sin estar conectado? el video se almacenará de forma local y se enviará en cuanto se reestablezca la conexión.")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(availableMemory() >= LIMITE_GIGABYTE){
                            Intent intent = new Intent(Principal.this,VideoCaptureActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d(TAG,"El espacio en memoria interna no es sufuciente");
                            dialogoMemoria();
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        builder.create().dismiss();
                    }
                });
        builder.create().show();
    }

    public void dialogoMemoria(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setTitle("Sin espacio")
                .setMessage("Espacio en memoria interna no suficiente. Debe tener almenos 30 MB libres.")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create().show();
    }


}
