package com.global.recordingvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<ItemVideo>mListVideos;
    private OnItemClickListener exampleListener;


    public void setOnclickListener(OnItemClickListener listener){
        exampleListener = listener;
    }

    public RecyclerAdapter(ArrayList<ItemVideo> mListVideos) {
        this.mListVideos = mListVideos;
        //this.exampleListener = exampleListener;
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
        holder.delete.setImageResource(itemVideo.getImgDelete());
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
         public ImageView delete;
        OnItemClickListener exampleListener;



         public RecyclerViewHolder(@NonNull View itemView, OnItemClickListener exampleListener) {
            super(itemView);

            this.exampleListener = exampleListener;
            fileName = itemView.findViewById(R.id.nombreVideo);
            delete = itemView.findViewById(R.id.imgDelete);
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(exampleListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            exampleListener.onItemClick(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(exampleListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            exampleListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

         @Override
         public void onClick(View view) {
             exampleListener.onItemClick(getAdapterPosition());
         }
     }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
}
