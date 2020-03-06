package com.global.recordingvideo;

import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.ResultListener;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.amplifyframework.storage.result.StorageUploadFileResult;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class ManageFiles {
    private static final String TAG = "UploadFile";

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
                        fileKey.resultKey(result.getKey());

                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d(TAG, "Error al subir archivo: " + error.getMessage());
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


}
