package com.hl.book.ui.dialog.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.listener.SourceSelectListener;
import com.hl.book.source.SourceManager;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.ChapterListAdapter;
import com.hl.book.util.StrUtil;

import java.util.List;


/**
 * Created by leon
 * 描述：网站源列表对话框
 */

public class ListDialog extends BaseDialog {
    private TextView mNameTv;
    private RecyclerView recyclerView;
    private SourceSelectListener selectListener;
    private Source currentSource;
    public ListDialog(Context context){
        super(context);
    }

    @Override
    protected void iniView() {
        super.iniView();
        layoutId = R.layout.dialog_list;
        setContentView(layoutId);
        mNameTv = findViewById(R.id.tv_name);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }
    public void setData(Source currentSource,SourceSelectListener selectListener){
        this.selectListener = selectListener;
        this.currentSource = currentSource;
        iniListView();
    }
    public void iniListView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ListAdapter adapter = new ListAdapter();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_done) {
            onListener(DialogMessage.RESULT_OK);
        } else if (v.getId() == R.id.tv_cancel) {
            onListener(DialogMessage.RESULT_CANCEL);
        }
        dismiss();
    }

    class ListAdapter extends RecyclerView.Adapter<MyViewHolder>{
        List<Source>  sourceList;
        OnListClickListener onItemClick;
        public ListAdapter() {
            sourceList = SourceManager.getInstance().getSourceList();
            onItemClick = new OnListClickListener(selectListener,ListDialog.this);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_website_source, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
            viewHolder.itemView.setTag(sourceList.get(position));
            viewHolder.itemView.setFocusable(true);
            viewHolder.itemView.setClickable(true);
            viewHolder.itemView.setOnClickListener(onItemClick);
            if (currentSource.name.equals(sourceList.get(position).name)){
                viewHolder.textView.setText(String.format("%s(当前选择)", sourceList.get(position).name));
            }else {
                viewHolder.textView.setText(sourceList.get(position).name);
            }
        }

        @Override
        public int getItemCount() {
            return sourceList.size();
        }
    }
    static class OnListClickListener implements View.OnClickListener {
        private SourceSelectListener selectListener;
        private BaseDialog dialog;
        public OnListClickListener(SourceSelectListener selectListener,BaseDialog dialog) {
            this.selectListener = selectListener;
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            Source source = (Source) v.getTag();
            if (selectListener!=null){
                selectListener.onSourceSelect(source);
            }
            dialog.dismiss();
        }
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

}
