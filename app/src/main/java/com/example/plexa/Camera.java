//package com.example.plexa;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.experimental.Experimental;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.camera.core.AspectRatio;
//import androidx.camera.core.CameraControl;
//import androidx.camera.core.CameraInfo;
//import androidx.camera.core.CameraInfoUnavailableException;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ExperimentalExposureCompensation;
//import androidx.camera.core.ExposureState;
//import androidx.camera.core.FocusMeteringAction;
//import androidx.camera.core.FocusMeteringResult;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureException;
//import androidx.camera.core.MeteringPoint;
//import androidx.camera.core.MeteringPointFactory;
//import androidx.camera.core.Preview;
//import androidx.camera.core.impl.utils.executor.CameraXExecutors;
//import androidx.camera.core.impl.utils.futures.FutureCallback;
//import androidx.camera.core.impl.utils.futures.Futures;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.MotionEventCompat;
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleOwner;
//
//import android.app.Dialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Paint;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.provider.Settings;
//import android.renderscript.Sampler;
//import android.util.Log;
//import android.util.Range;
//import android.util.Size;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.Toast;
//import android.widget.ZoomButton;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.concurrent.ExecutionException;
//
//public class Camera extends AppCompatActivity{
//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//    PreviewView previewView;
//    AppCompatButton Capture;
//    ImageView flip,flash,brightness,zoom,size,filters,plus,minus,Edit;
//    ImageCapture imageCapture;
//    ProcessCameraProvider cameraProvider;
//    AppCompatButton Gallery,Editor;
//    int lensFacing=CameraSelector.LENS_FACING_BACK,RESULT_LOAD_IMG=100;
//    CameraControl cameraControl;
//    CameraInfo cameraInfo;
//    String DEBUG_TAG="touch";
//    Boolean flashstate=false,zoomstate=false;
//    double zoomvalue=0F;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//        previewView=findViewById(R.id.previewView);
//        Capture=findViewById(R.id.Capture);
//        flip=findViewById(R.id.flip);
//        flash=findViewById(R.id.flash);
//        brightness=findViewById(R.id.brightness);
//        zoom=findViewById(R.id.zoom);
//        size=findViewById(R.id.size);
//        plus=findViewById(R.id.plus);
//        minus=findViewById(R.id.minus);
//        filters=findViewById(R.id.filters);
//        Edit=findViewById(R.id.Edit);
//        //camera getter
//        cameraProviderFuture=ProcessCameraProvider.getInstance(Camera.this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                cameraProvider = cameraProviderFuture.get();
//                CameraSelector cameraSelector = new CameraSelector.Builder()
//                        .requireLensFacing(lensFacing)
//                        .build();
//                if(cameraProvider.hasCamera(cameraSelector)) {
//                    bindPreview(cameraProvider);
//                }
//            } catch (ExecutionException | InterruptedException | CameraInfoUnavailableException e) {
//            }
//        }, ContextCompat.getMainExecutor(this));
//
//        flip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(lensFacing==CameraSelector.LENS_FACING_BACK){
//                    cameraProvider.unbindAll();
//                    lensFacing=CameraSelector.LENS_FACING_FRONT;
//                    bindPreview(cameraProvider);
//                }
//                else if(lensFacing==CameraSelector.LENS_FACING_FRONT){
//                    cameraProvider.unbindAll();
//                    lensFacing=CameraSelector.LENS_FACING_BACK;
//                    bindPreview(cameraProvider);
//                }
//            }
//        });
//        flash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                torch();
//            }
//        });
//        plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(zoomvalue<1){
//                    zoomvalue=zoomvalue+0.2;
//                    cameraControl.setLinearZoom((float) zoomvalue);
//                }
//            }
//        });
//        minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(zoomvalue>0){
//                    zoomvalue=zoomvalue-0.2;
//                    cameraControl.setLinearZoom((float) zoomvalue);
//
//                }
//            }
//        });
//       previewView.setOnTouchListener(new View.OnTouchListener() {
//           @Override
//           public boolean onTouch(View view, MotionEvent motionEvent) {
//               setUpCameraAutoFocus();
//               return true;
//           }
//       });
//        zoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ViewGroup.LayoutParams layoutParams = plus.getLayoutParams();
//                ViewGroup.LayoutParams layoutParams1 = minus.getLayoutParams();
//                if (!zoomstate) {
//                    layoutParams.width = 100;
//                    layoutParams1.width = 100;
//                    layoutParams.height = 100;
//                    layoutParams1.height = 100;
//                    plus.setLayoutParams(layoutParams);
//                    minus.setLayoutParams(layoutParams1);
//                    zoomstate = true;
//                } else if (zoomstate) {
//                    layoutParams.width = 0;
//                    layoutParams1.width = 0;
//                    layoutParams.height = 0;
//                    layoutParams1.height = 0;
//                    plus.setLayoutParams(layoutParams);
//                    minus.setLayoutParams(layoutParams1);
//                    zoomstate = false;
//
//                }
//            }
//        });
//        Capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                capture();
//            }
//        });
//
//       Capture.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               capture();
//           }
//       });
//       brightness.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//               Toast.makeText(Camera.this, "", Toast.LENGTH_SHORT).show();
//           }
//       });
//       Edit.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Intent intent=new Intent(Camera.this, com.example.plexa.Editor.class);
//               startActivity(intent);
//           }
//       });
//
//
//    }
//    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//        previewView.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);
//        cameraProvider.unbindAll();
//        Preview preview = new Preview.Builder()
//                .build();
//
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(lensFacing)
//                .build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
//        //image capture
//        imageCapture = new ImageCapture.Builder()
//                .setTargetRotation(previewView.getDisplay().getRotation())
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
//                .build();
//        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, preview);
//        androidx.camera.core.Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview,imageCapture);
//// For performing operations that affect all outputs.
//        cameraControl = camera.getCameraControl();
//// For querying information and states.
//        cameraInfo = camera.getCameraInfo();
//
//
//    }
//
//
//
//
//    void capture(){
//        File file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                "Image_" + System.currentTimeMillis() + ".jpg");
//        ImageCapture.OutputFileOptions outputFileOptions =
//                new ImageCapture.OutputFileOptions.Builder(file1).build();
//        imageCapture.takePicture(outputFileOptions, getMainExecutor(),
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
//                        String msg=file1.getAbsolutePath();
//                        Toast.makeText(Camera.this, "Sucess image stored at "+msg, Toast.LENGTH_SHORT).show();
//                        Bitmap bitmap=BitmapFactory.decodeFile(file1.getPath());
//
//
//                        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
//
//                        File file = new File(storageLoc,System.currentTimeMillis() + ".jpg");
//
//
//                        try{
//                            FileOutputStream fos = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                            fos.close();
//
//                            scanFile(Camera.this, Uri.fromFile(file));
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ImageCaptureException error) {
//                        Toast.makeText(Camera.this, "Failed"+error, Toast.LENGTH_SHORT).show();
//                    }
//
//               }
//        );
//
//    }
//    private static void scanFile(Context context, Uri imageUri){
//        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        scanIntent.setData(imageUri);
//        context.sendBroadcast(scanIntent);
//
//    }
//    void torch(){
//        if(cameraInfo.hasFlashUnit()){
//            if(!flashstate) {
//                cameraControl.enableTorch(true);
//                flash.setImageResource(R.drawable.flashon);
//                flashstate=true;
//            }
//            else {
//                cameraControl.enableTorch(false);
//                flashstate=false;
//                flash.setImageResource(R.drawable.flashoff);
//            }
//        }
//    }
//    private void setUpCameraAutoFocus() {
//        final float x =  previewView.getX() + previewView.getWidth() / 2f;
//        final float y =  previewView.getY() + previewView.getHeight() / 2f;
//
//        MeteringPointFactory pointFactory = previewView.getMeteringPointFactory();
//        float afPointWidth = 1.0f / 6.0f;  // 1/6 total area
//        float aePointWidth = afPointWidth * 1.5f;
//        MeteringPoint afPoint = pointFactory.createPoint(x, y, afPointWidth);
//        MeteringPoint aePoint = pointFactory.createPoint(x, y, aePointWidth);
//        cameraControl.startFocusAndMetering(
//                new FocusMeteringAction.Builder(afPoint,
//                        FocusMeteringAction.FLAG_AF).addPoint(aePoint,
//                        FocusMeteringAction.FLAG_AE).build());
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK){
//            final Uri imageUri = data.getData();
//            final InputStream imageStream;
//            try {
//                imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//
//               //convert to bitmap array
////                ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                byte[] byteArray = stream.toByteArray();
////
////                Intent intent = new Intent(Camera.this, com.example.plexa.Editor.class);
//             // intent.putExtra("image",byteArray);
//            //  startActivity(intent);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//
//
//        }
//    }
//}