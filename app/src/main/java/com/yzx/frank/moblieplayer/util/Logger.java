package com.yzx.frank.moblieplayer.util;

import android.util.Log;

/**
 * Created by Frank on 2017/12/6.
 */

public  class Logger {

    private static boolean isShowLog = true;
    public static final int VERBOSE		= 1;
    public static final int DEBUG		= 2;
    public static final int INFO		= 3;
    public static final int WARN		= 4;
    public static final int ERROR		= 5;
    public static final int NOTHING		= 6;

    public static final int level       = VERBOSE;

    /**
     * 显示info级别的日志
     * @param objTag
     * @param msg
     */
    public static void i(Object objTag, String msg) {

        if(level <= INFO) {
            if(!isShowLog) {
                return;
            }
            String tag;
            if (objTag instanceof String) {
                tag = (String) objTag;
            } else if (objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getSimpleName();
            }

            Log.i(tag, msg);
        }
    }

    /**
     * 显示debug级别的日志
     * @param objTag
     * @param msg
     */
    public static void d(Object objTag, String msg) {

        if(level <= DEBUG) {
            if(!isShowLog) {
                return;
            }
            String tag;
            if (objTag instanceof String) {
                tag = (String) objTag;
            } else if (objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getSimpleName();
            }

            Log.d(tag, msg);
        }
    }
    /**
     * 显示verbose级别的日志
     * @param objTag
     * @param msg
     */
    public static void v(Object objTag, String msg) {
        if(level <= VERBOSE) {
            if(!isShowLog) {
                return;
            }
            String tag;
            if(objTag instanceof String) {
                tag = (String)objTag;
            } else if(objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getSimpleName();
            }

            Log.v(tag, msg);
        }
    }

    /**
     * 显示warn级别的日志
     * @param objTag
     * @param msg
     */
    public static void w(Object objTag, String msg) {
        if(level <= WARN) {
            if(!isShowLog) {
                return;
            }
            String tag;
            if(objTag instanceof String) {
                tag = (String)objTag;
            } else if(objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getSimpleName();
            }

            Log.w(tag, msg);
        }
    }
    /**
     * 显示error级别的日志
     * @param objTag
     * @param msg
     */
    public static void e(Object objTag, String msg) {
        if(level <= ERROR) {
            if(!isShowLog) {
                return;
            }
            String tag;
            if(objTag instanceof String) {
                tag = (String)objTag;
            } else if(objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getSimpleName();
            }

            Log.e(tag, msg);
        }
    }
}
