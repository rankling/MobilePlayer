package com.yzx.frank.moblieplayer.fragment;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.activity.VideoPlayerActivity;
import com.yzx.frank.moblieplayer.adapter.VideoListAdapter;
import com.yzx.frank.moblieplayer.bean.VideoItem;
import com.yzx.frank.moblieplayer.interfaces.Keys;
import com.yzx.frank.moblieplayer.util.Utils;

import java.util.ArrayList;


/**
 * Created by Frank on 2017/12/6.
 */

public class VideoFragment extends BaseFragment {

    private ListView listView;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_media_list;
    }

    @Override
    public void initView() {
        listView = (ListView) rootView;

    }

    @Override
    public void initListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /**
                 * adapterView 就是ListView
                 */
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                ArrayList<VideoItem> videos = getVideoItems(cursor);
                enterVideoActivity(videos, position);

            }
        });
    }

    /**
     * 把Cursor里面的所有数据封装到一个ArraryList中
     * @param cursor
     * @return
     */
    private ArrayList<VideoItem> getVideoItems(Cursor cursor) {
        ArrayList<VideoItem> videos = new ArrayList<VideoItem>();
        cursor.moveToFirst();
        do{
            videos.add(VideoItem.fromCursor(cursor));

        }while (cursor.moveToNext());

        return videos;
    }

    /**
     * 进入视频播放界面
     * @param videoItems
     * @param position
     */
    protected void enterVideoActivity(ArrayList<VideoItem> videoItems, int position) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra(Keys.ITEMS, videoItems);
        intent.putExtra(Keys.CURRENT_POSITION, position);
        startActivity(intent);

    }

    @Override
    public void initData() {

        //这个查询方法会运行在主线程
        //getActivity().getContentResolver().query(uri,projection, selection, selectionArgs, sortOrder);
        //listView.setAdapter(adapter);
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            // 查询到数据的回调方法， 这个方法会运行在主线程
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

                Utils.printCursor(cursor);
                VideoListAdapter adapter = new VideoListAdapter(getActivity(),cursor);
                listView.setAdapter(adapter);

            }
        };

        int token = 0;          //相当于Message.what
        Object cookie = null;   //相当于Message.obj
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {//指定要查询哪些列
                MediaStore.Audio.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA,
        };
        String selection = null;                                //指定查询条件
        String [] selectionArgs = null;                         //指定查询条件中的参数
        String orderBy = MediaStore.Video.Media.TITLE+" ASC";   //指定为升序

        //这个方法会运行在子线程
        queryHandler.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
    }



    @Override
    public void onClick(View view, int id) {

    }
}
