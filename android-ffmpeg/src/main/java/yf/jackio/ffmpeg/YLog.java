package yf.jackio.ffmpeg;

import android.util.Log;

 class YLog {

    private static String TAG = FFmpeg.class.getSimpleName();
    private static boolean DEBUG = false;

    public static void setDebug(boolean debug) {
        YLog.DEBUG = debug;
    }

    public static void setTag(String tag) {
        YLog.TAG = tag;
    }

    static void d(Object obj) {
        if (DEBUG) {
            Log.d(TAG, obj != null ? obj.toString() : "");
        }
    }

    static void e(Object obj) {
        if (DEBUG) {
            Log.e(TAG, obj != null ? obj.toString() : "");
        }
    }

    static void w(Object obj) {
        if (DEBUG) {
            Log.w(TAG, obj != null ? obj.toString() : "");
        }
    }

    static void i(Object obj) {
        if (DEBUG) {
            Log.i(TAG, obj != null ? obj.toString() : "");
        }
    }

    static void v(Object obj) {
        if (DEBUG) {
            Log.v(TAG, obj != null ? obj.toString() : "");
        }
    }

    static void e(Object obj, Throwable throwable) {
        if (DEBUG) {
            Log.e(TAG, obj != null ? obj.toString() : "", throwable);
        }
    }

    static void e(Throwable throwable) {
        if (DEBUG) {
            Log.e(TAG, "", throwable);
        }
    }

}
