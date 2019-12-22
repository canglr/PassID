package net.gulernet.app.nxfolder.ServiceClass;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.iceteck.silicompressorr.SiliCompressor;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

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

public class SikistirmaKontrol extends IntentService {

    private static ThinDownloadManager downloadManager;

    public SikistirmaKontrol () {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "oncreate methodu " + Thread.currentThread().getName() + " threadi üzerinden cagrildi");
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        BgVideoSikistirmaKontrol task = new BgVideoSikistirmaKontrol(getApplicationContext());
        task.execute();
    }


    public static class BgVideoSikistirmaKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;
        String filePath="";
        public BgVideoSikistirmaKontrol(Context c) {
            this.c = c;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //https://github.com/smanikandan14/ThinDownloadManager

                File testvideofolder = new File(c.getFilesDir()+"/testvideo");
                testvideofolder.mkdir();
                final File videotmp = new File(c.getCacheDir() + "/testvideo");
                videotmp.mkdir();
                final File testvideo = new File(c.getFilesDir()+"/testvideo/V_20180420_120202.mp4");
                if(testvideo.length() != 1438381) {
                    Uri downloadUri = Uri.parse("https://passid.azurewebsites.net/test/video/V_20180420_120202.mp4");
                    Uri destinationUri = Uri.parse(testvideo.toString());
                    DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                            .addCustomHeader("Auth-Token", "YourTokenApiKey")
                            .setRetryPolicy(new DefaultRetryPolicy())
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setDownloadListener(new DownloadStatusListener() {
                                @Override
                                public void onDownloadComplete(int id) {
                                    try {
                                        filePath = SiliCompressor.with(c).compressVideo(testvideo.toString(), videotmp.toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                                }

                                @Override
                                public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                                }
                            });
                    downloadManager = new ThinDownloadManager();
                    int downloadId = downloadManager.add(downloadRequest);
                }else {

                    filePath = SiliCompressor.with(c).compressVideo(testvideo.toString(), videotmp.toString());


                }

            } catch (Exception e) {
                e.printStackTrace();
                GlobalKod.writeSharedPreference(c,"videosikistirma","false");
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            File videofileadress = new File(filePath);
            if (videofileadress.length() <= 160) {
                GlobalKod.writeSharedPreference(c, "videosikistirma", "false");
            } else {
                GlobalKod.writeSharedPreference(c, "videosikistirma", "true");
            }

            videofileadress.delete();

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
