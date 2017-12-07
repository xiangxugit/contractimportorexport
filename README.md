1,和之前插件安装一样 cordova plugin add 地址
2,插件删除 ：plugin remove SimpleMath
3,安装完成以后，进入platforms /android/src/ ag/dhmp/MainActivity.java 把当前activity的内容修全部替换为如下内容：
4,调用方式 

访问文件
  cordova.plugins.MyMath.openself(function (result) {
          alert("返回来的路径"+result);
          }, function (error) { });
          console.log("导出执行了");

导入

  import(){
         cordova.plugins.MyMath.file(null, null, function (result) {

          }, function (error) { });
          console.log("导入执行了");
    }

  导出

    export(){
         cordova.plugins.MyMath.folder(
           function (result) {

          }, function (error) { });

          console.log("导出执行了");
  
  
  }






/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package ag.dhmp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import org.apache.cordova.*;

import io.ionic.starter.VCardIO;

public   class MainActivity extends CordovaActivity
{

  public static  ProgressDialog progressDlg = null;//更新界面

  public static final int  FILE_RESULT_CODE = 1;//请求文件夹

  public static final int FILE_WENJIAN_CODE = 2;//导入；

  public static Handler mHandler = new Handler();;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    // enable Cordova apps to be started in the background
    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
      moveTaskToBack(true);
    }
    // Set by <content src="index.html" /> in config.xml
    loadUrl(launchUrl);

    progressDlg = new ProgressDialog(this);
    progressDlg.setCancelable(false);
    progressDlg.setProgress(10000);
    progressDlg.setMessage("操作中");

//       progressDlg = ProgressDialog.show(this, "提示", "正在登陆中");?


  }



  /**
   * 更新进度条
   *
   * @param progress
   *            进度
   */
  public   void updateProgress(final int progress) {


    if (progress == 100) {
//      Toast.makeText(MainActivity.this,"操作完成",Toast.LENGTH_SHORT).show();
    }

    mHandler.post(new Runnable() {
      public void run() {
        progressDlg.setProgress(progress * 100);
        progressDlg.setMessage("" + progress + "%");
        if (progress == 100) {

          progressDlg.dismiss();

        }

      }
    });
  }






  public static ProgressDialog progress (Activity activity){
    if(null==progressDlg){
      progressDlg = new ProgressDialog(activity);
      progressDlg.setCancelable(false);
      progressDlg.setTitle("提示");
      progressDlg.setTitle("操作中,请稍等");
    }
    return progressDlg;

  }



  /*@Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if(resultCode==20){
      if (FILE_RESULT_CODE == requestCode) {
        updateProgress(0);
        progressDlg.show();


        Bundle bundle = null;
        if (data != null && (bundle = data.getExtras()) != null) {
          String path = bundle.getString("file");
          String pathReally = path+"/"+"backup.vcf";
          VCardIO vcarIO = new VCardIO(MainActivity.this);
          vcarIO.doExport(pathReally,MainActivity.this);
          progressDlg.show();
        }
      }


      if(FILE_WENJIAN_CODE == requestCode){
//      progressDlg.show();
        updateProgress(0);
        progressDlg.show();
        Bundle bundle = null;
        if (data != null && (bundle = data.getExtras()) != null) {
          String path = bundle.getString("file");
          VCardIO vcarIO = new VCardIO(MainActivity.this);
          vcarIO.doImport(path,false,MainActivity.this);

        }
      }

    }
  }*/



}
