ht-imagepicker
===============

`ht-imagepicker` 是一款图片选择控件，提供了一套默认的本地图片扫描、本地图片显示/缩放预览、图片缓存、图片裁剪、系统拍照和自定义拍照功能，使用者可以通过设置 `HTPickParamConfig` 参数，来确定是选择数量限制，是否直接从相机拍照中获取，是否需要裁剪，裁剪输出的宽高比等。

考虑各个项目的 `UI` 特色和需求不同，基本上每个项目的项目的都会将 `UI` 大改，因此很难在默认实现的基础上提供一套 `UI` 设置接口来满足各个项目的需求。因此这里提供了一个 `HTRuntimeUIConfig` 参数，在启动图片选择控件的时候设置 `Activity` 或者 `Fragment` 的 `Class`，来完全自行实现 UI。

## 优点
1. 提供了一套默认实现，对于 UI 无要求的 app 可以方便接入使用
2. UI 可完全自定义，只需要按照要求继承相关的基类和正确实现相关的方法（可参照默认 UI 代码），就能完全自行编写需要的 UI，甚至能加入自行编写的其它功能
3. 提供了一个默认 UI 设置和运行时 UI 设置，通过设置不同 `RuntimeUIConfig` 参数，就能在同一个 app 打开不同 UI 的图片选择控件
4. ht-imagepicker 不会强制引入其它的如图片显示、图片缩放预览等相关的第三方库

## 2 步使用 ht-imagepicker 默认实现

1. 初始化

	```
	// 不修改默认实现的 UI，直接设置 null
	HTImagePicker.INSTANCE.init(this, null);
	```

2. 开启默认图片选择控件

	```
	// 从本地选择图片，最多选择单张图片，需要裁剪，无默认已经选中的图片
	HTPickParamConfig paramConfig = new HTPickParamConfig(HTImageFrom.FROM_LOCAL, null, null, true);
	// runtimeUIConfig 设置为null，开启默认 ui 界面
	HTImagePicker.INSTANCE.start(this, paramConfig, null, this);
	```

## ht-imagepicker 默认实现效果

![image](effect.gif)

## ht-imagepicker 的集成

ht-imagepicker 允许通过 Maven Center 下载使用。

Gradle:

compile 'com.netease.hearttouch:ht-imagepicker:1.0.2'


##ProGuard configuration

提交的aar包是并没有经过混淆的，所以在工程的 proguard 文件中，不需要额外设置代码

##开发文档
ht-imagepicker 的详细使用文档，请参见 [使用文档](https://github.com/NEYouFan/ht-imagepicker-android/blob/master/%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)

##版本发布历史

[CHANGELOG](https://github.com/NEYouFan/ht-imagepicker-android)

##杭研 HeartTouch 更多开源项目