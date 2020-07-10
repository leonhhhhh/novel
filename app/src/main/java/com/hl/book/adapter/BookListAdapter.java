package com.hl.book.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.model.Book;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder>{
    private ArrayList<Book> chapterList;
    private OnItemClickListener onItemClickListener;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvNew;
        TextView tvLast;
        MyViewHolder(View v) {
            super(v);
            ivCover = v.findViewById(R.id.ivCover);
            tvName = v.findViewById(R.id.tvName);
            tvNew = v.findViewById(R.id.tvNew);
            tvLast = v.findViewById(R.id.tvLast);
        }
    }
    public BookListAdapter(ArrayList<Book> chapterList, OnItemClickListener onItemClickListener) {
        this.chapterList = chapterList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BookListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookListAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(chapterList.get(position).name);
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
