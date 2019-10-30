package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayus on 08/08/2018.
 */

public class FileExplorerDownloadedAdapter extends BaseAdapter {

    private List<String> m_item;
    private List<String> m_path;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;

    public FileExplorerDownloadedAdapter(Context p_context, List<String> p_item, List<String> p_path, Boolean p_isRoot) {
        m_context = p_context;
        m_item = p_item;
        m_path = p_path;
        m_selectedItem = new ArrayList<>();
        m_isRoot = p_isRoot;
    }

    @Override
    public int getCount() {
        return m_item.size();
    }

    @Override
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int p_position, View p_convertView, ViewGroup p_parent) {
        View m_view;
        ViewHolder m_viewHolder;

        if (p_convertView == null) {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            m_view = m_inflater.inflate(R.layout.frag_file_explore_local_item, null);
            m_viewHolder = new ViewHolder();
            m_viewHolder.m_tvFileName = m_view.findViewById(R.id.lr_tvFileName);
            m_viewHolder.m_tvDate = m_view.findViewById(R.id.lr_tvdate);
            m_viewHolder.m_ivIcon = m_view.findViewById(R.id.lr_ivFileIcon);
            m_viewHolder.m_cbCheck = m_view.findViewById(R.id.lr_cbCheck);
            m_view.setTag(m_viewHolder);
        } else {
            m_view = p_convertView;
            m_viewHolder = ((ViewHolder) m_view.getTag());
        }
        if (!m_isRoot && p_position == 0) {
            m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);
        }

        m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
        m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(p_position))));
        m_viewHolder.m_tvDate.setText(getLastDate(p_position));

        m_viewHolder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    m_selectedItem.add(p_position);
                } else {
                    m_selectedItem.remove(m_selectedItem.indexOf(p_position));
                }
            }
        });
        return m_view;
    }

    private int setFileImageType(File m_file) {
        int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath = m_file.getAbsolutePath();

        if (m_file.isDirectory())
            return R.mipmap.ic_folder;
        else {
            if(m_filepath.contains(".")){
                if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                    return R.mipmap.ic_images;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                    return R.mipmap.ic_images;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".txt")) {
                    return R.mipmap.ic_file;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".doc")) {
                    return R.mipmap.ic_docx;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".docx")) {
                    return R.mipmap.ic_docx;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".xls")) {
                    return R.mipmap.ic_excel;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".xlsx")) {
                    return R.mipmap.ic_excel;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".ppt")) {
                    return R.mipmap.ic_powerpoint;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".pdf")) {
                    return R.mipmap.ic_pdf;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".zip")) {
                    return R.mipmap.ic_archive;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".rar")) {
                    return R.mipmap.ic_archive;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".mp3")) {
                    return R.mipmap.ic_music;
                } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".3gp")) {
                    return R.mipmap.ic_video;
                } else {
                    return R.mipmap.ic_file;
                }
            }
            return R.mipmap.ic_file;
        }
    }

    String getLastDate(int p_pos) {
        File m_file = new File(m_path.get(p_pos));
        SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }

    class ViewHolder {
        CheckBox m_cbCheck;
        ImageView m_ivIcon;
        TextView m_tvFileName;
        TextView m_tvDate;
    }
}
