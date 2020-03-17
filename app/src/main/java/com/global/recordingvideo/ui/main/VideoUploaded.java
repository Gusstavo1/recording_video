package com.global.recordingvideo.ui.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.global.recordingvideo.BroadcastUpdateUi;
import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapter;
import java.util.ArrayList;
import java.util.Map;

public class VideoUploaded extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private ArrayList<ItemVideo> mListvUploaded;
    private View view;
    private TextView textView;
    private LinearLayout linearLayout;
    private BroadcastUpdateUi broadcastUpdateUi;
    private BroadcastReceiver broadcastReceiver;

    private String TAG = "VideoUploaded";

    public VideoUploaded() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        Toast.makeText(getContext(), "Hello world", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_upload_video, container, false);
        textView = (TextView)view.findViewById(R.id.mMessageVideo);
        buildRecyclerView(view);
        getDataVideo();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void buildRecyclerView(View view){

        recyclerView = (RecyclerView)view.findViewById(R.id.miRecycler);
        mListvUploaded = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new RecyclerAdapter(mListvUploaded);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void getDataVideo(){

        SharedPreferences sh = getContext().getSharedPreferences("SUBIDOS", Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = sh.getAll();
        if(prefsMap.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);

            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
                mListvUploaded.add(new ItemVideo(nombreArchivo));
            }
        }else{

            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);

        }

    }
}
