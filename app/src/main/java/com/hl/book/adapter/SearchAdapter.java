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
import com.hl.book.model.bean.SearchBookBean;
import com.hl.book.util.image.ImageLoadUtil;
import com.hl.book.util.image.ImageOptionsFactory;

import java.text.MessageFormat;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private ArrayList<SearchBookBean> chapterList;
    private OnItemClickListener onItemClickListener;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvNew;
        TextView tvAuthor;
        TextView tvDesc;
        TextView tvAdd;
        MyViewHolder(View v) {
            super(v);
            ivCover = v.findViewById(R.id.ivCover);
            tvName = v.findViewById(R.id.tvName);
            tvNew = v.findViewById(R.id.tvNew);
            tvAuthor = v.findViewById(R.id.tvAuthor);
            tvDesc = v.findViewById(R.id.tvDesc);
            tvAdd = v.findViewById(R.id.tvAdd);
        }
    }
    public SearchAdapter(ArrayList<SearchBookBean> chapterList, OnItemClickListener onItemClickListener) {
        this.chapterList = chapterList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder holder, int position) {
        SearchBookBean searchBook = chapterList.get(position);
        ImageLoadUtil.loadImg(ImageOptionsFactory.getDefaultOption(holder.ivCover.getContext(),
                searchBook.cover,holder.ivCover));
        holder.tvName.setText(searchBook.name);
        holder.tvNew.setText(MessageFormat.format("最新:{0}", searchBook.newChapter));
        holder.tvDesc.setText(searchBook.desc);
        holder.tvAuthor.setText(MessageFormat.format("作者:{0}", searchBook.author));
        holder.tvAdd.setTag(position);
        if (searchBook.hasAdd){
            holder.tvAdd.setText("移除书架");
        }else {
            holder.tvAdd.setText("加入书架");
        }
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
