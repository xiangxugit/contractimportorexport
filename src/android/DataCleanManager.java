package io.ionic.starter;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by xiangxu on 2017/10/10.
 */

public class DataCleanManager {

  public static String getTotalCacheSize(Context context){
    long cacheSize = getFolderSize(context.getCacheDir());
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
      cacheSize += getFolderSize(context.getExternalCacheDir());
    }
    return  getFormatSize(cacheSize);
  }


  public static void clearAllCache(Context contex){
    deleteDir(contex.getCacheDir());
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
      deleteDir(contex.getExternalCacheDir());
    }
  }



  private static boolean deleteDir(File dir){
    if(dir!= null && dir.isDirectory()){
      String [] children = dir.list();
      for(int i = 0;i<children.length;i++){
        boolean success = deleteDir(new File(dir,children[i]));
        if(!success){
          return  false;
        }
      }
    }

    return dir.delete();
  }
  public static long getFolderSize(File file){
    long size = 0;
    File[] fileList = file.listFiles();
    for(int i=0;i<fileList.length;i++){
      if(fileList[i].isDirectory()){
        size = size+getFolderSize(fileList[i]);
      }else{
        size = size+fileList[i].length();
      }
    }
    return size;
  }

  /**
   * 格式化单位
   * @param size
   * @return
     */
  public static String getFormatSize(double size){
    double kiloByte = size/1024;
    if(kiloByte<1){
      return "0";
    }

    double megaByte = kiloByte/1024;
    if(megaByte<1){
      BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
      return result1.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"KB";
    }

    double gigaByte = megaByte/1024;
    if(gigaByte<1){
      BigDecimal result2 =new BigDecimal(Double.toString(megaByte));
      return  result2.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"MB";
    }

    double teraBytes = gigaByte/1024;
    if(teraBytes<1){
       BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
      return  result3.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"GB";

    }

    BigDecimal result4 = new BigDecimal(teraBytes);
    return result4.setScale(2,BigDecimal.ROUND_UP).toPlainString()+"TB";

  }


}
