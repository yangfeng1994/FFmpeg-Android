package yf.jackio.ffmpeg.example;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import yf.jackio.ffmpeg.ExecuteBinaryResponseHandler;
import yf.jackio.ffmpeg.FFmpeg;
import yf.jackio.ffmpeg.FFtask;

/**
 * Created by Brian on 11-12-17.
 */
public class ExampleActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private Handler handler = new Handler();
    private TextView mCommandOutput;
    private Button mRunCommand;
    private EditText mCommand;
    private FFtask fftask;
    private PlayerView mPlayerView;
    private SimpleExoPlayer exoPlayer;
    File tempFile;
    File file1;
    String[] cmd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_example);
        mCommandOutput = findViewById(R.id.command_output);
        mRunCommand = findViewById(R.id.run_command);
        mCommand = findViewById(R.id.command);
        mPlayerView = findViewById(R.id.mPlayerView);
        mRunCommand.setOnClickListener(this);
        File cache = getCacheDir();
        long time = System.currentTimeMillis();
        File file = new File(cache, "video_demo" + time + ".mp4");
        tempFile = new File(cache, "result_video" + time + ".mp4");
        try {
            file1 = redRawToFile(this, R.raw.video_demo, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //视频叠加成画中画
//        cmd = FFmpegUtil.picInPicVideo(file1.getAbsolutePath(), file1.getAbsolutePath(), 100, 100, tempFile.getAbsolutePath());
        //视频长度剪切
//        cmd = FFmpegUtil.cutVideo(file1.getAbsolutePath(), "00:00:00", "00:00:07", tempFile.getAbsolutePath());
        //多画面拼接视频
        cmd = FFmpegUtil.multiVideo(file1.getAbsolutePath(), file1.getAbsolutePath(), tempFile.getAbsolutePath(), FFmpegUtil.LAYOUT_HORIZONTAL);
//        cmd = new String[]{"-version"};
        String s = Arrays.toString(cmd).replace("[", "").replace("]", "").replace(",", " ");
        mCommand.setText(s);
        initExoVideo();
    }

    private void runCmd() {
        if (FFmpeg.getInstance(this).isSupported()) {
            // ffmpeg is supported
            versionFFmpeg();
            //ffmpegTestTaskQuit();
        } else {
            // ffmpeg is not supported
            Log.e("yyyy", "ffmpeg not supported!");
        }
    }

    /**
     * 读取流到文件中
     *
     * @param context
     * @param resourceId
     * @param file
     * @return
     * @throws IOException
     */
    private static File redRawToFile(Context context, int resourceId, File file) throws IOException {
        final InputStream inputStream = context.getResources().openRawResource(resourceId);
        if (file.exists()) {
            file.delete();
        }
        final FileOutputStream outputStream = new FileOutputStream(file);
        try {
            final byte[] buffer = new byte[1024];
            int readSize;

            while ((readSize = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readSize);
            }
        } catch (final IOException e) {
            Log.e("yyyy", "Saving raw resource failed.", e);
            return file;
        } finally {
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return file;
        }
    }

    private void versionFFmpeg() {
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在执行命令行");
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setButton("取消", this);
        String s = Arrays.toString(cmd).replace("[", "").replace("]", "").replace(",", " ");
        mCommand.setText(s);
        FFmpeg ffmpeg = FFmpeg.getInstance(this);

        fftask = ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(String message) {
                Log.e("yyyy", message);
                mCommandOutput.setText("成功" + message);
                mProgressDialog.dismiss();
                setVideo(tempFile);
            }

            @Override
            public void onProgress(String message) {
                mCommandOutput.setText(message);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                mCommandOutput.setText("失败" + message);
                mProgressDialog.dismiss();
            }
        });
    }

    private void initExoVideo() {
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
    }

    private void setVideo(File file) {
        mPlayerView.setVisibility(View.VISIBLE);
        DefaultBandwidthMeter meter = new DefaultBandwidthMeter.Builder(this).build();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)), meter);
        Uri parse = Uri.fromFile(file);
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(parse);
        exoPlayer.prepare(mediaSource);
        mPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
    }

    /**
     * 停止FFmpeg运行的例子
     */
    private void ffmpegTestTaskQuit() {
        String[] command = {"-i", "input.mp4", "output.mov"};

        final FFtask task = FFmpeg.getInstance(this).execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
                Log.e("yyyy", "on start");
            }

            @Override
            public void onFinish() {
                Log.e("yyyy", "on finish");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("yyyy", "RESTART RENDERING");
                        ffmpegTestTaskQuit();
                    }
                }, 5000);
            }

            @Override
            public void onSuccess(String message) {
                Log.e("yyyy", message);
            }

            @Override
            public void onProgress(String message) {
                Log.e("yyyy", message);
            }

            @Override
            public void onFailure(String message) {
                Log.e("yyyy", message);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("yyyy", "停止执行");
                task.sendQuitSignal();
            }
        }, 8000);
    }

    @Override
    public void onClick(View v) {
        runCmd();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (null != fftask) {
            fftask.sendQuitSignal();
        }
    }
}
