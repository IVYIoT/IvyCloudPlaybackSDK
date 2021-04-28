# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings
-dontwarn com.google.android.exoplayer2.**

-keep class com.ivyiot.playback.EVideoType { *; }
-keep class com.ivyiot.playback.ICloudVideoPlayListener { *; }
-keep class com.ivyiot.playback.ITimeLineListener { *; }
-keep class com.ivyiot.playback.IvyVideo { *; }
-keep class com.ivyiot.playback.DateAndTimeUtils { *; }
-keep class com.ivyiot.playback.CalendarUtils { *; }
-keep class com.ivyiot.playback.CustomDateCalendar { *; }
-keep class com.ivyiot.playback.DateUtilCalendar { *; }
-keep class com.ivyiot.playback.IvyCloudVideoView { *; }
-keep class com.ivyiot.playback.DownloadButton { *; }
-keep class com.ivyiot.playback.TimeLineView { *; }
-keep class com.ivyiot.playback.DateLine { *; }
-keep interface com.ivyiot.playback.TimeLineView$ScaleMode{ *; }
-keep interface com.ivyiot.playback.TimeLineView$State{ *; }
