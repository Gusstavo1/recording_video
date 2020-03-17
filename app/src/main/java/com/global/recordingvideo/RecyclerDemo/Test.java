package com.global.recordingvideo.RecyclerDemo;

import com.global.recordingvideo.ItemVideo;

import java.util.Map;

import static com.global.recordingvideo.RecyclerDemo.Demo.mListvUploaded;
import static com.global.recordingvideo.RecyclerDemo.Demo.sh;

public class Test {

    public static void  getData(){

        Map<String, ?> prefsMap = sh.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            String nombreArchivo = entry.getValue().toString();
            mListvUploaded.add(new ItemVideo(nombreArchivo));
        }
    }
}
