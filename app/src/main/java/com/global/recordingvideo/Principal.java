package com.global.recordingvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity {

    private String TAG = "Principal";
    private BroadCastInternet broadCastInternet;
    private PeriodicWorkRequest periodicWorkRequest;

    public static SharedPreferences miSharedPreferences;
    public static SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        miSharedPreferences = getApplicationContext().getSharedPreferences("RUTAS_VIDEO",Context.MODE_PRIVATE);
        editor = miSharedPreferences.edit();

        periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkInternet.class,1, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
        
        Button btnOpenCam = (Button)findViewById(R.id.openCam);
        btnOpenCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!connect()){
                    dialogo();
                }

            }
        });
    }

    public void dialogo(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sin conexión a internet     :(\n¿Desea grabar sin estar conectado? el video se almacenará de forma local y se enviará en cuanto se reestablezca la conexión.")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Principal.this,VideoCaptureActivity.class);
                        startActivity(intent);
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

    public boolean connect(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG,"Hay internet. Tipo de conexión: "+networkInfo.getType());
            return true;
        } else {
            Log.d(TAG,"Sin conexión.");
            //dialogo();
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        broadCastInternet = new BroadCastInternet();
        registerReceiver(broadCastInternet,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(broadCastInternet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        unregisterReceiver(broadCastInternet);
    }
}
