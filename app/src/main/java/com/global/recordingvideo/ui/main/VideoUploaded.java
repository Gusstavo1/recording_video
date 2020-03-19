package com.global.recordingvideo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.global.recordingvideo.ItemVideo;
import com.global.recordingvideo.R;
import com.global.recordingvideo.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class VideoUploaded extends Fragment {

    public static RecyclerView recyclerViewUp;
    public static RecyclerAdapter recyclerAdapterUp;
    public static ArrayList<ItemVideo> mListvUploaded;
    private TextView messageNoVideo;
    private ImageView imgDemo;

    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private View view ;
    //private LinearLayout linearLayout;
    private BroadcastReceiver broadcastReceiver;
    private String TAG = "VideoUploaded";
    private IntentFilter intentFilter;
    private SharedPreferences sh;
    private SharedPreferences.Editor edit;


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
        imgDemo = (ImageView)view.findViewById(R.id.imageDemo);

        buildRecyclerView(view);
        getDataVideo();
        //imgDemo.setImageBitmap(getFrame("/storage/emulated/0/videos_monitoreo_unidades/testVideo2020-03-18_05:01:00.mp4"));

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
        recyclerAdapterUp = new RecyclerAdapter(mListvUploaded);
        recyclerViewUp.setLayoutManager(layoutManager);
        recyclerViewUp.setAdapter(recyclerAdapterUp);

        recyclerAdapterUp.setOnclickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                Log.d(TAG,"Eliminando !");
                showDialog(position);
            }
        });
    }

    public void getDataVideo(){

        sh = getContext().getSharedPreferences("SUBIDOS", Context.MODE_PRIVATE);
        edit = sh.edit();

        Map<String, ?> prefsMap = sh.getAll();

        if(prefsMap.size()>0){
            recyclerViewUp.setVisibility(View.VISIBLE);
            messageNoVideo.setVisibility(View.GONE);

            for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
                Log.d(TAG, entry.getKey() + " VALOR: " +
                        entry.getValue().toString());
                String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
                Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);
                mListvUploaded.add(new ItemVideo(nombreArchivo,R.drawable.ic_delete));
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

    public void showDialog(int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Desea eliminar este elemento de la lista?")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(position);
                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
    }

    public void removeItem(int position){

        mListvUploaded.remove(position);
        recyclerAdapterUp.notifyItemRemoved(position);
        Map<String, ?> prefsMap = sh.getAll();

        int cont = 0;
        Log.d(TAG,"position "+position);
        ArrayList<String>demoLista = new ArrayList<>();

        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            Log.d(TAG,"VALUE "+entry.getValue().toString());
            demoLista.add(entry.getValue().toString());
        }

        for (int i = 0; i<demoLista.size(); i++){

            if(position == i){

                Log.d(TAG,"KEY en CACHE: "+demoLista.get(i)+" Borrando...");
                edit.remove("video_"+demoLista.get(i)).apply();
                edit.commit();
                File fileVideo = new File(getString(R.string.pathVideo)+demoLista.get(i));
                if(fileVideo.exists()){
                    fileVideo.delete();
                    Log.d(TAG,"Archivo de video borrado");
                }else {
                    Log.d(TAG,"Elemento no borrando");
                }
            }
        }
        demoLista.clear();
    }

}
