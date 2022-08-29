package com.example.plexa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class Filter extends AppCompatActivity {
    ArrayList<FilterModel> filters=new ArrayList<>();
    RecyclerView recyclerView;
    PhotoEditorView photoeditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        photoeditor=findViewById(R.id.photoEditorView);


        //Recycler view
//        recyclerView=findViewById(R.id.Filtersreyclerview);
//        filters.add(new FilterModel(R.drawable.ic_launcher_background));
//        filters.add(new FilterModel(R.drawable.ic_baseline_flash_on_24));
//        filters.add(new FilterModel(R.drawable.ic_baseline_flash_on_24));
//        filters.add(new FilterModel(R.drawable.ic_baseline_flash_on_24));
//        filters.add(new FilterModel(R.drawable.ic_baseline_aspect_ratio_24));
//        filters.add(new FilterModel(R.drawable.ic_baseline_aspect_ratio_24));
//        FilterAdapter adapter=new FilterAdapter(filters);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        recyclerView.setAdapter(adapter);
//        recyclerView.hasFixedSize();
        //Intent
        Intent intent=getIntent();
        String fileName =intent.getStringExtra("picture");
        File filePath = getFileStreamPath(fileName);
        Drawable d = Drawable.createFromPath(filePath.toString());


        photoeditor.getSource().setImageDrawable(d);




    }
}