package com.global.recordingvideo;

import android.app.Application;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class ClientAws extends Application {

    private String TAG = "ClientAws";

    @Override
    public void onCreate() {
        super.onCreate();

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
}
