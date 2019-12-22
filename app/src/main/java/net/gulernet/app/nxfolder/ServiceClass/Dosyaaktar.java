package net.gulernet.app.nxfolder.ServiceClass;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.iceteck.silicompressorr.SiliCompressor;

import net.gulernet.app.nxfolder.BgClass.Crypto;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.UiClass.FragmentOne;

import java.io.File;
import java.util.ArrayList;

import javax.crypto.Cipher;

/**
 * Created by necip on 21.02.2018.
 */

public class Dosyaaktar extends IntentService {

    private static final String TAG = Dosyaaktar.class.getSimpleName();
    public static ArrayList<String> adres;
    String SecilenKlasor;

    public Dosyaaktar () {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "oncreate methodu " + Thread.currentThread().getName() + " threadi üzerinden cagrildi");
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ArrayList<String> msg0 = intent.getStringArrayListExtra("adres");
        adres = msg0;
        SecilenKlasor = intent.getStringExtra("SecilenKlasor");
        new BackgroundZipİceAktar().execute((Void) null);

    }


    public class BackgroundZipİceAktar extends AsyncTask<Void, Integer, Void> {
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        int id = 1;

        public void ZipİceAktar(String file, Context context){

            //https://github.com/Tourenathan-G5organisation/SiliCompressor

            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            //String key = Crypto.md5(preferences.getString("kutusifre", ""));
            String key = GlobalKod.readSharedPreference(context,"kutusifre","");
            try {


                File inputFile = new File(file.toString());
                File encryptedFile = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))+"/"+SecilenKlasor+"/"+inputFile.getName()).toString());



                  if (GlobalKod.ResimUzantıKontrol(inputFile.getName()) == true) {
                      if(inputFile.length() < 83886080) {
                      Crypto.fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
                      GlobalKod.ThumbnailOlustur(inputFile, 96, 96, inputFile.getName(), context, 0);
                      }else{
                          GlobalKod global = new GlobalKod();
                          global.BildirimOlustur(context,inputFile.getName(),getResources().getString(R.string.Exceeds_the_80_MB_limit));
                      }
                  } else if (GlobalKod.VideoUzantıKontrol(inputFile.getName()) == true) {
                      GlobalKod global = new GlobalKod();
                      String videosikistirma = GlobalKod.readSharedPreference(context,"videosikistirma","false");
                      if (videosikistirma.equals("true")) {

                          if (inputFile.length() < 805306368) {
                              global.BildirimOlustur(context,getResources().getString(R.string.Transferring_Files),getResources().getString(R.string.processing));
                              File videotmp = new File(context.getCacheDir() + "/tmpvideo");
                              videotmp.mkdir();
                              String filePath = SiliCompressor.with(context).compressVideo(inputFile.toString(), videotmp.toString());
                              File videotmpfileadress = new File(filePath);
                              Crypto.fileProcessor(Cipher.ENCRYPT_MODE, key, videotmpfileadress, encryptedFile);
                              GlobalKod.ThumbnailOlustur(inputFile, 96, 96, videotmpfileadress.getName(), context, 1);
                              videotmpfileadress.delete();
                          } else {
                              global.BildirimOlustur(context, inputFile.getName(), getResources().getString(R.string.Exceeds_the_768_MB_limit));
                          }
                      }else{

                          if(inputFile.length() < 83886080) {
                              global.BildirimOlustur(context,getResources().getString(R.string.Transferring_Files),getResources().getString(R.string.processing));
                              Crypto.fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
                              GlobalKod.ThumbnailOlustur(inputFile, 96, 96, inputFile.getName(), context, 1);
                          }else{
                              global.BildirimOlustur(context,inputFile.getName(),getResources().getString(R.string.Exceeds_the_80_MB_limit));
                          }

                      }

                  }else{

                      if(inputFile.length() < 83886080) {
                          Crypto.fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
                      }else{
                          GlobalKod global = new GlobalKod();
                          global.BildirimOlustur(context,inputFile.getName(),getResources().getString(R.string.Exceeds_the_80_MB_limit));
                      }

                  }

            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNotifyManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setContentTitle(getResources().getString(R.string.Transferring_Files))
                    .setContentText(getResources().getString(R.string.processing))
                    .setSmallIcon(R.drawable.bildirim);
        }

        @Override
        protected Void doInBackground(Void... params) {

            int adressayisi = adres.size();
            int sayi = 1;
            for (String object: adres) {
                ZipİceAktar(object,getApplicationContext());
                mBuilder.setProgress(adressayisi, sayi, false);
                mNotifyManager.notify(id, mBuilder.build());
                sayi++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //ZipListeYenile();
            //globalkod.BildirimOlustur(getContext(),"Pass ID","Aktarım Tamamlandı");
            mBuilder.setContentText(getResources().getString(R.string.Transfer_Complete))
                    // Removes the progress bar
                    .setProgress(0,0,false);
            mNotifyManager.notify(id, mBuilder.build());

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.putExtra("dosyaaktar", "ok");
            sendBroadcast(intent);
            int pid =  android.os.Process.myPid();
            android.os.Process.killProcess(pid);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];

        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);

        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy methodu " + Thread.currentThread().getName() + " threadi üzerinden cagrildi");
        super.onDestroy();
    }
}


