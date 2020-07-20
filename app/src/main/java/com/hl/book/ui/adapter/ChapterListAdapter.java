package com.hl.book.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.model.bean.ChapterBean;

import java.util.ArrayList;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.MyViewHolder>{
    private ArrayList<ChapterBean> chapterBeanList;
    private OnItemClickListener onItemClickListener;
    private int lastIndex = -1;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }
    public ChapterListAdapter(ArrayList<ChapterBean> chapterBeanList, OnItemClickListener onItemClickListener) {
        this.chapterBeanList = chapterBeanList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    @NonNull
    @Override
    public ChapterListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChapterListAdapter.MyViewHolder holder, int position) {
        if (lastIndex==position){
            holder.textView.setTextColor(Color.RED);
        }else {
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.textView.setText(chapterBeanList.get(position).title);
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
        return chapterBeanList.size();
    }
}
