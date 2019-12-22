package net.gulernet.app.nxfolder.BgClass;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.iceteck.silicompressorr.SiliCompressor;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;
import com.yandex.metrica.YandexMetrica;

import net.gulernet.app.nxfolder.BuildConfig;
import net.gulernet.app.nxfolder.DataClass.KullaniciController;
import net.gulernet.app.nxfolder.DataClass.SurumController;
import net.gulernet.app.nxfolder.UiClass.MainActivity;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.UiClass.kullanicidogrulama;
import net.gulernet.app.nxfolder.UiClass.yeniguncelleme;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import github.nisrulz.easydeviceinfo.base.EasyAppMod;
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.base.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.base.EasyIdMod;
import github.nisrulz.easydeviceinfo.base.EasyNfcMod;
import github.nisrulz.easydeviceinfo.base.EasySimMod;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by necip on 2.12.2017.
 */

public class GlobalKod {



    public static boolean pingiris = false;

    public static String SunucuAdresi = "https://passid.gulernet.net";

    public static boolean pinyonlendirme = false;

    public static Class pinadres;

    public static String rastgele_oturum_id = null;

    public static InterstitialAd  mInterstitialAd;

    public static int reklamgoster;

    public void ToastOlustur(Context context,String mesaj){
        Toast.makeText(context,mesaj,Toast.LENGTH_LONG).show();
    }

    public void AlertDialogOlustur(Context context,String baslik,String mesaj,String EvetBtnTxt)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialog);
        builder.setTitle(baslik);
        builder.setMessage(mesaj);

        builder.setPositiveButton(EvetBtnTxt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });


        builder.show();
    }

    public  void BildirimOlustur(Context context,String baslik,String mesaj)
    {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);


        Notification n  = new Notification.Builder(context)
                .setContentTitle(baslik)
                .setContentText(mesaj)
                .setSmallIcon(R.drawable.bildirim)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    //Resim seçerken patch adresi çevirme -- başlangıç
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//Resim seçerken patch adresi çevirme -- bitiş

//SurumKontrol Başlangıç
    public static class BgSurumKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;

        public BgSurumKontrol(Context c) {
            this.c = c;
        }

        public void GuncellemeKontrol(){
            String surum = GlobalKod.readSharedPreference(c,"surum","");
            GlobalKod yeni = new GlobalKod();

            if(surum.equals("false"))
            {
                yeni.ToastOlustur(c,c.getResources().getString(R.string.Please_update_the_application));
                Intent intent = new Intent(c, yeniguncelleme.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();
            String versionName = Cryptotxt.encrypt(BuildConfig.VERSION_NAME);

            final String url = SunucuAdresi+"/surum?id="+ Uri.encode(versionName);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(Cryptotxt.decrypt(response));
                                String name = obj.getString("SurumDurumu");

                                SurumController.Surum(name);

                                if(!SurumController.Surum().equals(""))
                                {
                                    GlobalKod.writeSharedPreference(c,"surum",SurumController.Surum());
                                }


                                GuncellemeKontrol();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }

                            GuncellemeKontrol();


                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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
//SurumKontrol Bitiş

public static void ThumbnailOlustur(File Dosyayolu, int width, int height,String dosyaadi,Context context,int Type){
        if(Type == 0) {
            Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Dosyayolu.getPath()), width, height);
            SaveImage(resized,dosyaadi,context);
        }else if(Type == 1)
        {
            Bitmap resized = ThumbnailUtils.createVideoThumbnail(Dosyayolu.getPath(),
                    MediaStore.Images.Thumbnails.MICRO_KIND);
            SaveImage(resized,dosyaadi,context);
        }

}

    private static void SaveImage(Bitmap finalBitmap,String dosyaadi,Context context) {

        String mail = Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""));

        File myDir = new File(context.getFilesDir()+"/thumbnail/"+mail);
        myDir.mkdirs();
        File file = new File (myDir, dosyaadi);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String DosyaUzantısı(String fullName) {
        if(fullName != null) {
            String fileName = new File(fullName).getName();
            int dotIndex = fileName.lastIndexOf('.');
            return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        }
        return null;
    }

    public static boolean ResimUzantıKontrol(String uzanti){
        String yeniuzanti = DosyaUzantısı(uzanti);
        String [] dizi={"jpg","png","gif","bmp","webp","jpeg","JPG","PNG","GIF","BMP","WEBP","JPEG"};
        Boolean sonuc = Arrays.asList(dizi).contains(yeniuzanti);
        return sonuc;
    }

    public static boolean VideoUzantıKontrol(String uzanti){
        String yeniuzanti = DosyaUzantısı(uzanti);
        String [] dizi={"mp4","3gp","webm","MP4","3GP","WEBM"};
        Boolean sonuc = Arrays.asList(dizi).contains(yeniuzanti);
        return sonuc;
    }

    public static void İzinİste(final Context context){
        //https://github.com/nabinbhandari/Android-Permissions
        if (Build.VERSION.SDK_INT > 23) {
            Permissions.check(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    context.getResources().getString(R.string.Please_provide_the_necessary_permissions), new Permissions.Options()
                            .setSettingsDialogTitle("Warning!").setRationaleDialogTitle(context.getResources().getString(R.string.information)),
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {

                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                            GlobalKod global = new GlobalKod();
                            global.BildirimOlustur(context,context.getResources().getString(R.string.Application_failed_to_initialize),context.getResources().getString(R.string.The_application_failed_to_start));
                            System.exit(0);
                        }
                    });
        }
    }


    public static String readSharedPreference(Context context, String name,String defaults){
        defaults = Cryptotxt.encrypt(defaults);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sharedPreferences.getString(name,defaults);
        if(data.equals(""))
        {
            return data;
        }else{
            return Cryptotxt.decrypt(data);
        }

    }

    public static void writeSharedPreference(Context context,String name,String data ){
        if(!data.equals(""))
        {
            data = Cryptotxt.encrypt(data);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, data);
        editor.commit();
    }


    public static boolean ekrankilitlimi(Context context)
    {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            return true; //ekran kilitli
        } else {
            return false; //ekran kilitli değil
        }
    }


    public static int DosyaSayisi(String DosyaYolu)
    {

        File dir = new File(DosyaYolu);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            return children.length;

        }else{
            return 0;
        }
    }


    public static int KlasorSayisi(String KlasorYolu)
    {

        File folder = new File(KlasorYolu);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            return listOfFiles.length;
        }else{
           return 0;
        }

    }


    public static void loadImage(File url, ImageView imageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.yukleniyor)
                .error(R.drawable.nothumbnail)
                .into(imageView);
    }

    public static void Metrika(Context context){
        String MetricaKey =BuildConfig.Yandex_Metrica_Api;
        YandexMetrica.activate(context,MetricaKey);
        // Tracking user activity
        YandexMetrica.enableActivityAutoTracking((Application) context);
    }


    public final static boolean EmailKontrol(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void HashOlustur(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String xs = Crypto.md5(BuildConfig.Encryption_Key+BuildConfig.Encryption_Iv);
        editor.putString("sistemkontrol",xs);
        editor.commit();
    }

    public static void HashKontrol(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sharedPreferences.getString("sistemkontrol","");
        String xyzld = data;
        if(!xyzld.equals("")) {
            if (!xyzld.equals(Crypto.md5(BuildConfig.Encryption_Key + BuildConfig.Encryption_Iv))) {
                OturumuKapat(context);
            }
        }
    }


    public static void guvenlicikis(Context context)
    {
        File dir = new File(context.getExternalFilesDir(null)+"/onbellek/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }


        File dirxm = new File(context.getCacheDir()+"/onbellek/");
        if (dirxm.isDirectory())
        {
            String[] childrenxm = dirxm.list();
            for (int i = 0; i < childrenxm.length; i++)
            {
                new File(dirxm, childrenxm[i]).delete();
            }
        }



    }

    public static class BgKullaniciKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;
        String mail;
        public BgKullaniciKontrol(Context c,String mail) {
            this.c = c;
            this.mail = mail;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();

            mail = Cryptotxt.encrypt(mail);

            final String url = SunucuAdresi+"/kullanicilar/kontrol?zn5="+ Uri.encode(mail);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(response);
                                String durumx = obj.getString("durum");
                                durumx = Cryptotxt.decrypt(durumx);
                                Boolean durum = Boolean.parseBoolean(durumx);
                                KullaniciController.KontrolDurumu = durum;


                                GlobalKod globalKod = new GlobalKod();
                                if(KullaniciController.KullaniciKontrol() == true)
                                {
                                    globalKod.ToastOlustur(c,c.getResources().getString(R.string.Check_your_mail_box));
                                    Intent intent = new Intent(c, kullanicidogrulama.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.getApplicationContext().startActivity(intent);
                                }else if(KullaniciController.KullaniciKontrol() == false){
                                    globalKod.ToastOlustur(c,c.getResources().getString(R.string.There_was_a_problem_sending_the_code));
                                }else{

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                GlobalKod globalKod = new GlobalKod();
                                globalKod.ToastOlustur(c,c.getResources().getString(R.string.Could_not_establish_a_connection_with_the_server));
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }




                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

    public static class BgKullaniciDogrulama extends AsyncTask<Void, Integer, Void> {

        Context c;
        String mail;
        String kod;

        public BgKullaniciDogrulama(Context c,String mail,String kod) {
            this.c = c;
            this.mail = mail;
            this.kod = kod;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();

            mail = Cryptotxt.encrypt(mail);
            kod = Cryptotxt.encrypt(kod);

            final String url = SunucuAdresi+"/kullanicilar/dogrulama?zn5="+ Uri.encode(mail)+"&zn8="+Uri.encode(kod);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(response);
                                String anahtar = obj.getString("anahtar");
                                String oturum = obj.getString("oturum");

                                anahtar = Cryptotxt.decrypt(anahtar);
                                oturum = Cryptotxt.decrypt(oturum);
                                KullaniciController.KullaniciAnahtar(anahtar);
                                KullaniciController.KullaniciOturum(oturum);


                                GlobalKod global = new GlobalKod();
                                if(!KullaniciController.KullaniciAnahtar().equals("null") && !KullaniciController.KullaniciOturum().equals("null"))
                                {
                                    GlobalKod.writeSharedPreference(c,"kutusifre",KullaniciController.anahtar);
                                    GlobalKod.writeSharedPreference(c,"oturum",KullaniciController.oturum);
                                    GlobalKod.HashOlustur(c);
                                    if(GlobalKod.pinyonlendirme)
                                    {
                                        Intent intent = new Intent(c, GlobalKod.pinadres);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        c.getApplicationContext().startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(c, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        c.getApplicationContext().startActivity(intent);
                                    }

                                }else{
                                    global.ToastOlustur(c,c.getResources().getString(R.string.The_code_is_invalid));
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                GlobalKod globalKod = new GlobalKod();
                                globalKod.ToastOlustur(c,c.getResources().getString(R.string.Could_not_establish_a_connection_with_the_server));
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }




                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

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

    public static class BgOturumuKapat extends AsyncTask<Void, Integer, Void> {

        Context c;

        public BgOturumuKapat(Context c) {
            this.c = c;
        }

        public void OturumuKapat(){
            GlobalKod.OturumuKapat(c);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();
            String oturum = Cryptotxt.encrypt(GlobalKod.readSharedPreference(c,"oturum",""));

            final String url = GlobalKod.SunucuAdresi+"/kullanicilar/oturumukapat?zn12="+ Uri.encode(oturum);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(response);
                                String name = Cryptotxt.decrypt(obj.getString("oturumdurumu"));

                                if(name.equals("true"))
                                {
                                    OturumuKapat();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                GlobalKod globalKod = new GlobalKod();
                                globalKod.ToastOlustur(c,c.getResources().getString(R.string.Could_not_establish_a_connection_with_the_server));
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }


                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

    public static void OturumuKapat(Context context)
    {

        File dir = new File(context.getExternalFilesDir(null)+"/onbellek/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

        File dirx = new File(context.getFilesDir()+"/thumbnail/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail","")));
        if (dirx.isDirectory())
        {
            String[] childrenx = dirx.list();
            for (int i = 0; i < childrenx.length; i++)
            {
                new File(dirx, childrenx[i]).delete();
            }
        }


        File dirxm = new File(context.getCacheDir()+"/onbellek/");
        if (dirxm.isDirectory())
        {
            String[] childrenxm = dirxm.list();
            for (int i = 0; i < childrenxm.length; i++)
            {
                new File(dirxm, childrenxm[i]).delete();
            }
        }


        GlobalKod.writeSharedPreference(context,"kutusifre","");
        GlobalKod.writeSharedPreference(context,"oturum","");
        GlobalKod.writeSharedPreference(context,"pin","");
        GlobalKod.writeSharedPreference(context,"pinsifre","");
        GlobalKod.writeSharedPreference(context,"sahtepinsifre","");
        GlobalKod.writeSharedPreference(context,"mail","");
        GlobalKod.writeSharedPreference(context,"videosikistirma","");
        GlobalKod.writeSharedPreference(context,"parmakizidurumu","");
        GlobalKod.writeSharedPreference(context,"parmakizivepin","");
        GlobalKod.writeSharedPreference(context,"surum","");

        System.exit(0);
    }

    public static void Reklamyukle(Context context)
    {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-2335291179278222/4551962684");
        //mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BB6E02DE6F5B150FBC779BD97A26A8C5").build());
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public static void Reklamgoster()
    {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {

        }
    }

    public static boolean checkWriteExternalPermission(Context context)
    {
        //Yazma izni kontrolü
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public static ArrayList<String> GoogleMailListesi(Context context){
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        ArrayList<String> maillistesi = new ArrayList<String>();
        for (Account account : accounts) {
            maillistesi.add(account.name);
        }
        return  maillistesi;
    }

    public static class BgCihazKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;
        public BgCihazKontrol(Context c) {
            this.c = c;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            final String url = SunucuAdresi+"/kullanicilar/cihaz";
            RequestQueue queue = Volley.newRequestQueue(c);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    EasyAppMod easyAppMod = new EasyAppMod(c);
                    EasyDeviceMod easyDeviceMod = new EasyDeviceMod(c);
                    EasySimMod easySimMod = new EasySimMod(c);
                    EasyNfcMod easyNfcMod = new EasyNfcMod(c);
                    EasyDisplayMod easyDisplayMod = new EasyDisplayMod(c);
                    EasyIdMod easyIdMod = new EasyIdMod(c);
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("oturum", Cryptotxt.encrypt(GlobalKod.readSharedPreference(c,"oturum","")));
                    params.put("package_name", Cryptotxt.encrypt(easyAppMod.getPackageName()));
                    params.put("app_name", Cryptotxt.encrypt(easyAppMod.getAppName()));
                    params.put("app_version", Cryptotxt.encrypt(easyAppMod.getAppVersion()));
                    params.put("app_version_code", Cryptotxt.encrypt(easyAppMod.getAppVersionCode()));

                    params.put("manufacturer", Cryptotxt.encrypt(easyDeviceMod.getManufacturer()));
                    params.put("model", Cryptotxt.encrypt(easyDeviceMod.getModel()));
                    params.put("os_version", Cryptotxt.encrypt(easyDeviceMod.getOSVersion()));
                    params.put("product", Cryptotxt.encrypt(easyDeviceMod.getProduct()));
                    params.put("device", Cryptotxt.encrypt(easyDeviceMod.getDevice()));
                    params.put("board", Cryptotxt.encrypt(easyDeviceMod.getBoard()));
                    params.put("hardware", Cryptotxt.encrypt(easyDeviceMod.getHardware()));
                    params.put("is_device_rooted", Cryptotxt.encrypt(Boolean.toString(easyDeviceMod.isDeviceRooted())));

                    params.put("sim_country", Cryptotxt.encrypt(easySimMod.getCountry()));
                    params.put("sim_carrier", Cryptotxt.encrypt(easySimMod.getCarrier()));

                    params.put("is_nfc_present", Cryptotxt.encrypt(Boolean.toString(easyNfcMod.isNfcPresent())));
                    params.put("is_nfc_enabled", Cryptotxt.encrypt(Boolean.toString(easyNfcMod.isNfcEnabled())));

                    params.put("display_resolution", Cryptotxt.encrypt(easyDisplayMod.getResolution()));

                    params.put("PseudoID", Cryptotxt.encrypt(easyIdMod.getPseudoUniqueID()));


                    return params;
                }
            };
            queue.add(postRequest);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

    public static class BgGirisKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;
        String oturumanahtari;
        String  rastgele_id = UUID.randomUUID().toString().replace("-", "");
        public BgGirisKontrol(Context c) {
            this.c = c;
            this.oturumanahtari = GlobalKod.rastgele_oturum_id;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {




                            final String url = SunucuAdresi+"/kullanicilar/giris";
                            RequestQueue queue = Volley.newRequestQueue(c);
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            Log.d("Response", response);
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error
                                            Log.d("Error.Response", error.toString());
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams()
                                {
                                    EasyIdMod easyIdMod = new EasyIdMod(c);
                                    Map<String, String>  params = new HashMap<String, String>();
                                    params.put("oturum", Cryptotxt.encrypt(GlobalKod.readSharedPreference(c,"oturum","")));
                                    params.put("oturumanahtari", Cryptotxt.encrypt(oturumanahtari));
                                    params.put("random", Cryptotxt.encrypt(rastgele_id));
                                    params.put("PseudoID", Cryptotxt.encrypt(easyIdMod.getPseudoUniqueID()));
                                    return params;
                                }
                            };
                            queue.add(postRequest);





            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

    public static class BgGeribildirim extends AsyncTask<Void, Integer, Void> {

        Context c;
        String mesaj;
        public BgGeribildirim(Context c,String mesaj) {
            this.c = c;
            this.mesaj = mesaj;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {




            final String url = SunucuAdresi+"/kullanicilar/geribildirim";
            RequestQueue queue = Volley.newRequestQueue(c);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                String kod =  Cryptotxt.decrypt(obj.getString("kod"));
                                GlobalKod global = new GlobalKod();
                                if(kod.equals("1"))
                                {
                                    Intent intent = new Intent(c, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.getApplicationContext().startActivity(intent);
                                    global.ToastOlustur(c,c.getResources().getString(R.string.we_thank_you));
                                }else if(kod.equals("x0"))
                                {
                                    global.ToastOlustur(c,c.getResources().getString(R.string.your_daily_limit_is_over));
                                }else if(kod.equals("x2"))
                                {
                                    global.ToastOlustur(c,c.getResources().getString(R.string.your_message_must_be_at_least_8_characters_and_at_most_500_characters));
                                }

                            }catch (JSONException e) {
                                e.printStackTrace();

                            }



                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                GlobalKod globalKod = new GlobalKod();
                                globalKod.ToastOlustur(c,c.getResources().getString(R.string.Could_not_establish_a_connection_with_the_server));
                                Log.e(TAG, error.getMessage());
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    EasyIdMod easyIdMod = new EasyIdMod(c);
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("oturum", Cryptotxt.encrypt(GlobalKod.readSharedPreference(c,"oturum","")));
                    params.put("mesaj", Cryptotxt.encrypt(mesaj));
                    params.put("PseudoID", Cryptotxt.encrypt(easyIdMod.getPseudoUniqueID()));
                    return params;
                }
            };
            queue.add(postRequest);





            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

    public static class BgGoogleKullaniciKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;
        String mail;
        public BgGoogleKullaniciKontrol(Context c,String mail) {
            this.c = c;
            this.mail = mail;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();

            mail = Cryptotxt.encrypt(mail);

            final String url = SunucuAdresi+"/kullanicilar/googlekontrol?zn5="+ Uri.encode(mail);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(response);
                                String durumx = obj.getString("durum");
                                durumx = Cryptotxt.decrypt(durumx);
                                Boolean durum = Boolean.parseBoolean(durumx);
                                KullaniciController.KontrolDurumu = durum;



                                GlobalKod globalKod = new GlobalKod();
                                if(KullaniciController.KullaniciKontrol() == true)
                                {
                                    String anahtar = obj.getString("anahtar");
                                    String oturum = obj.getString("oturum");

                                    anahtar = Cryptotxt.decrypt(anahtar);
                                    oturum = Cryptotxt.decrypt(oturum);
                                    KullaniciController.KullaniciAnahtar(anahtar);
                                    KullaniciController.KullaniciOturum(oturum);

                                    GlobalKod.writeSharedPreference(c,"kutusifre",KullaniciController.anahtar);
                                    GlobalKod.writeSharedPreference(c,"oturum",KullaniciController.oturum);
                                    GlobalKod.HashOlustur(c);


                                    Intent intent = new Intent(c, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.getApplicationContext().startActivity(intent);

                                }else if(KullaniciController.KullaniciKontrol() == false){
                                    globalKod.ToastOlustur(c,c.getResources().getString(R.string.Check_your_mail_box));
                                    Intent intent = new Intent(c, kullanicidogrulama.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.getApplicationContext().startActivity(intent);
                                }else{

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                GlobalKod globalKod = new GlobalKod();
                                globalKod.ToastOlustur(c,c.getResources().getString(R.string.Could_not_establish_a_connection_with_the_server));
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }




                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


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

}


