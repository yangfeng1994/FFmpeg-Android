# FFmpeg-Android
[![](https://jitpack.io/v/yangfeng1994/FFmpeg-Android.svg)](https://jitpack.io/#yangfeng1994/FFmpeg-Android)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)


FFmpeg 在 Android中使用.
在您的Android项目中轻松执行FFmpeg命令。

## 关于
引入本项目使您的项目尽可能的小，功能尽可能的完善，已使用本项目上线的项目有 [影音坊](http://server.m.pp.cn/download/apk?appId=8061477&custom=0&ch_src=pp_dev&ch=default)。

项目支持 androidx 或者 support 

本项目没有引入任何第三方库，不会对您的项目有任何的代码侵入,可兼容最低api版本为15

# 项目截图

<img src="/app/imgs/ffmpeg_version.jpg" alt="图-1：ffmpeg_version" width="380px"></img>
<img src="/app/imgs/cmd.jpg" alt="图-2：cmd" width="380px"></img>
<img src="/app/imgs/multiple_video.jpg" alt="图-3：cmd" width="380px"></img>
<img src="/app/imgs/ic_picInPic.jpg" alt="图-3：cmd" width="380px"></img>

### 体系结构
FFmpeg-Android运行在以下架构上:
- armeabi
- armeabi-v7a
- armv7-neon
- arm64-v8a

### 特性

- 使用最新的 git-2020-01-25-fd11dd500 Copyright (c) 2000-2020 the FFmpeg developers
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

####

1.重新编译了ffmpeg

2.支持更多的命令，执行速度更快

3.支持在安卓10（aip29）上使用

4.支持进度回调，进度为执行文件的进度，如果想计算进度，拿（当前返回进度）除以（文件进度）

app 的 build.gradle 下添加

```
dependencies {
     implementation 'com.github.yangfeng1994:FFmpeg-Android:v2.0.0'
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

- 申请权限（对本地文件处理时，建议您务必申请权限，不然无法对音视频进行操作）

- 对输入的文件是否存在，进行判断（如您想要输出的文件 已经在手机中存在，将无法对输出文件）

### 运行 FFmpeg


##### java

```
    // 要执行“ffmpeg -version”命令
	
	String[] cmd = new String[]{"-version"};
	
    FFmpegAsyncUtils  asyncTask =new FFmpegAsyncUtils()
	
     asyncTask.setCallback(new FFmpegExecuteCallback() {
	 
	 		@Override
            public void onFFmpegStart() {}
	 		
            @Override
            public void onFFmpegSucceed(@Nullable String executeOutput) {
               
            }

            @Override
            public void onFFmpegFailed(@Nullable String executeOutput) {
               
            }

            @Override
            public void onFFmpegProgress(@Nullable Integer progress) {
                	fload mprogress = progress/执行视频文件或语音文件时长
            }

            @Override
            public void onFFmpegCancel() {

            }
        })
		
		asyncTask.execute(cmd);
		
        注意，传入的参数是一个lsit
		
        如果想要传入string，命令行拼接的时候，需要用空格隔开  使用FFmpegAsyncUtils2就行
	
```

##### kotlin


```
    // 要执行“ffmpeg -version”命令，只需传递“ arrayOf(-version) ”即可
	
    val  asyncTask = FFmpegAsyncUtils()
	
    asyncTask.setCallback(object :FFmpegExecuteCallback{
           
		   	override fun onFFmpegStart() {}
			
		   	override fun onFFmpegProgress(progress: Int?) {
       			//注意kotlin的除法，建议转为float后，再进行除以
				val mprogress = progress?.div(执行视频文件或语音文件时长)	
    			}
				
 			override fun onFFmpegCancel() {}

    		override fun onFFmpegSucceed(executeOutput: String?) {}

			override fun onFFmpegFailed(executeOutput: String?) {}

        })
		
        asyncTask.execute(cmd)
		
        注意，传入的参数是一个lsit
		
        如果想要传入string，命令行拼接的时候，需要用空格隔开  使用FFmpegAsyncUtils2就行
```


#### 注意：

-  所有命令行都不需要以 "ffmpeg"开头，直接命令行就行。

- 	   本项目使用的是AsyncTask，需要注意，每个子线程AsyncTask只能执行一次命令，取消后
       应重新new 一个AsyncTask 对象。

- 混淆在model里面已经添加，无需再次添加

### 停止(或退出)FFmpeg进程

- 	如果你想停止运行中的ffmpeg, 只需在调用' asyncTask.onCancel() '

####  FFmpegExecuteCallback 接口中方法的介绍

- 	onFFmpegStart() 开始执行

- 	onFFmpegProgress(progress: Int?)  进度 参数为执行音视频文件的所在的毫秒值

- 	onFFmpegCancel()  取消执行

- 	onFFmpegSucceed(executeOutput: String?) 执行成功 参数为ffmpeg的执行结果信息

- 	onFFmpegFailed(executeOutput: String?) 执行失败  参数为返回为失败原因

#### 历史版本

- v1.1.1 优化了代码的逻辑，兼容了低版本的手机，使项目同时兼容androidx与support

```
    implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.1.1'
    
```

- v1.0.1 移除x86 so包，优化项目大小

```
    implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.0.1'
    
```

- v1.0.0 新建项目 第一版本



```
    implementation 'com.github.yangfeng1994:FFmpeg-Android:v1.0.0'
```

# 体验demo

- [点击下载](https://raw.githubusercontent.com/yangfeng1994/FFmpeg-Android/master/app/release/app-release.apk)

## Licensing
- [Library license](https://github.com/yangfeng1994/FFmpeg-Android/blob/master/LICENSE)
- [FFmpeg license](https://www.ffmpeg.org/legal.html)
