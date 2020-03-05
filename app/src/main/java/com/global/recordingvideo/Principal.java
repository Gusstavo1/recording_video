package com.global.recordingvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity {

    private String TAG = "Principal";
    private BroadCastInternet broadCastInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


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
        unregisterReceiver(broadCastInternet);
    }
}
