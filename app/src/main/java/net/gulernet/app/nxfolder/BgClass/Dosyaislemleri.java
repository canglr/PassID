package net.gulernet.app.nxfolder.BgClass;

import android.content.Context;
import android.os.Environment;

import net.gulernet.app.nxfolder.DataClass.ZipEleman;
import net.gulernet.app.nxfolder.ServiceClass.Dosyaaktar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by necip on 23.02.2018.
 */

public class Dosyaislemleri {


    public static List<String> klasorlistesi = new ArrayList<String>();
    public static void KlasorListele(Context context){
        File folder = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))).toString());
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isDirectory()) {

                    klasorlistesi.add(file.getName());

                }
            }
        }

    }

    public static void KlasorOlustur(String klasoradi,Context context){
        File KlasorOlustur = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))+"/"+klasoradi).toString());
        KlasorOlustur.mkdirs();
    }

    public static void KlasorDuzenle(String eskiklasoradi,String yeniklasoradi,Context context)
    {
        File eskiklasor = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))+"/"+eskiklasoradi).toString());
        File yeniklasor = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))+"/"+yeniklasoradi).toString());
        boolean success = eskiklasor.renameTo(yeniklasor);
    }

    public static void KlasorSil(String klasoradi,Context context)
    {
        String mail = Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""));
        File KlasorSil = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+mail+"/"+klasoradi).toString());
        if (KlasorSil.isDirectory())
        {
            String[] children = KlasorSil.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(KlasorSil, children[i]).delete();
                File thumnail = new File(context.getFilesDir()+"/thumbnail/"+mail+"/"+children[i]);
                thumnail.delete();
            }
        }
        KlasorSil.delete();
    }

    public static boolean KlasorKontrol(String klasoradi,Context context)
    {

        File Klasor = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""))+"/"+klasoradi).toString());
        if(Klasor.exists())
        {
            return true;
        }else{
            return false;
        }
    }

    public static void DosyaDuzenle(Context context, String SecilenKlasor, String eskidosya, String yenidosya)
    {
        String mail = Crypto.md5(GlobalKod.readSharedPreference(context,"mail",""));
        File eskidosyax = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+mail+"/"+SecilenKlasor+"/"+eskidosya).toString());
        File yenidosyax = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+mail+"/"+SecilenKlasor+"/"+yenidosya).toString());
        boolean success = eskidosyax.renameTo(yenidosyax);
        File eskithumnail = new File(context.getFilesDir()+"/thumbnail/"+mail+"/"+eskidosya);
        File yenithumnail = new File(context.getFilesDir()+"/thumbnail/"+mail+"/"+yenidosya);
        boolean thumnailsuccess = eskithumnail.renameTo(yenithumnail);

    }

}
