package com.hl.book.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.model.bean.BookBean;
import com.hl.book.util.image.ImageLoadUtil;
import com.hl.book.util.image.ImageOptionsFactory;

import java.text.MessageFormat;
import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder>{
    private ArrayList<BookBean> chapterList;
    private OnItemClickListener onItemClickListener;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvNew;
        ImageView ivMore;
        View viewPoint;
        MyViewHolder(View v) {
            super(v);
            ivCover = v.findViewById(R.id.ivCover);
            tvName = v.findViewById(R.id.tvName);
            tvNew = v.findViewById(R.id.tvNew);
            ivMore = v.findViewById(R.id.ivMore);
            viewPoint = v.findViewById(R.id.viewPoint);
        }
    }
    public BookListAdapter(ArrayList<BookBean> chapterList, OnItemClickListener onItemClickListener) {
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
        BookBean bookBean = chapterList.get(position);
        ImageLoadUtil.loadImg(ImageOptionsFactory.getDefaultOption(holder.ivCover.getContext(),
                bookBean.cover,holder.ivCover));
        holder.tvName.setText(bookBean.name);
        holder.tvNew.setText(MessageFormat.format("{0}:{1}", bookBean.newShowTime, bookBean.newChapter));
        holder.ivMore.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(view,holder.getAdapterPosition());
                }
            }
        });
        if (bookBean.isShowChickPoint()){
            holder.viewPoint.setVisibility(View.VISIBLE);
        }else {
            holder.viewPoint.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
