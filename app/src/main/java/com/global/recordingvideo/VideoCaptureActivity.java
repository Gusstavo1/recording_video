package com.global.recordingvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import static android.os.Environment.getExternalStorageDirectory;
import static com.global.recordingvideo.ServiceCheckInternet.editor;

public class VideoCaptureActivity extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private ImageView capture, switchCamera,pause;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM = 3;
    private static final int RC_HANDLE_RECORD_AUDIO_PERM = 4;
    private String TAG = "VIDEOCAPTURE";
    private TextView mTimerTv;
    private String filePath, fecha;
    private Intent intent;
    private long mCountDownTimer = 16000;

    public static int cameraId = -1;
    private Chronometer chronometer;
    private long time  = 0;
    private File mediaFile;
    private int contGrabaciones = 0;
    //public static SharedPreferences miSharedPreferences;
    private LottieAnimationView recordAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_cap);
        getSupportActionBar().hide();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        mTimerTv = (TextView) findViewById(R.id.tvTimer);


        File nuevaCarpeta = new File(getExternalStorageDirectory(), "videos_monitoreo_unidades");
        if(!nuevaCarpeta.exists()){
            nuevaCarpeta.mkdirs();
            //Toast.makeText(myContext, "Creando carpeta de videos", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(myContext, "La carpeta ya existe.", Toast.LENGTH_SHORT).show();
        }

        intent   = getIntent();

        //Log.d(TAG,"Ruta de archivo: "+filePath);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int sdCard = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (sdCard == PackageManager.PERMISSION_GRANTED) {
            int recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (recordAudio == PackageManager.PERMISSION_GRANTED) {
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    initialize();
                    initializeCamera();
                } else {
                    requestCameraPermission();
                }
            } else {
                requestRecordAudioPermission();
            }
        } else {
            requestSDCardPermission();
        }
    }

    private int findFrontFacingCamera() {
        cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Cámara no encontrada.", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED)
            initializeCamera();
    }

    private void initializeCamera() {
        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "Cámara frontal no encontrada", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {

        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);

        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        capture = (ImageView) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (ImageView) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);
        capture.setSelected(true);

        chronometer = (Chronometer)findViewById(R.id.cronometro);
        recordAnimation = (LottieAnimationView)findViewById(R.id.lottieAnimationView);
        pause = (ImageView)findViewById(R.id.button_pause);
        pause.setOnClickListener(pauseListener);
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {
                    Toast toast = Toast.makeText(myContext, "Lo sentimos, tu dispositivo tiene una cámara.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };

    public void chooseCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                mPreview.refreshCamera(mCamera);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    View.OnClickListener pauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(recording){
                chronometer.stop();
                recordAnimation.setVisibility(View.INVISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                Toast.makeText(VideoCaptureActivity.this, "¡Video Grabado!"+filePath, Toast.LENGTH_LONG).show();
                dialogSendVideo();
            }

            recording = false;
            capture.setSelected(true);
        }
    };

    boolean recording = false;
    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {

                contGrabaciones++;

            } else {
                if (!prepareMediaRecorder()) {
                    Toast.makeText(VideoCaptureActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table

                        try {
                            capture.setSelected(false);
                            mediaRecorder.start();
                            //startCountDownTimer();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();

                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });

                recording = true;
            }
        }
    };



    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    private boolean prepareMediaRecorder() {

        pause.setVisibility(View.VISIBLE);
        recordAnimation.setVisibility(View.VISIBLE);
        Date miFecha = new Date();
        String format = "yyyy-MM-dd_hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        fecha = simpleDateFormat.format(miFecha);

        mediaFile = new File(getExternalStorageDirectory()+"/videos_monitoreo_unidades" + "/testVideo"+fecha+".mp4");
        filePath = Uri.fromFile(mediaFile).getPath();

        Log.d(TAG,"PATH VIDEO :"+filePath);

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOrientationHint(CameraPreview.rotate);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
        mediaRecorder.setVideoFrameRate(15);
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Handles the requesting of the camera permission.
     */
    private void requestCameraPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_CAMERA_PERM);

    }

    private void requestSDCardPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "SDCard permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM);

    }

    private void requestRecordAudioPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "Record audio permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_RECORD_AUDIO_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RC_HANDLE_CAMERA_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //cameraView.setVisibility(View.VISIBLE);
                    initialize();
                    initializeCamera();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setMessage("Esta aplicación necesita el permiso para acceder a la cámara y poder grabar video");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {

                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setMessage("Esta aplicación no puede grabar video porque no tiene permiso de escritura de almacenamiento externo.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_RECORD_AUDIO_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setMessage("Esta aplicación no puede grabar video porque no tiene el permiso de grabación de audio.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;

        }
    }

    public void dialogSendVideo(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El video se enviará cuando se reestablezca la conexión.")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(mediaFile.exists()){
                            //Toast.makeText(myContext, "El archivo del video existe"+filePath, Toast.LENGTH_LONG).show();
                            Log.d(TAG,"El archivo de video se genero :PATH"+filePath+"\nVideos : "+contGrabaciones);
                            String uniqueID = UUID.randomUUID().toString();
                            editor.putString("PATH_VIDEO_"+fecha,filePath);
                            editor.commit();
                            pause.setVisibility(View.INVISIBLE);

                        }else{
                            Log.d(TAG,"No existe el archivo.");
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
