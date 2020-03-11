package com.global.recordingvideo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Map;

import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;

public class WorkUpload extends Worker {

    private String TAG = "WorkUpload";
    public WorkUpload(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG,"Ejecutando workmanager");

        Map<String, ?> prefsMap = miSharedPreferences.getAll();
        ManageFiles manageFiles = new ManageFiles(getApplicationContext());

        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            Log.d(TAG, entry.getKey() + " VALOR: " +
                    entry.getValue().toString());
            String nombreArchivo = entry.getValue().toString().substring(entry.getValue().toString().indexOf("test"));
            Log.d(TAG,"Nombre del Archivo: "+nombreArchivo);

            manageFiles.uploadFile(entry.getValue().toString(), nombreArchivo, new ManageFiles.S3FileKey() {
                @Override
                public void resultKey(String key) {
                    manageFiles.generateS3URL(key);
                }
            });
        }
        return Result.success();
    }
}
