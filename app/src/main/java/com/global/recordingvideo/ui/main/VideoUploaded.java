package com.global.recordingvideo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapter;
import java.util.ArrayList;
import java.util.Map;

public class VideoUploaded extends Fragment implements RecyclerAdapter.onExampleListener {

    public static RecyclerView recyclerViewUp;
    public static RecyclerAdapter recyclerAdapterUp;
    public static ArrayList<ItemVideo> mListvUploaded;
    private TextView messageNoVideo;
    private ImageButton deleteItem;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private View view , viewItem;
    //private LinearLayout linearLayout;
    private BroadcastReceiver broadcastReceiver;
    private String TAG = "VideoUploaded";
    private IntentFilter intentFilter;


    public VideoUploaded() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_upload_video, container, false);

        messageNoVideo  = (TextView)view.findViewById(R.id.mMessageVideo);
        viewItem        = inflater.inflate(R.layout.item_video_upload,container,false);
        deleteItem      = (ImageButton)viewItem.findViewById(R.id.delete);

        buildRecyclerView(view);
        getDataVideo();


        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Delete !");
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void buildRecyclerView(View view){

        recyclerViewUp = (RecyclerView)view.findViewById(R.id.recyclerUpload);
        mListvUploaded = new ArrayList<>();
        recyclerViewUp.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAdapterUp = new RecyclerAdapter(mListvUploaded,this);
        recyclerViewUp.setLayoutManager(layoutManager);
        recyclerViewUp.setAdapter(recyclerAdapterUp);
    }

    public void getDataVideo(){

        SharedPreferences sh = getContext().getSharedPreferences("SUBIDOS", Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = sh.getAll();

        if(prefsMap.size()>0){
            recyclerViewUp.setVisibility(View.VISIBLE);
            messageNoVideo.setVisibility(View.GONE);

            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
                mListvUploaded.add(new ItemVideo(nombreArchivo));
            }
        }else{
            recyclerViewUp.setVisibility(View.GONE);
            messageNoVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        broadcastReceiver = new BroadCastUpdateUi_Up();
        intentFilter = new IntentFilter("com.global.recordingvideo.UPDATE_UI_UP");
        intentFilter.addAction("com.global.recordingvideo.UPDATE_UI_UP");
        getContext().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(int position) {

        Log.d(TAG,"Click position ! "+position);
    }
}
