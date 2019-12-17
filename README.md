# FFmpeg-Android
[![](https://jitpack.io/v/yangfeng1994/FFmpeg-Android.svg)](https://jitpack.io/#yangfeng1994/FFmpeg-Android)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)


FFmpeg 在 Android中使用.
在您的Android项目中轻松执行FFmpeg命令。

## 关于
引入本项目使您的项目尽可能的小，功能尽可能的完善，已使用本项目上线的项目有 [影音坊](http://server.m.pp.cn/download/apk?appId=8061477&custom=0&ch_src=pp_dev&ch=default)。

项目支持 androidx 或者 support 

本项目没有引入任何第三方库，不会对您的项目有任何的代码侵入
使用的是 ProcessBuilder 执行命令行操作 ,可兼容最低api版本为11

# 项目截图

<img src="/sample/imgs/ffmpeg_version.jpg" alt="图-1：ffmpeg_version" width="380px",height=600></img>
<img src="/sample/imgs/cmd.jpg" alt="图-2：cmd" width="380px",height=600></img>
<img src="/sample/imgs/multiple_video.jpg" alt="图-3：cmd" width="380px",height=600></img>
<img src="/sample/imgs/ic_picInPic.jpg" alt="图-3：cmd" width="380px",height=600></img>

### 体系结构
FFmpeg-Android运行在以下架构上:
- armeabi
- armeabi-v7a
- armv7-neon
- arm64-v8a

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
在项目的 build.gradle 中添加
```
allprojects{

	repositories {
		...
		maven { url 'https://www.jitpack.io' }
		}

	}

```

####此版本，优化了代码的逻辑，兼容了低版本的手机，使项目同时兼容androidx与support

app 的 build.gradle 下添加

```
dependencies {
     implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.1.1'
}
```


设置支持的so库版本
```
android{
        defaultConfig{
         ndk {
                    abiFilters 'armeabi-v7a', 'arm64-v8a'     //过滤的so库版本
                }
        }
}
```

#### 友情提示
1.申请权限（对本地文件处理时，建议您务必申请权限，不然无法对音视频进行操作）
2.对输入的文件是否存在，进行判断（如您想要输出的文件 已经在手机中存在，将无法对输出文件）
### 检查是否支持FFmpeg
要检查设备上是否有FFmpeg，可以使用以下方法。

##### java

建议先在application中调用此方法（此方法为判断是否支持 ffmpeg 如果ffmpeg so库没有正常加载，可以写入到您的手机缓存中）

``` 
if (FFmpeg.getInstance(this).isSupported()) {
  // 支持ffmpeg
} else {
  // 不支持ffmpeg
}
```

### 运行 FFmpeg command
在这个示例代码中，我们将运行ffmpeg -version命令。

##### java
```
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

### 设置超时时间
不建议使用超时时间，视频过大，一般执行都比较慢,我在项目中没有设置过超时时间

```
  ffmpeg.setTimeout(X毫秒);
```

### 停止(或退出)FFmpeg进程
如果你想停止运行中的ffmpeg, 只需在运行的FFtask上调用' .sendQuitSignal() '

```
java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
FFtask ffTask = ffmpeg.execute( ... )

ffTask.sendQuitSignal();
```

注意:这将导致' onFailure '方法被调用，而不是' onSuccess '


####历史版本


移除x86 so包，优化项目大小

```
    implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.0.1'
    
```

新建项目 第一版本

```
    implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.0.0'
```

# 体验demo

[点击下载](https://raw.githubusercontent.com/yangfeng1994/FFmpeg-Android/master/sample/release/sample-release.apk)

## Special Thanks To
- [diegoperini](https://github.com/diegoperini)

## Licensing
- [Library license](https://github.com/bravobit/FFmpeg-Android/blob/master/LICENSE)
- [FFmpeg license](https://www.ffmpeg.org/legal.html)
