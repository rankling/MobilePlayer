package com.yzx.frank.moblieplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.bean.VideoItem;
import com.yzx.frank.moblieplayer.util.Utils;

/**
 * Created by Frank on 2017/12/7.
 */

public class VideoListAdapter extends CursorAdapter {
    public VideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    //创建一个View
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //Step1 填充一个View
        View view = View.inflate(context, R.layout.adapter_video_list,null);

        //Step2 用一个ViewHolder保存View中的控件
        ViewHolder holder   = new ViewHolder();
        holder.tv_title     = view.findViewById(R.id.tv_title);
        holder.tv_duration  = view.findViewById(R.id.tv_duration);
        holder.tv_size      = view.findViewById(R.id.tv_size);

        //Step3 把ViewHolder保存到View中
        view.setTag(holder);
        return view;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_duration;
        TextView tv_size;

    }

    //把Cursor中的数据绑定到View上显示
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        VideoItem item = VideoItem.fromCursor(cursor);
        holder.tv_title.setText(item.getTitle());
        holder.tv_size.setText(Formatter.formatFileSize(context,item.getSize()));
        CharSequence time = Utils.formatMilliSeconds(item.getDuration());
        holder.tv_duration.setText(time);

    }
}
