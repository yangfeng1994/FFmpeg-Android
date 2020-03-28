package yf.jackio.ffmpeg;

import android.os.Build;

public class CpuArchHelper {
    public static final String ARM_64_CPU = "arm64-v8a";
    public static final String ARM_V7_CPU = "armeabi-v7a";

    public static CpuArch getCpuArch() {
        YLog.d("Build.CPU_ABI : " + Build.CPU_ABI);

        switch (Build.CPU_ABI) {
            case ARM_64_CPU:
            case ARM_V7_CPU:
                return CpuArch.ARMv7;
            default:
                return CpuArch.NONE;
        }
    }
}
