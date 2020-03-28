package yf.jackio.ffmpeg.example;

import android.app.Application;

import yf.jackio.ffmpeg.FFmpeg;


public class FFmpegExample extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FFmpeg.getInstance(this).isSupported();
    }
}
