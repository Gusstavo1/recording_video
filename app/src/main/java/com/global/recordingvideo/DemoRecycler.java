package com.global.recordingvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.global.recordingvideo.ui.main.BroadCastUpdateUi_Up;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DemoRecycler extends AppCompatActivity {

    public static RecyclerView recyclerDemo;
    public static RecyclerAdapter recyclerAdapterDemo;
    public static ArrayList<ItemVideo> mListvUploaded;
    String TAG = "DemoRecycler";
    BroadcastReceiver broadcastReceiver;

    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_recycler);

        Button btnRecycler = (Button)findViewById(R.id.btnDemo);
        recyclerDemo = (RecyclerView)findViewById(R.id.recyClerDemo);
        SharedPreferences sharedPreferences = getSharedPreferences("DEMO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        buildRecyclerView();
        getDataVideo();


        btnRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date miFecha = new Date();
                String format = "yyyy-MM-dd_hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                String fecha = simpleDateFormat.format(miFecha);

                edit.putString("VALUE_"+fecha,"EJEMPLO_"+fecha);
                edit.commit();

                Intent mIntent = new Intent("com.global.recordingvideo.UPDATE_UI_UP");
                mIntent.putExtra("VALUE","EJEMPLO_"+fecha);
                sendBroadcast(mIntent);

            }
        });

    }

    public void buildRecyclerView(){

        //recyclerViewUp = (RecyclerView)view.findViewById(R.id.miRecycler);
        mListvUploaded = new ArrayList<>();
        recyclerDemo.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerAdapterDemo = new RecyclerAdapter(mListvUploaded);
        recyclerDemo.setLayoutManager(layoutManager);
        recyclerDemo.setAdapter(recyclerAdapterDemo);
    }

    public void getDataVideo(){

        SharedPreferences sh = getSharedPreferences("DEMO", Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = sh.getAll();
        if(prefsMap.size()>0){
            recyclerDemo.setVisibility(View.VISIBLE);
            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                //String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                //Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
                mListvUploaded.add(new ItemVideo(entry.getValue().toString()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        broadcastReceiver = new BroadCastUpdateUi_Up();
        IntentFilter intentFilter = new IntentFilter("com.global.recordingvideo.UPDATE_UI_UP");
        intentFilter.addAction("com.global.recordingvideo.UPDATE_UI_UP");
        registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
