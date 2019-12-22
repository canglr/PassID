package net.gulernet.app.nxfolder.ServiceClass;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.gulernet.app.nxfolder.BgClass.Crypto;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

import java.io.File;
import java.util.ArrayList;

import javax.crypto.Cipher;

import static android.content.ContentValues.TAG;

/**
 * Created by necip on 23.03.2018.
 */

public class ThumbnailKontrol extends IntentService {



    public ThumbnailKontrol () {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "oncreate methodu " + Thread.currentThread().getName() + " threadi üzerinden cagrildi");
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        File folder = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getApplicationContext(),"mail",""))).toString());
        File[] listOfFiles = folder.listFiles();
        File VeriYoluOlustur = new File(getApplicationContext().getCacheDir()+"/tmp/");
        VeriYoluOlustur.mkdirs();
        int sayi=0;
        for (File file : listOfFiles) {
            if (file.isDirectory()) {

                    String[] children = file.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        String mail = Crypto.md5(GlobalKod.readSharedPreference(getApplicationContext(),"mail",""));
                        String dosyaadi = new File(file, children[i]).getName();
                        String dosyayolu = file.toString()+"/"+ new File(file, children[i]).getName();
                        File encryptedFile = new File(dosyayolu);
                        String key = GlobalKod.readSharedPreference(getApplicationContext(),"kutusifre","");
                        File decryptedFile = new File(getApplicationContext().getCacheDir()+"/tmp/"+dosyaadi);
                        File thumbnail = new File(getApplicationContext().getFilesDir()+"/thumbnail/"+mail+"/"+dosyaadi);


                        if (GlobalKod.ResimUzantıKontrol(decryptedFile.getName()) == true) {
                            if (!thumbnail.exists()) {
                                Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
                                GlobalKod.ThumbnailOlustur(decryptedFile, 96, 96, decryptedFile.getName(), getApplicationContext(), 0);
                                Log.d(TAG, dosyayolu);
                                decryptedFile.delete();
                                sayi++;
                            }
                        } else if (GlobalKod.VideoUzantıKontrol(decryptedFile.getName()) == true) {
                            if (!thumbnail.exists()) {
                                Crypto.fileProcessor(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);
                                GlobalKod.ThumbnailOlustur(decryptedFile, 96, 96, decryptedFile.getName(), getApplicationContext(), 1);
                                Log.d(TAG, dosyayolu);
                                decryptedFile.delete();
                                sayi++;
                            }
                        }

                    }


            }
        }

        if(sayi >= 1) {
            Intent send = new Intent();
            send.setAction(Intent.ACTION_MAIN);
            send.putExtra("thumnailkontrol", "ok");
            sendBroadcast(send);
        }
        int pid =  android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }




    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy methodu " + Thread.currentThread().getName() + " threadi üzerinden cagrildi");
        super.onDestroy();
    }


}
