package com.kathline.demo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kathline.library.content.ZFileBean;

import java.util.List;

public class SuperAdapter extends RecyclerView.Adapter<SuperAdapter.SuperViewHolder> {

    private List<ZFileBean> datas;

    public SuperAdapter(List<ZFileBean> datas) {
        this.datas = datas;
    }

    public SuperAdapter.SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_super, parent, false);
        return new SuperViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private ZFileBean getItem(int position) {
        return datas.get(position);
    }

    public void setDatas(List<ZFileBean> list) {
        datas.clear();
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull SuperAdapter.SuperViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getItem(position).getFileName();
                Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();
                Log.i("ZFileManager", "已选中$name");
            }
        });
        holder.setData(getItem(position));
    }

    static class SuperViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTxt = itemView.findViewById(R.id.item_super_nameTxt);
        private TextView dateTxt = itemView.findViewById(R.id.item_super_dateTxt);
        private TextView sizeTxt = itemView.findViewById(R.id.item_super_sizeTxt);

        public void setData(ZFileBean item) {
            nameTxt.setText(item.getFileName());
            dateTxt.setText(item.getDate());
            sizeTxt.setText(item.getSize());
        }

        public SuperViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}