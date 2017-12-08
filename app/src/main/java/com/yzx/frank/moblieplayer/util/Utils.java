package com.yzx.frank.moblieplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yzx.frank.moblieplayer.interfaces.Constants;

import java.util.Calendar;


/**
 * Created by Frank on 2017/12/6.
 */

public class Utils {
    /**
     * 查找Button、ImageButton并设置单击监听器
     * @param View
     * @param listener 单击监听器
     */
    public static void setButtonOnClickListener(View view, View.OnClickListener listener) {
        //遍历view里面所有的Button和ImageButton
        if(view instanceof Button || view instanceof ImageButton) {
            view.setOnClickListener(listener);
        } else if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i<viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setButtonOnClickListener(child,listener);
            }
        }

    }



    /**
     * 在屏幕中间显示一个Toast
     * @param context 传入上下文
     * @param text  传入想要显示的一段文本
     */
    public static void showToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context ) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        return  screenWidth;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        return  screenHeight;
    }

    public static void printCursor(Cursor cursor) {
        if(cursor  == null) {
            return ;
        }
        Logger.i(Utils.class, "共有"+cursor.getCount()+"条数据");
        //遍历所有行
        while (cursor.moveToNext()) {
            Logger.i(Utils.class, "--------------------------");
            //遍历所有列
            for( int i = 0 ; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String value = cursor.getString(i);
                Logger.i(Utils.class, columnName+ "=" +value);
            }
        }
    }


    /**
     * 格式化一个毫秒值
     * -如果有小时，则格式化为时分秒，如：02:30:59
     * -如果没有小时，则格式化为分秒，如：23:22
     * @param duration
     * @return
     */
    public static CharSequence formatMilliSeconds(long duration) {

        Calendar calendar = Calendar.getInstance();
        calendar.clear();   //清除时间，变成系统初始时间1970-1-1 00:00:00
        calendar.add(Calendar.MILLISECOND, (int)duration);

        CharSequence inFormat = duration / Constants.HOUR_MILLIS > 0 ?"kk:mm:ss":"mm:ss";
        return DateFormat.format(inFormat, calendar.getTime());
    }
}

