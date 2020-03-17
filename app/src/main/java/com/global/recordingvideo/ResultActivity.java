package com.global.recordingvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.global.recordingvideo.ui.main.BroadcastUpdateUi;

import java.util.ArrayList;
import java.util.Map;

import static com.global.recordingvideo.ManageFiles.mSharedPreferences;
import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;

public class ResultActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static RecyclerAdapter recyclerAdapter;
    static RecyclerView.LayoutManager layoutManager;
    static ArrayList<ItemVideo> mListvideos;

    private String TAG = "ResultActivity";
    private LinearLayout linearLayout;
    private BroadcastUpdateUi broadcastUpdateUi;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();
        linearLayout = (LinearLayout)findViewById(R.id.noCam);
        buildRecyclerView();

        try {
            Intent myIntent = new Intent();
            Log.d(TAG,"Mensaje from broadcast: "+myIntent.getStringExtra("MESSAGE_BROADCAST"));
        }catch (Exception e){
            Log.d(TAG,"Error intent: "+e.getMessage());
        }
        getData();
    }

    public void buildRecyclerView(){

        recyclerView = (RecyclerView)findViewById(R.id.miRecycler);
        mListvideos = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapter(mListvideos);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void getData(){


        if(miSharedPreferences.getAll().size()>0){
            linearLayout.setVisibility(View.GONE);
            Map<String, ?> prefsMap = miSharedPreferences.getAll();
            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                mListvideos.add(new ItemVideo(nombreArchivo));
                Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
            }

        }else{

            try {
                //mListvideos.clear();
                linearLayout.setVisibility(View.VISIBLE);
                Map<String, ?> prefMap = mSharedPreferences.getAll();

                for (Map.Entry<String, ?> entry: prefMap.entrySet()) {
                    Log.d(TAG, entry.getKey() + " VALOR: " +
                            entry.getValue().toString());
                    String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                    mListvideos.add(new ItemVideo(nombreArchivo));
                    Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
                }
            }catch (Exception e){
                Log.d(TAG,"Exception: "+e.getMessage());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG,"onStart. Register");
        broadcastReceiver = new BroadcastUpdateUi();
        IntentFilter intentFilter = new IntentFilter("com.global.recordingvideo.UPDATE_UI");
        intentFilter.addAction("com.global.recordingvideo.UPDATE_UI");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy. unRegister");
        unregisterReceiver(broadcastReceiver);
    }

}
