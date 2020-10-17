package com.mobile.ffmpeg.util;

import android.os.AsyncTask;

import com.arthenica.mobileffmpeg.Config;
import com.mobile.ffmpeg.FFmpeg;
import com.mobile.ffmpeg.Statistics;
import com.mobile.ffmpeg.StatisticsCallback;

public class FFmpegAsyncUtils2 extends AsyncTask<String, Integer, Integer> implements StatisticsCallback {

    private FFmpegExecuteCallback mCallback;

    public FFmpegAsyncUtils2 setCallback(FFmpegExecuteCallback mCallback) {
        this.mCallback = mCallback;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (null != mCallback) {
            mCallback.onFFmpegStart();
        }
        Config.resetStatistics();
        Config.enableStatisticsCallback(this);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        if (isCancelled()) {
            return Config.RETURN_CODE_CANCEL;
        }
        return FFmpeg.execute(strings[0]);
    }

    @Override
    public void apply(Statistics statistics) {
        if (null != statistics) {
            publishProgress(statistics.getTime());
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (isCancelled() || null == mCallback) {
            return;
        }
        mCallback.onFFmpegProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (null == mCallback) {
            return;
        }
        if (integer == Config.RETURN_CODE_SUCCESS) {
            mCallback.onFFmpegSucceed(Config.getLastCommandOutput());
        } else {
            mCallback.onFFmpegFailed(Config.getLastCommandOutput());
        }
    }

    @Override
    public void onCancel() {
        cancel(true);
        FFmpeg.cancel();
        if (null != mCallback) {
            mCallback.onFFmpegCancel();
        }
    }
}
