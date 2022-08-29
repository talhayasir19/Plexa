package com.example.plexa;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.experimental.Experimental;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalExposureCompensation;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ExposureState;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButton;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    AppCompatButton Capture;
    SeekBar brighnesseekbar;
    ImageView flip,flash,brightness,zoom,size,timer,filters,plus,minus,Edit;
    ImageCapture imageCapture;
    ProcessCameraProvider cameraProvider;
    AppCompatButton Gallery,Editor;
    int lensFacing=CameraSelector.LENS_FACING_BACK,RESULT_LOAD_IMG=100;
    CameraControl cameraControl;
    CameraInfo cameraInfo;
    String DEBUG_TAG="touch";
    androidx.camera.core.Camera camera;
    TextView timetext,zero,three,five;
    Boolean flashstate=false,zoomstate=false;
    LinearLayout timerlayout;
    int bright;
    int countdown=0,count=0;
    int Timer=0;
    double zoomvalue=0F;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            previewView=findViewById(R.id.previewView);
            Capture=findViewById(R.id.Capture);
            flip=findViewById(R.id.flip);
            flash=findViewById(R.id.flash);
            timetext=findViewById(R.id.timetext);
            brightness=findViewById(R.id.brightness);
            zoom=findViewById(R.id.zoom);
            size=findViewById(R.id.size);
            plus=findViewById(R.id.plus);
            minus=findViewById(R.id.minus);
            filters=findViewById(R.id.filters);
            Edit=findViewById(R.id.Edit);
            timerlayout=findViewById(R.id.timelayout);
            timer=findViewById(R.id.timer);
            zero=findViewById(R.id.zero);
            three=findViewById(R.id.three);
            five=findViewById(R.id.five);
            brighnesseekbar=findViewById(R.id.brightnessseekbar);
            //permissions
        ActivityResultLauncher<String> requestforpermission=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){

                }
            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestforpermission.launch(Manifest.permission.CAMERA);
        }




        //camera getter
            cameraProviderFuture = ProcessCameraProvider.getInstance(MainActivity.this);

            cameraProviderFuture.addListener(() -> {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(lensFacing)
                            .build();
                    if(cameraProvider.hasCamera(cameraSelector)) {
                        bindPreview(cameraProvider);
                    }
                } catch (ExecutionException | InterruptedException | CameraInfoUnavailableException e) {
                }
            }, ContextCompat.getMainExecutor(this));

            //Font back camera change

            flip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lensFacing==CameraSelector.LENS_FACING_BACK){
                        cameraProvider.unbindAll();
                        lensFacing=CameraSelector.LENS_FACING_FRONT;
                        try {
                            bindPreview(cameraProvider);
                        } catch (CameraInfoUnavailableException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(lensFacing==CameraSelector.LENS_FACING_FRONT){
                        cameraProvider.unbindAll();
                        lensFacing=CameraSelector.LENS_FACING_BACK;
                        try {
                            bindPreview(cameraProvider);
                        } catch (CameraInfoUnavailableException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            flash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    torch();
                }
            });
            //Timer buttons
            zero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     countdown=0;
                     count=0;
                     timerlayout.setVisibility(View.GONE);
                    timer.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.btnbackground1));

                }
            });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdown=3000;
                count=1000;
                timerlayout.setVisibility(View.GONE);
                timer.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.btnbackground1));
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdown=5000;
                count=1000;
                timerlayout.setVisibility(View.GONE);
                timer.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.btnbackground1));

            }
        });
        //Zoom buttons
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(zoomvalue<1){
                        zoomvalue=zoomvalue+0.2;
                        cameraControl.setLinearZoom((float) zoomvalue);
                    }
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(zoomvalue>0){
                        zoomvalue=zoomvalue-0.2;
                        cameraControl.setLinearZoom((float) zoomvalue);

                    }
                }
            });

            previewView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    setUpCameraAutoFocus();
                    animateFocusRing(motionEvent.getX(), motionEvent.getY());
                    return true;
                }
            });
            zoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup.LayoutParams layoutParams = plus.getLayoutParams();
                    ViewGroup.LayoutParams layoutParams1 = minus.getLayoutParams();
                    if (!zoomstate) {
                        layoutParams.width = 100;
                        layoutParams1.width = 100;
                        layoutParams.height = 100;
                        layoutParams1.height = 100;
                        plus.setLayoutParams(layoutParams);
                        minus.setLayoutParams(layoutParams1);
                        zoomstate = true;
                    } else if (zoomstate) {
                        layoutParams.width = 0;
                        layoutParams1.width = 0;
                        layoutParams.height = 0;
                        layoutParams1.height = 0;
                        plus.setLayoutParams(layoutParams);
                        minus.setLayoutParams(layoutParams1);
                        zoomstate = false;

                    }
                }
            });
            Capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  if(countdown==0){
                      MediaPlayer mp=MediaPlayer.create(MainActivity.this,R.raw.click);
                      mp.start();

                          capture();


                  }
                  else{
                   new CountDownTimer(countdown,count) {
                       @Override
                       public void onTick(long l) {
                           l=l+1000;
                           timetext.setVisibility(View.VISIBLE);
                           timetext.setText(String.valueOf(l/1000));
                       }

                       @Override
                       public void onFinish() {
                           MediaPlayer mp=MediaPlayer.create(MainActivity.this,R.raw.click);
                           mp.start();
                           capture();
                           timetext.setVisibility(View.GONE);
                       }
                   }.start();

                   };

                }
            });


         brightness.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(brighnesseekbar.getVisibility()==View.GONE){
                     brighnesseekbar.setVisibility(View.VISIBLE);
                 }
                 else{
                     brighnesseekbar.setVisibility(View.GONE);
                 }
             }
         });




            Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this, com.example.plexa.Editactivity.class);
                    startActivity(intent);

                }
            });
            timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(timerlayout.getVisibility()==View.GONE) {
                        timerlayout.setVisibility(View.VISIBLE);
                        timer.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.btnbackground2));
                    }
                    else{
                        timerlayout.setVisibility(View.GONE);
                        timer.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.btnbackground1));

                    }
                           }

            });
            brighnesseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @SuppressLint("UnsafeOptInUsageError")
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                     bright=i-15;
//                    @SuppressLint("UnsafeOptInUsageError") ExposureState exposureState = camera.getCameraInfo().getExposureState();
//
//                    @SuppressLint("UnsafeOptInUsageError") Range<Integer> range = exposureState.getExposureCompensationRange();
//                    //Range -20 to 20
                    camera.getCameraControl().setExposureCompensationIndex(bright);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @SuppressLint("UnsafeOptInUsageError")
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {


                }

            });


        }

        //After getting camera bind to pereview view
        void bindPreview(@NonNull ProcessCameraProvider cameraProvider) throws CameraInfoUnavailableException {
            previewView.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);
            cameraProvider.unbindAll();
            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build();
            if(cameraProvider.hasCamera(cameraSelector)){
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
                //image capture
                imageCapture = new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();
                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, preview);
                camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview,imageCapture);
// For performing operations that affect all outputs.
                cameraControl = camera.getCameraControl();
// For querying information and states.
                cameraInfo = camera.getCameraInfo();



            }
            else{
                Toast.makeText(this, "Camera not present", Toast.LENGTH_SHORT).show();
            }

        }





        void capture(){
            File file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Image_" + System.currentTimeMillis() + ".jpg");
            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(file1).build();

            imageCapture.takePicture(outputFileOptions, getMainExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                            String msg=file1.getAbsolutePath();
                            Toast.makeText(MainActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap=BitmapFactory.decodeFile(file1.getPath());


                            File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

                            File file = new File(storageLoc,System.currentTimeMillis() + ".jpg");


                            try{
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();

                                scanFile(MainActivity.this, Uri.fromFile(file));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ImageCaptureException error) {
                            Toast.makeText(MainActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                        }

                    }
            );

        }
        private static void scanFile(Context context, Uri imageUri){
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(imageUri);
            context.sendBroadcast(scanIntent);

        }
        void torch(){
            if(cameraInfo.hasFlashUnit()){
                if(!flashstate) {
                    cameraControl.enableTorch(true);
                    flash.setImageResource(R.drawable.flashon);
                    flashstate=true;
                }
                else {
                    cameraControl.enableTorch(false);
                    flashstate=false;
                    flash.setImageResource(R.drawable.flashoff);
                }
            }
        }
        private void setUpCameraAutoFocus() {
            final float x =  previewView.getX() + previewView.getWidth() / 2f;
            final float y =  previewView.getY() + previewView.getHeight() / 2f;

            MeteringPointFactory pointFactory = previewView.getMeteringPointFactory();
            float afPointWidth = 1.0f / 6.0f;  // 1/6 total area
            float aePointWidth = afPointWidth * 1.5f;
            MeteringPoint afPoint = pointFactory.createPoint(x, y, afPointWidth);
            MeteringPoint aePoint = pointFactory.createPoint(x, y, aePointWidth);
            cameraControl.startFocusAndMetering(
                    new FocusMeteringAction.Builder(afPoint,
                            FocusMeteringAction.FLAG_AF).addPoint(aePoint,
                            FocusMeteringAction.FLAG_AE).build());

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK){
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    //convert to bitmap array
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                Intent intent = new Intent(Camera.this, com.example.plexa.Editor.class);
                    // intent.putExtra("image",byteArray);
                    //  startActivity(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



            }
        }
    private void animateFocusRing(float x, float y) {
        ImageView focusRing = findViewById(R.id.focus);

        // Move the focus ring so that its center is at the tap location (x, y)
        float width = focusRing.getWidth();
        float height = focusRing.getHeight();
        focusRing.setX(x - width / 2);
        focusRing.setY(y - height / 2);

        // Show focus ring
        focusRing.setVisibility(View.VISIBLE);
        focusRing.setAlpha(1F);

        // Animate the focus ring to disappear
        focusRing.animate()
                .setStartDelay(3500)
                .setDuration(1000)
                .alpha(0F)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                      //  focusRing.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }

                    // The rest of AnimatorListener's methods.
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}






