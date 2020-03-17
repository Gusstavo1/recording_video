package com.global.recordingvideo.RecyclerDemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapaterUp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Demo extends AppCompatActivity {

    private int count = 0;

    public static RecyclerView recyclerView;
    public static RecyclerAdapaterUp recyclerAdapter;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    public static ArrayList<ItemVideo> mListvUploaded;

    public static SharedPreferences sh;
    private String TAG = "onCreate";
    private BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        sh = getSharedPreferences("DEMO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();

        Button btn = (Button)findViewById(R.id.btnAdd);
        Button btnQ = (Button)findViewById(R.id.btnQuitar);
        RecyclerView rec = (RecyclerView)findViewById(R.id.miRecyclerDemo);
        buildRecyclerView();

        getData();
        recyclerView.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date miFecha = new Date();
                String format = "yyyy-MM-dd_hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                String fecha = simpleDateFormat.format(miFecha);

                edit.putString("VALUE"+fecha,fecha);
                edit.commit();



                /*count++;
                String uniqueID = UUID.randomUUID().toString();
                */

                //recyclerView.setVisibility(View.INVISIBLE);
                //Intent mIntent = new Intent(getApplicationContext(),Demo.class);
                //mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(mIntent);
            }
        });

        btnQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("UPDATE_UI");
                intent.putExtra("miValue","VALUE"+count);
                sendBroadcast(intent);
            }
        });
    }


    public void buildRecyclerView(){

        recyclerView = (RecyclerView)findViewById(R.id.miRecyclerDemo);
        mListvUploaded = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapaterUp(mListvUploaded);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public static void  getData(){

        Map<String, ?> prefsMap = sh.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            //Log.d(TAG, entry.getKey() + " VALOR: " +
            //        entry.getValue().toString());
            String nombreArchivo = entry.getValue().toString();
            mListvUploaded.add(new ItemVideo(nombreArchivo));
           // Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        broadcastReceiver = new BroadcastRecycler();
        intentFilter = new IntentFilter("UPDATE_UI");
        intentFilter.addAction("UPDATE_UI");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
