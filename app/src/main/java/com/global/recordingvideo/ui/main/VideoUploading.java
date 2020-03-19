package com.global.recordingvideo.ui.main;


import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapaterUp;
import java.util.ArrayList;
import java.util.Map;
import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;

public class VideoUploading extends Fragment {


    public static  RecyclerView recyclerView;
    public static RecyclerAdapaterUp recyclerAdapter;
    public static ArrayList<ItemVideo> mListvUploading;
    public static   TextView menssage;

    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;
    private View view;

    IntentFilter intentFilter;
    private String TAG = "VideoUploading";

    public VideoUploading() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_uploading, container, false);
        menssage = (TextView)view.findViewById(R.id.messageVideo);
        buildRecyclerView(view);
        getDataVideo();

        return view;
    }

    public void buildRecyclerView(View view){

        recyclerView = (RecyclerView)view.findViewById(R.id.miRecycler2);
        mListvUploading = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new RecyclerAdapaterUp(mListvUploading,getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void getDataVideo(){

        Map<String, ?> prefsMap = miSharedPreferences.getAll();
        if(prefsMap.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            menssage.setVisibility(View.GONE);
            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                mListvUploading.add(new ItemVideo(nombreArchivo));
                Log.d(TAG,"Nombre del Archivo: ggg "+nombreArchivo);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            menssage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        broadcastReceiver = new BroadcastUpdateUi();
        intentFilter = new IntentFilter("com.global.recordingvideo.UPDATE_UI");
        intentFilter.addAction("com.global.recordingvideo.UPDATE_UI");
        getContext().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(broadcastReceiver);
    }

}
