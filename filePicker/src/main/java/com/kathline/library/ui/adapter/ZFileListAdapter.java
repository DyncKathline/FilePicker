package com.kathline.library.ui.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kathline.library.R;
import com.kathline.library.common.ZFileAdapter;
import com.kathline.library.common.ZFileTypeManage;
import com.kathline.library.common.ZFileViewHolder;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZFileListAdapter extends ZFileAdapter<ZFileBean> {

    private boolean isQW;
    private ZFileConfiguration config = ZFileContent.getZFileConfig();
    // box选中的数据
    private final ArrayMap<Integer, Boolean> boxMap = new ArrayMap<>();
    // 已选中的数据
    private ArrayList<ZFileBean> selectData = new ArrayList<>();
    private boolean isManage;

    public ZFileListAdapter(Context context) {
        this(context, false);
    }

    public ZFileListAdapter(Context context, boolean isQW) {
        super(context);
        this.isQW = isQW;
    }

    @Override
    public int getLayoutID(int viewType) {
        switch (viewType) {
            case ZFileContent.FILE:
                return R.layout.item_zfile_list_file;
            default:
                return R.layout.item_zfile_list_folder;
        }
    }

    @Override
    protected void bindView(ZFileViewHolder holder, ZFileBean item, int position) {
        if (item.isFile()) {
            setFileData(holder, item, position);
        } else {
            setFolderData(holder, item, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isFile()) {
            return ZFileContent.FILE;
        } else {
            return ZFileContent.FOLDER;
        }
    }

    private void setFileData(ZFileViewHolder holder, final ZFileBean item, final int position) {
        holder.setText(R.id.item_zfile_list_file_nameTxt, item.getFileName());
        holder.setText(R.id.item_zfile_list_file_dateTxt, item.getDate());
        holder.setText(R.id.item_zfile_list_file_sizeTxt, item.getSize());
        holder.setBgColor(R.id.item_zfile_list_file_line, ZFileContent.getLineColor());
        holder.setVisibility(R.id.item_zfile_list_file_line, position < getItemCount() - 1);
        holder.setVisibility(R.id.item_zfile_file_box_pic, !isManage);
        switch (config.getBoxStyle()) {
            case ZFileConfiguration.STYLE1:
                holder.setVisibility(R.id.item_zfile_list_file_box1, isManage);
                break;
            case ZFileConfiguration.STYLE2:
                holder.setVisibility(R.id.item_zfile_list_file_box2, isManage);
                break;
            default:
                throw new IllegalArgumentException("ZFileConfiguration boxStyle error");
        }
        CheckBox box1 = holder.getView(R.id.item_zfile_list_file_box1);
        box1.setChecked((boxMap.get(position) != null ? boxMap.get(position) : false));
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxClick(position, item);
            }
        });
        final TextView box2 = holder.getView(R.id.item_zfile_list_file_box2);
        box2.setSelected((boxMap.get(position) != null ? boxMap.get(position) : false));
        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box2.setSelected(!(boxMap.get(position) != null ? boxMap.get(position) : false));
                boxClick(position, item);
            }
        });

        final ImageView pic = holder.getView(R.id.item_zfile_list_file_pic);
        ZFileTypeManage.getTypeManager().loadingFile(item.getFilePath(), pic);
        holder.getView(R.id.item_zfile_list_file_boxLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxLayoutClick(position, item);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickByAnim != null) {
                    itemClickByAnim.onClick(pic, position, item);
                }
            }
        });
    }

    public void boxLayoutClick(int position, ZFileBean item) {
        if (isManage) { // 管理状态
            boxClick(position, item);
            notifyItemChanged(position);
        } else { // 非管理状态
            isManage = !isManage;
            notifyDataSetChanged();
            if (qwListener != null) {
                qwListener.invoke(isManage, item, false);
            }
        }
        if (changeListener != null) {
            changeListener.change(isManage, selectData.size());
        }
    }

    private void boxClick(int position, ZFileBean item) {
        Boolean box = boxMap.get(position);
        boolean isSelect = box != null ? box : false;
        if (isSelect) {
            selectData.remove(item);
            boxMap.put(position, !isSelect);
            if (changeListener != null) {
                changeListener.change(isManage, selectData.size());
            }
            if (qwListener != null) {
                qwListener.invoke(isManage, item, false);
            }
        } else {
            double size = item.getOriginalSize() / 1048576d; // byte -> MB
            if (size > config.getMaxSize()) {
                ZFileContent.toast(context, config.getMaxSizeStr());
                notifyItemChanged(position);
            } else {
                if (isQW) {
                    selectData.add(item);
                    boxMap.put(position, !isSelect);
                    if (changeListener != null) {
                        changeListener.change(isManage, selectData.size());
                    }
                    if (qwListener != null) {
                        qwListener.invoke(isManage, item, true);
                    }
                } else {
                    if (selectData.size() >= config.getMaxLength()) {
                        ZFileContent.toast(context, config.getMaxLengthStr());
                        notifyItemChanged(position);
                    } else {
                        selectData.add(item);
                        boxMap.put(position, !isSelect);
                        if (changeListener != null) {
                            changeListener.change(isManage, selectData.size());
                        }
                        if (qwListener != null) {
                            qwListener.invoke(isManage, item, true);
                        }
                    }
                }

            }
        }
    }

    private void setFolderData(ZFileViewHolder holder, final ZFileBean item, final int position) {
        holder.setText(R.id.item_zfile_list_folderNameTxt, item.getFileName());
        holder.setImageRes(R.id.item_zfile_list_folderPic, ZFileContent.getFolderRes());
        holder.setBgColor(R.id.item_zfile_list_folder_line, ZFileContent.getLineColor());
        holder.setVisibility(R.id.item_zfile_list_folder_line, position < getItemCount() - 1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickByAnim != null) {
                    itemClickByAnim.onClick(v, position, item);
                }
            }
        });
    }

    public ArrayList<ZFileBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(ArrayList<ZFileBean> selectData) {
        this.selectData = selectData;
    }

    public void setQWLastState(ZFileBean bean) {
        int lastIndex = -1;
        for (int i = 0; i < getDatas().size(); i++) {
            if (getItem(i) == bean) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1) {
            selectData.remove(bean);
            boxMap.put(lastIndex, false);
            notifyItemChanged(lastIndex);
        }
    }

    public void reset() {
        selectData.clear();
        boxMap.clear();
    }

    public interface ChangeListener {
        void change(boolean isManage, int size);
    }

    private ChangeListener changeListener;

    public void setChangeListener(ChangeListener listener) {
        changeListener = listener;
    }

    public interface QWListener {
        void invoke(boolean isManage, ZFileBean bean, boolean isSelect);
    }

    private QWListener qwListener;

    public void setQwListener(QWListener listener) {
        qwListener = listener;
    }

    public interface ItemClickByAnim {
        void onClick(View view, int position, ZFileBean bean);
    }

    private ItemClickByAnim itemClickByAnim;

    public void setItemClickByAnim(ItemClickByAnim itemClickByAnim) {
        this.itemClickByAnim = itemClickByAnim;
    }

    public boolean isManage() {
        return isManage;
    }

    public void setManage(boolean value) {
        if (isQW) {
            if (value) {
                notifyDataSetChanged();
            } else {
                selectData.clear();
                Set<Map.Entry<Integer, Boolean>> entrySet = boxMap.entrySet();
                for (Map.Entry<Integer, Boolean> entry : entrySet) {
                    boxMap.put(entry.getKey(), false);
                }
                notifyDataSetChanged();
            }
        } else {
            if (!value) {
                selectData.clear();
                Set<Map.Entry<Integer, Boolean>> entrySet = boxMap.entrySet();
                for (Map.Entry<Integer, Boolean> entry : entrySet) {
                    boxMap.put(entry.getKey(), false);
                }
                notifyDataSetChanged();
            }
        }
        isManage = value;
    }

    public void setDatas(List<ZFileBean> list) {
        if (list == null || list.isEmpty()) {
            clear();
        } else {
            boxMap.clear();
            for (int i = 0; i < list.size(); i++) {
                ZFileBean bean = list.get(i);
                if (selectData == null || selectData.isEmpty()) {
                    boxMap.put(i, false);
                } else {
                    boxMap.put(i, selectData.contains(bean));
                }
            }
            super.setDatas(list);
        }
    }
}
