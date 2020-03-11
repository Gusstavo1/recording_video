package com.global.recordingvideo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class ClientAws extends Application {

    public static final String CHANNEL_ID = "com.global.recordingvideo.ANDROID";
    public static IdAletorio  NOTIFICATION_ID = new IdAletorio();
    private String TAG = "ClientAws";
    //private BroadCastInternet broadCastInternet;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {

                try{
                    Amplify.addPlugin(new AWSS3StoragePlugin());
                    Amplify.configure(getApplicationContext());
                    Log.d(TAG, "AWS INICIADO RESULT: "+result.toString());
                }catch (Exception   e){
                    Log.e(TAG, e.getMessage());
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al iniciar Storage...");
            }
        });
    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG,"Creando canal de notificacion.");
            CharSequence name = "NombreChannel";//getString(R.string.channel_name);
            String description = "Despcription"; //getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
