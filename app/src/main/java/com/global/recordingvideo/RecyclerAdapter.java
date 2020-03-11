package com.global.recordingvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<ItemVideo>mListVideos;

    public RecyclerAdapter(ArrayList<ItemVideo> mListVideos) {
        this.mListVideos = mListVideos;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        RecyclerAdapter.RecyclerViewHolder re = new RecyclerAdapter.RecyclerViewHolder(view);
        return re;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ItemVideo itemVideo = mListVideos.get(position);
        holder.fileName.setText(itemVideo.getNombreArchivo());
    }

    @Override
    public int getItemCount() {
        return mListVideos.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView fileName;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.nombreVideo);
        }
    }
}
