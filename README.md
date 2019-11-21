# FFmpeg-Android
[![](https://jitpack.io/v/yangfeng1994/FFmpeg-Android.svg)](https://jitpack.io/#yangfeng1994/FFmpeg-Android)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
FFMpeg 在 Android中使用.
在您的Android项目中轻松执行FFmpeg命令。

## 关于
这个项目是 [FFmpeg-Android](https://github.com/bravobit/FFmpeg-Android) 的延续分支。删除了FFprobe,
使so包大小减少了一半,尽量压缩项目的大小，使您的项目尽可能的小，功能尽可能的完善。


# 项目截图

<img src="/sample/imgs/ffmpeg_version.jpg" alt="图-1：ffmpeg_version" width="380px"></img>
<img src="/sample/imgs/cmd.jpg" alt="图-2：cmd" width="380px"></img>

### 体系结构
FFmpeg-Android运行在以下架构上:
- armv7
- armv7-neon
- armv8

### FFmpeg构建
在本项目中，FFmpeg是通过以下库构建的:
- x264 `r2851 ba24899`
- libpng `1.6.21`
- freetype2 `2.8.1`
- libmp3lame `3.100`
- libvorbis `1.3.5`
- libvpx `v1.6.1-1456-g7d1bf5d`
- libopus `1.2.1`
- fontconfig `2.11.94`
- libass `0.14.0`
- fribidi `0.19.7`
- expat `2.1.0`
- fdk-aac `0.1.6`

### 特性
- 使用最新的FFmpeg发行版 `n4.0-39-gda39990`
- 在ARM架构上使用本机CPU功能
- 启用网络功能
- 多线程

## 使用

### 开始
包括依赖


allprojects
{

	repositories {
		...
		maven { url 'https://www.jitpack.io' }
		}

	}

```gradle
dependencies {
     implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.0.1'
}
```

### 检查是否支持FFmpeg
要检查设备上是否有FFmpeg，可以使用以下方法。
```java
if (FFmpeg.getInstance(this).isSupported()) {
  // 支持ffmpeg
} else {
  // 不支持ffmpeg
}
```
这就是加载FFmpeg库所要做的全部工作。

### 运行 FFmpeg command
在这个示例代码中，我们将运行ffmpeg -version命令。
```java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
  // 要执行“ffmpeg -version”命令，只需传递“-version”即可
ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

    @Override
    public void onStart() {}

    @Override
    public void onProgress(String message) {}

    @Override
    public void onFailure(String message) {}

    @Override
    public void onSuccess(String message) {}

    @Override
    public void onFinish() {}

});
```

### 停止(或退出)FFmpeg进程
如果你想停止运行中的ffmpeg, 只需在运行的FFtask上调用' .sendQuitSignal() '

```java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
FFtask ffTask = ffmpeg.execute( ... )

ffTask.sendQuitSignal();
```

注意:这将导致' onFailure '被调用，而不是' onSuccess ' ._

# 体验demo

[点击下载](https://raw.githubusercontent.com/yangfeng1994/FFmpeg-Android/master/sample/release/sample-release.apk)

## Special Thanks To
- [diegoperini](https://github.com/diegoperini)

## Licensing
- [Library license](https://github.com/bravobit/FFmpeg-Android/blob/master/LICENSE)
- [FFmpeg license](https://www.ffmpeg.org/legal.html)
