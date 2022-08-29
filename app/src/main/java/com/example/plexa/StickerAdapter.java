package com.example.plexa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StikerHolder>{
    ArrayList<StickerModel> data;
    clickIt clickIt;
    public void setClickIt(StickerAdapter.clickIt clickIt) {
        this.clickIt = clickIt;
    }





    public StickerAdapter(ArrayList<StickerModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public StikerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stickerlayout,null);
        return new StikerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StikerHolder holder, int position) {
         StickerModel index=data.get(position);
         holder.stickerview.setImageResource(index.getStickerid());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class StikerHolder extends RecyclerView.ViewHolder{
        ImageView stickerview;
        public StikerHolder(@NonNull View itemView) {
            super(itemView);
            stickerview=itemView.findViewById(R.id.Stickerview);
            stickerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickIt.clickOn(view,getAdapterPosition());
                }
            });

        }
    }
    interface clickIt{
        void clickOn(View view, int position);
    }
}
