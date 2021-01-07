package com.kathline.library.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kathline.library.Function1;
import com.kathline.library.content.ZFileContent;

public final class ZFileViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> array;

    public final <V extends View> V getView(int id) {
        View view = (View)this.array.get(id);
        if (view == null) {
            view = this.itemView.findViewById(id);
            this.array.put(id, view);
        }

        return (V) view;
    }

    public final void setText(int id, String msg) {
        TextView txtView = (TextView)this.getView(id);
        txtView.setText((CharSequence)msg);
    }

    public final void setImageRes(int id, int res) {
        ImageView pic = (ImageView)this.getView(id);
        pic.setImageResource(res);
    }

    public final void setBgColor(int id, int color) {
        View view = this.getView(id);
        Context context = itemView.getContext();
        view.setBackgroundColor(ZFileContent.getColorById(context, color));
    }

    public final void setVisibility(int id, int visibility) {
        this.getView(id).setVisibility(visibility);
    }

    public final void setVisibility(int id, boolean isVisibility) {
        if (isVisibility) {
            this.setVisibility(id, 0);
        } else {
            this.setVisibility(id, 8);
        }

    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View view);
    }

    public final void setOnViewClickListener(int id, final OnClickListener listener) {
        this.getView(id).setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                listener.onClick(it);
            }
        }));
    }

    public final void setOnItemClickListener(final OnClickListener listener) {
        this.itemView.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                listener.onClick(it);
            }
        }));
    }

    public final void setOnItemLongClickListener(final OnLongClickListener listener) {
        this.itemView.setOnLongClickListener((View.OnLongClickListener)(new View.OnLongClickListener() {
            public final boolean onLongClick(View it) {
                return listener.onLongClick(it);
            }
        }));
    }

    public ZFileViewHolder(View itemView) {
        super(itemView);
        this.array = new SparseArray<>();
    }
}

