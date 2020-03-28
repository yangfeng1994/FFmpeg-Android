package yf.jackio.ffmpeg;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FileUtils {
    private static final String FFMPEG_FILE_NAME = "ffmpeg";

    static File getFFmpeg(Context context) {
        File folder = context.getFilesDir();
        return new File(folder, FFMPEG_FILE_NAME);
    }

    /**
     * 将asset中的ffmpeg写入文件中
     * @param stream
     * @param file
     * @return 是否写入成功
     */
    static boolean inputStreamToFile(InputStream stream, File file) {
        try {
            InputStream input = new BufferedInputStream(stream);
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
            input.close();
            return true;
        } catch (IOException e) {
            YLog.e("error while writing ff binary file", e);
        }
        return false;
    }
}