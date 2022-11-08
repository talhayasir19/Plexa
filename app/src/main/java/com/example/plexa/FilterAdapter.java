package com.example.plexa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder>{
    ArrayList<FilterModel> filters=new ArrayList<>();
    onClick onClick;

    public void setonclick(onClick onClick){
        this.onClick=onClick;
    }
    public FilterAdapter(ArrayList<FilterModel> filters) {
        this.filters = filters;
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.filter,null);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
           FilterModel index=filters.get(position);
           holder.filter.setImageResource(index.getResid());
           holder.filtertext.setText(index.getFiltername());

    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder{
        ImageView filter;
        TextView filtertext;
        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            filter=itemView.findViewById(R.id.filter);
            filtertext=itemView.findViewById(R.id.filtertext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(view,getAdapterPosition());
                }
            });

        }
    }
    interface onClick{
        public void onClick(View view,int position);
    }
}
