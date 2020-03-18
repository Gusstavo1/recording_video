package com.global.recordingvideo;

public class ItemVideo {

    private String nombreArchivo;
    private int imgDelete;

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
