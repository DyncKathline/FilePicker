package com.kathline.library.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kathline.library.Function1;

import java.util.ArrayList;
import java.util.List;

public abstract class ZFileAdapter<T> extends RecyclerView.Adapter<ZFileViewHolder> {

    public Context context;
    private int layoutID;
    protected List<T> datas = new ArrayList<>();

    public ZFileAdapter(Context context) {
        this.context = context;
    }

    public ZFileAdapter(Context context, int layoutID) {
        this.context = context;
        this.layoutID = layoutID;
    }

    public interface OnClickListener<T> {
        void onClick(View view, int position, T data);
    }

    private OnClickListener<T> itemClick;

    public void setOnClickListener(OnClickListener<T> listener) {
        itemClick = listener;
    }

    public interface OnLongClickListener<T> {
        boolean onLongClick(View view, int position, T data);
    }

    private OnLongClickListener<T> itemLongClick;

    public void setOnLongClickListener(OnLongClickListener<T> listener) {
        itemLongClick = listener;
    }

    @NonNull
    @Override
    public ZFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = getLayoutID(viewType);
        View view = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        return new ZFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZFileViewHolder holder, final int position) {
        holder.setOnItemClickListener(new ZFileViewHolder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {
                    itemClick.onClick(view, position, getItem(position));
                }
            }
        });
        holder.setOnItemLongClickListener(new ZFileViewHolder.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (itemLongClick != null) {
                    return itemLongClick.onLongClick(view, position, getItem(position));
                }
                return false;
            }
        });
        bindView(holder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public T getItem(int position) {
        return datas.get(position);
    }

    public int getLayoutID(int viewType) {
        return this.layoutID;
    }

    protected abstract void bindView(ZFileViewHolder holder, T item, int position);

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> list) {
        this.datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        int count = getItemCount();
        datas.addAll(list);
        notifyItemChanged(count, list.size());
    }

    public void addItem(int position, T data) {
        datas.add(position, data);
        notifyItemChanged(position);
    }

    public void setItem(int position, T data) {
        if (getItemCount() > 0) {
            datas.set(position, data);
            notifyItemChanged(position);
        }
    }

    public void remove(int position, boolean changeDataNow) {
        if (getItemCount() > 0) {
            datas.remove(position);
            if (changeDataNow) {
                notifyItemRangeRemoved(position, 1);
            }
        }
    }

    public void remove(int position) {
        remove(position, true);
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean changeDataNow) {
        datas.clear();
        if (changeDataNow) {
            notifyDataSetChanged();
        }
    }
}
