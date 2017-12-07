package io.ionic.starter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import ag.dhmp.MainActivity;


public class VCardIO {
  private Context context;

  public VCardIO(Context context) {
    this.context = context;
  }

  /**
   * 导入联系人信息
   *
   * @param fileName
   *            要导入的文件
   * @param replace
   *            是否替换先有联系人
   * @param activity
   *            主窗口
   */
  public void doImport(final String fileName, final boolean replace,
                       final MainActivity activity) {
    new Thread() {
      @Override
      public void run() {
        try {

          File vcfFile = new File(fileName);

          final BufferedReader vcfBuffer = new BufferedReader(
            new FileReader(fileName));
          final long maxlen = vcfFile.length();

          // 后台执行导入过程
          new Thread(new Runnable() {
            public void run() {
              long importStatus = 0;
              Contact parseContact = new Contact();
              try {
                long ret = 0;
                do {
                  ret = parseContact.parseVCard(vcfBuffer);
                  if (ret < 0) {
                    break;
                  }
                  parseContact.addContact(
                    context.getApplicationContext(), 0,
                    replace);
                  importStatus += parseContact.getParseLen();

                  // 更新进度条
                  activity.updateProgress((int) (100 * importStatus / maxlen));

                } while (true);
                activity.updateProgress(100);
//                stop();
              } catch (IOException e) {
              }
            }
          }).start();

        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  /**
   * 导出联系人信息
   *
   * @param fileName
   *            存放导出信息的文件
   * @param activity
   *            主窗口
   */
  public void doExport(final String fileName, final MainActivity activity) {
    new Thread() {
      @Override
      public void run() {
        try {
          final BufferedWriter vcfBuffer = new BufferedWriter(
            new FileWriter(fileName));

          final ContentResolver cResolver = context
            .getContentResolver();
          String[] projection = { ContactsContract.Contacts._ID };
          final Cursor allContacts = cResolver.query(
            ContactsContract.Contacts.CONTENT_URI, projection,
            null, null, null);
          if (!allContacts.moveToFirst()) {
//            allContacts.close();
//            stop();
          }

          final long maxlen = allContacts.getCount();
          // 线程中执行导出
          new Thread(new Runnable() {
            public void run() {
              long exportStatus = 0;
              String id = null;
              Contact parseContact = new Contact();
              try {
                do {
                  id = allContacts.getString(0);
                  parseContact.getContactInfoFromPhone(id, cResolver);
                  parseContact.writeVCard(vcfBuffer);
                  ++exportStatus;
                  // 更新进度条
                  activity.updateProgress((int) (100 * exportStatus / maxlen));
                } while (allContacts.moveToNext());
                activity.updateProgress(100);
                vcfBuffer.close();
                allContacts.close();
//								stop();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }).start();
        } catch (IOException e) {
          e.printStackTrace();


        }
      }
    }.start();

  }


  public void doExportself(final String fileName, final MainActivity activity) {
    Thread th1 = new Thread(new Runnable() {
      @Override
      public void run() {


        try {
          final BufferedWriter vcBuffer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {

        }
        final ContentResolver cResolver = context
          .getContentResolver();
        String[] projection = { ContactsContract.Contacts._ID };
        final Cursor allContacts = cResolver.query(
          ContactsContract.Contacts.CONTENT_URI, projection,
          null, null, null);
        if (!allContacts.moveToFirst()) {
          allContacts.close();
        }

        final long maxlen = allContacts.getCount();
      }
    });

    th1.start();


  }
}
