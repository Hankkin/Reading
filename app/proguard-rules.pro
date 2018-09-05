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
-libraryjars libs/ YoudaoBase.jar
-libraryjars libs/ YoudaoCnDictOffline.jar
-libraryjars libs/ YoudaoTranslateOnline.jar
-libraryjars libs/ YoudaoTranslateOffline.jar

-keep class com.youdao.sdk.ydtranslate.** { *;}
-keep class com.youdao.sdk.chdict.** { *;}
-keep class com.youdao.localtransengine.** { *;}
-keep class com.youdao.sdk.ydonlinetranslate.** { *;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}