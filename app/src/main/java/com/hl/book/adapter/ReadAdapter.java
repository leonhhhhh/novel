package com.hl.book.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.model.Chapter;

import java.util.ArrayList;

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.MyViewHolder>{
    private ArrayList<Chapter> chapterList;
    private OnItemClickListener onItemClickListener;
    private int  textSize=12;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        MyViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
        }
    }
    public ReadAdapter(ArrayList<Chapter> chapterList, OnItemClickListener onItemClickListener) {
        this.chapterList = chapterList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.read_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReadAdapter.MyViewHolder holder, int position) {
        holder.tvContent.setText(Html.fromHtml(chapterList.get(position).content));
        holder.tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
        holder.tvTitle.setText(chapterList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(view,holder.getAdapterPosition());
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
