package com.github.sisong;

import android.content.Context;
import java.io.File;
import android.util.Log;

/*
 customize edit for Unity export android project:
    1. add this file HotUnity.java to project;
    2. add libhotunity.so to project jniLibs;
    3. edit file UnityPlayerActivity.java in project;
        add code: `import com.github.sisong.HotUnity;`
        add code: `HotUnity.hotUnity(this);` before `mUnityPlayer = new UnityPlayer(this);`
 */

public class HotUnity{
    public static native void doHot(String apkPath,String libDir,String newApkPath,String libCacheDir);
    public static void hotUnity(Context app){
        String apkPath=app.getPackageResourcePath();
        String libDir=app.getApplicationInfo().nativeLibraryDir;
        String newApkPath=app.getFilesDir().getPath() + "/HotUpdate/hot.apk";
        String libCacheDir=newApkPath+"_lib";
        
        System.loadLibrary("main"); //can't mapPathLoadLib
        mapPathLoadLib(libCacheDir,"unity");
        mapPathLoadLib(libCacheDir,"hotunity");
        //note: You can load other your lib by mapPathLoadLib, when lib have newVersion;
        doHot(apkPath,libDir,newApkPath,libCacheDir);
    }
    
    private static void mapPathLoadLib(String libCacheDir,String libName){
        String libSoName=System.mapLibraryName(libName);
        String cachedLibPath=libCacheDir+"/"+libSoName;
        if (pathIsExists(cachedLibPath)) {
            Log.w("HotUnity", "java MAP_PATH() to "+cachedLibPath);
            System.load(cachedLibPath);
        }else {
            Log.w("HotUnity", "java MAP_PATH() not found "+cachedLibPath);
            System.loadLibrary(libName);
        }
    }
    private static boolean pathIsExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}
