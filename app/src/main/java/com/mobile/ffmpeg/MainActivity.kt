package com.mobile.ffmpeg

import android.app.ProgressDialog
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mobile.ffmpeg.test.R
import com.mobile.ffmpeg.util.FFmpegAsyncUtils
import com.mobile.ffmpeg.util.FFmpegExecuteCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(),
    FFmpegExecuteCallback {
    var asyncTask: FFmpegAsyncUtils? = null
    var mProgressDialog: ProgressDialog? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var duration: Int = 1
    override fun onFFmpegCancel() {
        Log.e("yyy", "         onFFmpegCancel ")
    }

    override fun onFFmpegStart() {
        Log.e("yyy", "         onFFmpegStart ")
    }

    override fun onFFmpegSucceed(executeOutput: String?) {
        mProgressDialog?.dismiss()
        command_output.text = executeOutput
        Log.e("yyy", "         onFFmpegSucceed $executeOutput")
        setVideo(tempFile)
    }

    override fun onFFmpegFailed(executeOutput: String?) {
        Log.e("yyyy", "onFFmpegFailed   $executeOutput")
        command_output.text = executeOutput
    }

    override fun onFFmpegProgress(progress: Int?) {
        val mprogress = progress?.toFloat()?.div(duration)?.times(100)?.toInt() ?: 0
        mProgressDialog?.setMessage("正在执行命令行 \n  \n        ${mprogress}%")
    }

    fun getDuration(videoPath: String?): Int {
        return try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(videoPath)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    var tempFile: File? = null
    var file1: File? = null
    var cmd: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initExoVideo()
        run_command.setOnClickListener {
            runThread()
        }
    }

    private fun runThread() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setMessage("开始执行命令行")
        mProgressDialog?.setProgressNumberFormat("")
        mProgressDialog?.setButton("取消") { dialog, which ->
            asyncTask?.onCancel()
        }
        mProgressDialog?.show()

        val cache = cacheDir
        val time = System.currentTimeMillis()
        val file = File(cache, "video_demo$time.mp4")
        tempFile = File(cache, "result_video$time.mp4")
        try {
            file1 = redRawToFile(this, R.raw.video_demo, file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //多画面拼接视频
        cmd = FFmpegUtil.multiVideo(
            file1?.getAbsolutePath(),
            file1?.getAbsolutePath(),
            tempFile?.getAbsolutePath(),
            FFmpegUtil.LAYOUT_HORIZONTAL
        )
        duration = getDuration(file1?.absolutePath)
        asyncTask = FFmpegAsyncUtils()
        asyncTask?.setCallback(this)
        asyncTask?.execute(cmd)
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
    @Throws(IOException::class)
    private fun redRawToFile(context: Context, resourceId: Int, file: File): File {
        val inputStream = context.resources.openRawResource(resourceId)
        if (file.exists()) {
            file.delete()
        }
        val outputStream = FileOutputStream(file)
        try {
            val buffer = ByteArray(1024)
            var readSize: Int = -1
            while ({ readSize = inputStream.read(buffer);readSize }() > 0) {
                outputStream.write(buffer, 0, readSize)
            }
        } catch (e: IOException) {
            Log.e("yyyy", "Saving raw resource failed.", e)
            return file
        } finally {
            inputStream.close()
            outputStream.flush()
            outputStream.close()
            return file
        }
    }

    private fun initExoVideo() {
        exoPlayer = SimpleExoPlayer.Builder(this).build()
    }

    private fun setVideo(file: File?) {
        mPlayerView.visibility = View.VISIBLE
        val meter = DefaultBandwidthMeter.Builder(this).build()
        val defaultDataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name)),
            meter
        )
        val parse = Uri.fromFile(file)
        val mediaSource =
            ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(parse)
        exoPlayer?.prepare(mediaSource)
        mPlayerView.player = exoPlayer
        exoPlayer?.setPlayWhenReady(true)
    }

}
