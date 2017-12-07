package SimpleMath;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ag.dhmp.MainActivity;
import io.ionic.starter.DataCleanManager;
import io.ionic.starter.FileBrowserActivity;
import io.ionic.starter.UpdateService;
import io.ionic.starter.VCardIO;
import io.ionic.starter.getPath;
//import io.ionic.starter.VCardIO;

/**
 * This class echoes a string called from JavaScript.
 */
public class MyMath extends CordovaPlugin {
  public static  Handler mHandler = new Handler();;
//    VCardIO vcarIO = null;

//  public static  ProgressDialog progressDlg = null;//更新界面

  private String importing = "";

  public static final int  FILE_RESULT_CODE = 1;//请求文件夹
  public static final int FILE_WENJIAN_CODE = 2;//导入；
  private   CallbackContext callbackcontexta;
  private String flag;
  private String actionTemp;
  private MainActivity mainActivity = null;
  public static final int FILE_SELF = 8;//访问android原生文件系统
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.callbackcontexta = callbackContext;
    this.actionTemp = action;
    if(null==mainActivity){
      mainActivity = new MainActivity();
    }
    if (!PermissionHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) || !PermissionHelper.hasPermission(this, Manifest.permission.WRITE_CONTACTS)) {
      Toast.makeText(cordova.getActivity(), "允许权限后重试", Toast.LENGTH_SHORT).show();
//      PermissionHelper.requestPermission(this, 10, Manifest.permission.READ_EXTERNAL_STORAGE);
//      PermissionHelper.requestPermission(this, 11, Manifest.permission.WRITE_CONTACTS);
      String[] strArray={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_CONTACTS};
      PermissionHelper.requestPermissions(this,10,strArray);
    } else
    {
      if (action.equals("plus")) {
        Intent it = new Intent();
        it.setClass(cordova.getActivity(), FileBrowserActivity.class);
        cordova.startActivityForResult((CordovaPlugin) this, it, 200);
//      callbackContext.success("返回数据是true");
//      backData(callbackContext);
        return true;
      }

      // 跳转到文件夹浏览界面

      if (action.equals("folder")) {
        Intent it = new Intent();
        it.setClass(cordova.getActivity(), FileBrowserActivity.class);
        it.putExtra("area", true);//外部sd卡
        it.putExtra("type", "daochu");
        cordova.startActivityForResult((CordovaPlugin) this, it, FILE_RESULT_CODE);
      }
      //TODO 选择文件
      if (action.equals("file")) {
        Intent it = new Intent();
        it.setClass(cordova.getActivity(), FileBrowserActivity.class);
        it.putExtra("area", true);//外部sd卡
        it.putExtra("type", "daoru");
        cordova.startActivityForResult((CordovaPlugin) this, it, FILE_WENJIAN_CODE);
      }

      //TODO  导入

      if (action.equals("import")) {


      }


      //TODO  导出
      if (action.equals("export")) {

      }

      //TODO 访问文件
      if (action.equals("fileself")){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，此处用任意类型
        intent.addCategory(intent.CATEGORY_OPENABLE);
        cordova.startActivityForResult((CordovaPlugin) this, intent, FILE_SELF);
      }

      //获取缓存大小

      if(action.equals("getcachesize")){
        String size = DataCleanManager.getTotalCacheSize(cordova.getActivity());
        this.callbackcontexta.success(size);


      }

      //清空缓存
      if(action.equals("clearcache")){
        DataCleanManager.clearAllCache(cordova.getActivity());
        this.callbackcontexta.success("0");
      }


      if(action.equals("updateapk")){

        this.callbackcontexta.success("0");

        new Thread(new Runnable() {
          @Override
          public void run() {
            Intent service = new Intent(cordova.getActivity(), UpdateService.class);
//            startService(service);
            cordova.getActivity().startService(service);
          }
        }).start();
      }






    }

    return true;


  }

  @Override
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
    super.onRequestPermissionResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 8) {
        Uri uri = data.getData();
        Log.e("uri",uri.getPath());
        Log.e("uri2",uri.getEncodedPath());
        Log.e("uri3",uri.toString());
//        getPath patha = new getPath();

        String picurl = getPath.getImageAbsolutePath(cordova.getActivity(),uri);
        Log.e("picurl",picurl);
        this.callbackcontexta.success(picurl);
      }
    }


    if (resultCode == 20) {
      if (FILE_RESULT_CODE == requestCode) {
        mainActivity.updateProgress(0);
        mainActivity.progressDlg.show();


        Bundle bundle = null;
        if (data != null && (bundle = data.getExtras()) != null) {
          String path = bundle.getString("file");

          if(path==null){

            
            String pathReally = "/storage/emulated/0" + "/" + "backup.vcf";
            Log.e("pathReally",pathReally);
            VCardIO vcarIO = new VCardIO(cordova.getActivity());
            vcarIO.doExport(pathReally, mainActivity);
            mainActivity.progressDlg.show();

          }
          else{
            String pathReally = path + "/" + "backup.vcf";

            VCardIO vcarIO = new VCardIO(cordova.getActivity());
            vcarIO.doExport(pathReally, mainActivity);
            mainActivity.progressDlg.show();
          }
//          Log.i("path",path);
//          Log.e("path",path);

        }
      }


      if (FILE_WENJIAN_CODE == requestCode) {
//      progressDlg.show();

        Bundle bundle = null;
        if (data != null && (bundle = data.getExtras()) != null) {
          String path = bundle.getString("file");

          String name = path.substring(path.lastIndexOf("/")+1,path.length());
          Log.e("name",name);

          String [] token = name.split("\\.");
//          IMG_20170609_035300.jpg
          String backname = token[1];

          if(backname.equals("vcf")){
            mainActivity.updateProgress(0);
            mainActivity.progressDlg.show();
            VCardIO vcarIO = new VCardIO(cordova.getActivity());
            vcarIO.doImport(path, false, mainActivity);
          }else{
            Toast.makeText(cordova.getActivity(),"必须导入导出的文件",Toast.LENGTH_LONG).show();
          }


        }

      }
    }
  }


//    super.onActivityResult(requestCode, resultCode, intent);
  }
