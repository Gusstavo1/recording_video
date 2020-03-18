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
    private onExampleListener exampleListener;



    public RecyclerAdapter(ArrayList<ItemVideo> mListVideos,onExampleListener exampleListener) {
        this.mListVideos = mListVideos;
        this.exampleListener = exampleListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_upload,parent,false);
        //RecyclerAdapter.RecyclerViewHolder re = new RecyclerAdapter.RecyclerViewHolder(view);
        RecyclerAdapter.RecyclerViewHolder re = new RecyclerAdapter.RecyclerViewHolder(view,exampleListener);
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

    /*class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView fileName;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.nombreVideo);
        }
    }*/

     class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView fileName;
        onExampleListener exampleListener;



         public RecyclerViewHolder(@NonNull View itemView, onExampleListener exampleListener) {
            super(itemView);
            this.exampleListener = exampleListener;
            fileName = itemView.findViewById(R.id.nombreVideo);
            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View view) {
             exampleListener.onClick(getAdapterPosition());
         }
     }

    public interface onExampleListener{
        void onClick(int position);
    }
}
