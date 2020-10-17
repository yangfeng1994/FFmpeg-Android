package com.mobile.ffmpeg.util;

public interface FFmpegExecuteCallback {
    void onFFmpegStart();

    void onFFmpegSucceed(String executeOutput);

    void onFFmpegFailed(String executeOutput);

    void onFFmpegProgress(Integer progress);

    void onFFmpegCancel();
}
