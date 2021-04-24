package com.hl.book.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.model.bean.ChapterBean;

import java.util.List;

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.MyViewHolder>{
    private List<ChapterBean> chapterBeanList;
    private int  textSize=12;
    private boolean isNight = false;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        MyViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
        }
    }
    public ReadAdapter(List<ChapterBean> chapterBeanList) {
        this.chapterBeanList = chapterBeanList;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        notifyDataSetChanged();
    }

    public void setNight(boolean night) {
        isNight = night;
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
        holder.tvContent.setText(Html.fromHtml(chapterBeanList.get(position).textBean.content));
        holder.tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
        if (isNight){
            holder.tvContent.setTextColor(getColor(holder,R.color.color_eaedf2));
            holder.tvTitle.setTextColor(getColor(holder,R.color.color_eaedf2));
        }else {
            holder.tvContent.setTextColor(getColor(holder,R.color.color_4a4b50));
            holder.tvTitle.setTextColor(getColor(holder,R.color.color_4a4b50));
        }
        holder.tvTitle.setText(chapterBeanList.get(position).title);
    }
    @Override
    public int getItemCount() {
        return chapterBeanList.size();
    }
    private int getColor(ReadAdapter.MyViewHolder holder,int colorId){
        return holder.tvContent.getContext().getResources().getColor(colorId);
    }
}
