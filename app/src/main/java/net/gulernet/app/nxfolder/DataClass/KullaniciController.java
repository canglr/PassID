package net.gulernet.app.nxfolder.DataClass;

/**
 * Created by necip on 18.03.2018.
 */

public class KullaniciController {

    public static boolean KontrolDurumu;
    public static String anahtar;
    public static String oturum;

    public static void KullaniciKontrol(Boolean durum){
        KontrolDurumu = durum;
    }

    public static Boolean KullaniciKontrol(){
        return KontrolDurumu;
    }

    public static void KullaniciAnahtar(String anahtarx){
        anahtar = anahtarx;
    }

    public static String KullaniciAnahtar(){
        return anahtar;
    }


    public static void KullaniciOturum(String oturumx){
        oturum = oturumx;
    }

    public static String KullaniciOturum(){
        return oturum;
    }

}
