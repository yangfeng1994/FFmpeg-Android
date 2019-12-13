package yf.jackio.ffmpeg.example;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_example);
        mCommandOutput = findViewById(R.id.command_output);
        mRunCommand = findViewById(R.id.run_command);
        mCommand = findViewById(R.id.command);
        mRunCommand.setOnClickListener(this);
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

    private void versionFFmpeg() {
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在执行命令行");
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setButton("取消", this);
        //视频长度剪切
//        String cmd = "-ss 00:00:00 -t 00:00:07 -i /storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1575169611100.mp4 -c:v libx264 -c:a aac -strict experimental -b:a 98k /storage/emulated/0/tencent/MicroMsg/WeiXin/wx_11camera45_abcjfjf.mp4";
        String cmd = mCommand.getText().toString();
        FFmpeg ffmpeg = FFmpeg.getInstance(this);

        fftask = ffmpeg.execute(cmd.split(" "), new ExecuteBinaryResponseHandler() {

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
