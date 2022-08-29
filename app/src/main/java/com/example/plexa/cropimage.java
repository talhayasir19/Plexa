package com.example.plexa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;

import com.oginotihiro.cropview.CropView;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;

public class cropimage extends AppCompatActivity {
    RecyclerView Filtersrecyclerview;
    ArrayList<FilterModel> filters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        CropView cropView = (CropView) findViewById(R.id.cropView);
        Uri uri = Uri.parse("android.resource://your.package.here/drawable/ic_launcher_background");
        cropView.of(uri)
                .withAspect(16, 9)
                .withOutputSize(500, 500)
                .initialize(cropimage.this);


    }
}