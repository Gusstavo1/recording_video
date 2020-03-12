package com.global.recordingvideo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.ResultListener;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.amplifyframework.storage.result.StorageUploadFileResult;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.concurrent.TimeUnit;

import static com.global.recordingvideo.ClientAws.CHANNEL_ID;
import static com.global.recordingvideo.ClientAws.NOTIFICATION_ID;
import static com.global.recordingvideo.ServiceCheckInternet.editor;
import static com.global.recordingvideo.ServiceCheckInternet.miSharedPreferences;

public class ManageFiles {

    private static final String TAG = "UploadFile";
    OneTimeWorkRequest oneTimeWorkRequest;
    private Context context;
    public Context getContext() {
        return context;
    }
    public ManageFiles(Context context) {
        this.context = context;
    }


    public void uploadFile(String pathFile, String fileName, final S3FileKey fileKey){

        StorageUploadFileOptions options =   StorageUploadFileOptions.builder().accessLevel(StorageAccessLevel.PUBLIC).build();

        Amplify.Storage.uploadFile(
                fileName,
                pathFile,
                options,
                new ResultListener<StorageUploadFileResult>() {
                    @Override
                    public void onResult(StorageUploadFileResult result) {
                        Log.d(TAG, "Carga correcta: " + result.getKey());

                        String nombreKey = fileName.substring(fileName.indexOf("20"),(fileName.length())-4);
                        Log.d(TAG,"PATH_VIDEO_"+nombreKey);

                        fileKey.resultKey(result.getKey());
                        //Lanza notificacion
                        //createNotification(result.getKey());
                         createNotification("Upload","Video enviado.","Se envio "+fileName);

                        //Quital el valor de memoria cache.
                        editor.remove("PATH_VIDEO_"+nombreKey).commit();

                        if( miSharedPreferences.getAll().size()== 0){
                            androidx.work.WorkManager.getInstance().cancelAllWorkByTag("uploadFiles");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d(TAG, "Error al subir archivo: " + error.getMessage());
                        //createErrorNotification();
                        //Drawable iconError = getContext().getResources().getDrawable(R.drawable.ic_error);

                        createNotification("Error","Video no enviado."
                                ,"Algo ocurrio, el video será enviado de nuevo." );

                        if(miSharedPreferences.getAll().size()>0){
                            Log.d(TAG,"Lanza WorkManager ");

                            oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkUpload.class)
                                    .addTag("uploadFiles")
                                    .setInitialDelay(1, TimeUnit.SECONDS)
                                    .build();
                            WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);

                        }
                    }
                }
        );
    }

    public String generateS3URL(String fileKey){

        final String BUCKET_NAME =   "bucketmoviles121652-dev";
        AmazonS3Client client   =   ((AWSS3StoragePlugin) Amplify.Storage.getPlugin("awsS3StoragePlugin")).getEscapeHatch();
        return client.getResourceUrl(BUCKET_NAME, "public/" + fileKey);
    }

    public interface S3FileKey{
        void resultKey(String key);
    }

    public void createNotification(String tipe, String Title, String text){

        Log.d(TAG,"Creando notificacion "+tipe);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
        Intent mIntent = new Intent(getContext(),ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),1,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(tipe.equals("Error")){
            builder.setSmallIcon(R.drawable.ic_error);
        }else{
            builder.setSmallIcon(R.drawable.ic_file_upload);
        }
        builder.setContentTitle(Title);
        builder.setContentText(text);
        builder.setColor(Color.BLUE);
        builder.setVibrate(new long[]{1000,1000,1000,1000});
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(NOTIFICATION_ID.idAleatorio(),builder.build());
    }

}
