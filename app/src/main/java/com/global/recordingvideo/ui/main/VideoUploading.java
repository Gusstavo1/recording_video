package com.global.recordingvideo.ui.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.global.recordingvideo.BroadcastUpdateUi;
import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapaterUp;
import com.global.recordingvideo.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Map;

import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;


public class VideoUploading extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerAdapaterUp recyclerAdapter;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private ArrayList<ItemVideo> mListvUploaded;
    private View view;
    private  TextView menssage;


    private String TAG = "VideoUploading";


    public VideoUploading() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_up, container, false);
        menssage = (TextView)view.findViewById(R.id.messageVideo);
        buildRecyclerView(view);
        getDataVideo();

        return view;
    }

    public void buildRecyclerView(View view){

        recyclerView = (RecyclerView)view.findViewById(R.id.miRecycler2);
        mListvUploaded = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new RecyclerAdapaterUp(mListvUploaded);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void getDataVideo(){

        Map<String, ?> prefsMap = miSharedPreferences.getAll();
        recyclerView.setVisibility(View.VISIBLE);
        if(prefsMap.size()>0){
            menssage.setVisibility(View.GONE);
            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                mListvUploaded.add(new ItemVideo(nombreArchivo));
                Log.d(TAG,"Nombre del Archivo: ggg "+nombreArchivo);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            menssage.setVisibility(View.VISIBLE);
        }

    }

}