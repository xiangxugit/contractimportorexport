package io.ionic.starter;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;



/**
 * Created by xiangxu on 2017/10/12.
 */

public class UpdateService extends Service {

  public UpdateService(){

  }

  /**
   * android系统下载类
   */
  DownloadManager manager;
//  DownLoadManager manager;

//  接受下载完的广播
  DownloadCompleteReceiver receiver;

//  初始化下载器

  private void initDownManager(){
    manager =  (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    receiver = new DownloadCompleteReceiver();

    //设置下载地址
    String urlPath = "http://47.93.61.22/wordpress/app-release.apk";
    Uri parse = Uri.parse(urlPath);
    DownloadManager.Request down = new DownloadManager.Request(parse);
    //设置允许使用的网络类型，这里是移动和wifi都可以
    down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);

    //下载时，通知栏显示
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
      down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
    }

    //显示下载界面
    down.setVisibleInDownloadsUi(true);

    //设置下载后文件存放的位置
    String apkName = parse.getLastPathSegment();
    down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,apkName);

    //将下载放入请求队列
//    manager.enquenue(down);
    manager.enqueue(down);

    //注册下载广播
    registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    initDownManager();
    return super.onStartCommand(intent, flags, startId);
  }


  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {

    //注销下载广播
    if(receiver!=null){
      unregisterReceiver(receiver);
    }
    super.onDestroy();
  }


//  接受下载完成后的intetn

  class DownloadCompleteReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
      if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
          if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
              //获取下载文件id

            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            Log.d("kodulf","id="+downId);
            //自动安装
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
              Uri uriForDownloadedFile = manager.getUriForDownloadedFile(downId);
              Log.d("kodulf","uri="+uriForDownloadedFile);
              installApkNew(uriForDownloadedFile);
            }

            UpdateService.this.stopSelf();

          }
      }
    }
  }


  //安装apk
  protected  void installApkNew(Uri uri){
    Intent intent = new Intent();
    //执行动作
    intent.setAction(Intent.ACTION_VIEW);
    //执行的数据类型
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(uri,"application/vnd.android.package-archive");
    //加以下几句话，防止单击打开的时候崩溃
    android.os.Process.killProcess(android.os.Process.myPid());
    try {
      startActivity(intent);
    }catch (Exception e){
      e.printStackTrace();
    }

  }

}
