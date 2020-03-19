package com.global.recordingvideo;

import android.graphics.Bitmap;

public class ItemVideo {

    private String nombreArchivo;
    private int imgDelete;
    private Bitmap bitmap;

    public ItemVideo(String nombreArchivo, int imgDelete, Bitmap bitmap) {
        this.nombreArchivo = nombreArchivo;
        this.imgDelete = imgDelete;
        this.bitmap = bitmap;
    }

    public ItemVideo(String nombreArchivo, int imgDelete) {
        this.nombreArchivo = nombreArchivo;
        this.imgDelete = imgDelete;
    }

    public int getImgDelete() {
        return imgDelete;
    }

    public ItemVideo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
}
