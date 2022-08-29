package com.example.plexa;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import ja.burhanrashid52.photoeditor.CustomEffect;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

public class Editactivity extends AppCompatActivity implements View.OnClickListener{
    private static final String UUID="image";
    ArrayList<FilterModel> filters=new ArrayList<>();
    ArrayList<StickerModel> Stikerlist=new ArrayList<>();
    ImageView  imageview,Edit, addimage,addfilter,stickerbtn,rotationbtn;
    TextView addtext,Save;
    PhotoEditor mPhotoEditor;
    Boolean imgselected=false;
    Float intensity,brushsize;
    int RESULT_LOAD_IMG = 100,opacity;
    Uri imageUri;
    Float degree=90F;
    String fileName;
    Bitmap selectedImage,originalimage;
    LinearLayout rotater,background,flipper,Croplayout,Filterslayout,stickerslayout,textlayout,imagetextlayout,Drawinglayout,functionbar,process;
    PhotoEditorView mPhotoEditorView;
    RecyclerView recyclerView,StickerRecyclerView;
    SeekBar seekBar,sizeseekbar;
    Boolean Eraser=false;
    EditText imagetext;
    ImageView imagetextbtn,Drawingbtn,Erase,backbutton;
    View filteractivity,stickeractivity,Drawingactivity,grey,blue,red,orange,green,yeallow,black,purple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactivity);
       // imageView = findViewById(R.id.imageview);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        filteractivity=findViewById(R.id.filteractivity);
        Drawingactivity=findViewById(R.id.Drawingactivity);
        stickerslayout=findViewById(R.id.Stickerslayout);
        addfilter=findViewById(R.id.addfilter);
        textlayout=findViewById(R.id.textlayout);
        Save=findViewById(R.id.Save);
        background=findViewById(R.id.background);
        seekBar=findViewById(R.id.seekbar);
        sizeseekbar=findViewById(R.id.sizeseekbar);
        backbutton=findViewById(R.id.backbutton);
        imagetextlayout=findViewById(R.id.imagetextlayout);
        imagetext=findViewById(R.id.imagetext);
        imagetextbtn=findViewById(R.id.imagetextbtn);
        functionbar=findViewById(R.id.functionbar);
        imageview=findViewById(R.id.imageview);
        stickeractivity=findViewById(R.id.stickeractivity);
        stickerbtn=findViewById(R.id.Stickerbtn);
        process=findViewById(R.id.process);
        Drawinglayout=findViewById(R.id.Drawinglayout);
        Drawingbtn=findViewById(R.id.Drawingbtn);
        Erase=findViewById(R.id.Eraser);
        //permission
        ActivityResultLauncher<String> requestforpermission=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){

                }
            }
        });

        //colors
        grey=findViewById(R.id.grey);
        blue=findViewById(R.id.blue);
        red=findViewById(R.id.red);
        orange=findViewById(R.id.orange);
        green=findViewById(R.id.green);
        yeallow=findViewById(R.id.yellow);
        black=findViewById(R.id.black);
        purple=findViewById(R.id.purple);
        grey.setOnClickListener(this);
        blue.setOnClickListener(this);
        red.setOnClickListener(this);
        orange.setOnClickListener(this);
        green.setOnClickListener(this);
        yeallow.setOnClickListener(this);
        black.setOnClickListener(this);
        purple.setOnClickListener(this);

        //photoeditorclass
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setClipSourceImage(true)
                .build();

        //recycler view
        recyclerView=findViewById(R.id.Filtersreyclerview);
        filters.add(new FilterModel(R.drawable.orignal,"Original"));
        filters.add(new FilterModel(R.drawable.grain,"Grain"));
        filters.add(new FilterModel(R.drawable.saturate,"Saturate"));
        filters.add(new FilterModel(R.drawable.vignette,"Vignette"));
        filters.add(new FilterModel(R.drawable.autofix,"Autofix"));
        filters.add(new FilterModel(R.drawable.sharpen,"Sharpen"));
        filters.add(new FilterModel(R.drawable.tint,"Tint"));
        filters.add(new FilterModel(R.drawable.sepia,"Sepia"));
        FilterAdapter adapter=new FilterAdapter(filters);
        //Stickers Recyclerview
        StickerRecyclerView=findViewById(R.id.StickerRecyclerView);
        Stikerlist.add(new StickerModel(R.drawable.emoji1));
        Stikerlist.add(new StickerModel(R.drawable.emoji2));
        Stikerlist.add(new StickerModel(R.drawable.emoji3));
        Stikerlist.add(new StickerModel(R.drawable.emoji4));
        Stikerlist.add(new StickerModel(R.drawable.emoji5));
        Stikerlist.add(new StickerModel(R.drawable.emoji6));
        Stikerlist.add(new StickerModel(R.drawable.emoji7));
        Stikerlist.add(new StickerModel(R.drawable.emoji8));
        Stikerlist.add(new StickerModel(R.drawable.emoji9));
        Stikerlist.add(new StickerModel(R.drawable.emoji10));
        Stikerlist.add(new StickerModel(R.drawable.emoji11));
        Stikerlist.add(new StickerModel(R.drawable.emoji12));
        StickerAdapter stickerAdapter=new StickerAdapter(Stikerlist);
        StickerRecyclerView.setLayoutManager(new GridLayoutManager(Editactivity.this,5));
        StickerRecyclerView.setAdapter(stickerAdapter);
        stickerAdapter.setClickIt(new StickerAdapter.clickIt() {
            @Override
            public void clickOn(View view, int position) {
                 StickerModel index=Stikerlist.get(position);
                 int imgid=index.getStickerid();
                BitmapDrawable resImg = (BitmapDrawable)Editactivity.this.getResources().getDrawable(imgid);
                Bitmap bitmap = resImg.getBitmap();
                mPhotoEditor.addImage(bitmap);

            }
        });

        adapter.setonclick(new FilterAdapter.onClick(){
            @Override
            public void onClick(View view, int position){
                if(position==0){
                   mPhotoEditorView.getSource().setImageBitmap(selectedImage);
                }
                else if(position==1){
                    CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_GRAIN)
                            .setParameter("strength",0.5F)
                            .build();
                    mPhotoEditorView.setFilterEffect(customEffect);

                }
                else if(position==2){
                    CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_SATURATE)
                            .setParameter("scale",-1F)
                            .build();
                    mPhotoEditorView.setFilterEffect(customEffect);
                }
                else if(position==3){
                    CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_VIGNETTE)
                            .setParameter("scale",0.7F)
                            .build();
                    mPhotoEditorView.setFilterEffect(customEffect);
                }
                else if(position==4){
                    CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_AUTOFIX)
                            .setParameter("scale",0.7F)
                            .build();
                    mPhotoEditorView.setFilterEffect(customEffect);
                }
                else if(position==5){
                    CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_SHARPEN)
                            .setParameter("scale",0.4F)
                            .build();
                    mPhotoEditorView.setFilterEffect(customEffect);
                }
                else if(position==6){
                    filtersetter(EffectFactory.EFFECT_TINT);
                }
                else if(position==7){
                    filtersetter(EffectFactory.EFFECT_SEPIA);
                }
                else{
                    Toast.makeText(Editactivity.this, "Not assigned", Toast.LENGTH_SHORT).show();
                }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
        recyclerView.hasFixedSize();

        addtext = findViewById(R.id.addtext);
        Croplayout = findViewById(R.id.croplayout);
        Filterslayout = findViewById(R.id.Filterslayout);

        mPhotoEditorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Editactivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                    requestforpermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if(!imgselected) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }


            }
        });
        Croplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String destinationFileName = java.util.UUID.randomUUID().toString() + ".jpg";

                String destinationUri=java.util.UUID.randomUUID().toString()+".jpg";

                if(imgselected) {
                    UCrop uCrop = UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

                    uCrop.start(Editactivity.this);

                }
                else{
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //stickers
        stickerslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgselected) {
                    functionbar.setVisibility(View.GONE);
                    process.setVisibility(View.GONE);
                    stickeractivity.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }

            }
        });
        stickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionbar.setVisibility(View.VISIBLE);
                process.setVisibility(View.VISIBLE);
                stickeractivity.setVisibility(View.GONE);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               alertdialog();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgselected) {

                    BitmapDrawable drawable = (BitmapDrawable)mPhotoEditorView.getSource().getDrawable();
                    Bitmap bitmap = drawable.getBitmap();


                   SaveImage();
                    Toast.makeText(Editactivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filteractivity.setVisibility(View.GONE);
            }
        });
        Filterslayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(imgselected) {
                    filteractivity.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Drawinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgselected) {
                    process.setVisibility(View.GONE);
                    functionbar.setVisibility(View.GONE);
                    Drawingactivity.setVisibility(View.VISIBLE);
                    mPhotoEditor.setBrushDrawingMode(true);
                    mPhotoEditor.setBrushSize(25F);
                    mPhotoEditor.setOpacity((int) 0.2);

                }
                else {
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Drawingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.setBrushDrawingMode(false);
                process.setVisibility(View.VISIBLE);
                functionbar.setVisibility(View.VISIBLE);
                Drawingactivity.setVisibility(View.GONE);
            }
        });
        Erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!Eraser){
                    mPhotoEditor.brushEraser();
                    mPhotoEditor.setBrushEraserSize(150F);
                    Erase.setImageResource(R.drawable.drawing);
                    Eraser=true;
                }
                else{
                      mPhotoEditor.setBrushDrawingMode(true);
                      Erase.setImageResource(R.drawable.erase);
                      Eraser=false;
                }

            }
        });
        sizeseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brushsize=Float.parseFloat(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPhotoEditor.setBrushSize(brushsize);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Text
        textlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgselected) {
                    mPhotoEditor.addText("Add a text", R.color.black);
                    Toast.makeText(Editactivity.this, "Long press the text to edit", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Editactivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(@Nullable View view2, @Nullable String s, int i) {
                imagetextlayout.setVisibility(View.VISIBLE);
                imagetextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1){
                        mPhotoEditor.editText(view2,imagetext.getText().toString(),R.id.imagetextbtn);
                        imagetextlayout.setVisibility(View.GONE);
                        functionbar.setVisibility(View.VISIBLE);
                        imagetext.setText("");
                    }
                });
            }

            @Override
            public void onAddViewListener(@Nullable ViewType viewType, int i) {
            }

            @Override
            public void onRemoveViewListener(@Nullable ViewType viewType, int i) {

            }

            @Override
            public void onStartViewChangeListener(@Nullable ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(@Nullable ViewType viewType) {

            }

            @Override
            public void onTouchSourceImage(@Nullable MotionEvent motionEvent) {

            }
        });
    }

    private void SaveImage() {
        mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(@NonNull Bitmap saveBitmap) {
                imageview.setImageBitmap(saveBitmap);
                String root = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/Plexa");
                myDir.mkdirs();
                Random generator = new Random();

                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-"+ n +".jpg";
                File file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
// Tell the media scanner about the new file so that it is
// immediately available to the user.
                MediaScannerConnection.scanFile(Editactivity.this, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("PhotoEditor","Failed to save Image");
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            mPhotoEditorView.getSource().setImageURI(resultUri);
            background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.charcolblack));

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            imageUri = data.getData();
            final InputStream imageStream;

            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imageview.setImageBitmap(selectedImage);
                BitmapDrawable drawable = (BitmapDrawable)imageview.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
              mPhotoEditorView.getSource().setImageBitmap(bitmap);
                imgselected=true;
                filecreater();
                if (mPhotoEditorView.getDisplay() != null) {
                    addtext.setText("");
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    void filecreater(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable)mPhotoEditorView.getSource().getDrawable();
        Bitmap bmp = drawable.getBitmap();



        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

         byte[] byteArray = stream.toByteArray();
        fileName = "image.jpg";
        try {
            FileOutputStream fileOutStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutStream.write(byteArray);
            fileOutStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void filtersetter(String Effect){
        CustomEffect customEffect = new CustomEffect.Builder(Effect)
                .build();
        mPhotoEditorView.setFilterEffect(customEffect);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.grey){
            mPhotoEditor.setBrushColor(Color.parseColor("#808080"));
        }
        else if(view.getId()==R.id.blue){
            mPhotoEditor.setBrushColor(Color.parseColor("#3F51B5"));
        }
        else if(view.getId()==R.id.red){
            mPhotoEditor.setBrushColor(Color.parseColor("#D50B1F"));
        }
        else if(view.getId()==R.id.orange){
          mPhotoEditor.setBrushColor(Color.parseColor("#FF9800"));
        }
        else if(view.getId()==R.id.green){
           mPhotoEditor.setBrushColor(Color.parseColor("#0C3E39"));
        }
        else if(view.getId()==R.id.yellow){
            mPhotoEditor.setBrushColor(Color.parseColor("#E4D75E"));
        }
        else if(view.getId()==R.id.purple){
           mPhotoEditor.setBrushColor(Color.parseColor("#FF000000"));
        }
        else{
          mPhotoEditor.setBrushColor(Color.parseColor("#4C2C87"));
        }
    }
    void alertdialog(){
        AlertDialog.Builder ad=new AlertDialog.Builder(Editactivity.this)
                .setTitle("Choose the action")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editactivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        ad.show();
    }

    @Override
    public void onBackPressed() {
        alertdialog();
    }
}